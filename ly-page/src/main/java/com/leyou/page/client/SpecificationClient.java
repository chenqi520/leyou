package com.leyou.page.client;

import com.leyou.item.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author cq
 * @create 2018-07-27 17:16
 * @copy alibaba
 */
//规格参数
@FeignClient("item-service")
public interface SpecificationClient extends SpecificationApi {
}
