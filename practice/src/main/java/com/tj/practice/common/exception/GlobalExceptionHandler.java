package com.tj.practice.common.exception;

import com.tj.practice.common.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *       全局异常
 * @author Administrator
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public JsonResult defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
		log.error("==========未知异常:", e);
		return JsonResult.error("未知错误，请联系管理员", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}

	@ExceptionHandler(value = BusinessException.class)
	public JsonResult businessExceptionHandler(HttpServletRequest request, HttpServletResponse response,
                                               BusinessException e) {
		log.error("==========业务异常:", e);
		return JsonResult.error(e);
	}

	@ExceptionHandler(value = NotPermitException.class)
	@ResponseBody
	public JsonResult notPermitExceptionHandler(HttpServletRequest request, HttpServletResponse response,
                                                NotPermitException e) {
		log.error("==========权限不足，访问禁止:", e);
		return JsonResult.error(String.valueOf(HttpStatus.FORBIDDEN.value()), e);
	}
}