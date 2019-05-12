package com.leyou.zuul.filter;

import com.leyou.auth.utils.JwtUtils;
import com.leyou.utils.CookieUtils;
import com.leyou.utils.CookieUtils2;
import com.leyou.zuul.config.FilterProperties;
import com.leyou.zuul.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: HuYi.Zhang
 * @create: 2018-05-29 08:57
 **/
@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class AuthFilter extends ZuulFilter {

    @Autowired
    private JwtProperties prop;

    @Autowired
    private  FilterProperties filterProperties;

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 1;
    }

    @Override
    public boolean shouldFilter() {
        //路劲过滤
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest request = ctx.getRequest();
         //获取请求路径
        String requestPath = request.getRequestURI();
        //判断是否拦截  这个是返回的是false     ture 代表放行接受    false 代表拦截
    return   !isAllowPath(requestPath);
    }

    private boolean isAllowPath(String requestPath) {

        if (StringUtils.isBlank(requestPath)){

            return  false;
        }

        List<String> allowPaths = filterProperties.getAllowPaths();

        if (CollectionUtils.isEmpty(allowPaths)){

            return  false;
        }

        for (String allowPath : allowPaths) {
            if (requestPath.startsWith(allowPath)){
                    //允许放行
                return true;

            }
        }
        //不允许放行
        return  false;
    }

    @Override
    public Object run() throws ZuulException {
        // 获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        // 获取request
        HttpServletRequest request = ctx.getRequest();
        // 获取token
        String token = CookieUtils2.getCookieValue(request, prop.getCookieName());
        // 校验
          if (StringUtils.isBlank(token)){
              //没有token 未登录
              ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
              //阻止后续的路由
              ctx.setSendZuulResponse(false);
          }
  //解析token
        try {
            // 校验通过什么都不做，即放行
            JwtUtils.getInfoFromToken(token, prop.getPublicKey());
        } catch (Exception e) {
            // 校验出现异常， token 无效 未登录 返回401
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());

            //阻止后续的路由
            ctx.setSendZuulResponse(false);
        }
        //TODO 根据token获取权限信息
        //TODO 根据权限和请求路径判断
        return null;
    }
}