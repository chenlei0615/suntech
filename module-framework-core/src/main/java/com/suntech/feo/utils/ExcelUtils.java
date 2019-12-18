package com.suntech.feo.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.List;
import java.util.Map;

/**
 * @Project : sun-tech
 * @Package Name : com.suntech.feo.utils
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 22:01
 * ------------    --------------    ---------------------------------
 */
public class ExcelUtils {
    /**
     * @param workbook
     * @param sheetNum   (sheet的位置，0表示第一个表格中的第一个sheet)
     * @param sheetTitle （sheet的名称）
     * @param rowTitles  （表格的列标题）
     * @param headers    （表格的列标字段）
     * @param result     （表格的数据）
     * @param desc       （表格的描述）
     * @throws Exception
     * @Description: 导出Excel
     */
    public static void exportExcel(HSSFWorkbook workbook, int sheetNum, String sheetTitle, int[] mergeIndex,
                                   String[] rowTitles, String[] headers, List<Map<String, Object>> result, String desc) {
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(sheetNum, sheetTitle);
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth((short) 20);

        int startRow = 0;

        if (mergeIndex != null) {
            sheet.addMergedRegion(new CellRangeAddress(mergeIndex[0], mergeIndex[1], mergeIndex[2], mergeIndex[3]));
        }

        // 产生表格描述
        if (StringUtils.isNotBlank(desc)) {
            startRow = 1;
            HSSFRow row = sheet.createRow(0);
            row.setHeight((short) 600);
            HSSFCell cellTitle = row.createCell(0);
            cellTitle.setCellStyle(genExcelSheetTitleStyle(workbook));
            cellTitle.setCellValue(desc);
        }

        // 产生表格字段名称
        HSSFRow row0 = sheet.createRow(startRow);
        HSSFCellStyle cellStyle1 = genExcelSheetSecondTitleStyle(workbook);
        for (int i = 0; i < rowTitles.length; i++) {
            HSSFCell cellHeader = row0.createCell((short) i);
            cellHeader.setCellStyle(cellStyle1);
            HSSFRichTextString text = new HSSFRichTextString(rowTitles[i]);
            cellHeader.setCellValue(text.toString());
        }
        HSSFCellStyle cellStringStyle = genExcelSheetStringDataStyle(workbook);
        HSSFCellStyle cellNumricStyle = genExcelSheetNumricDataStyle(workbook);

        //data是否为数值型
        Boolean isNum = false;
        //data是否为百分数
        Boolean isPercent = false;
        // 设置列值-内容
        if (result != null) {
            for (int i = 0; i < result.size(); i++) {
                HSSFRow row1 = sheet.createRow(i + startRow + 1);
                if (row1 != null) {
                    for (int j = 0; j < headers.length; j++) {
                        Map map = result.get(i);
                        Object data = map.get(headers[j]);
                        HSSFCell cell = row1.createCell(j);
                        if (data != null || "".equals(data)) {
                            //判断data是否为数值型
                            isNum = data.toString().matches("^(-?\\d+)(\\.\\d+)?$");
                            //判断data是否为百分数（是否包含“%”）
                            isPercent = data.toString().contains("%");
                        }
                        if (isNum || isPercent) {
                            cell.setCellStyle(cellNumricStyle);
                        } else {
                            cell.setCellStyle(cellStringStyle);
                        }
                        CellUtil.setCellValue(cell, data);
                    }
                }
            }
            if (sheetNum == 1) {
                sheet.setColumnWidth(10, 100 * 256);
            }
        }
    }


    /**
     * 自定义生成sheet页title
     *
     * @param workbook
     * @return
     */
    public static HSSFCellStyle genExcelSheetTitleStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setFont(genExcelFont(workbook));
        style.setFillForegroundColor(IndexedColors.LIME.index);
        HSSFPalette customPalette = workbook.getCustomPalette();
        customPalette.setColorAtIndex(IndexedColors.LIME.index, (byte) 235, (byte) 247, (byte) 255);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.index);
        font.setFontHeightInPoints((short) 10);
        font.setBold(true);

        // 把字体应用到当前的样式
        style.setFont(font);

        // 指定当单元格内容显示不下时自动换行
        style.setWrapText(true);
        return style;
    }

    /**
     * 自定义生成sheet页2级title
     *
     * @param workbook
     * @return
     */
    public static HSSFCellStyle genExcelSheetSecondTitleStyle(HSSFWorkbook workbook) {
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFont(genExcelFont(workbook));
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        HSSFPalette customPalette = workbook.getCustomPalette();
        customPalette.setColorAtIndex(IndexedColors.LIGHT_YELLOW.getIndex(), (byte) 106, (byte) 173, (byte) 251);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 15);
        // 把字体应用到当前的样式
        style.setFont(font);

        // 指定当单元格内容显示不下时自动换行
        style.setWrapText(true);
        return style;
    }

    /**
     * 自定义生成sheet页数据样式
     *
     * @param workbook
     * @return
     */
    private static HSSFCellStyle genExcelSheetStringDataStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();

        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        // 把字体应用到当前的样式
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIME.index);
        HSSFPalette customPalette = workbook.getCustomPalette();
        customPalette.setColorAtIndex(IndexedColors.LIME.index, (byte) 255, (byte) 255, (byte) 255);
        // 指定当单元格内容显示不下时自动换行
        style.setWrapText(true);
        return style;
    }


    /**
     * 自定义生成sheet页数据样式
     *
     * @param workbook
     * @return
     */
    public static HSSFCellStyle genExcelSheetNumricDataStyle(HSSFWorkbook workbook) {
        HSSFCellStyle style = workbook.createCellStyle();

        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        // 把字体应用到当前的样式
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        style.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        HSSFPalette customPalette = workbook.getCustomPalette();
        customPalette.setColorAtIndex(IndexedColors.LIGHT_TURQUOISE.getIndex(), (byte) 255, (byte) 255, (byte) 255);
        // 指定当单元格内容显示不下时自动换行
        style.setWrapText(true);
        return style;
    }

    /**
     * 自定义字体
     *
     * @param workbook
     * @return
     */
    public static Font genExcelFont(HSSFWorkbook workbook) {
        Font font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 16);//设置字体大小
        return font;
    }


}
