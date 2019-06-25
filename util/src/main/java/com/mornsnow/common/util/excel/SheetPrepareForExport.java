package com.mornsnow.common.util.excel;

import java.util.List;

/**
 * 利用开源组件POI 动态导出EXCEL文档
 *
 */
public class SheetPrepareForExport {
    private List<String> header;
    private List<List<String>> dataList;
    private String sheetName;

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public SheetPrepareForExport(List<String> header, List<List<String>> dataList) {
        this.header = header;
        this.dataList = dataList;
    }

    public SheetPrepareForExport(List<String> header, List<List<String>> dataList, String sheetName) {
        this.header = header;
        this.dataList = dataList;
        this.sheetName = sheetName;
    }
    public List<String> getHeader() {
        return header;
    }

    public void setHeader(List<String> header) {
        this.header = header;
    }

    public List<List<String>> getDataList() {
        return dataList;
    }

    public void setDataList(List<List<String>> dataList) {
        this.dataList = dataList;
    }
}
