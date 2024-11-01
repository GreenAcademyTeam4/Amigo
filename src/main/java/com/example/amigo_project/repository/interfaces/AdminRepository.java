package com.example.amigo_project.repository.interfaces;

import com.example.amigo_project.dto.*;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.repository.model.payment.ChargeHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminRepository {


    // 유저 조회
    public List<User> findUserAll();

    // 탈퇴 회원 조회
    public List<User> findDeletedUsers();

    // 유저 상세보기 (Detail)
    public User findById(int id);

    // 유저 탈퇴 처리
    public int deactivatedUserId(int id);

    // 게시글 개수 조회 (userId)
    public AdminDTO findBoardCount(int userId);

    // 댓글 개수 조회 (userId)
    public CommentDTO findCommentCount(int userId);

    // 결제 조회
    public List<PayListDTO> findChargeHistoryList();

    /**
     * 신고
     */
    // 유저 신고 조회
    public List<UserReportDTO> findReportUser();

    // 게시글 신고 조회
    public List<BoardReportDTO> findReportBoard();

    /**
     * chart
     */
    // 남녀 성비 조회
    public List<GenderRatioDTO> findGenderCount ();

    // 나이 분포 조회
    public List<UserDTO> findBirthCount();

    // 연별
    public List<ChargeHistory> yearlySales();

    // 총 유저 수
    public Integer findTotalUserCount();

    // 총 매출
    public Integer totalRevenue();

    // 총 가입자 수
    public Integer totalMembers();

    // 하루 방문자 수
    public Integer dailyVisits();

    // 총 게시글 수
    public Integer totalPosts();

    // 총 댓글 수
    public Integer totalComments();

    // 탈퇴 유저 수
    public Integer withdrawnUserCount();

    // 가장 유저가 많은 학교 순위 3 조회
    public List<SchoolUserCountDTO> topUserCountSchool();

    // 가장 잘 팔린 상품 (리스트)
    public List<ProductDTO> bestSellingProducts();

    // 베스트 상품
    public List<ProductDTO> bestSellingItem();
}
