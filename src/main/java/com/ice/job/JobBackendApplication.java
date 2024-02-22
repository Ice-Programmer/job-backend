package com.ice.job;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ice.job.mapper")
public class JobBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobBackendApplication.class, args);
    }

}
