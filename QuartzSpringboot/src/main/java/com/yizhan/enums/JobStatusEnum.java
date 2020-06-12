package com.yizhan.enums;

import lombok.Getter;

@Getter
public enum JobStatusEnum {

    ENABLED(0,"未启动"),
    NEW(1,"启动"),
    PAUSE(2,"暂停"),
    RESUME(3,"恢复"),
    FINISH(4,"已完成"),
    ;

    private Integer code;
    private String message;

    JobStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
