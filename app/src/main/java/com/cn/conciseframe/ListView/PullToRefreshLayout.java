package com.cn.conciseframe.ListView;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.conciseframe.R;
import com.cn.conciseframe.util.Logger;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tangzy on 2016/7/7.
 */
public class PullToRefreshLayout extends RelativeLayout{
    // 初始状态
    public static final int INIT = 0;
    // 释放刷新
    public static final int RELEASE_TO_REFRESH = 1;
    // 正在刷新
    public static final int REFRESHING = 2;
    // 释放加载
    public static final int RELEASE_TO_LOAD = 3;
    // 正在加载
    public static final int LOADING = 4;
    // 操作完毕
    public static final int DONE = 5;
    //无数据加载
    public static final int LOAD_NO_MORE = 6;
    // 当前状态
    private int state = INIT;
    // 刷新回调接口
    private OnRefreshListener mListener;
    // 按下Y坐标，上一个事件点Y坐标
    private float downY, lastY;

    // 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0
    public float pullDownY = 0;
    // 上拉的距离
    private float pullUpY = 0;

    // 释放刷新的距离
    private float refreshDist = 200;
    // 释放加载的距离
    private float loadmoreDist = 200;

    private MyTimer timer;
    // 回滚速度
    public float MOVE_SPEED = 8;
    // 第一次执行布局
    private boolean isLayout = false;
    //是否已经无数据可加载
    private boolean isLoadNoMore = false;
    // 在刷新过程中滑动操作
    private boolean isTouch = false;
    // 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
    private float radio = 2;

    // 下拉箭头的转180°动画
    private RotateAnimation rotateAnimation;
    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;

    /**
     * 下拉头布局
     */
    private View refreshLayout;
    /**
     * 下拉的箭头
     */
    private View pullArrow;
    /**
     * 正在刷新的图标
     */
    private View pullRefreshing;
    /**
     * 刷新结果图标
     */
    private View pullStateImageView;
    /**
     * 刷新结果：成功或失败
     */
    private TextView pullStateTextView;

    /**
     * 上拉加载布局
     */
    private View upwardLayout;
    /**
     * 上拉的箭头
     */
    private View upwardArrow;
    /**
     * 正在加载的图标
     */
    private View upwardLoading;
    /**
     * 加载结果图标
     */
    private View upwardLoadStateImageView;
    /**
     * 加载结果：成功或失败
     */
    private TextView upwardLoadStateTextView;

    // 实现了Pullable接口的View
    private View pullableView;
    // 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
    private boolean canPullDown = true;
    private boolean canPullUp = true;

    //提供给外部关闭下拉刷新和上拉加载功能的参数
    private boolean isArrowRefresh = true;
    private boolean isArrowMore = true;

    private Context mContext;

