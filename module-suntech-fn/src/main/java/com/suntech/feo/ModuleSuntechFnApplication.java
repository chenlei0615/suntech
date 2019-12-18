package com.suntech.feo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EntityScan
public class ModuleSuntechFnApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleSuntechFnApplication.class, args);
    }

}
