package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.repository.model.User;

public interface OuthRepository {
        OauthTokenDto socialLogin(String code) throws JsonProcessingException, UnsupportedEncodingException;


    String getToken(String code) throws JsonProcessingException, UnsupportedEncodingException;


    OauthUserDto getUserInfo(String accessToken) throws JsonProcessingException;

    User registerUserIfNeeded(OauthUserDto oauthTokenDto);

}
