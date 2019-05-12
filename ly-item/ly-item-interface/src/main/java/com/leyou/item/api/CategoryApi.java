package com.leyou.item.api;

import com.leyou.item.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author cq
 * @create 2018-07-27 16:49
 * @copy alibaba
 */
public interface CategoryApi {

    @GetMapping("category/list/ids")
    List<Category> queryByIds(@RequestParam("ids") List<Long> ids );


}
