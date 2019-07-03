package com.l9e.transaction.service;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.l9e.transaction.vo.NoticeVo;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:context/applicationContext.xml")
public class NoticeServiceTest {

	
	@Resource
	NoticeService noticeService;
	
	@Test
	public void testQueryNoticeList() {
		
		NoticeVo notice = new NoticeVo();
		notice.setNotice_name("test");
		notice.setNotice_content("test");
		
		noticeService.insertNotice(notice);
		
		notice = new NoticeVo();
		notice.setNotice_name("test2");
		notice.setNotice_content("test");
		noticeService.insertNotice(notice);
		
		
		int count = noticeService.queryNoticeListCount(null);
		
		System.out.println("count:"+count);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("everyPagefrom", 0);
		params.put("pageSize", 6);
		
		List<Map<String, Object>> notices = noticeService.queryNoticeList(params);
		
		System.out.println("notices:"+notices.size());
		
		for (Map<String, Object> map : notices) {
			Map<String, Object> notice1 =  noticeService.queryNotice(String.valueOf(map.get("notice_id")));
			
			System.out.println(notice1.get("notice_name"));
			
			notice = new NoticeVo();
			notice.setNotice_id(String.valueOf(notice1.get("notice_id")));
			notice.setNotice_name("test3333");
			
			noticeService.updateNotice(notice);
			
			
		}
		
	}

}
