package com.l9e.transaction.service;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.l9e.transaction.vo.AcquireVo;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:context/applicationContext.xml")
public class AcquireServiceTest {

	@Resource
	AcquireService acquireService;
	
	@Test
	public void testUpdateAcquire() {
		AcquireVo acquire = new AcquireVo();
		
		acquire.setOrder_id("HC1305301927191012");
		acquire.setOrder_status("88");
		
		//acquireService.updateAcquire(acquire);
	}

}
