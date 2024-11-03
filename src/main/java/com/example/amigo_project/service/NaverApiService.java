package com.example.amigo_project.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.amigo_project.dto.UserDTO;
import com.example.amigo_project.dto.UserDTO.NaverDTO;
import com.example.amigo_project.repository.interfaces.UserRepository;
import com.example.amigo_project.repository.model.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Service
@RequiredArgsConstructor
public class NaverApiService {
    private final UserRepository userRepository;

    @Value("${naver.client_id}")
    private String naverClientId;

    @Value("${naver.redirect_uri}")
    private String naverRedirectUri;

    @Value("${naver.client_secret}")
    private String naverSecret;

    private final static String NAVER_AUTH_URI = "https://nid.naver.com";
    private final static String NAVER_API_URI = "https://openapi.naver.com";

    

    public String getNaverAccessToken(String code, String state) throws Exception {
        String accessToken = "";
        String apiURL = NAVER_AUTH_URI + "/oauth2.0/token?grant_type=authorization_code"
                + "&client_id=" + naverClientId
                + "&client_secret=" + naverSecret
                + "&redirect_uri=" + URLEncoder.encode(naverRedirectUri, "UTF-8")
                + "&code=" + code
                + "&state=" + state;

        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else { // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuilder res = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                res.append(inputLine);
            }
            br.close();

            if (responseCode == 200) {
                // JSON 응답 파싱
                JSONParser parser = new JSONParser();
                JSONObject jsonObj = (JSONObject) parser.parse(res.toString());
                accessToken = (String) jsonObj.get("access_token");
            } else {
                throw new Exception("Failed to retrieve access token. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            throw new Exception("Error retrieving access token: " + e.getMessage());
        }

        return accessToken;
    }

    public NaverDTO createNaverUser(String accessToken) {
        String reqURL = NAVER_API_URI + "/v1/nid/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else { // 에러 발 생
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder resultBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                resultBuilder.append(line);
            }
            br.close();

            JSONParser parser = new JSONParser();
            JSONObject responseJson = (JSONObject) parser.parse(resultBuilder.toString());
            JSONObject responseObject = (JSONObject) responseJson.get("response");

            String naverId = (String) responseObject.get("id");
            String name = (String) responseObject.get("name");

            String naverPassword = UUID.randomUUID().toString();

            return new NaverDTO(naverId, naverPassword);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public User findNaverUser(UserDTO.NaverDTO naverDTO) {
        User principal = userRepository.findByUserId(naverDTO.getNaverId());
        
        if (principal == null) {
            userRepository.naverInsert(naverDTO.getNaverId(), naverDTO.getNaverPassword());
            principal = userRepository.findByUserId(naverDTO.getNaverId());
        }

        return principal;
    }
}


