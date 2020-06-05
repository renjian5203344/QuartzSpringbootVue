package com.yizhan.enums;

import lombok.Getter;

@Getter
public enum JobStatusEnum {

    NEW(0,"创建"),
    PAUSE(1,"暂停"),
    RESUME(2,"重启"),
    ;

    private Integer code;
    private String message;

    JobStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
