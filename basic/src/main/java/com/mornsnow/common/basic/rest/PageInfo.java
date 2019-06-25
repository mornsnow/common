package com.mornsnow.common.basic.rest;

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
public class PageInfo {
    private Integer total;
    private Integer pageSize;
    private Integer currentPage;
    /**
     * 排序字段
     */
    private String orderBy;
    /**
     * DESC OR ASC
     */
    private String sortBy;

    public PageInfo() {
    }

    public PageInfo(int total) {
        this.total = total;
    }

    public PageInfo(int total, int currentPage, int pageSize) {
        this.total = total;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
