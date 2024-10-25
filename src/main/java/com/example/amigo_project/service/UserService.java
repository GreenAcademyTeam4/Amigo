package com.example.amigo_project.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.amigo_project.dto.UserDTO;
import com.example.amigo_project.repository.interfaces.UserRepository;
import com.example.amigo_project.repository.model.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

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
    public User findUserById(UserDTO.loginDTO dto){
     
        User user = userRepository.findByUserId(dto.getUserId());

        if (user != null && passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return user; 
        } else {
            return null; 
        }
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
        // 비밀번호 해싱 도입 시  if(passwordEncoder.matches(hashpwd, password)){
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


}

