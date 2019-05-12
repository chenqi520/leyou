package com.leyou.item.service;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author cq
 * @create 2018-07-22 20:19
 * @copy alibaba
 */
@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper specGroupMapper;


    @Autowired
    private SpecParamMapper specParamMapper;



    public List<SpecGroup> queryGroupByCid(Long cid) {

        SpecGroup t = new SpecGroup();

        t.setCid(cid);

        return this.specGroupMapper.select(t);
    }

    public List<SpecParam> queryParamByGid(Long gid, Long cid, Boolean generic, Boolean searching) {

        SpecParam t = new SpecParam();


        t.setGroupId(gid);
        t.setCid(cid);
        t.setGeneric(generic);
        t.setSearching(searching);

        return  this.specParamMapper.select(t);
    }
//查询组下所有参数
    public List<SpecGroup> querySpecsByCid(Long cid) {
        SpecGroup t = new SpecGroup();
        t.setCid(cid);
        List<SpecGroup> groups = specGroupMapper.select(t);
        for (SpecGroup group : groups) {
            //根据组查询规格参数 这里是和借组specParam查 应为参数在这个pojo钟
            //既有组又有租下得参数
            SpecParam param = new SpecParam();
            param.setGroupId(group.getId());
           group.setParams(specParamMapper.select(param));

        }
return  groups;
    }
}
