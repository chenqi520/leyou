package com.leyou.page.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author cq
 * @create 2018-07-27 16:02
 * @copy alibaba
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {



    }
