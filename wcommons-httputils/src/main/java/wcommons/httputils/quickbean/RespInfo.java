package wcommons.httputils.quickbean;

import java.io.Serializable;

import wcommons.lang.JsonUtils;

/**
 * @author Wayne.Wang<5waynewang@gmail.com>
 * @since 11:18:42 AM Nov 25, 2013
 */
public class RespInfo<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private int code; // 消息码 200表示成功

	private String message; // 消息内容

	private T result;

	// 默认的 成功响应信息
	private static final RespInfo<Object> SUCCESS_RESP_INFO = new RespInfo<Object>();

	public static final int SUCCESS_CODE = 200;

	public RespInfo() {
	}

	public RespInfo(int code) {
		this(code, null, null);
	}

	public RespInfo(T result) {
		this(SUCCESS_CODE, null, result);
	}

	public RespInfo(int code, String message) {
		this(code, message, null);
	}

	public RespInfo(int code, T result) {
		this(code, null, result);
	}

	public RespInfo(int code, String message, T result) {
		this.code = code;
		this.message = message;
		this.result = result;
	}

	public int getCode() {
		return code;
	}

	public RespInfo<T> setCode(int code) {
		this.code = code;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public RespInfo<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public T getResult() {
		return result;
	}

	public RespInfo<T> setResult(T result) {
		this.result = result;
		return this;
	}

	public boolean isSuccess() {
		return this.code == SUCCESS_CODE;
	}

	@Override
	public String toString() {
		return toJSONString();
	}

	public String toJSONString() {
		return JsonUtils.toJSONString(this);
	}

	/**
	 * 获取 默认的成功响应信息 JSON字符串
	 * 
	 * @return
	 */
	public static String getSuccessRespInfoJSONString() {
		return JsonUtils.toJSONString(SUCCESS_RESP_INFO);
	}
}
