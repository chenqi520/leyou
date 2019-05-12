package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author cq
 * @create 2018-07-16 15:50
 * @copy alibaba
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.leyou.item.mapper")//扫描mapper包
public class LyItemApplication {

    public static void main(String[] args) {

        SpringApplication.run(LyItemApplication.class,args);
    }
}