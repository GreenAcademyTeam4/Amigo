package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.dto.StoreDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
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

    // 아바타 이름으로 아바타 검색
    public List<StoreDTO.avatarListDTO> searchAvatarListByName(@Param("userId") Integer userId, @Param("search") String search);

    // 아바타 구매 시 포인트 차감 기능
    public void updateUserPointByUserId(Integer userId);

    // 아바타 구매 시 인벤토리에 아바타 정보 삽입 기능
    public void insertAvatarInventoryByAvatarIdAndUserId(@Param("userId") Integer userId, @Param("avatarId") Integer avatarId);

    // 구매 후 포인트 히스토리 테이블에 히스토리 정보 삽입
    public void insertPointHistory(@Param("userId")Integer userId,
                                   @Param("orderHead") String orderHead,
                                   @Param("orderBody") String orderBody,
                                   @Param("usePoint") Integer usePoint,
                                   @Param("lessPoint")Integer lessPoint);

}
