package com.leyou.item.service;

import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cq
 * @create 2018-07-19 21:43
 * @copy alibaba
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    public List<Category> queryByParentId(Long pid) {

        Category t = new Category();
        t.setParentId(pid);

        return  this.categoryMapper.select(t);
    }

    public List<Category>queryCategoryByids(List<Long >ids){
        return  this.categoryMapper.selectByIdList(ids);

    }


}
