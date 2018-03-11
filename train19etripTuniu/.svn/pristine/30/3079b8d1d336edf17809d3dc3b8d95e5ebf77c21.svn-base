package com.l9e.transaction.vo;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.l9e.transaction.service.TuniuCommonService;

/**
 * 途牛同步输出
 * 
 * @author licheng
 * 
 */
public class SynchronousOutput {

	/**
	 * 操作成功与否
	 */
	private Boolean success = true;
	/**
	 * 结果码
	 */
	@JsonIgnore
	private String returnCode = TuniuCommonService.RETURN_CODE_SUCCESS;
	/**
	 * 异常错误信息
	 */
	private String errorMsg = "请求成功";
	/**
	 * 返回结果
	 */
	private Object data;
	
	/*输出参数*/
	/**
	 * 实际返回结果码
	 */
	@JsonProperty("returnCode")
	private Integer _returnCode;

	public SynchronousOutput() {
		super();
	}

	public SynchronousOutput(Boolean success, String returnCode, String errorMsg) {
		super();
		this.success = success;
		this.returnCode = returnCode;
		this.errorMsg = errorMsg;
	}

	public SynchronousOutput(Boolean success, String returnCode,
			String errorMsg, Object data) {
		super();
		this.success = success;
		this.returnCode = returnCode;
		this.errorMsg = errorMsg;
		this.data = data;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public Integer get_returnCode() {
		if(returnCode == null) 
			_returnCode = null;
		else {
			if(returnCode.equals("") || !returnCode.matches("\\d+")) {
				returnCode = TuniuCommonService.RETURN_CODE_UNKNOWN_ERROR;
				errorMsg = "未知异常";
			}
		}
		try {
			_returnCode = Integer.valueOf(returnCode);
		} catch (NumberFormatException e) {

		}
		return _returnCode;
	}

	public void set_returnCode(Integer returnCode) {
		_returnCode = returnCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
