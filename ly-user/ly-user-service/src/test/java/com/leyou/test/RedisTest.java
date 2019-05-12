package com.leyou.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author cq
 * @create 2018-08-02 16:48
 * @copy alibaba
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private StringRedisTemplate redisTempla;

    @Test
    public void testRedis(){

        redisTempla.opsForValue().set("name","Rose");

        String name = redisTempla.opsForValue().get("name");

        System.out.println("name = " + name);

    }
}
