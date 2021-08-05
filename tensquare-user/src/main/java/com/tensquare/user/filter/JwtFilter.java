package com.tensquare.user.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 签权的拦截器
 */
@Component
public class JwtFilter extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * token: eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxNDIyOTIxNzgxMzkxNTI3OTM2Iiwic3ViIjoiYWRtaW4iLCJpYXQiOjE2MjgwODY5NTEsInJvbGVzIjoiYWRtaW4iLCJleHAiOjE2MjgwOTA1NTF9.T7PzWFNs-7MOUsj78ym5PHHfJD-WQmflAOJBOO8iIlE
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("执行拦截器");

        String auth = request.getHeader("Authorization");//Authorization

        System.out.println("auth=" + auth);

        if (auth != null) {
            if (auth.startsWith("Bearer ")) {
                String token = auth.substring(7);
                //解析token
                Claims claims = jwtUtil.parseJWT(token);
                if (claims != null) {
                    //如果当前角色是管理员
                    if ("admin".equals(claims.get("roles"))) {
                        //存入标记
                        request.setAttribute("admin_claims", claims);
                    }
                    //如果当前角色是普通用户
                    if ("user".equals(claims.get("roles"))) {
                        //存入标记
                        request.setAttribute("user_claims", claims);
                    }
                }
            }
        }
        return true;
    }
}
