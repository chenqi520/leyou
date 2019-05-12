package com.leyou.user;

import com.leyou.user.pojo.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author cq
 * @create 2018-08-03 20:47
 * @copy alibaba
 */
public interface UserApi {

    @GetMapping("/query")
    User queryUserByUsernameAndPassword(
    @RequestParam("username")String username,
    @RequestParam("password")String password
    );
}
