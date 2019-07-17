package com.tj.practice.common.exception;

/**
 * 用户没权限
 * @author Administrator
 */
public class NotPermitException extends RuntimeException{

    private static final long serialVersionUID = 8566572900818858358L;

    public NotPermitException(Throwable cause) {
        super(cause);
    }

    public NotPermitException(String errorMsg) {
        super(errorMsg);
    }

    public NotPermitException(String errorMsg,Throwable cause) {
        super( errorMsg,cause);
    }
}