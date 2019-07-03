package com.l9e.transaction.pubsub;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.l9e.transaction.service.RobTicketService;
import com.l9e.transaction.vo.RobTicketVo;
import com.l9e.transaction.vo.RobTicket_OI;
import com.l9e.util.JedisUtil;
import com.l9e.util.RobTicketUtils;

@SuppressWarnings("all")
@Component("redisSub")
public class InitEngine {
	private static final Logger logger = Logger.getLogger(InitEngine.class);
	private static final String STU_PROVICE = "stu_provice";
	private static final String STU_SCHOOL = "stu_school";
	private static final String STU_CITY = "stu_city";
	@PostConstruct
	public void excute(){
		// 学校,省份 简称
				Jedis jedis = null;
				try {
					jedis = JedisUtil.getJedis();
					Boolean b1 = jedis.exists(STU_PROVICE);
					Boolean b2 = jedis.exists(STU_SCHOOL);
					Boolean b3 = jedis.exists(STU_CITY);
					if (!b1) {
						InputStream province = this.getClass().getClassLoader().getResourceAsStream("province.json");
						String provinceJSON = RobTicketUtils.inputStream2String(province);
						List<HashMap> provs = JSON.parseArray(provinceJSON, HashMap.class);
						for (HashMap hashMap : provs) {
							jedis.hset(STU_PROVICE, String.valueOf(hashMap.get("name")), String.valueOf(hashMap.get("code")));
						}
						
					}
					if (!b2) {
						InputStream school = RobTicketUtils.class.getClassLoader().getResourceAsStream("school.json");
						String schoolJSON = RobTicketUtils.inputStream2String(school);
						List<HashMap> schools = JSON.parseArray(schoolJSON, HashMap.class);
						for (HashMap hashMap : schools) {
							jedis.hset(STU_SCHOOL, String.valueOf(hashMap.get("name")), String.valueOf(hashMap.get("code")));
						}
						
					}
					if (!b3) {
						InputStream city = RobTicketUtils.class.getClassLoader().getResourceAsStream("city.json");
						String cityJSON = RobTicketUtils.inputStream2String(city);
						List<HashMap> cities = JSON.parseArray(cityJSON, HashMap.class);
						for (HashMap hashMap : cities) {
							jedis.hset(STU_CITY, String.valueOf(hashMap.get("name")), String.valueOf(hashMap.get("code")));
						}
					}
				} catch (Exception e) {
					logger.error("初始化 JSON 数据失败");
				} finally {
					jedis.close();
				}
	}
	
	

}
