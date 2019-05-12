package com.leyou.auth.controller;

import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.service.AuthService;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.utils.CookieUtils2;
import com.netflix.ribbon.proxy.annotation.Http;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cq
 * @create 2018-08-03 20:34
 * @copy alibaba
 */
@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private  JwtProperties  jwtProperties;
   //cookie 的 name
    private static final String COOKIE_NAME = "LY_TOKEN";

    @PostMapping("accredit")
    public ResponseEntity<Void>accredit(@RequestParam("username")String username,
                                        @RequestParam("password")String password,
             HttpServletRequest  request,HttpServletResponse response

    ){

        //登陆授权
      String token = authService.accredit(username,password);

      if (StringUtils.isBlank(token)){
          //授权失败
       return    new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
        //授权成功 把token 写入cookie

        CookieUtils2.newBuilder(request,response)
        .httpOnly().build(COOKIE_NAME,token);
 return  new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
//核实token
    @GetMapping("verify")
    public ResponseEntity<UserInfo>verify(@CookieValue("LY_TOKEN")String token,
      HttpServletRequest  request,HttpServletResponse response
    ){

        //解析token
        try {
            UserInfo Info = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());

            if (Info==null){
                //返回403
                return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            //刷新token
            String newToken = JwtUtils.generateToken(Info, jwtProperties.getPrivateKey(), jwtProperties.getExpire());

            //写入cookie
            CookieUtils2.newBuilder(request,response)
                    .httpOnly().build(COOKIE_NAME,newToken);
            return   ResponseEntity.ok(Info);
        } catch (Exception e) {
            return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }
}
