package com.example.amigo_project.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.amigo_project.dto.UserDTO.GoogleDTO;
import com.example.amigo_project.repository.interfaces.UserRepository;
import com.example.amigo_project.repository.model.OAuthToken;
import com.example.amigo_project.repository.model.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Getter
public class GoogleService {

    private final UserRepository userRepository;

    @Value("${google.oauth.client.id}")
    private String CLIENT_ID;

    @Value("${google.oauth.client.secret}")
    private String CLIENT_SECRET;

    @Value("${google.oauth.client.redirect-uri}")
    private String REDIRECT_URI;
    

    public String getGoogleAccessToken(String code) {
        String accessToken = "";
        String reqURL = "https://oauth2.googleapis.com/token";

        try {
            RestTemplate rt = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", CLIENT_ID);
            params.add("client_secret", CLIENT_SECRET);
            params.add("redirect_uri", REDIRECT_URI);
            params.add("code", code);

            HttpEntity<MultiValueMap<String, String>> googleTokenRequest = new HttpEntity<>(params, headers);
            ResponseEntity<OAuthToken> response = rt.exchange(reqURL, HttpMethod.POST, googleTokenRequest, OAuthToken.class);

            OAuthToken oauthToken = response.getBody();
            accessToken = oauthToken.getAccessToken();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    public GoogleDTO createGoogleUser(String token) {
        String reqURL = "https://www.googleapis.com/oauth2/v1/userinfo";

        try {
            RestTemplate rt = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);

            HttpEntity<MultiValueMap<String, String>> googleUserInfoRequest = new HttpEntity<>(headers);
            ResponseEntity<GoogleDTO> response = rt.exchange(reqURL, HttpMethod.GET, googleUserInfoRequest, GoogleDTO.class);

            GoogleDTO googleDTO = response.getBody();
            googleDTO.setGooglePassword(UUID.randomUUID().toString());
            return googleDTO;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to retrieve Google user information");
        }
    }

    public User findGoogleUser(GoogleDTO googleDTO) {
       
        User principal = userRepository.findByUserId(googleDTO.getEmail());

        if (principal == null) {
            userRepository.googleInsert(googleDTO.getEmail(), googleDTO.getGooglePassword());
            principal = userRepository.findByUserId(googleDTO.getEmail());
        }

        return principal;
    }
}

