package com.leyou.user.service;

import com.leyou.user.mapper.UserMapper;
import com.leyou.user.pojo.User;
import com.leyou.user.utils.CodecUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author cq
 * @create 2018-08-02 15:25
 * @copy alibaba
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    private static final String KEY_PREFIX = "ly:user:verify:";

    public Boolean cherckData(String data, Integer type) {
          //查询条件
        User t = new User();
        switch (type){
            case 1:
            t.setUsername(data);
            break;
            case 2:
            t.setPhone(data);
            break;
            default:
              return  null;

        }
        return  userMapper.selectCount(t)<=0;
    }

    public void sendCode(String phone) {

        String key = KEY_PREFIX + phone;
        //先生成验证码
        String code = com.leyou.common.utils.NumberUtils.generateCode(6);
        // 保存验证码到redis

        redisTemplate.opsForValue().set(key, code, 5, TimeUnit.MINUTES);

        //发送短信到smsService
        Map<String, String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
        amqpTemplate.convertAndSend("ly.sms.exchange", "sms.verify.code", msg);

        }

    public Boolean register(User user, String code) {
        //验证code
        String key = KEY_PREFIX +user.getPhone();
        //先取出redis中的code
        String cacheCode = redisTemplate.opsForValue().get(key);

        if (!code.equals(cacheCode)){
            return  false;
        }
        //生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        // // 对密码进行加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(),salt));


        user.setCreated(new Date());
        // 写入数据库//储存
        int count = userMapper.insert(user);

        return count==1;

    }

    public User queryUserByUsernameAndPassword(String username, String password) {
       //根据用户名查询
        User t = new User();
        t.setUsername(username);
        User user = userMapper.selectOne(t);
        //判断是否存在
        if (user==null){
            //用户名错误
            return  null;
        }
        //判断密码md5加密后查询
        // 校验密码
        if (!user.getPassword().equals(CodecUtils.md5Hex(password, user.getSalt()))) {
            return null;

         }
        return  user;
    }
}


