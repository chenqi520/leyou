package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


/**
 * @author cq
 * @create 2018-07-16 15:23
 * @copy alibaba
 */
@SpringBootApplication
@EnableEurekaServer
@EnableDiscoveryClient
public class LyRegistry {

    public static void main(String[] args) {

        SpringApplication.run(LyRegistry.class,args);
    }

}
