package com.leyou.controller;

import com.leyou.service.UploadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author cq
 * @create 2018-07-21 17:49
 * @copy alibaba
 */
@RestController
public class UploadController {

    @Autowired
    private UploadService uploadService;
    /**
     * 上传图片功能
     * @param file
     * @return
     */
    @RequestMapping("image")
    public ResponseEntity<String>UploadImage(@RequestParam("file")MultipartFile file){
         String url =this.uploadService.upload(file);

           if (StringUtils.isBlank(url)){
               //如果 为空证明文件类型不对  HttpStatus.BAD_REQUEST返回400 请求参数有误
               return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
           }
           //返回200 并携带url 路径

        return  ResponseEntity.ok(url);
    }

}
