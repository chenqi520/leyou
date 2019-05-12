package com.leyou.cart.service;

import com.leyou.auth.pojo.UserInfo;
import com.leyou.cart.interceptor.UserInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.utils.JsonUtils;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundGeoOperations;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cq
 * @create 2018-08-06 10:36
 * @copy alibaba
 */
@Service
public class CartService {

   @Autowired
   private StringRedisTemplate redisTemplate;

   private  static final  String KEY_REEFIX ="ly:cart:user";

    public void addCart(Cart cart) {
        //获取用户
        UserInfo user = UserInterceptor.getUser();
         //key就是用户得id
        String key = KEY_REEFIX + user.getId();
        //获取操作用户得对象
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
          //这个skuId 抽取出来  第二层map结构得key 也就是skuId
        String hashkey = cart.getSkuId().toString();
        Integer num = cart.getNum();
        // 判断是否存在这个商品
        if (hashOps.hasKey(hashkey)){
            //存在 修改数量
            String json = hashOps.get(hashkey).toString();
            //转成cast 对象
            cart=JsonUtils.parse(json, Cart.class);
            cart.setNum(num+cart.getNum());

        }else {
            //不存在 新增
            cart.setUserId(user.getId());

            }
        //放回 第一层 skuId  第二层cart对象得各属性得值
        // 写入redis
      hashOps.put(hashkey,JsonUtils.serialize(cart));
        }


    public List<Cart> queryCarts() {
        //获取用户信息
        UserInfo user = UserInterceptor.getUser();

        //判断是否存在购物车  hashKey 是商品id
      String key = KEY_REEFIX + user.getId();
      if (!redisTemplate.hasKey(key)){

          return  null;
      }
        //获取操作用户得对象
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
        //拿到所有购物车对象
        List<Object> carts = hashOps.values();
        if (CollectionUtils.isEmpty(carts)){
            return  null;
        }

        return carts.stream().map(o -> JsonUtils.parse(o.toString(),Cart.class)).collect(Collectors.toList());
    }
//修改num
    public  void updateNum(Long skuId, Integer num) {
        //获取用户信息
        UserInfo user = UserInterceptor.getUser();

       String key = KEY_REEFIX + user.getId();
   //获取操作用户得对象
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
      //获取购物车
        // 获取购物车
        String json = hashOps.get(skuId.toString()).toString();
        Cart cart = JsonUtils.parse(json, Cart.class);
        cart.setNum(num);
        // 写入购物车
        hashOps.put(skuId.toString(), JsonUtils.serialize(cart));

    }

    public void deleteCart(String skuId) {
        //获取用户
        UserInfo user = UserInterceptor.getUser();
        String key = KEY_REEFIX + user.getId();
        //获取操作用户得对象
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);

        hashOps.delete(skuId);


    }
}
