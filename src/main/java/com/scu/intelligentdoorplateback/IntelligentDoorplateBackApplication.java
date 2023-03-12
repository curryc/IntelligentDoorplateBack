package com.scu.intelligentdoorplateback;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.scu.intelligentdoorplateback.mapper"})
public class IntelligentDoorplateBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntelligentDoorplateBackApplication.class, args);
    }
}