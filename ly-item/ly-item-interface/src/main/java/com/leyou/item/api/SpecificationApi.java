package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author cq
 * @create 2018-07-27 17:12
 * @copy alibaba
 */
//规格参数
@RequestMapping("spec")
public interface SpecificationApi {

    @GetMapping("/params")
    List<SpecParam>queryParam(
            @RequestParam(value = "gid",required =false) Long gid,
            @RequestParam(value = "cid",required =false) Long cid,
            @RequestParam(value = "generic",required =false) Boolean generic,
            @RequestParam(value = "searching",required =false) Boolean searching);

//组和组下得所有规格参数
    @GetMapping("/{cid}")
List<SpecGroup>querySpecsByCid(@PathVariable("cid")Long cid);

}
