package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.dto.OauthTokenDto;
import com.example.amigo_project.repository.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.UnsupportedEncodingException;

public interface OuthRepository {
        OauthTokenDto socialLogin(String code) throws JsonProcessingException, UnsupportedEncodingException;


    String getToken(String code) throws JsonProcessingException, UnsupportedEncodingException;


    OauthTokenDto getUserInfo(String accessToken) throws JsonProcessingException;

    User registerUserIfNeeded(OauthTokenDto oauthTokenDto);

}
