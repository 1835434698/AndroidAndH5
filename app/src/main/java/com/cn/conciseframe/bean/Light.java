package com.cn.conciseframe.bean;

/**
 * Created by tangzy on 2016/9/12.
 */
public enum Light {
    // 利用构造函数传参
    RED (1), GREEN (3), YELLOW (2);

    // 定义私有变量
    private   int   nCode ;

    // 构造函数，枚举类型只能为私有
    private Light( int _nCode) {
        this . nCode = _nCode;
    }

    @Override
    public String toString() {
        return String.valueOf ( this . nCode );
    }

}
