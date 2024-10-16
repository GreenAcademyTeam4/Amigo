package com.example.amigo_project.service;

import com.example.amigo_project.repository.interfaces.AdminRepository;
import com.example.amigo_project.repository.model.User;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {


    private final AdminRepository adminRepository;

    
    public List<User> getUserList(){
        
        List<User> userList = adminRepository.findUserAll();
        
        // TODO 예외처리, 유효성 검사
        
        
        return userList;
    }
    
    

}
