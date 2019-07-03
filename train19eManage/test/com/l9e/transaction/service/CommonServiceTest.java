package com.l9e.transaction.service;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.l9e.transaction.dao.RobTicketDao;
import com.l9e.transaction.dao.impl.RobTicketDaoImpl;
import com.l9e.transaction.vo.AreaVo;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:context/applicationContext.xml")
public class CommonServiceTest {

	@Resource
	private CommonService commonService;
	
	@Test
	public void testQuery() {
		
		List<AreaVo> list = commonService.getProvince();
		
		System.out.println("provinc:"+list.size());
		
		list = commonService.getCity("130000");
		ObjectMapper map = new ObjectMapper();
		
		try {
			
			System.out.println("city:"+map.writeValueAsString(list));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		list = commonService.getArea("130100");
		
		System.out.println("area:"+list.size());
	}
	@Resource
	private RobTicketDaoImpl robTicketDao;
	@Test
	public void test22(){
		String orderId = "HC_ROB1612191609091733";
		Map<String,Object> successMap = new HashMap<String,Object>();
		successMap.put("orderStatus", "88");//88支付成功
		successMap.put("optionTime", "now()");
		successMap.put("pay_time", "now()");
		successMap.put("channel", orderId.split("_")[0]);
		successMap.put("orderId", orderId);
		robTicketDao.getSqlMapClientTemplate().update("robTicket.updateJlOrderInfo", successMap);
	}

}
