package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.dto.StoreDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository {
    
    // 상점 방문시 모든 아바타 정보 조회(아바타 id , 아바타 이름 , 아바타 가격, 아바타 타입, 보유중 여부)
    public List<StoreDTO.avatarListDTO> readAllAvatarList(Integer userId);

    // 머리 아바타 정보 필터링해서 조회(아바타 id , 아바타 이름 , 아바타 가격, 아바타 타입, 보유중 여부)
    public List<StoreDTO.avatarListDTO> readHairAvatarList(Integer userId);

    // 상의 아바타 정보 필터링해서 조회(아바타 id , 아바타 이름 , 아바타 가격, 아바타 타입, 보유중 여부)
    public List<StoreDTO.avatarListDTO> readTopAvatarList(Integer userId);

    // 하의 아바타 정보 필터링해서 조회(아바타 id , 아바타 이름 , 아바타 가격, 아바타 타입, 보유중 여부)
    public List<StoreDTO.avatarListDTO> readBottomAvatarList(Integer userId);

    // 신발 아바타 정보 필터링해서 조회(아바타 id , 아바타 이름 , 아바타 가격, 아바타 타입, 보유중 여부)
    public List<StoreDTO.avatarListDTO> readShoesAvatarList(Integer userId);
}
