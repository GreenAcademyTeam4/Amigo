package com.example.amigo_project.service;

import com.example.amigo_project.dto.*;
import com.example.amigo_project.repository.interfaces.AdminRepository;
import com.example.amigo_project.repository.interfaces.NoticeRepository;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.repository.model.payment.ChargeHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {



    private final AdminRepository adminRepository;
    private final NoticeRepository noticeRepository;

    
    public List<User> getUserList(){
        
        List<User> userList = adminRepository.findUserAll();
        
        // TODO 예외처리, 유효성 검사
        
        
        return userList;
    }

    public User findById(int id){
        User user = adminRepository.findById(id);

        return user;
    }

    // 유저 탈퇴
    public void deactivatedUserId(int id){
        adminRepository.deactivatedUserId(id);
    }

    // 탈퇴 회원 조회
    public List<User> findDeletedUsers(){
        return adminRepository.findDeletedUsers();
    }

   public AdminDTO findBoardCount(int id) {
        AdminDTO adminDTO = adminRepository.findBoardCount(id);
        return adminDTO;
   }

   public CommentDTO findCommentCount(int id){
        CommentDTO commentDTO = adminRepository.findCommentCount(id);
        return commentDTO;
   }

   // 결제 내역 조회
    public List<PayListDTO> findChargeHistoryList(){
        return adminRepository.findChargeHistoryList();
    }

    /**
     * 신고
     */
    // 유저 신고 조회
    public List<UserReportDTO> findReportUser(){
        return adminRepository.findReportUser();
    }

    // 게시글 신고 조회
    public List<BoardReportDTO> findReportBoard(){
        return adminRepository.findReportBoard();
    }


    /**
     * chart
     */
    // 성별
    public List<GenderRatioDTO> findGenderCount(){
        return adminRepository.findGenderCount();
    }

    // 나이 분포 조회
    public List<UserDTO> findBirthCount(){
        return adminRepository.findBirthCount();
    }

    // 연별
    public List<ChargeHistory> getYearlySales(){
        return adminRepository.yearlySales();
    }

    // 총 유저 수
    public Integer getTotalUserCount(){
        Integer count = adminRepository.findTotalUserCount();
        System.out.println("count : " + count);
        return count;
    }

    // 총 매출
    public Integer getTotalRevenue(){
        Integer count = adminRepository.totalRevenue();
        System.out.println("totalRevenue : " + count);
        return count;
    }

    // 총 가입자 수
    public Integer getTotalMembers(){
        Integer count = adminRepository.totalMembers();
        System.out.println("totalMembers : " + count);
        return count;
    }

    // 하루 방문자 수
    public Integer getDailyVisits(){
        Integer count = adminRepository.dailyVisits();
        System.out.println("dailyVisits : " + count);
        return count;
    }

    // 총 게시글 수
    public Integer getTotalPosts(){
        Integer count = adminRepository.totalPosts();
        System.out.println("totalPosts : " + count);
        return count;
    }

    // 총 댓글 수
    public Integer getTotalComments(){
        Integer count = adminRepository.totalComments();
        System.out.println("totalComments : " + count);
        return count;
    }

    // 탈퇴 유저 수
    public Integer getWithdrawnUserCount(){
        Integer count = adminRepository.withdrawnUserCount();
        System.out.println("withdrawnUserCount : " + count);
        return count;
    }

    // 가장 유저가 많은 학교
    public List<SchoolUserCountDTO> getTopUserCountSchool(){
        return adminRepository.topUserCountSchool();
    }

    // 가장 잘 팔린 상품들(리스트)
    public List<ProductDTO> getBestSellingProducts(){
        return adminRepository.bestSellingProducts();
    }

    // 베스트 상품
    public List<ProductDTO> getBestSellingItem(){
        return adminRepository.bestSellingItem();
    }
}
