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
	public void testJson() {
		
		//String reqResult ="{\"ErrorCode\":0,\"ErrorInfo\":[\"{\"retInfo\":\"输入证件类型错误，请重新输入\",\"orderId\":\"\",\"retValue\":\"failure\",\"outTicketBillno\":\"\"}\"]}"; 

		//reqResult = reqResult.replace("[\"{", "[{");
		//reqResult = reqResult.replace("}\"]", "}]");
		//reqResult = reqResult.replace("\\", "");
		
		//System.out.println(reqResult);
		
		//"ErrorCode\":1,\"MyOrderNO":HC1305271605291001,\"SELFOrderNO\":E449071290,"ErrorInfo\":{}: 
		
		/*ObjectMapper map = new ObjectMapper();
		ReturnVO retObj=null;
		try {
			retObj = map.readValue(reqResult, ReturnVO.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		//String str = "";
		
		System.out.println("无".contains("无座"));
		
		
		
		
		//System.out.println(retObj.getErrorInfo().size());
	   
	}
	
}
