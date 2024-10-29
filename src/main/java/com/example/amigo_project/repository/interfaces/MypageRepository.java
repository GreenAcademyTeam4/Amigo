package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.dto.MypageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MypageRepository {

    // 유저 id로 마이페이지 정보 조회
    public MypageDTO findMypageInfoByUserId(Integer userId);

    // 유저 id로 아바타 인벤토리 조회
    public List<MypageDTO.inventoryDTO> findInventoryByUserId(Integer userId);

    // 내 현재 아바타 정보 가져오기
    public MypageDTO.nowAvatarDTO findNowAvatarByUserId(Integer userId);

    // 인벤토리 뷰에서 아바타 변경하기
    public void updateNowAvatarByAvatarChangeDTO(@Param("userId")Integer userId, @Param("head")Integer head,
                                                 @Param("top")Integer top,@Param("bottom")Integer bottom,
                                                 @Param("shoes")Integer shoes);

    // 마이페이지에서 내 정보 변경 기능
    public void updateStatusByStatusDTO(@Param("userId")Integer userId, @Param("nickname")String nickname,
                                        @Param("elementarySchool")String elementarySchool,
                                        @Param("middleSchool")String middleSchool,
                                        @Param("highSchool")String highSchool);

    // 내 친구 목록 조회
    public List<MypageDTO.myFriendListDTO> findMyFriendListByUserId(Integer userId);

    // 내 친구 목록에서 검색기능 적용
    public List<MypageDTO.myFriendListDTO> searchFriend(@Param("userId") Integer userId, @Param("search") String search);

    //유저 id로 나한테 온 친구 요청 목록 조회
    public List<MypageDTO.friendReqDTO> findFriendReqByUserId(Integer userId);

    // 친구요청 보내는 사람 id와 받는 사람 id로 친구추가 요청 정보 삽입
    public void insertFriendReqBySenderIdAndReceiverId(@Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId);

    // 친구요청 수락 part1 친구 테이블에 정보 삽입(수락한 쪽)
    public void insertFriendBySenderIdAndReceiverIdToSender(@Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId);

    // 친구요청 수락 part2 친구 테이블에 정보 삽입(요청한 쪽)
    public void insertFriendBySenderIdAndReceiverIdToReceiver(@Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId);

    // 친구요청 수락 part3 친구요청 테이블의 수락한 기존 요청 정보 삭제
    public void deleteFriendReqBySenderIdAndReceiverId(@Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId);

    // 추천 친구 조회
    public List<MypageDTO.reccomendFriendDTO> findRecommendFriendListByBirthAndSchool(@Param("elementarySchool") String elementarySchool, @Param("middleSchool") String middleSchool, @Param("highSchool")String highSchool , @Param("year")Integer year);
}
