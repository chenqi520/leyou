package com.leyou.item.mapper;


import com.leyou.item.pojo.Category;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author cq
 * @create 2018-07-19 21:39
 * @copy alibaba
 */
public interface CategoryMapper extends Mapper<Category>,SelectByIdListMapper<Category,Long>{
}
