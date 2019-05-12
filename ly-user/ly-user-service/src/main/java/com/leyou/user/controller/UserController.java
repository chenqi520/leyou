package com.leyou.user.controller;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author cq
 * @create 2018-08-02 15:26
 * @copy alibaba
 */
//实现用户数据的校验，主要包括对：手机号、用户名、邮箱的唯一性校验。
@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean>cherckData(
            @PathVariable("data")String data,
            @PathVariable(value = "type",required =false )Integer type) {
      if (type==null){

          type = 1;
      }
        Boolean boo = userService.cherckData(data,type);
      if (boo==null){

          return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }

        return  ResponseEntity.ok(boo);

    }
    //根据用户输入的手机号，生成随机验证码，长度为6位，纯数字。并且调用短信服务，发送验证码到用户手机
    @PostMapping("/code")
    public ResponseEntity<Void>sendCode(@RequestParam("phone")String phone){

        if(!phone.matches("^1[2345678]\\d{9}$")){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

     userService.sendCode(phone);


        return new  ResponseEntity(HttpStatus.NO_CONTENT);

    }
//实现用户注册功能，需要对用户密码进行加密存储，
// 使用MD5加密，加密过程中使用随机码作为salt加盐。另外还需要对用户输入的短信验证码进行校验。
    @PostMapping("/register")
    public ResponseEntity<Void>register(@Valid User user, @RequestParam("code")String code){
           //数据校验自动完成Hibernate Validator 提供了 JSR 303 规范中所有内置 constraint（约束） 的实现

     Boolean boo   =  userService.register(user,code);
     if (boo==null){

         new ResponseEntity<>(HttpStatus.BAD_REQUEST);
     }

        return  new ResponseEntity<>(HttpStatus.CREATED);

    }
   // 查询功能，根据参数中的用户名和密码查询指定用户
    @GetMapping("/query")
    public ResponseEntity<User>queryUserByUsernameAndPassword(
            @RequestParam("username")String username,
            @RequestParam("password")String password
    ){
    User user  = userService.queryUserByUsernameAndPassword(username,password);
       if (user==null){

           new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }

       return  ResponseEntity.ok(user);

    }

}
