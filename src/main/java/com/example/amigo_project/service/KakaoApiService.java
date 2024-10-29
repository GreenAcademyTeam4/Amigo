package com.example.amigo_project.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.amigo_project.dto.UserDTO;
import com.example.amigo_project.dto.UserDTO.KakaoDTO;
import com.example.amigo_project.repository.interfaces.UserRepository;
import com.example.amigo_project.repository.model.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Service
@RequiredArgsConstructor
public class KakaoApiService {

    private final UserRepository userRepository;

    @Value("${kakao.api_key}")
    private String kakaoApiKey;

    @Value("${kakao.redirect_uri}")
    private String kakaoRedirectUri;

    public String getKakaoAccessToken(String code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + kakaoApiKey);
            sb.append("&redirect_uri=" + kakaoRedirectUri);
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

           

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    public KakaoDTO createKakaoUser(String token) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder resultBuilder = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                resultBuilder.append(line);
            }
            String result = resultBuilder.toString();
            System.out.println("response body : " + result);

            JsonElement element = JsonParser.parseString(result);

            System.out.println("Parsed JSON: " + element.toString());

            long id = element.getAsJsonObject().get("id").getAsLong();

            JsonObject kakaoAccount = null;
            if (element.getAsJsonObject().has("kakao_account")) {
                kakaoAccount = element.getAsJsonObject().getAsJsonObject("kakao_account");
            } else {
                System.err.println("kakao_account 키가 JSON 응답에 존재하지 않습니다.");
            }

            boolean hasEmail = false;
            if (kakaoAccount != null && kakaoAccount.has("has_email")) {
                JsonElement hasEmailElement = kakaoAccount.get("has_email");
                if (hasEmailElement != null && !hasEmailElement.isJsonNull()) {
                    hasEmail = hasEmailElement.getAsBoolean();
                } else {
                    System.err.println("has_email 키가 null 또는 JsonNull입니다.");
                }
            } else {
                System.err.println("has_email 키가 kakao_account 객체에 존재하지 않습니다.");
            }

            String email = "";
            if (hasEmail && kakaoAccount.has("email")) {
                JsonElement emailElement = kakaoAccount.get("email");
                if (emailElement != null && !emailElement.isJsonNull()) {
                    email = emailElement.getAsString();
                } else {
                    System.err.println("email 키가 null 또는 JsonNull입니다.");
                }
            }

            String kakaoPassword = UUID.randomUUID().toString();
            br.close(); // br.close()를 return 전에 위치시킴
            UserDTO userDTO = new UserDTO();
            UserDTO.KakaoDTO kakaoDTO = userDTO.new KakaoDTO(email, kakaoPassword);
            return kakaoDTO;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public User findKakaoUser(UserDTO.KakaoDTO kakaoDTO) {

        User principal = userRepository.findByUserId(kakaoDTO.getKakaoId());

        if (principal == null) {

            userRepository.kakaoInsert(kakaoDTO.getKakaoId(), kakaoDTO.getKakaoPassword());
            principal = userRepository.findByUserId(kakaoDTO.getKakaoId());

        }

        return principal;
    }

}
