package com.example.amigo_project.service;


import com.example.amigo_project.dto.MypageDTO;
import com.example.amigo_project.repository.interfaces.MypageRepository;
import com.example.amigo_project.repository.model.User;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.amigo_project.repository.interfaces.BoardRepository;
import com.example.amigo_project.repository.interfaces.UserRepository;

@Service
@RequiredArgsConstructor
public class MypageService {

    private final MypageRepository mypageRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    /**
     * 유저 id로 마이페이지 호출에 필요한 정보 조회
     * @param userId (user의 pk아이디 / 로그인 아이디 아님)
     * @return myPage 뷰 호출시 필요한 모든 정보를 MypageDTO 에 담아 보냄
     */
    public MypageDTO.nowAvatarDTO findMypageInfoByUserId(Integer userId){
        return mypageRepository.findNowAvatarByUserId(userId);
    }

    /**
     * 유저 Id로 아바타 인벤토리 조회
     * @param userId (user의 pk아이디 / 로그인 아이디 아님)
     * @return 내가 보유중인 아바타 목록 리턴
     */
    public List<MypageDTO.inventoryDTO> findInventoryByUserId(Integer userId){
        return mypageRepository.findInventoryByUserId(userId);
    }





    /**
     * userId로 현재 착용중인 아바타 정보 조회
     * @param userId
     * @return 현재 착용중인 아바타 정보를 MypageDTO.nowAvatarDTO 에 담아 리턴
     */
    public MypageDTO.nowAvatarDTO findNowAvatarByUserId(Integer userId){
        return mypageRepository.findNowAvatarByUserId(userId);
    }


    /**
     * 아바타 변경에 필요한 userId / 각 부위 아바타 Id 를 dto형식으로 받아와 아바타 변경 실행
     * @param dto
     */
    @Transactional
    public void updateNowAvatarByAvatarChangeDTO(MypageDTO.nowAvatarDTO dto){
        mypageRepository.updateNowAvatarByAvatarChangeDTO(dto.getUserId(), dto.getHead(), dto.getTop(), dto.getBottom(), dto.getShoes());
    }

    /**
     * 마이페이지 -> 친구관리 요청시 친구 목록 출력
     * @param userId
     * @return 내 친구 목록 출력
     */
    public List<MypageDTO.myFriendListDTO> findMyFriendListByUserId(Integer userId){
        return mypageRepository.findMyFriendListByUserId(userId);
    }

    public List<MypageDTO.myFriendListDTO> searchFriend(Integer userId, String search){
        return mypageRepository.searchFriend(userId, search);
    }




    /**
     * 마이페이지 -> 친구 관리 -> 받은 친구 요청 목록 조회 기능
     * @param userId
     * @return 받은 친구 요청 리스트 (요청자 이름 , 요청자 id)
     */
    public List<MypageDTO.friendReqDTO> findFriendReqByUserId(Integer userId){
        return mypageRepository.findFriendReqByUserId(userId);
    }

    // 이름 or 학교 입력값으로 친구 찾기
    public List<MypageDTO.myFriendListDTO> findFriendBySchoolOrName(Integer userId, String search){
        return mypageRepository.findFriendBySchoolOrName(userId, search);
    }

    /**
     * 친구 요청 기능
     * @param senderId 보내는 쪽 id
     * @param receiverId 받는 쪽 id
     */
    @Transactional
    public void reqFriend(Integer senderId, Integer receiverId){
        mypageRepository.insertFriendReqBySenderIdAndReceiverId(senderId, receiverId);
    }

    // 보낸 친구 요청 취소 기능
    @Transactional
    public void cancelfriendreq(Integer senderId, Integer receiverId){
        mypageRepository.deleteFriendReqBySenderIdAndReceiverId(senderId, receiverId);
    }



    /**
     * 친구요청 수락 시 보낸쪽 / 받은쪽 양측의 친구 목록에 등록하는 쿼리 발송 / 수락한 친구요청 삭제 처리
     * @param senderId 친구요청을 보낸 쪽의 id
     * @param receiverId 친구요청을 받은 쪽의 id
     */
    @Transactional
    public void acceptFriendReq(Integer senderId, Integer receiverId){
        mypageRepository.insertFriendBySenderIdAndReceiverIdToReceiver(senderId, receiverId); // 받는쪽 친구 정보 삽입
        mypageRepository.insertFriendBySenderIdAndReceiverIdToSender(senderId, receiverId); // 보내는쪽 친구 정보 삽입
        mypageRepository.deleteFriendReqBySenderIdAndReceiverId(senderId, receiverId); // 수락한 친구 요청 삭제
    }


    // 추천 친구 조회
    public List<MypageDTO.reccomendFriendDTO> findRecommendFriendListByBirthAndSchool(User user){
        List<MypageDTO.userSchoolDTO> dto = mypageRepository.findSchoolIdByUserId(user.getId());
        Integer year = user.getBirth()/(10*10*10*10); // 생년월일 8자리중 앞 4자리만 추출
        List<MypageDTO.reccomendFriendDTO> recFriList = new ArrayList<>();
        for(int i = 0; i < dto.size(); i++){
            recFriList.addAll(mypageRepository.findRecommendFriendListByBirthAndSchool(user.getId(), dto.get(i).getSchoolId(), year));
        }
        Set<MypageDTO.reccomendFriendDTO> recset = new HashSet<>(recFriList); // 중복 제거를 위해 List를 set으로 변환
        List<MypageDTO.reccomendFriendDTO> distinctList = new ArrayList<>(recset);  // set에서 다시 List로 변환
        return distinctList;
    }

    public int countMyBoards (int id){
        return boardRepository.countBoardByUserId(id);
    } 
    public int countFriendByUserId (int id){
        return userRepository.countFriendByUserId(id);
    } 
    // 받는 id와 보내는 id로 친구 요청 정보 여부 확인
    public Integer findFriendReq(Integer senderId, Integer receiverId){
        return mypageRepository.findCountFriendReqBySenderIdAndReceiverId(senderId, receiverId);
    }

    // 친구 요청 거부
    public void rejectFriendReq(Integer senderId, Integer receiverId){
        mypageRepository.deleteFriendReqBySenderIdAndReceiverId(senderId, receiverId);
    }

    // 내가 보낸 친구 요청 리스트 조회
    public List<MypageDTO.friendReqDTO> myFriendReq(Integer userId){
        return mypageRepository.findMyFriendReqByUserId(userId);
    }

    // 친구 삭제
    public void deleteFriendByUserIdAndFriendId(Integer userId, Integer friendId){
        mypageRepository.deleteFriendByUserIdAndFriendId(userId, friendId);
    }

    // 닉네임 변경
    public void changeNickname(Integer userId, String nickname){
        mypageRepository.changeNickname(userId, nickname);
    }

    // 학교 변경
    public void changeSchool(Integer userId, String school){
        mypageRepository.changeSchool(userId, school);
    }



}
