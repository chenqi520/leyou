package com.leyou.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

/**
 * @author cq
 * @create 2018-07-21 18:59
 * @copy alibaba
 */
@Service
public class UploadService {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    //文件的类型校验
    private static final List<String> suffixes = Arrays.asList("image/png", "image/jpeg");

    public String upload(MultipartFile file) {

try {

      //图片校验
    String contentType = file.getContentType();
    if (!suffixes.contains(contentType)){

        return  null;

    }
    //校验文件内容  转为流
    BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
     if (bufferedImage==null){

         return null;
     }

   /* //文件上传目的地
        File dest = new File("D:\\develop\\nginx-1.12.2\\html\\" + file.getOriginalFilename());
        //上传
        file.transferTo(dest);*/

    // 保存到FastDFS
    String extName = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
    StorePath path = this.fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), extName, null);


    return "http://image.leyou.com/" + path.getFullPath();
       }catch (Exception e){

       throw  new RuntimeException(e);

       }

     }
}
