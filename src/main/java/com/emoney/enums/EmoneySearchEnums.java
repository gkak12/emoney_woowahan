package com.emoney.enums;

import lombok.Getter;

@Getter
public enum EmoneySearchEnums {

    AMOUNT("amount", "초기에 받은 적립금입니다."),
    EXPIRATION_DATE("expirationDate", "적립금 만료일입니다."),
    CREATION_DATE("creationDate", "적립금 발급일입니다."),
    IS_EXPIRED("isExpired", "적립금 만료 여부입니다.");

    private final String val;
    private final String desc;

    EmoneySearchEnums(String val, String desc) {
        this.val = val;
        this.desc = desc;
    }
}
