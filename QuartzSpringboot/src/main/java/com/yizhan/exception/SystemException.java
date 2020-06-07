package com.yizhan.exception;

import com.yizhan.enums.ResultEnum;

public class SystemException extends RuntimeException{

    private Integer code;

    public SystemException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public SystemException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}

