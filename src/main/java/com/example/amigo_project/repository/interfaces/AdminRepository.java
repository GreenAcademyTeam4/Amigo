package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.dto.UserDTO;
import com.example.amigo_project.repository.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminRepository {

    // 관리자 유저 추가 (TODO - 생각해보기)
    public int insert(User user);

    // 유저 조회
    public List<User> findUserAll();
    
    // 유저 상세보기 (Detail)

    // 유저 수
    public int countUser();

}
