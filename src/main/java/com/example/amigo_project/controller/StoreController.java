package com.example.amigo_project.controller;


import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.amigo_project.dto.StoreDTO;
import com.example.amigo_project.repository.interfaces.StoreRepository;
import com.example.amigo_project.repository.model.User;
import com.example.amigo_project.service.StoreService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreService storeService;
    
    private final HttpSession session;

    /**
     * 뷰에 표시할 아바타 리스트
     * @param type(아바타 부위) = 0 -> 모든 아바타 , 1 -> 머리 , 2 -> 상의 ...
     * @return 아바타의 id, 이름, 부위, 가격, 보유중 여부  등을 model에 "avatarList" 키값으로 담아 뷰에 유저정보와 함께 전달
     */
    @GetMapping("/shop")
    public String getAllAvatarInfo(Model model,
                                   @RequestParam(name = "type", defaultValue = "0") Integer type){
        User principal = (User) session.getAttribute("principal");
        System.out.println(principal);
        if (principal == null) {
            model.addAttribute("msg", "로그인 후 이용 가능합니다. 먼저 로그인 해 주세요");
            model.addAttribute("url", "redirect:/user/login");
            return "alert";
        } else {
            if(type == 0){
                List<StoreDTO.avatarListDTO> avatarList = storeService.readAllAvatarList(principal.getId());
                model.addAttribute("avatarList", avatarList);
                model.addAttribute("user", principal);
                return "/views/store/main";
            }else if(type == 1){
                List<StoreDTO.avatarListDTO> avatarList = storeService.readHairAvatarList(principal.getId());
                model.addAttribute("avatarList", avatarList);
                model.addAttribute("user", principal);
                return "views/store/main";
            }else if(type == 2) {
                List<StoreDTO.avatarListDTO> avatarList = storeService.readTopAvatarList(principal.getId());
                model.addAttribute("avatarList", avatarList);
                model.addAttribute("user", principal);
                return "views/store/main";
            }else if(type == 3) {
                List<StoreDTO.avatarListDTO> avatarList = storeService.readBottomAvatarList(principal.getId());
                model.addAttribute("avatarList", avatarList);
                model.addAttribute("user", principal);
                return "views/store/main";
            }else if(type == 4) {
                List<StoreDTO.avatarListDTO> avatarList = storeService.readShoesAvatarList(principal.getId());
                model.addAttribute("avatarList", avatarList);
                model.addAttribute("user", principal);
                return "views/store/main";
            }else{
                model.addAttribute("msg", "알 수 없는 오류 다시 시도해 주세요");
                model.addAttribute("url", "redirect:/user/login");
                return "alert"; 
            }
        }


    }

    /**
     * 비동기적으로 검색 내용을 전달받아 결과값 리턴
     * @param search
     * @param session
     * @return
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchAvatarList(@RequestParam("search") String search, HttpSession session) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "로그인 후 이용 가능합니다. 먼저 로그인 해 주세요"));
        } else {
            List<StoreDTO.avatarListDTO> avatarList = storeService.searchAvatarListByName(principal.getId(), search);
            return ResponseEntity.ok(avatarList);
        }
    }






    /**
     * fetch 비동기 방식으로 장바구니에 있는 물건 구매 로직 전달
     * @param dto
     * @param model
     * @return
     */
    @PostMapping("/buy")
    public ResponseEntity<String> buyBasket(@RequestBody StoreDTO.basketDTO dto) {
        User principal = (User) session.getAttribute("principal");
        if (principal == null) {
            return ResponseEntity.ok("UnAuthorized");
        } else {
            if (principal.getPoint() < dto.getTotalPrice()) {
                return ResponseEntity.ok("LackOfPoint");
            } else {
                // 포인트 차감 및 구매 처리 로직 수행
                User user = storeService.buyBasket(new StoreDTO.pointHistoryDTO(
                        principal.getId(),
                        dto.getTotalPrice(),
                        principal.getPoint(),
                        dto.getProdNameList(),
                        dto.getProdIdList()
                ));
                // 구매한 아바타 id를 히스토리 테이블에 전달
                for(int i = 0; i < dto.getProdIdList().length; i++){
                    storeService.insertProdHistory(dto.getProdIdList()[i]);
                }
                session.setAttribute("principal", user);
                return ResponseEntity.ok("Success");
            }
        }
    }
















}
