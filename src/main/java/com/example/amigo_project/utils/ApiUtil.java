package com.example.amigo_project.utils;

import lombok.Data;

@Data
public class ApiUtil<T> {
    
    
    private Integer status; // 협의 - 1이면 성공 , -1이면 실패
    private String msg;
    private T body; // 동일한 변수, 다른 데이터 타입 -> 제네릭 T 사용

    public ApiUtil(T body) {
        this.status = 200;
        this.msg = "성공";
        this.body = body;

    }

    public ApiUtil(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
        this.body = null;
    }



}
