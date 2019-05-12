package com.leyou.item.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.pojo.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public PageResult<Brand> queryBrandByPageAndSort(
            Integer page, Integer rows, String sortBy, Boolean desc, String key) {
        // 开始分页
        PageHelper.startPage(page, rows);
        // 过滤
        Example example = new Example(Brand.class);
        if (StringUtils.isNotBlank(key)) {
            //不为空过滤
            example.createCriteria().orLike("name", "%" + key + "%")
                    .orEqualTo("letter", key.toUpperCase());
        }
        if (StringUtils.isNotBlank(sortBy)) {
            // 排序
            String orderByClause = sortBy + (desc ? " DESC" : " ASC");
            example.setOrderByClause(orderByClause);
        }
        // 查询
        List<Brand> brands = brandMapper.selectByExample(example);
        //获取分页数据
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);

        // 返回结果
        return new PageResult<>(pageInfo.getTotal(), pageInfo.getList());
    }

    @Transactional
    public void saveBrand (Brand brand, List<Long> cids) {

        try {
            // 保存品牌
            brand.setId(null);
            brandMapper.insert(brand);

            // 保存中间表
            for (Long cid : cids) {
                brandMapper.insertCategoryBrand(cid, brand.getId());
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public Brand queryById(Long id){

        return  brandMapper.selectByPrimaryKey(id);
    }

    public List<Brand> queryBrandByCategory(Long cid) {
       return brandMapper.queryByCategoryId(cid);
    }

    public List<Brand> queryByIds(List<Long> ids) {

     return    brandMapper.selectByIdList(ids);
    }
}

