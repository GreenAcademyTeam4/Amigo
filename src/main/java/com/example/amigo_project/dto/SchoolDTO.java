package com.example.amigo_project.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SchoolDTO {
    private String regionCode;
    private String regionName;

    public SchoolDTO(String regionCode, String regionName) {
        this.regionCode = regionCode;
        this.regionName = regionName;
    }
}

