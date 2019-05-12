package com.leyou.item.mapper;

import com.leyou.item.pojo.Stock;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;

/**
 * @author cq
 * @create 2018-07-24 19:42
 * @copy alibaba
 */
public interface StockMapper extends Mapper<Stock> ,InsertListMapper<Stock>, DeleteByIdListMapper<Stock, Long> {

}
