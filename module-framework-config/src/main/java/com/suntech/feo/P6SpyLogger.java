package com.suntech.feo;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Project : suntech
 * @Package Name : com.suntech.feo
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 12:40
 * ------------    --------------    ---------------------------------
 */
public class P6SpyLogger implements MessageFormattingStrategy {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql) {
        return !"".equals(sql.trim()) ? this.format.format(new Date()) + " | took " + elapsed + "ms | " + category + " | connection " + connectionId + "\n " + sql + ";" : "";
    }
}
