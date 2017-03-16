package com.cn.conciseframe.transmit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tangzy on 2016/9/20.
 */
public class Session {

    private final Map<String, Object> data = new ConcurrentHashMap<String, Object>();

    private final String sessionid;
    private long expired;

    public static final String KEY_FINAL_RUNNABLE = "_runnable_when_clear";

    /* package */Session(String sid, long exp) {
        if (sid == null) {
            throw new RuntimeException(
                    "session id created for this session is null");
        }
        sessionid = sid;
        expired = exp;
    }

    public String getId() {
        return sessionid;
    }

    public long getExpired() {
        return expired;
    }

    /* package */void resetExpired(long ex) {
        expired = ex;
    }

    public void put(String key, Object value) {
        if (value != null) {
            data.put(key, value);
        }
    }

    public Object get(String key) {
        return data.get(key);
    }

    public Object remove(String key) {
        return data.remove(key);
    }

    /**
     * 将session中所缓存的runnable取出运行，并清空session中的data数据集合，相当于将该session清理干净，仅保留sessionid和expired两个属性
     */
    public void clear() {
        if (data.containsKey(KEY_FINAL_RUNNABLE)) {
            Object obj = data.get(KEY_FINAL_RUNNABLE);
            if (obj != null && obj instanceof Runnable) {
                ((Runnable) obj).run();
            }
        }
        data.clear();
    }

    /**
     * 销毁该session
     */
    public void destroy() {
        destroy(false);
    }

    /**
     * 销毁该session
     * @param isLoginSession 该条session是否为LoginSession
     */
    public void destroy(boolean isLoginSession) {
        SessionManager.delSession(sessionid,isLoginSession);
    }

    public String getString(String key) {
        try {
            Object s = get(key);
            if (s == null) {
                return null;
            }
            return String.valueOf(get(key));
        } catch (Exception e) {
            return null;
        }
    }

    public boolean test(String key, Object obj) {
        if (obj != null) {
            Object dest = get(key);
            return obj == dest || obj.equals(dest);
        }
        return false;
    }

    public int getInt(String key, int def) {
        try {
            Object obj = get(key);
            if (obj instanceof Number) {
                return (Integer) obj;
            }
            return Integer.parseInt((String) obj);
        } catch (Exception e) {
            return def;
        }
    }

    public long getLong(String key, int def) {
        try {
            Object obj = get(key);
            if (obj instanceof Number) {
                return (Long) obj;
            }
            return Long.parseLong((String) obj);
        } catch (Exception e) {
            return def;
        }
    }

    public boolean getBoolean(String key) {
        Object obj = get(key);
        if (obj == null) {
            return false;
        }
        return (Boolean) obj;
    }

    public boolean getBoolean(String key, boolean def) {
        Object obj = get(key);
        if (obj == null) {
            return def;
        }
        return (Boolean) obj;
    }

}
