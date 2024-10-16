package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.repository.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminRepository {

    public List<User> findUserAll();


}
