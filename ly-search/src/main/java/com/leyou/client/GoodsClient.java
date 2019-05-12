package com.leyou.client;

import com.leyou.item.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author cq
 * @create 2018-07-27 17:07
 * @copy alibaba
 */

@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {
}
