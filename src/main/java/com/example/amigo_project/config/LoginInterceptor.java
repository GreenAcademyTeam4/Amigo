package com.example.amigo_project.config;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.amigo_project.errors.Exception401;
import com.example.amigo_project.errors.Exception500;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.utils.Define;
import com.example.amigo_project.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String jwt = request.getHeader(Define.AUTHORIZATION);

        if(jwt == null || ! jwt.startsWith(Define.BEARER)) {
            throw new Exception401("JWT 토큰을 전달해주세요");
        }

        jwt = jwt.replace(Define.BEARER, "");

        try {
            User sessionUser = JwtUtil.verify(jwt);
            request.setAttribute(Define.SESSION_USER, sessionUser);
            return  true;

        } catch (TokenExpiredException e) {
            throw new Exception401("토큰 만료 시간이 지났습니다. 다시 로그인 하세요");
        } catch (JWTDecodeException e) {
            throw new Exception401("유효하지 않은 토큰입니다");
        } catch (Exception e) {
            throw new Exception500("서버 오류 : " + e.getMessage());
        }
    }



}
