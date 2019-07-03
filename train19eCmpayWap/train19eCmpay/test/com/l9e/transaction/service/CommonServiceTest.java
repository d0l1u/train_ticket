package com.l9e.transaction.service;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:context/applicationContext.xml")
public class CommonServiceTest {

	@Resource
	private CommonService commonService;
	
	@Test
	public void testQuery() {
		
		String str = commonService.query();
		
		System.out.println(str);
	}

}
