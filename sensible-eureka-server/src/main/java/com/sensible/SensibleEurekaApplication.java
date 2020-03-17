package com.sensible;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author liuyc
 * @Description: EurekaServer启动类
 * @date 2020/3/11 001115:43
 */
@EnableEurekaServer
@SpringBootApplication
public class SensibleEurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(SensibleEurekaApplication.class,args);
    }
}
