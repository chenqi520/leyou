package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author cq
 * @create 2018-07-30 16:32
 * @copy alibaba
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class LyPageApplication {

    public static void main(String[] args) {

        SpringApplication.run(LyPageApplication.class,args);

    }
}
