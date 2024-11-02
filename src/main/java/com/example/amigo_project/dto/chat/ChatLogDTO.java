package com.example.amigo_project.dto.chat;

import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChatLogDTO {

    // 값 받아올 때 사용하는 DTO
    private int roomId; // chat_room_tb의 PK(방번호)
    private int userId; // user_tb의 PK
    private String nickName; // 사용자 닉네임
    private byte[] profile; // 프로필 이미지
    private String type; // 메시지 타입
    private String message; // 메시지
    private Timestamp createdAt; // 메시지 보낸 시간

}
