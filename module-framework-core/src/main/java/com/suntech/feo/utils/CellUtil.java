package com.suntech.feo.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

/**
 * @Project : sun-tech
 * @Package Name : com.suntech.feo.utils
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 22:15
 * ------------    --------------    ---------------------------------
 */
public class CellUtil {

    //避免cell.setCellValue(checkOrderQmSave.getSellOrderNo())中参数为空就会报错
    public static void setCellValue(HSSFCell cell, Object data){
        //此处设置数据格式
        if(data == null){
            cell.setCellValue("--");
        }else{
            if (data instanceof String) {
                cell.setCellValue(data.toString());
            }else if(data instanceof Long){
                Long temp = (Long)data;
                String cellvalue = new DecimalFormat().format(temp.doubleValue());
                cell.setCellValue(cellvalue);
            }else if(data instanceof Double){
                Double temp = (Double)data;
                String cellvalue = new DecimalFormat().format(temp.doubleValue());
                cell.setCellValue(cellvalue);
            }else if(data instanceof Float){
                Float temp = (Float)data;
                String cellvalue = new DecimalFormat().format(temp.doubleValue());
                cell.setCellValue(cellvalue);
            }else if(data instanceof Integer){
                Integer temp = (Integer)data;
                cell.setCellValue(temp.intValue());
            }else if(data instanceof BigDecimal){
                BigDecimal temp = (BigDecimal)data;
                String cellvalue = new DecimalFormat().format(temp.doubleValue());
                cell.setCellValue(cellvalue);
            }else{
                cell.setCellValue("--");
            }
        }
    }
    public static void setCellValue(HSSFCell cell, Object object, String model){
        if(object == null){
            cell.setCellValue("");
        }else{
            if (object instanceof String) {
                cell.setCellValue(String.valueOf(object));
            }else if(object instanceof Long){
                Long temp = (Long)object;
                String cellvalue = new DecimalFormat("#0.00").format(temp.doubleValue());
                cell.setCellValue(cellvalue);
            }else if(object instanceof Double){
                Double temp = (Double)object;
                String cellvalue = new DecimalFormat("#0.00").format(temp.doubleValue());
                cell.setCellValue(cellvalue);
            }else if(object instanceof Float){
                Float temp = (Float)object;
                String cellvalue = new DecimalFormat("#0.00").format(temp.doubleValue());
                cell.setCellValue(cellvalue);
            }else if(object instanceof Integer){
                Integer temp = (Integer)object;
                cell.setCellValue(temp.intValue());
            }else if(object instanceof BigDecimal){
                BigDecimal temp = (BigDecimal)object;
                String cellvalue = new DecimalFormat("#0.00").format(temp.doubleValue());
                cell.setCellValue(cellvalue);
            }else if(object instanceof Date){
                cell.setCellValue(new SimpleDateFormat(model).format(object));
            }else if(object instanceof java.util.Date){
                cell.setCellValue(new SimpleDateFormat(model).format(object));
            }else{
                cell.setCellValue("");
            }
        }
    }
    public static void setCellValue(HSSFCell cell, String object){
        if(object == null){
            cell.setCellValue("");
        }else{
            cell.setCellValue(object);
        }
    }
    public static void setCellValue(HSSFCell cell, Long object){
        if(object == null){
            cell.setCellValue("");
        }else{
            cell.setCellValue(object.doubleValue());
        }
    }
    public static void setCellValue(HSSFCell cell, Double object){
        if(object == null){
            cell.setCellValue("");
        }else{
            cell.setCellValue(object.doubleValue());
        }
    }
    public static void setCellValue(HSSFCell cell, double object){

        cell.setCellValue(object);

    }
    public static void setCellValue(HSSFCell cell, Date object, String model){
        if(object == null){
            cell.setCellValue("");
        }else{
            cell.setCellValue(new SimpleDateFormat(model).format(object));
        }
    }
    public static void setCellValue(HSSFCell cell, java.util.Date object, String model){
        if(object == null){
            cell.setCellValue("");
        }else{
            cell.setCellValue(new SimpleDateFormat(model).format(object));
        }
    }
    public static void setCellValue(HSSFCell cell, BigDecimal object){
        if(object == null){
            cell.setCellValue("");
        }else{
            cell.setCellValue(object.toString());
        }
    }

    //判断EXCEL表格高度用 row.setHeight((short) CellUtil.getExcelCellAutoHeight(TAR_VAL_ALL_STRING, 280, 30));
    public static float getExcelCellAutoHeight(String str,float defaultRowHeight, int fontCountInline) {
        int defaultCount = 0;
        for (int i = 0; i < str.length(); i++) {
            int ff = getregex(str.substring(i, i + 1));
            defaultCount = defaultCount + ff;
        }
        if (defaultCount > fontCountInline){
            return ((defaultCount / fontCountInline) + 1) * defaultRowHeight;//计算
        } else {
            return defaultRowHeight;
        }
    }
    public static int getregex(String charStr) {
        if("".equals(charStr) || charStr == null){
            return 1;
        }
        // 判断是否为字母或字符
        if (Pattern.compile("^[A-Za-z0-9]+$").matcher(charStr).matches()) {
            return 1;
        }
        // 判断是否为全角
        if (Pattern.compile("[\u4e00-\u9fa5]+$").matcher(charStr).matches()) {
            return 2;
        }
        //全角符号 及中文
        if (Pattern.compile("[^x00-xff]").matcher(charStr).matches()) {
            return 2;
        }
        return 1;
    }
}
