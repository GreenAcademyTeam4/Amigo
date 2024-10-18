package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.dto.UserDTO;
import com.example.amigo_project.repository.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRepository {

    // 회원 가입
    public int create(UserDTO.joinDTO dto);
    //  아이디 중복확인
    public boolean checkUserId(String userId);
    // 닉네임 중복확인
    public  boolean checkUserNickname(String nickname);
    // 로그인
    public User loginByUserIdandPassword(UserDTO.loginDTO dto);



}