    /**
     * 执行自动回滚的handler
     */
    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // 回弹速度随下拉距离moveDeltaY增大而增大
            MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2
                    / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
            if (!isTouch) {
                // 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
                if (state == REFRESHING && pullDownY <= refreshDist) {
                    pullDownY = refreshDist;
                    timer.cancel();
                } else if (state == LOADING && -pullUpY <= loadmoreDist) {
                    pullUpY = -loadmoreDist;
                    timer.cancel();
                }

            }
            if (pullDownY > 0)
                pullDownY -= MOVE_SPEED;
            else if (pullUpY < 0)
                pullUpY += MOVE_SPEED;
            if (pullDownY < 0) {
                // 已完成回弹
                pullDownY = 0;
                pullArrow.clearAnimation();
                // 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (state != REFRESHING && state != LOADING) {
                    changeState(INIT);
                }
                timer.cancel();
                requestLayout();
            }
            if (pullUpY > 0) {
                // 已完成回弹
                pullUpY = 0;
                upwardArrow.clearAnimation();
                // 隐藏上拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (state != REFRESHING && state != LOADING) {
                    changeState(INIT);
                }
                timer.cancel();
                requestLayout();
            }
            Log.d("handle", "handle");
            // 刷新布局,会自动调用onLayout
            requestLayout();
            // 没有拖拉或者回弹完成
            if (pullDownY + Math.abs(pullUpY) == 0)
                timer.cancel();
        }

    };

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public PullToRefreshLayout(Context context) {
        this(context, null);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        timer = new MyTimer(updateHandler);
        rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.reverse_anim);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        rotateAnimation.setInterpolator(lir);
        refreshingAnimation.setInterpolator(lir);
    }

    private void hide() {
        timer.schedule(5);
    }

    /**
     * 下拉刷新功能开关
     *
     * @param arrowRefresh true 意思是打开下拉刷新功能
     */
    public void setAllowRefresh(boolean arrowRefresh) {
        isArrowRefresh = arrowRefresh;
    }

    /**
     * 上拉加载功能开关
     *
     * @param arrowMore true 意思是打开上拉加载功能
     */
    public void setAllowMore(boolean arrowMore) {
        isArrowMore = arrowMore;
    }

    /**
     * 设置是否已经无数据可加载
     *
     * @param loadNoMore
     */
    public void setLoadNoMore(boolean loadNoMore) {
        isLoadNoMore = loadNoMore;
    }

    /**
     * 完成刷新操作，显示刷新结果。注意：刷新完成后一定要调用这个方法
     */
    public void onRefreshFinish() {
        pullRefreshing.clearAnimation();
        pullRefreshing.setVisibility(View.GONE);
        // 刷新结果停留1秒
        changeState(DONE);
        hide();
    }

    /**
     * 加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法
     */
    public void onLoadmoreFinish() {
        upwardLoading.clearAnimation();
        upwardLoading.setVisibility(View.GONE);
        changeState(DONE);
        hide();
    }

    /**
     * 设置加载刷新的状态
     *
     * @param to
     */
    private void changeState(int to) {
        state = to;
        switch (state) {
            case INIT:
                // 下拉布局初始状态
                pullStateImageView.setVisibility(View.GONE);
                pullStateTextView.setText(R.string.pull_to_refresh);
                pullArrow.clearAnimation();
                pullArrow.setVisibility(View.VISIBLE);
                // 上拉布局初始状态
                upwardLoadStateImageView.setVisibility(View.GONE);
                upwardLoadStateTextView.setText(isLoadNoMore ? R.string.pulluo_no_more : R.string.pullup_to_load);
                upwardArrow.clearAnimation();
                upwardArrow.setVisibility(isLoadNoMore ? GONE : VISIBLE);
                break;
            case RELEASE_TO_REFRESH:
                // 释放刷新状态
                pullStateTextView.setText(R.string.release_to_refresh);
                pullArrow.startAnimation(rotateAnimation);
                break;
            case REFRESHING:
                // 正在刷新状态
                isLoadNoMore = false;
                pullArrow.clearAnimation();
                pullRefreshing.setVisibility(View.VISIBLE);
                pullArrow.setVisibility(View.INVISIBLE);
                pullRefreshing.startAnimation(refreshingAnimation);
                pullStateTextView.setText(R.string.refreshing);
                // 刷新操作
                if (mListener != null)
                    mListener.onRefresh();
                break;
            case RELEASE_TO_LOAD:
                // 释放加载状态
                if (!isLoadNoMore) {
                    upwardLoadStateTextView.setText(R.string.release_to_load);
                    upwardArrow.startAnimation(rotateAnimation);
                } else {
                    upwardLoadStateTextView.setText(R.string.pulluo_no_more);
                    upwardArrow.clearAnimation();
                    upwardArrow.setVisibility(View.GONE);
                }
                break;
            case LOADING:
                // 正在加载状态
                upwardArrow.clearAnimation();
                upwardLoading.setVisibility(View.VISIBLE);
                upwardArrow.setVisibility(View.INVISIBLE);
                upwardLoading.startAnimation(refreshingAnimation);
                upwardLoadStateTextView.setText(R.string.loading);
                // 加载操作
                if (mListener != null)
                    mListener.onMore();
                break;
            case LOAD_NO_MORE:
                onLoadmoreFinish();
                break;
            case DONE:
                // 刷新或加载完毕，啥都不做
                break;
        }
    }


    /**
     * 不限制上拉或下拉
     */
    private void releasePull() {
        canPullDown = true;
        canPullUp = true;
    }

    /*
     * （非 Javadoc）由父控件决定是否分发事件，防止事件冲突
     *
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if (isLoadNoMore) {
                    upwardLoadStateTextView.setText(isLoadNoMore ? R.string.pulluo_no_more : R.string.pullup_to_load);
                    upwardArrow.setVisibility(View.GONE);
                }
                downY = ev.getY();
                lastY = downY;
                timer.cancel();
                releasePull();
                break;
            case MotionEvent.ACTION_MOVE:
                if (pullDownY > 0 || (((Pullable) pullableView).canPullDown() && canPullDown && state != LOADING)) {
                    if (isArrowRefresh) {
                        // 可以下拉，正在加载时不能下拉
                        // 对实际滑动距离做缩小，造成用力拉的感觉
                        pullDownY = pullDownY + (ev.getY() - lastY) / radio;
                        if (pullDownY < 0) {
                            pullDownY = 0;
                            canPullDown = false;
                            canPullUp = true;
                        }
                        if (pullDownY > getMeasuredHeight())
                            pullDownY = getMeasuredHeight();
                        if (state == REFRESHING) {
                            // 正在刷新的时候触摸移动
                            isTouch = true;
                        }
                    }
                } else if (pullUpY < 0 || (((Pullable) pullableView).canPullUp() && canPullUp && state != REFRESHING)) {
                    if (isArrowMore) {
                        // 可以上拉，正在刷新时不能上拉
                        pullUpY = pullUpY + (ev.getY() - lastY) / radio;
                        if (pullUpY > 0) {
                            pullUpY = 0;
                            canPullDown = true;
                            canPullUp = false;
                        }
                        if (pullUpY < -getMeasuredHeight())
                            pullUpY = -getMeasuredHeight();
                        if (state == LOADING) {
                            // 正在加载的时候触摸移动
                            isTouch = true;
                        }
                    }
                } else
                    releasePull();
                lastY = ev.getY();
                // 根据下拉距离改变比例
                radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
                if (pullDownY > 0 || pullUpY < 0)
                    requestLayout();
                if (pullDownY > 0) {
                    if (pullDownY <= refreshDist && (state == RELEASE_TO_REFRESH || state == DONE)) {
                        // 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
                        changeState(INIT);
                    }
                    if (pullDownY >= refreshDist && state == INIT) {
                        // 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
                        changeState(RELEASE_TO_REFRESH);
                    }
                } else if (pullUpY < 0) {
                    // 下面是判断上拉加载的，同上，注意pullUpY是负值
                    if (-pullUpY <= loadmoreDist && (state == RELEASE_TO_LOAD || state == DONE)) {
                        changeState(INIT);
                    }
                    // 上拉操作
                    if (-pullUpY >= loadmoreDist && state == INIT)
                        changeState(RELEASE_TO_LOAD);


                }
                // 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
                // Math.abs(pullUpY))就可以不对当前状态作区分了
                if ((pullDownY + Math.abs(pullUpY)) > 8) {
                    // 防止下拉过程中误触发长按事件和点击事件
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (pullDownY > refreshDist || -pullUpY > loadmoreDist)
                // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
                {
                    isTouch = false;
                }
                if (state == RELEASE_TO_REFRESH) {
                    changeState(REFRESHING);
                } else if (state == RELEASE_TO_LOAD) {
                    changeState(isLoadNoMore ? LOAD_NO_MORE : LOADING);
                }
                hide();
            default:
                break;
        }
        // 事件分发交给父类
        super.dispatchTouchEvent(ev);
        return true;
    }

    private void initHeadAndFootView() {
        // 初始化下拉布局
        pullArrow = refreshLayout.findViewById(R.id.pull_icon);
        pullStateTextView = (TextView) refreshLayout
                .findViewById(R.id.state_tv);
        pullRefreshing = refreshLayout.findViewById(R.id.refreshing_icon);
        pullStateImageView = refreshLayout.findViewById(R.id.state_iv);
        // 初始化上拉布局
        upwardArrow = upwardLayout.findViewById(R.id.pullup_icon);
        upwardLoadStateTextView = (TextView) upwardLayout
                .findViewById(R.id.loadstate_tv);
        upwardLoading = upwardLayout.findViewById(R.id.loading_icon);
        upwardLoadStateImageView = upwardLayout.findViewById(R.id.loadstate_iv);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isLayout) {
            // 这里是第一次进来的时候做一些初始化
            refreshLayout = getChildAt(0);
            pullableView = getChildAt(1);
            upwardLayout = getChildAt(2);
            isLayout = true;
            initHeadAndFootView();
            refreshDist = ((ViewGroup) refreshLayout).getChildAt(0)
                    .getMeasuredHeight();
            loadmoreDist = ((ViewGroup) upwardLayout).getChildAt(0)
                    .getMeasuredHeight();
        }
        // 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
        refreshLayout.layout(0,
                (int) (pullDownY + pullUpY) - refreshLayout.getMeasuredHeight(),
                refreshLayout.getMeasuredWidth(), (int) (pullDownY + pullUpY));
        pullableView.layout(0, (int) (pullDownY + pullUpY),
                pullableView.getMeasuredWidth(), (int) (pullDownY + pullUpY)
                        + pullableView.getMeasuredHeight());
        upwardLayout.layout(0,
                (int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight(),
                upwardLayout.getMeasuredWidth(),
                (int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight()
                        + upwardLayout.getMeasuredHeight());
    }

    class MyTimer {
        private Handler handler;
        private Timer timer;
        private MyTask mTask;

        public MyTimer(Handler handler) {
            this.handler = handler;
            timer = new Timer();
        }

        public void schedule(long period) {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
            mTask = new MyTask(handler);
            timer.schedule(mTask, 0, period);
        }

        public void cancel() {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
        }

        class MyTask extends TimerTask {
            private Handler handler;

            public MyTask(Handler handler) {
                this.handler = handler;
            }

            @Override
            public void run() {
                handler.obtainMessage().sendToTarget();
            }

        }
    }

    /**
     * 刷新加载回调接口
     *
     * @author chenjing
     */
    public interface OnRefreshListener {
        /**
         * 刷新操作
         */
        void onRefresh();

        /**
         * 加载操作
         */
        void onMore();
    }

}
