package com.example.amigo_project.service;

import com.example.amigo_project.dto.StoreDTO;
import com.example.amigo_project.repository.interfaces.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;


    /**
     *  모든 아바타 조회 (id, 이름, 가격, 타입)
     *  유저 id를 입력값으로 받아 보유중 여부 확인
      * @param userId
     *  @return avatarListDTO
     */ 
    public List<StoreDTO.avatarListDTO> readAllAvatarList(Integer userId){
        return storeRepository.readAllAvatarList(userId);
    }

    // 머리 아바타만 조회
    public List<StoreDTO.avatarListDTO> readHairAvatarList (Integer userId){
        return storeRepository.readHairAvatarList(userId);
    }

    // 상의 아바타만 조회
    public List<StoreDTO.avatarListDTO> readTopAvatarList (Integer userId){
        return storeRepository.readTopAvatarList(userId);
    }

    // 하의 아바타만 조회
    public List<StoreDTO.avatarListDTO> readBottomAvatarList (Integer userId){
        return storeRepository.readBottomAvatarList(userId);
    }

    // 신발 아바타만 조회
    public List<StoreDTO.avatarListDTO> readShoesAvatarList (Integer userId){
        return storeRepository.readShoesAvatarList(userId);
    }


}
