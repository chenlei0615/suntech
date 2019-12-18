package com.suntech.feo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Project : sun-tech
 * @Package Name : com.suntech.feo
 * @Description : TODO
 * @Author : chenlei
 * @Create Date : 2019年12月18日 21:25
 * ------------    --------------    ---------------------------------
 */
@SpringBootApplication
@EnableScheduling
public class TaskApplication {
    public static void main(String[] args) {
        System.out.println(" The suntech schedule job is starting ...");
        SpringApplication.run(TaskApplication.class, args);
    }
}
