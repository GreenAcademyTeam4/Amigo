package com.example.amigo_project.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.amigo_project.dto.MypageDTO;
import com.example.amigo_project.dto.payment.ChargeHistoryDTO;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.service.MypageService;
import com.example.amigo_project.service.PaymentService;
import com.example.amigo_project.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/my-page")
public class MypageController {

    private final MypageService mypageService;
    private final PaymentService paymentService;
    private final UserService userService;


    /**
     * 마이페이지 호출
     *
     * @param session - 세션에서 현재 로그인 중인 사용자의 id값(pk) 추출
     * @param model   - 마이페이지 렌더링에 필요한 정보를 MypageDTO 형식으로 model 에 담아 전달
     * @return - info.mustache 파일 호출
     */
    @GetMapping("/info")
    public String getMypage(HttpSession session, Model model) {

        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            model.addAttribute("msg", "로그인 후 사용 가능합니다.");
            model.addAttribute("url", "redirect:/user/login");
            return "alert";
        } else {
            MypageDTO dto = mypageService.findMypageInfoByUserId(principal.getId());
            int boardCount = mypageService.countMyBoards(principal.getId());
            int friendCount = mypageService.countFriendByUserId(principal.getId());
            model.addAttribute("dto", dto);
            model.addAttribute("user", principal);
            model.addAttribute("boardCount", boardCount);
            model.addAttribute("friendCount", friendCount);
            return "views/mypage/info";
        }
    }

    /**
     * 인벤토리 호출
     * @param session - 세션에서 userId추출
     * @param model   - 인벤토리 정보를 Model에 담아 전달 (내가 가진 아바타 + 현재 입고있는 아바타)
     * @return - inventory.mustache 호출
     */
    @GetMapping("/inventory")
    public String getInventory(HttpSession session, Model model) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            model.addAttribute("msg", "로그인 정보가 만료되었습니다. 다시 로그인해 주세요");
            model.addAttribute("url", "redirect:/user/login");
            return "/alert";
        } else {
            List<MypageDTO.inventoryDTO> inventorydto = mypageService.findInventoryByUserId(principal.getId());
            MypageDTO.nowAvatarDTO nowAvatarDTO = mypageService.findNowAvatarByUserId(principal.getId());
            model.addAttribute("inventoryList", inventorydto);
            model.addAttribute("nowAvatar", nowAvatarDTO);
            model.addAttribute("user", principal);
            return "views/mypage/inventory";
        }

    }

    /**
     * 아바타 변경 동작
     * 인벤토리에서 아바타 변경 시 동작. 뷰에서 각 부위의 아바타 id를 받아와 db에서 가져온
     * 현재 보유중인 아바타 목록과 대조한 후 모두 유효한 값일 시 아바타 변경 기능 실행
     *
     * @param session
     * @param model
     * @param request
     * @return
     */
    @PostMapping("/inventory/change")
    public String changeAvatar(HttpSession session, Model model, HttpServletRequest request) {
        Integer head = Integer.parseInt(request.getParameter("head"));
        Integer top = Integer.parseInt(request.getParameter("top"));
        Integer bottom = Integer.parseInt(request.getParameter("bottom"));
        Integer shoes = Integer.parseInt(request.getParameter("shoes"));
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            model.addAttribute("msg", "로그인 정보가 만료되었습니다. 다시 로그인해 주세요");
            model.addAttribute("url", "redirect:/user/login");
            return "alert";
        } else {
            List<MypageDTO.inventoryDTO> dto = mypageService.findInventoryByUserId(principal.getId());
            int having = 0; // 변경 요청한 아바타를 보유 중인지 확인하는 변수
            for (int i = 0; i < dto.size(); i++) {
                if (dto.get(i).getAvatarId() == head) {
                    having++;
                } else if (dto.get(i).getAvatarId() == top) {
                    having++;
                } else if (dto.get(i).getAvatarId() == bottom) {
                    having++;
                } else if (dto.get(i).getAvatarId() == shoes) {
                    having++;
                }
            }

            if (having == 4) {
                MypageDTO.nowAvatarDTO nowAvatarDTOdto = MypageDTO.nowAvatarDTO.builder()
                        .userId(principal.getId()).head(head).top(top)
                        .bottom(bottom).shoes(shoes).build();
                mypageService.updateNowAvatarByAvatarChangeDTO(nowAvatarDTOdto);
                model.addAttribute("msg", "아바타 변경 완료.");
                model.addAttribute("url", "/my-page/inventory");
                return "alert";
            } else {
                model.addAttribute("msg", "아바타 기간이 만료되었거나 소유하지 않은 아바타입니다. 다시 시도해 주세요");
                model.addAttribute("url", "/my-page/inventory");
                return "alert";
            }
        }

    }

    // 비밀번호 변경 전 기존 비밀번호 확인 창 띄우기
    @GetMapping("/pwdcheck")
    public String pwdcheck(){
        System.out.println("여기왜안탐??");
        return "/views/mypage/pw-check";
    }

    /**
     * 비밀번호 변경 요청 전 기존 비밀번호 1회 입력 확인 기능
     * @param password 기존 pw
     * @param session 에서 userId 추출
     * @return 세션에서 얻은 userId로 db의 저장된 pwd 조회 후 입력한 password 값이랑 대조 (유저서비스에서 수행)
     */
    @GetMapping("/pwdValid")
    public String pwdValid(String password, HttpSession session, Model model, HttpServletRequest request) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            model.addAttribute("msg", "로그인 정보가 만료되었습니다. 다시 로그인해 주세요");
            model.addAttribute("url", "redirect:/user/login");
            return "alert";
        } else {
            int result = userService.checkPasswordValid(principal.getId(), password);
            if(result == 1){
                model.addAttribute("user", principal);
                return "/views/mypage/pw-ch";
            }else{
                model.addAttribute("msg", "비밀번호가 일치하지 않습니다. 확인 후 다시 시도해 주세요");
                model.addAttribute("url", "/my-page/info");
                return "alert";
            }
        }

    }

    /**
     * 비밀번호 변경 기능
     * @param session 에서 userId 추출
     * @return alert창 출력 후 mypage 초기 페이지로 이동
     */
    @PostMapping("/pwd-update")
    public String pwdUpdate(HttpServletRequest request,  HttpSession session, Model model){
        String password = request.getParameter("password");
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            model.addAttribute("msg", "로그인 정보가 만료되었습니다. 다시 로그인해 주세요");
            model.addAttribute("url", "redirect:/user/login");
            return "alert";
        } else {
            // 회원가입 뷰의 비밀번호 입력값 유효성 체크 재활용
            userService.updatePasswordByUserId(principal.getId(), password);
            model.addAttribute("msg", "비밀번호 변경 완료.");
            model.addAttribute("url", "/my-page/info");
            return "alert";
        }
    }



    /**
     * 친구목록 출력
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/friend-list")
    public String friendList(HttpSession session, Model model){
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            model.addAttribute("msg", "로그인 정보가 만료되었습니다. 다시 로그인해 주세요");
            model.addAttribute("url", "redirect:/user/login");
            return "alert";
        } else {
            List<MypageDTO.myFriendListDTO> friendList = mypageService.findMyFriendListByUserId(principal.getId());
            List<MypageDTO.friendReqDTO> friendReqList = mypageService.findFriendReqByUserId(principal.getId());
            List<MypageDTO.reccomendFriendDTO> recFriendList = mypageService.findRecommendFriendListByBirthAndSchool(principal);
            List<MypageDTO.friendReqDTO> myFriendReq = mypageService.myFriendReq(principal.getId());

            model.addAttribute("friendList" ,friendList); // 친구 목록
            model.addAttribute("recFriendList", recFriendList); // 추천 친구(나이가 같고 학교가 같은 적이 있는 유저) 목록
            model.addAttribute("friendReqList", friendReqList); // 받은 친구요청 목록
            model.addAttribute("myFriendReq", myFriendReq); // 내가 보낸 친구요청 목록
            model.addAttribute("user", principal);

            return "/views/mypage/friend-management";
        }
    }


    /**
     * 내 친구 목록 중 이름으로 친구 검색 기능
     * 비동기 방식으로 검색 내용을 전달받아 결과 리턴
     * @param search
     * @param session
     * @return
     */
    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<?> searchFriend(@RequestParam("search") String search, HttpSession session) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 만료되었습니다. 다시 로그인해 주세요");
        } else {
            List<MypageDTO.myFriendListDTO> friendList = mypageService.searchFriend(principal.getId(), search);
            if (friendList.isEmpty()) {
                return ResponseEntity.ok("검색 결과가 없습니다.");
            }
            return ResponseEntity.ok(friendList);
        }
    }


    /**
     * 학교 또는 이름을 입력받아서 해당 정보로 사용자 검색
     */
    @GetMapping("/find")
    @ResponseBody
    public ResponseEntity<?> findFriendBySchoolOrName(@RequestParam("search") String search, HttpSession session) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 만료되었습니다. 다시 로그인해 주세요");
        } else {
            List<MypageDTO.myFriendListDTO> friendList = mypageService.findFriendBySchoolOrName(principal.getId(), search);
            if (friendList.isEmpty()) {
                return ResponseEntity.ok("검색 결과가 없습니다.");
            }
            return ResponseEntity.ok(friendList);
        }
    }


    /**
     * 친구요청 보내기 기능
     */
    @ResponseBody
    @GetMapping("/send-friend-req")
    public ResponseEntity<?> sendFriendReq(HttpSession session, Model model,@RequestParam("receiverId")Integer receiverId){
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 만료되었습니다. 다시 로그인해 주세요");
        } else {
            mypageService.reqFriend(principal.getId(), receiverId);
            return ResponseEntity.ok("친구 요청 보냄.");
        }
    }



 

    /**
     * 친구요청 수락 기능
     */
    @ResponseBody
    @PostMapping("/accept-friend-req")
    public ResponseEntity<?> acceptFriendReq(HttpSession session, Model model, @RequestParam("receiverId")Integer receiverId){
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 만료되었습니다. 다시 로그인해 주세요");
        } else {
            mypageService.acceptFriendReq(principal.getId(), receiverId);
            return ResponseEntity.ok("친구 요청 수락됨.");
        }
    }

    // 친구요청 취소 기능
    @ResponseBody
    @PostMapping("/cancel-friend-req")
    public ResponseEntity<?> cancleFriendReq(HttpSession session, Model model, @RequestParam("receiverId")Integer receiverId){
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 만료되었습니다. 다시 로그인해 주세요");
        } else {
            mypageService.cancelfriendreq(principal.getId(), receiverId);
            return ResponseEntity.ok("친구 요청 취소됨.");
        }
    }




    /**
     * 포인트 층전 내역 조회 (결제 측 기능 재활용)
     * TODO 포인트 결제 내역 조회
     * @param session - userId 추출
     * @return 포인트 충전 내역 정보 + 페이징
     */
    @PostMapping("/charge-history")
    public String findChargeHistory(HttpSession session, Model model){
        int page = 1;
        int size = 10;
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            model.addAttribute("msg", "로그인 정보가 만료되었습니다. 다시 로그인해 주세요");
            model.addAttribute("url", "redirect:/user/login");
            return "alert";
        } else {
            List<ChargeHistoryDTO> list = paymentService.readChargeHistory(page, size, principal.getId());
            model.addAttribute("paylist", list);
            model.addAttribute("user", principal);
            return "/views/mypage/charge-history";
        }


    }

    /**
     * 받은 친구 요청 거부
     * @param session
     * @param senderId
     * @return
     */
    @GetMapping("/reject")
    @ResponseBody
    public ResponseEntity<?> rejectFriendReq(HttpSession session, @RequestParam("senderId") Integer senderId) {
        User principal = (User) session.getAttribute("principal");
        Integer result = 0;
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 만료되었습니다. 다시 로그인해 주세요");
        } else {

                mypageService.rejectFriendReq(principal.getId(), senderId);
                return ResponseEntity.ok("친구 요청 삭제");

        }
    }


    /**
     * 친구 삭제 기능
     */
    @ResponseBody
    @GetMapping("/delete-friend")
    public ResponseEntity<?> deleteFriend(HttpSession session, @RequestParam("friendId") Integer friendId) {
        User principal = (User) session.getAttribute("principal");
        Integer result = 0;
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 정보가 만료되었습니다. 다시 로그인해 주세요");
        } else {
            mypageService.deleteFriendByUserIdAndFriendId(friendId, principal.getId());
            return ResponseEntity.ok("친구 삭제 완료");
        }
    }
    









}














