package com.l9e.transaction.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.l9e.util.MemcachedUtil;

public class MyTest {

	@Before
	public void setUp() throws Exception {
	}


	@org.junit.Test
	public void test() {
		List<String> list = new ArrayList<String>();
		list.add("00");
		list.add("22");
		
		System.out.println(list.toString());
	}
	
	@Test
	public void test00(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "1");
		map.put("2", "1");
		map.put("3", "1");
		map.put("4", "1");
		MemcachedUtil.getInstance().setAttribute("hello1",map,0);
	}
	@SuppressWarnings("all")
	@Test
	public void test01(){
		Map<String, String> attribute = (Map<String, String>) MemcachedUtil.getInstance().getAttribute("hello1");
		System.out.println(attribute);
	}
	
	
	
	

}
