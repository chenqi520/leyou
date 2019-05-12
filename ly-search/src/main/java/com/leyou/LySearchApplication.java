package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author cq
 * @create 2018-07-26 22:05
 * @copy alibaba
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients //远程调用Feign的客户端
public class LySearchApplication {

    public static void main(String[] args) {

        SpringApplication.run(LySearchApplication.class,args);
    }
}
