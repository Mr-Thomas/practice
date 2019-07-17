package com.tj.practice.common.exception;

/**
 * 业务异常
 * @author Administrator
 */
public class BusinessException extends RuntimeException{

	private static final long serialVersionUID = -5752463592725337937L;

	public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String errorMsg) {
        super(errorMsg);
    }

    public BusinessException(String errorMsg,Throwable cause) {
        super( errorMsg,cause);
    }
}