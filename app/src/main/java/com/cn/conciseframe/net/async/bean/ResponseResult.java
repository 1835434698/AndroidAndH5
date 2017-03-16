package com.cn.conciseframe.net.async.bean;

import okhttp3.Response;

/**
 * Created by tangzy on 2016/7/6.
 */
public class ResponseResult {


    public static final String MSG_REFUSE = "正在处理请求中，请稍后再试。";

    public static final String STATUS_OK = "0"; // 操作成功

    public static final String STATUS_REFUSE = "-2"; // 重复操作

    public static final String STATUS_NET_ERROR = "1"; // 网络错误
    public static final String MSG_NET_ERROR = "网络错误，请稍后再试。";


    private String status;
    private String errorMsg;
    public volatile boolean isOperationed;
    private Response response;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setOperationed(boolean operationed) {
        isOperationed = operationed;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

}
