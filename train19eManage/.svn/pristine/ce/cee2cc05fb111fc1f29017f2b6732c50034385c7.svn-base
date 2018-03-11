package com.l9e.transaction.service;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.l9e.transaction.vo.AccountVo;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:context/applicationContext.xml")
public class AccountServiceTest {

	
	@Resource
	AccountService accountService;
	@Resource
	RobTicketService robTicketService;
	
	@Test
	public void testQueryAccountList() {
		int count = accountService.queryAccountListCount(null);
		
		
		System.out.println("count:"+count);
	}

	@Test
	public void testQueryAccountListCount() {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("everyPagefrom", 0);
		params.put("pageSize", 6);
		
		List<Map<String, String>> account = accountService.queryAccountList(params);
		
		
		System.out.println("list:"+account.size());
		
	}

	@Test
	public void testQueryAccount() {
		
		
		
		Map<String, String> account =  accountService.queryAccount("2");
		
		
		System.out.println("accName:"+account.get("acc_name"));
		
		System.out.println("accName:"+account.get("acc_username"));
		
	}

	@Test
	public void testUpdateAccount() {
		
		
	}

	@Test
	public void testInsertAccount() {
		AccountVo account = new AccountVo();
		
		accountService.insertAccount(account);
		
		
	}
	@Test
	public void test01(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("order_id", "HC_ROB1612221643551849");
		map.put("order_optlog", "携程退票申请HTTP请求成功,系统等待携程退票通知");
		map.put("create_time", new Date());
		map.put("opter", "携程回调接口");
		robTicketService.insertJLHistory(map);
	}

}
