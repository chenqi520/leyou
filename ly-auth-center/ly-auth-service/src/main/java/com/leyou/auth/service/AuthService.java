package com.leyou.auth.service;

import com.leyou.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.pojo.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * @author cq
 * @create 2018-08-03 20:37
 * @copy alibaba
 */
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private  JwtProperties prop;

    @Autowired
    private UserClient userClient;

    public String accredit(String username, String password) {

        try {
            //校验用户名和密码
            User user = userClient.queryUserByUsernameAndPassword(username, password);
            if (user==null){
                //校验失败
                return  null;
            }
            //校验成功  生成token
            UserInfo userInfo = new UserInfo(user.getId(),user.getUsername());
            return    JwtUtils.generateToken(userInfo,prop.getPrivateKey(),prop.getExpire());

        }catch (Exception e){

            return  null;

        }


    }
}
