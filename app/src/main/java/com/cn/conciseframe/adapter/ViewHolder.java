package com.cn.conciseframe.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder
{
	private final SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;

	private ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position)
	{
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		// setTag
		mConvertView.setTag(this);
	}

	/**
	 * 拿到一个ViewHolder对象
	 *
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position)
	{
		if (convertView == null)
		{
			return new ViewHolder(context, parent, layoutId, position);
		}
		return (ViewHolder) convertView.getTag();
	}

	public View getConvertView()
	{
		return mConvertView;
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 *
	 * @param viewId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId)
	{
		View view = mViews.get(viewId);
		if (view == null)
		{
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	public ViewHolder setVisibility(int viewId, int visibility)
	{
		getView(viewId).setVisibility(visibility);
		return this;
	}

	/**
	 * 给整个convertView设置enable属性
	 * @param enabled 是否enable
	 * @return
	 */
	public ViewHolder setConvertViewEnabled(boolean enabled)
	{
		mConvertView.setEnabled(enabled);
		return this;
	}

	/**
	 * 给某个资源文件设置enable
	 * @param viewId 资源文件的id
	 * @param enabled 是否enable
	 * @return
	 */
	public ViewHolder setResEnabled(int viewId, boolean enabled)
	{
		getView(viewId).setEnabled(enabled);
		return this;
	}

	public ViewHolder setTextViewCompoundDrawables(int viewId, Drawable drawableLeft,Drawable drawableTop,
										   Drawable drawableRight,Drawable drawableBottom)
	{
		View v = getView(viewId);
		if (v instanceof TextView){
			((TextView) v).setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
		}

		return this;
	}
	/**
	 * 为TextView设置字符串
	 *
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text)
	{
		((TextView)getView(viewId)).setText(text);
		return this;
	}

	public ViewHolder setOnClick(int viewId, View.OnClickListener listener){
		getView(viewId).setOnClickListener(listener);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 *
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, int drawableId)
	{
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);

		return this;
	}

	/**
	 * 为ImageView设置图片
	 *
	 * @param viewId
	 * @param bm
	 * @return
	 */
	public ViewHolder setImageBitmap(int viewId, Bitmap bm)
	{
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 *
	 * @param //viewId
	 * @param //url
	 * @return
	 */
//	public ViewHolder setImageByUrl(int viewId, String url)
//	{
//		ImageLoader.getInstance(3, Type.LIFO).loadImage(url,
//				(ImageView) getView(viewId));
//		return this;
//	}

	public int getPosition()
	{
		return mPosition;
	}

}
