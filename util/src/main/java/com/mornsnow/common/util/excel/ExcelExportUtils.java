package com.mornsnow.common.util.excel;

import com.mornsnow.common.util.base.FileUtil;
import com.mornsnow.common.util.base.OssServiceUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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
 * @author jianyang 2018/3/8
 */
public class ExcelExportUtils {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelExportUtils.class);


    public static String export(String chartTitle, List<String> header, List<List<String>> dataList) {
        ExportExcel<Map> export = new ExportExcel<>();

        SheetData sheetData = new SheetData();
        sheetData.setSheetTitle("DATA");
        sheetData.setHeaders(header);
        List<List<CellInfo>> resultList = new ArrayList<>();

        for (int i = 0; i < dataList.size(); i++) {
            List<CellInfo> cellInfoList = new ArrayList<>();
            for (String str : dataList.get(i)) {
                CellInfo cellInfo = new CellInfo();
                cellInfo.setValue(str);
                cellInfoList.add(cellInfo);
            }
            resultList.add(cellInfoList);
        }

        sheetData.setDataList(resultList);
        export.setSheetDatas(Arrays.asList(sheetData));
        return export(chartTitle, export);
    }

    public static String export(String chartTitle, List<SheetPrepareForExport> sheets) {
        ExportExcel<Map> export = new ExportExcel<>();
        List<SheetData> sheetList = new ArrayList<>();
        for(SheetPrepareForExport sheet : sheets) {
            SheetData sheetData = new SheetData();
            sheetData.setSheetTitle(StringUtils.isEmpty(sheet.getSheetName()) ? "DATA" : sheet.getSheetName());
            sheetData.setHeaders(sheet.getHeader());
            List<List<CellInfo>> resultList = new ArrayList<>();
            for (int i = 0; i < sheet.getDataList().size(); i++) {
                List<CellInfo> cellInfoList = new ArrayList<>();
                for (String str : sheet.getDataList().get(i)) {
                    CellInfo cellInfo = new CellInfo();
                    cellInfo.setValue(str);
                    cellInfoList.add(cellInfo);
                }
                resultList.add(cellInfoList);
            }

            sheetData.setDataList(resultList);
            sheetList.add(sheetData);
        }

        export.setSheetDatas(sheetList);
        return export(chartTitle, export);
    }

    public static String export(String chartTitle, ExportExcel<Map> export) {
        File file = null;
        OutputStream out = null;
        String ossFileUrl = null;

        try {

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

            String fileName = chartTitle + "(" + format.format(new Date()) + ")" + ".xlsx";
            String excelPath = FileUtil.getSystemPath(fileName);
            export.setName(fileName);

            file = new File(excelPath);
            if (Files.notExists(Paths.get(file.getParent()))) {
                Files.createDirectory(Paths.get(file.getParent()));
            }
            out = new FileOutputStream(excelPath);
            export.export(out);
            OssServiceUtil ossServiceUtil = OssServiceUtil.getInstance();

            int i = 0;
            while (ossFileUrl == null && i < 3) {
                i++;
                try {
                    ossFileUrl = ossServiceUtil.uploadFileAndGetUrl(excelPath);
                } catch (Exception e) {
                    LOG.error("export excel faild", e);
                }
            }
        } catch (Exception e) {
            LOG.error("export excel faild", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (file != null) {
                    file.delete();
                }
            } catch (IOException e) {
                LOG.error("export excel faild", e);
            }
        }

        return ossFileUrl;
    }
}
