package com.mornsnow.common.util.excel;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <strong>描述：</strong> <br>
 * <strong>功能：</strong><br>
 * <strong>使用场景：</strong><br>
 * <strong>注意事项：</strong>
 * <ul>
 * <li></li>
 * </ul>
 *
 * @author jianyang 2017/8/15
 */
@Component
public class ExcelReaderUtils implements InitializingBean {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");


    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelExportUtils.class);


    public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
    public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

    private static List<String> emptyList = new ArrayList<>();

    /**
     * 读取第一个sheet，每行作为一个内层list
     *
     * @param file        文件
     * @param ignoreTitle 是否忽略标题（第一行）
     * @return
     * @throws IOException
     */
    public static List<List<String>> readFirstSheetAsList(MultipartFile file, boolean ignoreTitle) throws IOException {
        Map<String, List<List<String>>> resultMap = read(file, ignoreTitle, false);
        return resultMap.values().iterator().next();
    }

    /**
     * 读取第一个sheet，每行数据以第一行对应列的值为key，组成map
     *
     * @param file 文件
     * @return
     * @throws IOException
     */
    public static List<Map<String, String>> readFirstSheetAsMap(MultipartFile file) throws IOException {
        Map<String, List<List<String>>> resultMap = read(file, false, false);
        List<List<String>> dataList = resultMap.values().iterator().next();
        if (CollectionUtils.isEmpty(dataList)) {
            return null;
        }
        return list2Map(dataList);
    }

    /**
     * 读取所有sheet
     *
     * @param file        文件
     * @param ignoreTitle 是否忽略标题（第一行）
     * @return Map<Sheet名称,数据List>
     * @throws IOException
     */
    public static Map<String, List<List<String>>> readAllSheetsAsList(MultipartFile file, boolean ignoreTitle) throws IOException {
        return read(file, ignoreTitle, true);
    }

    /**
     * 读取所有sheet
     *
     * @param file 文件
     * @return Map<Sheet名称,数据Map>
     * @throws IOException
     */
    public static Map<String, List<Map<String, String>>> readAllSheetsAsMap(MultipartFile file) throws IOException {
        Map<String, List<List<String>>> dataMap = read(file, false, true);

        Map<String, List<Map<String, String>>> resultMap = new HashMap<>();
        for (String key : dataMap.keySet()) {
            resultMap.put(key, list2Map(dataMap.get(key)));
        }

        return resultMap;
    }

    private static List<Map<String, String>> list2Map(List<List<String>> dataList) {
        if (CollectionUtils.isEmpty(dataList) || dataList.size() <= 1) {
            return new ArrayList<>();
        }
        List<Map<String, String>> resultList = new ArrayList<>();
        List<String> keyList = dataList.get(0);
        for (int i = 1; i < dataList.size(); i++) {
            Map<String, String> map = new HashMap<>();
            List<String> cellList = dataList.get(i);
            int cellCount = CollectionUtils.isNotEmpty(cellList) ? cellList.size() : -1;
            for (int j = 0; j < keyList.size(); j++) {
                if (j <= cellCount - 1) {
                    map.put(keyList.get(j), cellList.get(j));
                } else {
                    map.put(keyList.get(j), "");
                }
            }
            if (!map.isEmpty()) {
                resultList.add(map);
            }
        }

        return resultList;
    }

    private static Map<String, List<List<String>>> read(MultipartFile file, boolean ignoreTitle, boolean allSheets) throws IOException {
        if (file == null || file.getOriginalFilename().trim().length() == 0) {
            return null;
        } else {
            int startRow = ignoreTitle ? 1 : 0;
            String postfix = getPostfix(file.getOriginalFilename());
            if (StringUtils.isNotBlank(postfix)) {
                if (OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
                    return readXls(file.getInputStream(), startRow, allSheets);
                } else if (OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
                    return readXlsx(file.getInputStream(), startRow, allSheets);
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * read the Excel 2010 .xlsx
     *
     * @return
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public static Map<String, List<List<String>>> readXlsx(InputStream input, int startRow, boolean allSheets) {
        Map<String, List<List<String>>> resultMap = new HashMap<>();

        // IO流读取文件
        XSSFWorkbook wb;
        List<String> rowList;
        try {
            // 创建文档
            wb = new XSSFWorkbook(input);
            //读取sheet(页)
            for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
                if (!allSheets && numSheet > 0) {
                    break;
                }
                XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
                if (xssfSheet == null) {
                    continue;
                }
                List<List<String>> list = new ArrayList<>();
                resultMap.put(xssfSheet.getSheetName(), list);

                XSSFRow firstRow = xssfSheet.getRow(0);

                int size = firstRow.getLastCellNum();

                Iterator rowItr = xssfSheet.rowIterator();

                for (int i = 0; i < startRow; i++) {
                    if (rowItr.hasNext()) {
                        rowItr.next();
                    } else {
                        break;
                    }
                }

                while (rowItr.hasNext()) {
                    XSSFRow xssfRow = (XSSFRow) rowItr.next();
                    if (xssfRow != null) {
                        rowList = new ArrayList<>();
                        int totalCells = xssfRow.getLastCellNum();
                        int ct = 0;
                        for (int c = 0; c <= totalCells - 1; c++) {
                            XSSFCell cell = xssfRow.getCell(c);
                            if (cell == null) {
                                rowList.add("");
                            } else {
                                String val = getXValue(cell).trim();
                                rowList.add(val);
                                if (StringUtils.isNotBlank(val)) {
                                    ct++;
                                }
                            }
                        }

                        if (ct > 0) {
                            pad(rowList, size);
                            list.add(rowList);
                        }
                    }
                }

            }
            return resultMap;
        } catch (IOException e) {
            LOGGER.error("read excel faild", e);
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                LOGGER.error("read excel faild", e);
            }
        }
        return null;

    }

    /**
     * read the Excel 2003-2007 .xls
     *
     * @return
     * @throws IOException
     */
    public static Map<String, List<List<String>>> readXls(InputStream inputStream, int startRow, boolean allSheets) {

        Map<String, List<List<String>>> resultMap = new HashMap<>();
        // IO流读取文件
        HSSFWorkbook wb;
        List<String> rowList;
        try {
            // 创建文档
            wb = new HSSFWorkbook(inputStream);
            //读取sheet(页)
            for (int numSheet = 0; numSheet < wb.getNumberOfSheets(); numSheet++) {
                if (!allSheets && numSheet > 0) {
                    break;
                }
                HSSFSheet hssfSheet = wb.getSheetAt(numSheet);
                if (hssfSheet == null) {
                    continue;
                }
                List<List<String>> list = new ArrayList<>();
                resultMap.put(hssfSheet.getSheetName(), list);
                HSSFRow firstRow = hssfSheet.getRow(0);
                int column = firstRow.getLastCellNum();

                Iterator rowItr = hssfSheet.rowIterator();

                for (int i = 0; i < startRow; i++) {
                    if (rowItr.hasNext()) {
                        rowItr.next();
                    } else {
                        break;
                    }
                }

                while (rowItr.hasNext()) {
                    HSSFRow xssfRow = (HSSFRow) rowItr.next();
                    if (xssfRow != null) {
                        rowList = new ArrayList<>();
                        int totalCells = xssfRow.getLastCellNum();
                        int ct = 0;
                        for (int c = 0; c <= totalCells - 1; c++) {
                            HSSFCell cell = xssfRow.getCell(c);
                            if (cell == null) {
                                rowList.add("");
                            } else {
                                String val = getHValue(cell).trim();
                                rowList.add(val);
                                if (StringUtils.isNotBlank(val)) {
                                    ct++;
                                }
                            }
                        }

                        if (ct > 0) {
                            pad(rowList, column);
                            list.add(rowList);
                        }
                    }
                }
            }
            return resultMap;
        } catch (IOException e) {
            LOGGER.error("read excel faild", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                LOGGER.error("read excel faild", e);
            }
        }
        return null;
    }

    /**
     * 获得path的后缀名
     *
     * @param path
     * @return
     */
    private static String getPostfix(String path) {
        if (net.logstash.logback.encoder.org.apache.commons.lang.StringUtils.isBlank(path) || !path.contains(".")) {
            return "";
        }
        return path.substring(path.lastIndexOf(".") + 1, path.length());
    }


    /**
     * 单元格格式
     *
     * @param hssfCell
     * @return
     */
    private static String getHValue(HSSFCell hssfCell) {
        if (hssfCell.getCellTypeEnum().equals(CellType.BOOLEAN)) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellTypeEnum().equals(CellType.NUMERIC)) {
            String cellValue;
            if (HSSFDateUtil.isCellDateFormatted(hssfCell)) {
                Date date = HSSFDateUtil.getJavaDate(hssfCell.getNumericCellValue());
                cellValue = sdf.format(date);
            } else {
                cellValue = formatCellNumberValue(hssfCell.getNumericCellValue());
            }
            return cellValue;
        } else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }

    private static String formatCellNumberValue(double value) {
        return value % 1 > 0 ? value + "" : Double.valueOf(value).intValue() + "";
    }

    /**
     * 单元格格式
     *
     * @param xssfCell
     * @return
     */
    private static String getXValue(XSSFCell xssfCell) {
        if (xssfCell.getCellTypeEnum().equals(CellType.BOOLEAN)) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellTypeEnum().equals(CellType.NUMERIC)) {
            String cellValue;
            if (XSSFDateUtil.isCellDateFormatted(xssfCell)) {
                Date date = XSSFDateUtil.getJavaDate(xssfCell.getNumericCellValue());
                cellValue = sdf.format(date);
            } else {
                cellValue = formatCellNumberValue(xssfCell.getNumericCellValue());
            }
            return cellValue;
        } else {
            return String.valueOf(xssfCell.getStringCellValue());
        }
    }

    private static void pad(List<String> list, int size) {
        if (list.size() < size) {
            list.addAll(new ArrayList<String>(emptyList.subList(0, size - list.size())));
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        for (int i = 0; i < 100; i++) {
            emptyList.add("");
        }
    }
}

