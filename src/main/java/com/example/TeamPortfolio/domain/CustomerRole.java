package com.example.TeamPortfolio.domain;

import lombok.Getter;

//enum:클래스처럼 보이게 하는 상수, 서로 관련 있는 상수들끼리 모아 상수들을 대표하는 이름으로 타입을 정의하는것
@Getter
public enum CustomerRole {
    //USER:사용자 AEMIN:관리자 (대문자로 선언하고, 쉼표로 구분하고 세미콜론 사용하지않음)
    //인덱스 있음. USER(0), ADMIN(1)
    USER("ROLE_ADMIN"),
    ADMIN("ROLE_USER");

    CustomerRole(String value){
        this.value=value;
    }
    private String value;
}

