package com.example.amigo_project.repository.interfaces;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.amigo_project.dto.UserDTO;
import com.example.amigo_project.repository.model.User;

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
    // 아이디로 로그인
    public User findByUserId(String userId);
    // info 추가하기 (닉네임,학교)
    public User updateInfo(UserDTO.infoDTO dto);
    
    /**
     * 간편로그인 관련 인터페이스
     */
    public void kakaoInsert (@Param("kakaoId") String  kakaoId,  @Param("kakaoPassword") String kakaoPassword);
    public void googleInsert (@Param("email") String  email,  @Param("googlePassword") String googlePassword);
    public User loginByUserIdAndPassword(UserDTO.loginDTO dto);
    // 비밀번호 변경 로직 -> 기존 비밀번호 1회 입력후 변경 진행을 위한 비밀번호 확인
    public String findPasswordByUserId(Integer userId);
    // 비밀번호 변경하기
    public void updatePasswordByUserId(@Param("userId") Integer userId, @Param("password")String password);
    public void naverInsert (@Param("naverId") String  naverId,  @Param("naverPassword") String naverPassword);

    // id로 유저 정보 가져오기
    public User findUserById(int id);
    // 친구수 조회
    public int countFriendByUserId(int id);
}

