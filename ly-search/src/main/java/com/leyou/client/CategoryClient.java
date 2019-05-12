package com.leyou.client;

import com.leyou.item.api.CategoryApi;
import com.leyou.item.pojo.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author cq
 * @create 2018-07-27 16:02
 * @copy alibaba
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {



    }
