package com.mornsnow.common.util.excel;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 利用开源组件POI 动态导出EXCEL文档
 *
 * @param <T> 应用泛型，代表任意一个符合javabean风格的类 注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx() byte[]表jpg格式的图片数据
 */
public class ExportExcel<T> {

    private String excelType = "2007";
    private String name;
    private List<SheetData> sheetDatas;
    private List<Sheet> sheets;
    private static String defaultSheetTitle = "sheet";
    private int sheetIndex = 1;
    private Set<String> titleSet = new HashSet<>();

    private Workbook workbook;

    public List<SheetData> getSheetDatas() {
        return sheetDatas;
    }

    public void setSheetDatas(List<SheetData> sheetDatas) {
        this.sheetDatas = sheetDatas;

        buildExcel();
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    private int maxRow;

    public String getExcelType() {
        return excelType;
    }

    public void setExcelType(String excelType) {
        this.excelType = excelType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Sheet> getSheets() {
        return sheets;
    }

    public void setSheets(List<Sheet> sheets) {
        this.sheets = sheets;
    }

    public int getMaxRow() {
        return maxRow;
    }

    public void setMaxRow(int maxRow) {
        this.maxRow = maxRow;
    }

    /**
     * @param excelType (2003/2007)
     */
    public ExportExcel(String excelType) {
        this.excelType = excelType;
    }

    public ExportExcel() {
        init();
    }

    private void createFistRow(int sheetIndex, List<String> headers) {
        // 产生表格标题行
        Row row = sheets.get(sheetIndex).createRow(0);
        for (short i = 0; i < headers.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(headers.get(i));
        }
    }

    private void buildExcel() {
        for (int i = 0; i < sheetDatas.size(); i++) {
            createSheet(sheetDatas.get(i).getSheetTitle(), i, sheetDatas.get(i));
        }
    }

    private void createSheet(String title, int index, SheetData sheetData) {

        if (StringUtils.isEmpty(title) || !titleSet.add(title)) {
            title = defaultSheetTitle + "_" + (sheetIndex++);
        }

        Sheet sheet = workbook.createSheet(title);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth((short) 15);
        sheets.add(sheet);

        setData(sheetData.getDataList(), index);
    }

    @SuppressWarnings("rawtypes")
    private void setData(List<List<CellInfo>> dataList, int sheetIndex) {
        int size = dataList.size();
        Row row;

        SheetData sheetData = sheetDatas.get(sheetIndex);
        List<String> headers = sheetData.getHeaders();

        row = sheets.get(sheetIndex).createRow(0);
        CellStyle style2 = getStyle2(workbook);
        for (int i = 0; i < headers.size(); i++) {
            Cell cell = row.createCell(i);
            CellInfo info = new CellInfo();
            info.setValue(headers.get(i));
            cell.setCellStyle(style2);
            handelCellValue(cell, info);
        }

        for (int rowIndex = 0; rowIndex < size; rowIndex++) {
            row = sheets.get(sheetIndex).createRow(rowIndex + 1);

            List<CellInfo> cellList = dataList.get(rowIndex);
            for (int i = 0; i < cellList.size(); i++) {
                CellInfo cellInfo = cellList.get(i);


                Cell cell = row.createCell(i);
                handelCellValue(cell, cellInfo);
            }
        }
    }

    public void export(OutputStream out) throws Exception {
        workbook.write(out);
    }

    private void handelCellValue(Cell cell, CellInfo cellInfo) {

        Object value = cellInfo.getValue();
        String textValue = value == null ? "" : value.toString();
        String type = cellInfo.getType() == null ? "" : cellInfo.getType();
        switch (type) {
            case "number":
                cell.setCellValue(Double.parseDouble(textValue));
                break;
            case "date":
                Date date = (Date) value;
                String format = null;
                if (StringUtils.isEmpty(cellInfo.getFormat())) {
                    format = "yyyy-MM-dd hh:ssmm";
                }
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                textValue = sdf.format(date);
                cell.setCellValue(textValue);
                break;
            default:
                cell.setCellValue(textValue);
                break;
        }

    }

    /**
     * 生成一个样式
     */
    private CellStyle getStyle1(Workbook workbook) {
        // 生成一个样式
        CellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColorPredefined.DARK_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        // 生成一个字体
        Font font = workbook.createFont();
        font.setColor(HSSFColorPredefined.BLACK.getIndex());
        font.setFontHeightInPoints((short) 12);
        font.setBold(true);
        // 把字体应用到当前的样式
        style.setFont(font);
        return style;
    }

    /**
     * 生成并设置另一个样式
     */
    private CellStyle getStyle2(Workbook workbook) {
        // 生成并设置另一个样式
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColorPredefined.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成另一个字体
        Font font = workbook.createFont();
        // font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        font.setBold(true);
        // 把字体应用到当前的样式
        style.setFont(font);
        return style;
    }

    private void init() {
        workbook = new SXSSFWorkbook();
        maxRow = 65500;
        sheets = new ArrayList<>();
    }
}
