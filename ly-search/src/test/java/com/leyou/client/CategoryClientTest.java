package com.leyou.client;

import com.leyou.item.pojo.Category;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;


/**
 * @author cq
 * @create 2018-07-27 16:05
 * @copy alibaba
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryClientTest {

    @Autowired
    private  CategoryClient categoryClient;

    @Test
    public void queryByIds() {
        List<Category> list = categoryClient.queryByIds(Arrays.asList(74L, 75L,76L));

        Assert.assertEquals(3,list.size());
    }
}