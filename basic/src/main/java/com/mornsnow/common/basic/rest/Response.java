package com.mornsnow.common.basic.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <strong>描述：</strong> <br>
 * <strong>功能：</strong><br>
 * <strong>使用场景：</strong><br>
 * <strong>注意事项：</strong>
 * <ul>
 * <li></li>
 * </ul>
 *
 * @author jianyang 2017/6/19
 */
public class Response<T> {
    private boolean success;
    private Integer code;
    private String message;
    private T result;

    public static final String MESSAGE_SUCCESS = "SUCCESS";
    public static final String MESSAGE_ERROR = "ERROR";

    /**
     * @Deprecated replaced by BaseErrorCode.BAD_REQUEST
     */
    @Deprecated
    public static final int ERR_PARAMETER = 901;


    /**
     * @Deprecated replaced by BaseErrorCode.SYSTEM_ERROR
     */
    @Deprecated
    public static final int ERR_OTHER = 998;

    /**
     * @Deprecated replaced by BaseErrorCode.SYSTEM_ERROR
     */
    @Deprecated
    public static final int ERR_SYS = 999;

    /**
     * @Deprecated replaced by BaseErrorCode.SUCCESS
     */
    @Deprecated
    public static final int CODE_SUCCESS = 0;

    public static <T> Response<T> success() {
        return success(null);
    }

    public static <T> Response<T> success(T result) {
        Response<T> resp = new Response<>();
        resp.setCode(BaseErrorCode.SUCCESS);
        resp.setMessage(MESSAGE_SUCCESS);
        resp.setResult(result);
        resp.setSuccess(true);
        return resp;
    }

    public static <T> Response<ListResult<T>> success(List<T> result, PageInfo pageInfo) {
        Response<ListResult<T>> resp = new Response<>();
        resp.setCode(BaseErrorCode.SUCCESS);
        resp.setMessage(MESSAGE_SUCCESS);

        resp.setResult(new ListResult<T>(result, pageInfo));
        resp.setSuccess(true);
        return resp;
    }

    public static <T> Response<ListResult<T>> success(Set<T> result, PageInfo pageInfo) {
        return success(new ArrayList<T>(result), pageInfo);
    }

    public static <T> Response<T> error() {
        return error(ERR_SYS, MESSAGE_ERROR);
    }

    public static <T> Response<T> error(Integer errorCode) {
        return error(errorCode, MESSAGE_ERROR);
    }

    public static <T> Response<T> error(Integer errorCode, String message) {
        Response<T> resp = new Response<>();
        resp.setMessage(message);
        resp.setCode(errorCode);
        return resp;
    }


    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public static String getMessageSuccess() {
        return MESSAGE_SUCCESS;
    }

    public static String getMessageError() {
        return MESSAGE_ERROR;
    }

    public static int getCodeSuccess() {
        return BaseErrorCode.SUCCESS;
    }

}
