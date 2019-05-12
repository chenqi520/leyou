package com.leyou.page.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author cq
 * @create 2018-07-29 17:17
 * @copy alibaba
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
