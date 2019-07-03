package com.unlun.timeout.request;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.l9e.train.po.ReturnVO;

public class TestPO {

	
	@Test
	public void testJson() throws Exception {
		
		String str ="{\"ErrorCode\":1,\"ErrorInfo\":[\"success|HC1305271605291001|E449071290\"]}";
		
		
		
		if(!str.contains("\"ErrorCode\":0")){
			System.out.println("ok");
		}else{
			System.out.println("no");
		}
	   
	}
	
}
