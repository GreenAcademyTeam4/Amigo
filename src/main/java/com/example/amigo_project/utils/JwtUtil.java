package com.example.amigo_project.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.amigo_project.repository.model.User;

import java.util.Date;

/**
 * JWT 토큰의 생성 및 검증을 위한 유틸 클래스이다.
 * 여기서는 알고리즘 HMAC512 알고리즘을 사용한다.
 */
public class JwtUtil {

    public static String create(User user) {

        /**
         * 주어진 사용자 정보(USER)로 JWT 토근을 생성한다.
         *
         * return 생성된 JWT String
         */
        return JWT.create()
                // 헤더
                .withSubject("web")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1시간 후에 만료
                // 페이로드 - 데이터 조각 클래임(사용자 id, 사용자 이름)(민감한 정보 넣지 않음)
                .withClaim("id", user.getId())
                .withClaim("username", user.getName())
                // 서명
                .sign(Algorithm.HMAC512("amigo"));

    }

    // 사용자가 던진 token을 받음
    public static User verify(String token) {

        // jwt 디코딩
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512("amigo"))
                .build().verify(token);

        // 검증된 JWT 에서 사용자 ID와 이름 추출해보자
        int id = decodedJWT.getClaim("id").asInt(); // int로 형변환
        String username = decodedJWT.getClaim("username").asString(); // String 으로 형변환

        return User.builder().id(id).name(username).build();

    }

}
