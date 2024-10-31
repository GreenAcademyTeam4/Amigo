package com.example.amigo_project.service;

import com.example.amigo_project.dto.StoreDTO;
import com.example.amigo_project.repository.interfaces.StoreRepository;
import com.example.amigo_project.repository.interfaces.UserRepository;
import com.example.amigo_project.repository.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;

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

    // 아바타 이름으로 아바타 검색
    public List<StoreDTO.avatarListDTO> searchAvatarListByName(Integer userId, String search){
        return storeRepository.searchAvatarListByName(userId, search);
    }


    /**
     * 아바타 구매 로직
     * TODO 개별 예외처리
     */
    @Transactional
    public User buyBasket(StoreDTO.pointHistoryDTO dto){
        // 유저 포인트 차감
        storeRepository.updateUserPointByUserId(dto.getUserId(), dto.getLessPoint());
        // 유저 인벤토리에 구매한 아바타 정보 입력
        for(int i = 0; i<dto.getProdIdList().length; i++){
            storeRepository.insertAvatarInventoryByAvatarIdAndUserId(dto.getUserId(), dto.getProdIdList()[i]);
        }
        // 포인트 히스토리 입력
        storeRepository.insertPointHistory(dto.getUserId(), dto.getOrderHead(), dto.getOrderBody(), dto.getUsePoint(), dto.getLessPoint());

        return userRepository.findUserById(dto.getUserId());

    }

    // 구매한 아바타 id를 아바타 구매 히스토리 테이블에 넣기
    @Transactional
    public void insertProdHistory(Integer avatarId){
        storeRepository.insertProdHistory(avatarId);
    }



}
