package com.suntech.feo.ip;

import com.suntech.feo.interceptor.IpAddressInterceptor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @Project : sun-tech
 * @Package Name : com.suntech.feo.annotation
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月19日 14:35
 * ------------    --------------    ---------------------------------
 */
public class LoadIpWhiteRegexTask {
    private static final Logger logger = LoggerFactory.getLogger(LoadIpWhiteRegexTask.class);
    @Autowired

//    @Scheduled(fixedRate = 600000)
    public void reloadIpWhiteRule() {
        String value = "10.247.67.10,10.22.33.44";
        if (StringUtils.isNotBlank(value)) {
            // 可在数据库配置 定时拉取
            String splitRegex = ",";
            String[] rules = value.split(splitRegex);
            IpAddressInterceptor.urlRules = Arrays.asList(rules);
            logger.debug("reload ip white rule regex success: {}" ,IpAddressInterceptor.urlRules);
        } else {
            IpAddressInterceptor.urlRules = new ArrayList<>();
        }
    }
}
