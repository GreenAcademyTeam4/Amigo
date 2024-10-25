package com.example.amigo_project.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.amigo_project.dto.UserDTO;
import com.example.amigo_project.repository.interfaces.UserRepository;
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



}

