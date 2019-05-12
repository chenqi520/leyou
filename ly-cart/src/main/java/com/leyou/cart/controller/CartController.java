package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cq
 * @create 2018-08-06 10:36
 * @copy WeiLaiQiChe
 */
@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
   public ResponseEntity<Void>addCart(@RequestBody Cart cart) {

    cartService.addCart(cart);

    return  new ResponseEntity<>(HttpStatus.CREATED);
    }
    //查询购物车列表
    @GetMapping("list")
    public ResponseEntity<List<Cart>>queryCarts(){

     List<Cart> list = cartService.queryCarts();
     if (CollectionUtils.isEmpty(list)){

       return   new ResponseEntity<>(HttpStatus.NOT_FOUND);
     }
     return  ResponseEntity.ok(list);
    }
    @PutMapping("update")
    public ResponseEntity<Void> updateNum(@RequestParam("skuId")Long skuId,@RequestParam("num")Integer num){

       cartService.updateNum(skuId,num);

          return  ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{skuId}")
    public ResponseEntity<Void>deleteCart(@PathVariable("skuId")String skuId){

        cartService.deleteCart(skuId);

        return  ResponseEntity.ok().build();

    }
}
