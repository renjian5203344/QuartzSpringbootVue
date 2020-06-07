package com.yizhan.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {

    PARAM_ERROR(1,"参数不正确"),





    ;
    private Integer code;
    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
