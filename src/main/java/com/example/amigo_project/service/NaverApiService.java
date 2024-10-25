package com.example.amigo_project.service;

import com.example.amigo_project.repository.interfaces.UserRepository;
import org.springframework.stereotype.Service;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Service
@RequiredArgsConstructor
public class NaverApiService {
private final UserRepository userRepository;

    @Value("${naver.client_id}")
    private String naverClientId;

    @Value("${naver.redirect_uri}")
    private String naverRedirectUri;

    @Value("{naver.client_secret}")
    private String naverSecret;


}
