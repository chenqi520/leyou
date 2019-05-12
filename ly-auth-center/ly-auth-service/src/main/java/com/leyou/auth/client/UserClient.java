package com.leyou.auth.client;

import com.leyou.user.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author cq
 * @create 2018-08-03 20:55
 * @copy alibaba
 */
@FeignClient("user-service")
public interface UserClient extends UserApi{

}
