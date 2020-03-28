package com.tj.practice.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口返回类对象
 * @author Administrator
 *
 */
@Data
@ApiModel
public class JsonResult implements Serializable {

	private static final long serialVersionUID = 8910443851351413447L;

	/**
	 * 请求结果状态：true-成功；false-失败；
	 */
	@ApiModelProperty("请求结果状态：true-成功；false-失败；")
	private Boolean result;

	/**
	 * 成功消息
	 */
	protected static String succMsg = "操作成功";

	/**
	 * 请求结果消息：操作成功或用户错误提示；
	 */
	@ApiModelProperty("请求结果消息：操作成功或用户错误提示")
	private String msg;

	/**
	 * 状态码；
	 */
	@ApiModelProperty("请求结果状态码：100-成功；(其它值)-失败；")
	private String code = "100";

	private static String errCode = "500" ;

	/**
	 * 请求结果返回值：Object类型；
	 */
	@ApiModelProperty("请求结果返回值：Object类型")
	private Object data;

	public JsonResult() {
	}

	public JsonResult(boolean result, String msg) {
		this.result = result;
		this.msg = msg;
	}

	public JsonResult(boolean result, String msg, Object data) {
		this.result = result;
		this.msg = msg;
		this.data = data;
	}

	public JsonResult(boolean result, String msg, String code) {
		this.result = result;
		this.msg = msg;
		this.code = code;
	}

	/**
	 * 请求成功
	 * @param obj obj
	 * @return
	 */
	public static JsonResult success(Object obj) {
		return new JsonResult(true,succMsg,obj);
	}

	/**
	 * 请求成功
	 * @param msg
	 * @return
	 */
	public static JsonResult success(String msg, Object data) {
		JsonResult res = new JsonResult(true,msg,data);
		return res;
	}

	/**
	 * 请求失败
	 * @param msg
	 * @return
	 */
	public static JsonResult error(String msg) {
		JsonResult res = new JsonResult(false,msg);
		res.setCode(errCode);
		return res;
	}

	/**
	 * 请求失败
	 * @param msg
	 * @return
	 */
	public static JsonResult error(String msg, String code) {
		return new JsonResult(false,msg,code);
	}

	/**
	 * 错误信息响应，适合BusinessException返回
	 * @param e
	 * @return
	 */
	public static JsonResult error(Throwable e) {
		JsonResult rst = new JsonResult(false,e.getMessage());
		rst.setCode(errCode);
		return rst ;
	}

	public static JsonResult error(String code, Throwable e) {
		return new JsonResult(false,e.getMessage(),code);
	}

	public Boolean getResult() {
		return result;
	}

	public void setResult(Boolean result) {
		this.result = result;
	}

	public static String getSuccMsg() {
		return succMsg;
	}

	public static void setSuccMsg(String succMsg) {
		JsonResult.succMsg = succMsg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public static String getErrCode() {
		return errCode;
	}

	public static void setErrCode(String errCode) {
		JsonResult.errCode = errCode;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}