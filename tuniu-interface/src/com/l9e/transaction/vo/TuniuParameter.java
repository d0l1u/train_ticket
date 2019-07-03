package com.l9e.transaction.vo;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l9e.transaction.exception.TuniuCommonException;
import com.l9e.transaction.service.TuniuCommonService;
import com.l9e.transaction.vo.model.Parameter;
import com.l9e.util.JacksonUtil;


/**
 * 途牛业务参数
 * 
 * @author licheng
 * 
 */
public class TuniuParameter implements Parameter {

	/**
	 * 参数json字符串
	 */
	private String data;
	
	/**
	 * 参数json对象
	 */
	private JsonNode root;
	/**
	 * jackson objectmapper
	 */
	private ObjectMapper mapper = new ObjectMapper();

	public TuniuParameter(String data) throws TuniuCommonException {
		super();
		this.data = data;
		if(this.data == null || "".equals(this.data))
			this.root = null;
		else {
			try {
				this.root = mapper.readTree(this.data);
			} catch (Exception e) {
				throw new TuniuCommonException(TuniuCommonService.RETURN_CODE_PARAM_ERROR);
			}
		}
	}
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	public String getString(String key) {
		if(root == null)
			return "";
		
		JsonNode node = root.path(key);
		if(node.isMissingNode())
			return "";
		if(!node.isTextual())
			return "";
		return node.textValue();
	}
	
	public Boolean getBoolean(String key) {
		if(root == null)
			return null;
		
		JsonNode node = root.path(key);
		if(node.isMissingNode())
			return null;
		if(!node.isBoolean())
			return null;
		
		return node.booleanValue();
	}
	
	public Integer getInt(String key) {
		if(root == null)
			return null;
		
		JsonNode node = root.path(key);
		if(node.isMissingNode())
			return null;
		if(!node.isInt())
			return null;
		
		return node.intValue();
	}
	
	public <T> List<T> getList(String key, Class<T> clazz) {
		if(root == null)
			return null;
		
		JsonNode node = root.path(key);
		if(node.isMissingNode() || !node.isArray())
			return null;
		
		try {
			return JacksonUtil.readList(node.toString(), clazz);
		} catch (IOException e) {
			return null;
		}
	}
	public JSONArray getJSONArray(String key){
		if(data==null){
			return null;
		}
		JSONObject obj = JSONObject.fromObject(data);
		if(obj!=null){
			Object jsonObject = obj.get(key);
			System.out.print(jsonObject);
			if(jsonObject!=null&&!jsonObject.equals("null")){
				JSONArray jsonArray = obj.getJSONArray(key);
				return jsonArray;
			}else{
				return null;
			}
		}else{
			return null;
		}
		
	}
	
	
	public JSONObject getJSONObject(String key){
		if(data==null){
			return null;
		}
		JSONObject obj = JSONObject.fromObject(data);
		if(obj!=null){
			Object jsonObject = obj.get(key);
			System.out.print(jsonObject);
			if(jsonObject!=null&&!jsonObject.equals("null")){
				JSONObject jsonArray = obj.getJSONObject(key);
				return jsonArray;
			}else{
				return null;
			}
		}else{
			return null;
		}
		
	}
}
