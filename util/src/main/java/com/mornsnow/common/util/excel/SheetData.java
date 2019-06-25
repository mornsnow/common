package com.mornsnow.common.util.excel;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * 利用开源组件POI 动态导出EXCEL文档
 *
 */
public class SheetData {

    private int maxRow;
    private int currentRow = 1;
    private String excelType = "2007";
    private Workbook workbook;
    private org.apache.poi.ss.usermodel.Sheet sheet;
    private List<String> headers;
    private String sheetTitle;
    private int sheetIndex = 1;
    private List<List<CellInfo>> dataList;         // 列信息


    public int getMaxRow() {
        return maxRow;
    }

    public void setMaxRow(int maxRow) {
        this.maxRow = maxRow;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public String getExcelType() {
        return excelType;
    }

    public void setExcelType(String excelType) {
        this.excelType = excelType;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public Sheet getSheet() {
        return sheet;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public String getSheetTitle() {
        return sheetTitle;
    }

    public void setSheetTitle(String sheetTitle) {
        this.sheetTitle = sheetTitle;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public List<List<CellInfo>> getDataList() {
        return dataList;
    }

    public void setDataList(List<List<CellInfo>> dataList) {
        this.dataList = dataList;
    }
}
