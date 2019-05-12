package com.leyou.item.controller;

import com.leyou.item.pojo.Category;
import com.leyou.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author cq
 * @create 2018-07-19 21:26
 * @copy alibaba
 */
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @GetMapping("list")
    public ResponseEntity<List<Category>>queryByParentId(@RequestParam("pid") Long pid ){

        List<Category> list = this.categoryService.queryByParentId(pid);

        if (list == null || list.size() < 1 ) {

            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);


        }
        return  ResponseEntity.ok(list);

    }

    @GetMapping("list/ids")
    public ResponseEntity<List<Category>>queryByIds(@RequestParam("ids") List<Long> ids ){

        List<Category> list= this.categoryService.queryCategoryByids(ids);


        if (list == null || list.size() < 1 ) {
            //为空返回404
            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);


        }
        return  ResponseEntity.ok(list);

    }


}

