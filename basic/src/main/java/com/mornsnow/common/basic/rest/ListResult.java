package com.mornsnow.common.basic.rest;

import java.util.List;

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
public class ListResult<T> {

    private List<T> data;
    private PageInfo pageInfo;

    public ListResult() {

    }

    public ListResult(List<T> result, PageInfo pageInfo) {
        this.data = result;
        this.pageInfo = pageInfo;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
