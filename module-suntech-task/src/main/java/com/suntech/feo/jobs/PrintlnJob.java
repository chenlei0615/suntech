package com.suntech.feo.jobs;

import com.suntech.feo.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Project : sun-tech
 * @Package Name : com.suntech.feo.jobs
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 21:06
 * ------------    --------------    ---------------------------------
 */
@Component
public class PrintlnJob {
    private static final Logger logger = LoggerFactory.getLogger(PrintlnJob.class);

//    @Scheduled(cron = "0/5 * * * * ?")
    public void printlnLog(){
        System.out.println("当前时间 :"+ DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
    }
}
