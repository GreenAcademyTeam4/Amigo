package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.repository.model.Emoticon;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChatRepository {

    public List<Emoticon>findEmoticonList();
}
