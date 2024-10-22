package com.example.amigo_project.controller;

import com.example.amigo_project.dto.MypageDTO;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.service.MypageService;
import com.example.amigo_project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/my-page")
public class MypageController {

    private final MypageService mypageService;

    private final UserService userService;


    /**
     * 마이페이지 호출
     *
     * @param session - 세션에서 현재 로그인 중인 사용자의 id값(pk) 추출
     * @param model   - 마이페이지 렌더링에 필요한 정보를 MypageDTO 형식으로 model 에 담아 전달
     * @return - info.mustache 파일 호출
     */
    @GetMapping("/")
    public String getMypage(HttpSession session, Model model) {

        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            model.addAttribute("msg", "로그인 후 사용 가능합니다.");
            model.addAttribute("url", "redirect:/user/login");
            return "alert";
        } else {
            MypageDTO dto = mypageService.findMypageInfoByUserId(principal.getId());
            model.addAttribute("dto", dto);

            return "views/mypage/info";
        }
    }

    /**
     * @param session - 세션에서 userId추출
     * @param model   - 인벤토리 정보를 Model에 담아 전달 (내가 가진 아바타 + 현재 입고있는 아바타)
     * @return - inventory.mustache 파일 호출
     */
    @GetMapping("/inventory")
    public String getInventory(HttpSession session, Model model) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            model.addAttribute("msg", "로그인 정보가 만료되었습니다. 다시 로그인해 주세요");
            model.addAttribute("url", "redirect:/user/login");
            return "alert";
        } else {
            List<MypageDTO.inventoryDTO> inventorydto = mypageService.findInventoryByUserId(principal.getId());
            MypageDTO.nowAvatarDTO nowAvatarDTO = mypageService.findNowAvatarByUserId(principal.getId());
            model.addAttribute("inventoryList", inventorydto);
            model.addAttribute("nowAvatar", nowAvatarDTO);
            return "views/mypage/inventory";
        }

    }

    /**
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

    /**
     * 비밀번호 변경 요청 전 기존 비밀번호 1회 입력 확인 기능
     * @param password 기존 pw
     * @param session 에서 userId 추출
     * @return 세션에서 얻은 userId로 db의 저장된 pwd 조회 후 입력한 password 값이랑 대조 (유저서비스에서 수행)
     */
    @PostMapping("/pwdValid")
    public String pwdValid(String password, HttpSession session, Model model, HttpServletRequest request) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            model.addAttribute("msg", "로그인 정보가 만료되었습니다. 다시 로그인해 주세요");
            model.addAttribute("url", "redirect:/user/login");
            return "alert";
        } else {
            int result = userService.checkPasswordValid(principal.getId(), password);
            if(result == 1){
                // TODO - 뷰에서 비동기로 처리
                return "";
            }else{
                model.addAttribute("msg", "비밀번호가 일치하지 않습니다. 확인 후 다시 시도해 주세요");
                model.addAttribute("url", "/my-page/");
                return "alert";
            }
        }

    }

    /**
     * 비밀번호 변경 기능
     * @param password 변경할 비밀번호
     * @param session 에서 userId 추출
     * @return alert창 출력 후 mypage 초기 페이지로 이동
     */
    @PostMapping("/pwdupdate")
    public String pwdUpdate(String password, HttpSession session, Model model){
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            model.addAttribute("msg", "로그인 정보가 만료되었습니다. 다시 로그인해 주세요");
            model.addAttribute("url", "redirect:/user/login");
            return "alert";
        } else {
            // 회원가입 뷰의 비밀번호 입력값 유효성 체크 재활용
            userService.updatePasswordByUserId(principal.getId(), password);
            model.addAttribute("msg", "비밀번호 변경 완료.");
            model.addAttribute("url", "/my-page/");
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
            // 뷰 측에서 친구가 0명일때 등록된 친구가 없습니다. 띄우기
            model.addAttribute("friendList" ,friendList);
            return "";  // TODO - 뷰 mustache 파일 명 넣기
        }
    }


    /**
     * 받은 친구 요청 출력
     * @param session
     * @param model
     * @return
     */
    @GetMapping("/friend-req-list")
    public String friendReqList(HttpSession session, Model model) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            model.addAttribute("msg", "로그인 정보가 만료되었습니다. 다시 로그인해 주세요");
            model.addAttribute("url", "redirect:/user/login");
            return "alert";
        } else {
            List<MypageDTO.friendReqDTO> friendReqList = mypageService.findFriendReqByUserId(principal.getId());
            // 받은 친구 요청이 없을 때 뷰 측에서 받은 친구 요청이 없습니다 출력
            model.addAttribute("friendReqList", friendReqList);
            return ""; // TODO - 뷰 mustache 파일 명 넣기
        }
    }



    /**
     * 친구요청 보내기 기능
     * @param session
     * @param model
     * @return
     */
    @PostMapping("/send-friend-req")
    public String sendFriendReq(HttpSession session, Model model, HttpServletRequest request){
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            model.addAttribute("msg", "로그인 정보가 만료되었습니다. 다시 로그인해 주세요");
            model.addAttribute("url", "redirect:/user/login");
            return "alert";
        } else {
            Integer receiverId = Integer.parseInt(request.getParameter("receiverId"));
            mypageService.reqFriend(principal.getId(), receiverId);
            model.addAttribute("msg", "친구 요청 보내기 완료");
            model.addAttribute("url", "/my-page/friend-list");
            return "alert";
        }
    }



 

    /**
     * 친구요청 수락 기능
     * @param session - 유저 id 추출
     * @param model
     * @param request - 요청 보낸 이 id 추출
     * @return 내 친구 리스트 재호출
     */
    @PostMapping("/accept-friend-req")
    public String acceptFriendReq(HttpSession session, Model model, HttpServletRequest request){
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            model.addAttribute("msg", "로그인 정보가 만료되었습니다. 다시 로그인해 주세요");
            model.addAttribute("url", "redirect:/user/login");
            return "alert";
        } else {
            Integer receiverId = Integer.parseInt(request.getParameter("receiverId"));
            mypageService.acceptFriendReq(principal.getId(), receiverId);
            model.addAttribute("msg", "친구 요청이 수락되었습니다.");
            model.addAttribute("url", "/my-page/friend-list");
            return "alert";
        }
    }



}














