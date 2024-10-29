package com.example.amigo_project.dto.chat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatDataDTO {

    private int id;
    private String nickname;
}
