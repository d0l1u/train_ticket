package com.l9e.transaction.vo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * 同程响应
 * 
 * @author licheng
 * 
 */
public class TCReturnVO {

	/* 通用输出部分 */
	private Boolean success = true;
	private Integer code = 100;
	private String msg = "处理或操作成功";

	/* 业务输出参数 */
	private Map<String, Object> parameter = new HashMap<String, Object>();

	/**
	 * 日志
	 */
	private TongchengChangeLogVO log = new TongchengChangeLogVO();

	/**
	 * 添加业务输出参数
	 * 
	 * @param key
	 * @param value
	 */
	public void putParameter(String key, Object value) {
		this.parameter.put(key, value);
	}

	/**
	 * 移除业务输出参数并返回值
	 * 
	 * @param key
	 * @return
	 */
	public Object removeParameter(String key) {
		return this.parameter.remove(key);
	}

	/**
	 * 同程响应json串
	 * 
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public String toReturnString() throws JsonGenerationException,
			JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		// 通用
		this.parameter.put("success", this.success);
		this.parameter.put("code", this.code);
		this.parameter.put("msg", this.msg);

		return mapper.writeValueAsString(this.parameter);
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public TongchengChangeLogVO getLog() {
		return log;
	}

	public void setLog(TongchengChangeLogVO log) {
		this.log = log;
	}

}
