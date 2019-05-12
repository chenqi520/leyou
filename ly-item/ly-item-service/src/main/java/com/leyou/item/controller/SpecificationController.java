package com.leyou.item.controller;

import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author cq
 * @create 2018-07-22 20:17
 * @copy alibaba
 */
@RestController
@RequestMapping("spec")
//参数和规格组
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;



    @GetMapping("/groups/{cid}")
    public ResponseEntity<List<SpecGroup>>queryGroupByCid(@PathVariable("cid")Long cid){

    List<SpecGroup> list  = this.specificationService.queryGroupByCid(cid);

         if (CollectionUtils.isEmpty(list)){

             return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }


 return  ResponseEntity.ok(list);

    }

    @GetMapping("/params")
    public ResponseEntity<List<SpecParam>>queryParamByGid(
            @RequestParam(value = "gid",required =false) Long gid,
            @RequestParam(value = "cid",required =false) Long cid,
            @RequestParam(value = "generic",required =false) Boolean generic,
            @RequestParam(value = "searching",required =false) Boolean searching

    ){

     List<SpecParam> list  =  this.specificationService.queryParamByGid(gid,cid,generic,searching);

       if (CollectionUtils.isEmpty(list)){

           return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
       }
       return  ResponseEntity.ok(list);
    }
  //规格参数查询根据商品id
    @GetMapping("/{cid}")
    public ResponseEntity<List<SpecGroup>>querySpecsByCid(@PathVariable("cid")Long cid){

        List<SpecGroup> list  = this.specificationService.querySpecsByCid(cid);

        if (CollectionUtils.isEmpty(list)){

            return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        return  ResponseEntity.ok(list);

    }

}
