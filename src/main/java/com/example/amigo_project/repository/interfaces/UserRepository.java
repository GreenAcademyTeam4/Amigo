package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.dto.UserDTO;
import com.example.amigo_project.repository.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    // 비밀번호 변경 로직 -> 기존 비밀번호 1회 입력후 변경 진행을 위한 비밀번호 확인
    public String findPasswordByUserId(Integer userId);
    // 비밀번호 변경하기
    public void updatePasswordByUserId(@Param("userId") Integer userId, @Param("password")String password);


}

