package com.mornsnow.common.basic.rest;

/**
 * restful接口通用错误码
 * 参照http状态码
 *
 * @author jianyang 2018/8/23
 */
public class BaseErrorCode {
    /**
     * 请求成功
     */
    public static final Integer SUCCESS = 200;

    /**
     * 参数错误
     */
    public static final Integer BAD_REQUEST = 400;

    /**
     * 无权访问
     */
    public static final Integer UNAUTHORIZED = 401;

    /**
     * 系统异常
     */
    public static final Integer SYSTEM_ERROR = 500;


}
