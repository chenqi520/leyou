package com.leyou.page.controller;

import com.leyou.page.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * @author cq
 * @create 2018-07-30 19:08
 * @copy alibaba
 */
@Controller
public class PageController {

    @Autowired
    private PageService pageService;

    @GetMapping("item/{spuId}.html")
    public String toGoodsPage(@PathVariable("spuId")Long spuId, Model model){
       //加载模型数据
        Map<String,Object> map = pageService.loadModel(spuId);
        //放入model
        model.addAllAttributes(map);
      //生成 hetml页面
        pageService.syncCreateHtml(spuId);
        //返回视图*/
        return  "item" ;
    }
}
