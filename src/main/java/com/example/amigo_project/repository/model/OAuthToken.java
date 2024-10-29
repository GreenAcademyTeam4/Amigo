package com.example.amigo_project.repository.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.ToString;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@ToString
public class OAuthToken {

    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private Integer expiresIn;
    private String scope;
    private Integer refreshTokenExpiresIn;

}
