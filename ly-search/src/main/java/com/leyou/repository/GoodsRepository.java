package com.leyou.repository;

import com.leyou.pojo.Goods;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author cq
 * @create 2018-07-27 19:42
 * @copy alibaba
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods,Long> {
}
