package com.leyou.item.mapper;

import com.leyou.item.pojo.Sku;
import tk.mybatis.mapper.additional.idlist.DeleteByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author cq
 * @create 2018-07-24 19:41
 * @copy alibaba
 */
public interface SkuMapper extends Mapper<Sku>,DeleteByIdListMapper<Sku,Long> {

}
