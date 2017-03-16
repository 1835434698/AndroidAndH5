package com.cn.conciseframe.transmit;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.cn.conciseframe.util.Logger;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tangzy on 2016/9/20.
 */
public class SessionManager {

    public static final String TAG = "SessionManager";
    public static final String PREFIX = "_SID_UUID_";
    public static final int SESSION_EXPIR = 0;

    private static SessionManager manager;
    private static Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();
    private static long normal_session_expired_time = 3;
    private Handler sessionHandler;

    private SessionManager(Context context, Looper l) {
        sessionHandler = new SessionHandler(l);
    }

    public static void initManager(Context context, Looper l) {
        if (manager == null) {
            manager = new SessionManager(context, l);
        }
    }

    public static void initManager(Context context) {
        initManager(context, Looper.myLooper());
    }

    public static Session newSession(long expired) {
        String sid = PREFIX + UUID.randomUUID().toString();
        return new Session(sid, expired);
    }

    /**
     * 创建一个新的session，（如果sessions集合中存在，可以复用），并开启定时器，时间到后清理该session。
     * @param sid 目标sessionid
     * @param expired 清理时间
     * @return
     */
    public static Session newSession(String sid, long expired) {
        if(expired <= 0) expired = normal_session_expired_time;
        Session session = getSession(sid, true, expired);
        if (session != null) {
            session.clear();
            log("session has RE-CREATED, sid=" + session.getId() + ", expired = " + expired / 1000 + "s");
        } else {
            session = new Session(sid, expired);
            sessions.put(sid, session);
            sendMessageDelayed(SESSION_EXPIR, session, expired);
            log("session has CREATED, sid=" + session.getId() + ", expired = " + expired / 1000 + "s");
        }
        return session;
    }

    /**
     * 根据sessionid获取sessions集合中所存session对象，如果resetExpir为true，会重新开启清除session的计时，如果
     * expired为正数，会重新给目标session对象的expired属性赋值。
     * @param sessionid 目标sessionid
     * @param resetExpir 是否重置清空session的计时器
     * @param expired 给session赋的新的expired属性
     * @return
     */
	/*protected*/ static Session getSession(String sessionid, boolean resetExpir, long expired) {
        if (sessionid == null) return null;
        Session session = sessions.get(sessionid);
        if (session == null) {
            return null;
        }
        if (resetExpir) {
            //log("session reset, sid=" + sid);
            manager.sessionHandler.removeMessages(SESSION_EXPIR, session);
            if (expired >= 0) {
                session.resetExpired(expired);
            }
            sendMessageDelayed(SESSION_EXPIR, session, session.getExpired());
        }
        return session;
    }

    /**
     * 根据sessionid获取sessions集合中所存session对象，如果resetExpir为true，会重新开启清除session的计时
     * @param sessionid 目标sessionid
     * @param resetExpir 是否重置清空session的计时器
     * @return
     */
    public static Session getSession(String sessionid, boolean resetExpir) {
        return getSession(sessionid, resetExpir, -1);
    }

    /**
     * 根据sessionid获取sessions集合中所存session对象
     * @param sessionid 目标sessionid
     * @return
     */
    public static Session getSession(String sessionid) {
        return getSession(sessionid, false);
    }

    /**
     * 根据sessionid删除sessions集合中所存session对象，并清除计时器
     * @param sessionid
     */
    public static void delSession(String sessionid) {
        delSession(sessionid,false);
    }

    /**
     * 根据sessionid删除sessions集合中所存session对象，并清除计时器，如果该session为LoginSession则中止心跳连接，清空sessions集合中的所有session，并移除sessionHandler中的所有消息
     * @param sessionid
     * @param isLoginSession
     */
    public static void delSession(String sessionid, boolean isLoginSession) {
        if (isLoginSession){
            Logger.i(TAG,"要删除的该session为LoginSession，中止心跳连接");
            // TODO: 2016/9/20
           // Http.stopKeepAlive();//中止心跳连接倒计时
        }else {
            Logger.i(TAG,"要删除的该session不为LoginSession");
        }

        if (sessionid == null) return;
        Session session = getSession(sessionid, false);
        if (session != null) {
            session.clear();
            String sid = session.getId();
            if (isLoginSession)
                sessions.clear();
            else
                sessions.remove(sid);
            Logger.d(TAG, "remove exp message, sid=" + sid);
            if (isLoginSession)
                manager.sessionHandler.removeCallbacksAndMessages(null);
            else
                manager.sessionHandler.removeMessages(SESSION_EXPIR, session);
        }
    }

    private static void sendMessageDelayed(int what, Object obj, long expired) {
        Message msg = manager.sessionHandler.obtainMessage(what, obj);
        manager.sessionHandler.sendMessageDelayed(msg, expired);
    }

    private static void log(String s) {
        Logger.d(TAG, s);
    }

    static class SessionHandler extends Handler {

        public SessionHandler(Looper l) {
            super(l);
        }

        @Override
        public void handleMessage(Message msg) {
            Session session;
            switch(msg.what) {
                case SESSION_EXPIR:
                    session = (Session) msg.obj;
                    if (session != null) {
                        String sid = session.getId();
                        delSession(sid);
                        log("session has expired, sid=" + sid);
                        // notify activity session expired
                        /******************
                         * We use Session.KEY_FINAL_RUNNABLE to add a final runnable
                         * manually which can do anything including notifying
                         ******************/
                    }
                    break;
            }
        }
    }
}
