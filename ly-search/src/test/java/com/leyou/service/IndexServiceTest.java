package com.leyou.service;

import com.leyou.client.GoodsClient;
import com.leyou.item.pojo.Spu;
import com.leyou.pojo.Goods;
import com.leyou.pojo.PageResult;
import com.leyou.repository.GoodsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


/**
 * @author cq
 * @create 2018-07-27 17:22
 * @copy alibaba
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexServiceTest {

    @Autowired
    private ElasticsearchTemplate template;

    @Autowired
    //CRUD
    private GoodsRepository goodsRepository;

    @Autowired
    //spu的查询
    private GoodsClient goodsClient;

    @Autowired
    private  IndexService indexService;



    @Test
    public void  testCreatrIndex(){

        template.createIndex(Goods.class);
        template.putMapping(Goods.class);

    }

    @Test
    public  void loadData() {
        // 分页数据
        int page = 1;
        int rows = 100;
        int size = 0;
        do {
            // 查询spu
            PageResult<Spu> result = goodsClient.querySpuByPage(page, rows, null, true);
            List<Spu> spus = result.getItems();

            List<Goods> goodsList = new ArrayList<>();

            for (Spu spu : spus) {
                Goods goods = indexService.buildGoods(spu);
                goodsList.add(goods);
            }

            goodsRepository.saveAll(goodsList);

            size = spus.size();
            page++;
        } while (size == 100);
    }
}