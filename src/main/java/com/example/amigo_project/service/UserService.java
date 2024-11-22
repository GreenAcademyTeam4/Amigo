package com.example.amigo_project.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.amigo_project.dto.EquipAvatarDTO;
import com.example.amigo_project.errors.Exception401;
import com.example.amigo_project.utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.amigo_project.dto.UserDTO;
import com.example.amigo_project.repository.interfaces.UserRepository;
import com.example.amigo_project.repository.model.School;
import com.example.amigo_project.repository.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    
    // 비밀번호 엄호
	private final PasswordEncoder passwordEncoder;
    /**
     * 중복확인
     * Map 활용하여 필요한 재사용 가능한 메서드
     * @param
     * @return
     */
    public Map<String, String> checkFieldRepetition(UserDTO.joinDTO dto) {
        Map<String, String> result = new HashMap<>();
        boolean repetition = false;
        if(dto.getUserId() != null){
            repetition = userRepository.checkUserId(dto.getUserId());
            if(repetition == true){
                result.put("repetition", "repetition");
            }
       }
        if(dto.getUserId().length()< 6 || dto.getUserId().length() > 21 ) {
            result.put("repetition", "iderror");
        }
        if(dto.getUserId().matches(".*[^a-zA-Z0-9-_].*")){
            result.put("repetition", "iderror");
        }

        return result;
    }

    public Map<String, String> checkNickNameRepetition(UserDTO.infoDTO dto) {
        Map<String, String> result = new HashMap<>();
        boolean repetition = false;
        if(dto.getNickname() != null){
            repetition = userRepository.checkUserNickname(dto.getNickname());
            if(repetition == true){
                result.put("repetition", "repetition");
            }
       }
        return result;
    }

    /**
     * 회원가입 
     * 
     * @param dto // joinDTO 사용
     * @return
     */
    public int joinUser(UserDTO.joinDTO dto){
      int result = 0;
      String hashPwd = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(hashPwd);
    result =  userRepository.create(dto);
     return result;
    } 

    /**
     * 로그인 로직
     * @param dto
     * @return
     */
    public User findUserById(UserDTO.loginDTO dto) {
        User user = userRepository.findByUserId(dto.getUserId());
        //return (user != null && passwordEncoder.matches(dto.getPassword(), user.getPassword())) ? user : null;
        //if (user != null && passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            //return user;
        //} else {
//            return null;
  //      }
  
//        if (user != null && passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return user;
//        } else {
//            return null;
//        }
       // return (user != null && passwordEncoder.matches(dto.getPassword(), user.getPassword())) ? user : null;
        
    }

    /**
     * 비밀번호 변경을 위해 입력한 기존 비밀번호가 DB의 값과 일치하면 1을 반환
     * 불일치시 0을 반환
     * TODO - 비밀번호 해싱 기능 도입시 해싱처리
     * @param userId
     * @param password
     * @return 일치하면 1 , 불일치시 0
     */
    public int checkPasswordValid(Integer userId, String password){
        String hashpwd = userRepository.findPasswordByUserId(userId);
        // TODO - 배포 시 변경, 개발 단계에선 해싱 처리 생략 if(passwordEncoder.matches(hashpwd, password)){
        if(hashpwd.equals(password)){
            return 1;
        }else{
            return 0;
        }
    }

    /**
     * 비밀번호 변경 기능
     * TODO - 비밀번호 해싱 기능 도입시 해싱처리
     * @param userId 입력값 userId
     * @param password 입력값 변경할 pwd
     */
    @Transactional
    public void updatePasswordByUserId(Integer userId, String password){
        // String hashpwd = passwordEncoder.encode(password);
        // userRepository.updatePasswordByUserId(userId, hashpwd);
        userRepository.updatePasswordByUserId(userId, password);
    }

    public User findUser(int id) {
        return userRepository.findUserById(id);
    }
    public void updateInfo(UserDTO.infoDTO dto){
        userRepository.updateInfo(dto);
    }

    // 온라인인 친구 찾기
    public List<User> findOnlineFriends(int id) {
        return userRepository.findOnlineFriends(id);
    }

    // 오프라인인 친구 찾기
    public List<User> findOfflineFriends(int id) {
        return userRepository.findOfflineFriends(id);
    }

    // 학교 데이터 넣기
    public void createSchool(UserDTO.infoDTO dto) {
        userRepository.createSchool(dto);
    }

    // 유저 학교 데이터 넣기
    public void createUserSchool(UserDTO.infoDTO dto) {
        userRepository.createUserSchool(dto);
    }

    // 유저가 가진 학교 찾기
    public List<School> findUserSchoolList(int id) {
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@"+id);
        return userRepository.findUserSchool(id);
    }

    // 학교 데이터가 있는지 검사
    public boolean existsSchool(UserDTO.infoDTO dto) {
        return userRepository.existsSchool(dto);
    }

    // 유저 프로필 삽입
    public void insertUserProfile(User user) {
        userRepository.insertUserProfile(user);
    }

    // 온라인 상태로 업데이트
    public void updateOnline(int id) {
        userRepository.updateOnline(id);
    }
    // 오프라인 상태로 업데이트
    public void updateOffline(int id) {
        userRepository.updateOffline(id);
    }
    // 학교 정보 가져오기
    public School findSchoolData(int id) {
        return userRepository.findSchoolData(id);
    }
    // 회원 가입시 기본 아바타 삽입
    public void insertDefaultAvatar(int userId,int avatarId) {
        userRepository.insertDefaultAvatar(userId,avatarId);
    }
    // 유저가 가지고있는 아바타 정보 가져오기
    public EquipAvatarDTO equipUserAvatar(int id) {
        return userRepository.equipUserAvatar(id);
    }

    public String signIn(UserDTO.loginDTO reqDTO, int id) {

        if (!isValidUser(reqDTO.getUserId(), reqDTO.getPassword())) {
            throw new Exception401("인증되지 않았습니다");
        }

        User sessionUser = User.builder()
                .id(id)
                .name(reqDTO.getUserId())
                .profile("profile-image".getBytes())
                .build();

        return JwtUtil.create(sessionUser);
    }

    private boolean isValidUser(String username, String password) {
        return "user".equals(username) && "password".equals(password);
    }

}

