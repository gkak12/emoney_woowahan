package com.emoney.comm.enums;

import lombok.Getter;

@Getter
public enum EmoneyEnums {

    SAVE(1L, "적립금 적립"),
    USAGE(2L, "적립금 사용"),
    DEDUCTION(3L, "적립금 차감"),
    EXPIRATION(4L, "적립금 만료");

    private final Long val;
    private final String desc;

    EmoneyEnums(Long val, String desc){
        this.val = val;
        this.desc = desc;
    }
}
