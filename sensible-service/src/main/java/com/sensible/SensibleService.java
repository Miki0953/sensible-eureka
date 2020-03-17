package com.sensible;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author liuyc
 * @Description: 服务提供者实例启动类
 * @date 2020/3/11 001115:59
 */
@EnableDiscoveryClient
@SpringBootApplication
public class SensibleService {
    public static void main(String[] args) {
        SpringApplication.run(SensibleService.class,args);
    }
}
