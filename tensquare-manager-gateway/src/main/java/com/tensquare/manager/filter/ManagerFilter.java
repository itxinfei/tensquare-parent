package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 后台zuul过滤器
 */
@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;

    /*pre为前置过滤器，post为后置过滤器，route在路由请求时被调用*/
    @Override
    public String filterType() {
        return "pre";
    }

    /*过滤器的优先级，值越小，优先级越高*/
    @Override
    public int filterOrder() {
        return 0;
    }

    /*是否执行该过滤器，true表示要过滤*/
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /*过滤当前要执行的操作的人是不是管理员身份*/
    @Override
    public Object run() throws ZuulException {
        System.out.println("后端管理Zuul过滤器...");
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //放行
        if (request.getMethod().equals("OPTIONS")) {
            return null;
        }
        String url = request.getRequestURL().toString();
        //放行，如果是管理员登录
        if (url.indexOf("/admin/login") > 0) {
            System.out.println("登陆页面" + url + ",所以就不需要验证token，直接跳转登录");
            return null;
        }

        String authHeader = (String) request.getHeader("X-Token");//获取头信息
        //&& authHeader.startsWith("Bearer ")
        if (authHeader != null) {
            //.substring(7);
            String token = authHeader;
            Claims claims = jwtUtil.parseJWT(token);
            if (claims != null) {
                //验证了当前的确是管理员
                if ("admin".equals(claims.get("roles"))) {
                    //二次转发，填头继续传递
                    requestContext.addZuulRequestHeader("Authorization", authHeader);
                    System.out.println("token 验证通过，添加了头信息" + authHeader);
                    return null;
                }
            }
        }
        //没有设置头信息或者头信息格式不正确
        requestContext.setSendZuulResponse(false);//终止运行，直接令zuul过滤该请求，不对其进行路由
        requestContext.setResponseStatusCode(401);//http状态码
        requestContext.setResponseBody("无权访问");
        requestContext.getResponse().setContentType("text/html;charset=UTF‐8");

        return null;
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 允许cookies跨域
        config.addAllowedOrigin("*");// 允许向该服务器提交请求的URI，*表示全部允许。。这里尽量限制来源域，比如http://xxxx:8080 ,以降低安全风险。。
        config.addAllowedHeader("*");// 允许访问的头信息,*表示全部
        config.setMaxAge(18000L);// 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
        config.addAllowedMethod("*");// 允许提交请求的方法，*表示全部允许，也可以单独设置GET、PUT等
        config.addAllowedMethod("HEAD");
        config.addAllowedMethod("GET");// 允许Get的请求方法
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("PATCH");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
