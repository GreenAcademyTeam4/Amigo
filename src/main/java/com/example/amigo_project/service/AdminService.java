package com.example.amigo_project.service;

import com.example.amigo_project.dto.AdminDTO;
import com.example.amigo_project.dto.CommentDTO;
import com.example.amigo_project.repository.interfaces.AdminRepository;
import com.example.amigo_project.repository.interfaces.NoticeRepository;
import com.example.amigo_project.repository.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {



    private final AdminRepository adminRepository;
    private final NoticeRepository noticeRepository;

    
    public List<User> getUserList(){
        
        List<User> userList = adminRepository.findUserAll();
        
        // TODO 예외처리, 유효성 검사
        
        
        return userList;
    }

    public User findById(int id){
        User user = adminRepository.findById(id);

        return user;
    }

   public AdminDTO findBoardCount(int id) {
        AdminDTO adminDTO = adminRepository.findBoardCount(id);
        return adminDTO;
   }

   public CommentDTO findCommentCount(int id){
        CommentDTO commentDTO = adminRepository.findCommentCount(id);
        return commentDTO;
   }


}
