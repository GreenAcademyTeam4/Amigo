package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.repository.model.School;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.amigo_project.dto.UserDTO;
import com.example.amigo_project.repository.model.User;

import java.util.List;

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
    public int updateInfo(UserDTO.infoDTO dto);
    
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

    // 온라인인 친구 찾기
    public List<User>findOnlineFriends(int id);
    // 오프라인인 친구 찾기
    public List<User>findOfflineFriends(int id);
    // 학교 데이터 추가
    public void createSchool(UserDTO.infoDTO dto);
    // 유저가 가진 학교데이터 추가
    public void createUserSchool(UserDTO.infoDTO dto);
    // 유저가 가진 학교 찾기
    public List<School> findUserSchool(int id);
    // 학교가 존재하는지 확인
    public boolean existsSchool(UserDTO.infoDTO dto);
    // 유저 프로필사진 삽입
    public void insertUserProfile(User user);
    // 온라인 상태로 업데이트
    public void updateOnline(int id);
    // 오프라인 상태로 업데이트
    public void updateOffline(int id);

}

