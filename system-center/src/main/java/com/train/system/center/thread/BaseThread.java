package com.train.system.center.thread;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import com.train.system.center.jedis.JedisClient;
import com.train.system.center.util.HttpUtil;
import com.train.system.center.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public abstract class BaseThread {

	private Logger logger = LoggerFactory.getLogger(BaseThread.class);

	protected String logid;

	@Resource
	JedisClient privateLockJedisClient;

	public JSONObject appendTrainTime(Date departureDate, Date departureTime, Date arrivalTime, String trainCode,
			String fromStationName, String toStationName) {
		JSONObject responseJson = new JSONObject();
		responseJson.put("fromTime", departureTime);
		responseJson.put("toTime", arrivalTime);
		String key = trainCode + "_" + fromStationName + "_" + toStationName;
		try {
			if (departureTime == null || arrivalTime == null) {
				// 从redis中获取时间
				String fromDateStr = new SimpleDateFormat("yyyy-MM-dd").format(departureDate);

				String trainStr = privateLockJedisClient.get(key);
				if (StringUtils.isNotBlank(trainStr)) {
					JSONObject trainJson = JSON.parseObject(trainStr);
					String departureTimeStr = trainJson.getString("departureTimeStr");
					Integer integer = trainJson.getInteger("diffTime");
					logger.info("{}Redis中缓存{}的出发时间:{}, 历时:{}分钟", logid, key, departureTimeStr, integer);
					departureTime = new SimpleDateFormat("yyyy-MM-dd HH:mm")
							.parse(fromDateStr + " " + departureTimeStr);
					arrivalTime = new Date(departureTime.getTime() + integer * 60 * 1000);

					responseJson.put("fromTime", departureTime);
					responseJson.put("toTime", arrivalTime);
					return responseJson;
				}

				// 从携程获取车次信息
				String timeStamp = Long.toString(System.currentTimeMillis() / 1000);
				// 从携程获取车次信息
				StringBuffer sb = new StringBuffer();
				sb.append("From=").append(fromStationName)//
						.append("&To=").append(toStationName)//
						.append("&DepartDate=").append(fromDateStr)//
						.append("&TrainNo=").append(trainCode)//
						.append("&User=19e")//
						.append("&timeStamp=").append(timeStamp)//
						.append("&Sign=").append(MD5Util.md5(timeStamp + "7a692b08bb10a9c0681cc54697e8447d", "utf-8"));
				String result = new HttpUtil().doHttpPost("http://m.ctrip.com/restapi/soa2/12976/json/SearchS2S",
						sb.toString(), 3000, false);
				if (StringUtils.isNotBlank(result)) {
					JSONObject resultJson = JSON.parseObject(result);
					JSONArray trainArray = resultJson.getJSONArray("Trains");
					JSONObject trainJson = null;
					if (trainArray != null && trainArray.size() != 0) {
						trainJson = trainArray.getJSONObject(0);
					}
					if (trainJson != null) {
						String startTimeStr = trainJson.getString("StartTime");
						Integer diffTime = trainJson.getInteger("DurationMinutes");
						logger.info("{}Ctrip响应{}的出发时间:{}, 历时:{}分钟", logid, key, startTimeStr, diffTime);

						departureTime = new SimpleDateFormat("yyyy-MM-dd HH:mm")
								.parse(fromDateStr + " " + startTimeStr);
						arrivalTime = new Date(departureTime.getTime() + diffTime * 60 * 1000);
						responseJson.put("fromTime", departureTime);
						responseJson.put("toTime", arrivalTime);

						JSONObject json = new JSONObject(true);
						json.put("departureTimeStr", startTimeStr);
						json.put("diffTime", diffTime);
						privateLockJedisClient.setex(key, 60 * 60 * 24 * 7, json.toJSONString());
						return responseJson;
					}
				}
			} else {
				logger.info("{}不需要补填出发时间-到达时间", logid);
				// 计算出发时间和到达时间，进行redis缓存
				String departureTimeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(departureTime).split(" ")[1];
				// String arrivalTimeStr = new SimpleDateFormat("yyyy-MM-dd
				// HH:mm").format(arrivalTime).split(" ")[1];
				Integer diffTime = (int) ((arrivalTime.getTime() - departureTime.getTime()) / 1000 / 60);

				JSONObject json = new JSONObject(true);
				json.put("departureTimeStr", departureTimeStr);
				// json.put("arrivalTimeStr", arrivalTimeStr);
				json.put("diffTime", diffTime);
				privateLockJedisClient.setex(key, 60 * 60 * 24 * 7, json.toJSONString());
			}
		} catch (Exception e) {
			logger.info("{}补填出发时间-到达时间异常:{}", logid, e.getClass().getSimpleName(), e);
		}
		return responseJson;
	}

	String lock(JedisClient jedisClient, String key, String value, int timeout) {
		String lockResult = "";
		try {
			lockResult = jedisClient.set(key, value, "NX", "EX", timeout);
		} catch (Exception e) {
			logger.error("{}Jedis Lock Exception", logid, e);
		}
		logger.info("{}加锁{}:{}结果:{}", logid, key, value, lockResult);
		return lockResult;
	}

	Object release(JedisClient jedisClient, String key, String value) {
		Object returnValue = null;
		try {
			returnValue = jedisClient.eval(
					"if redis.call(\"get\",KEYS[1]) == ARGV[1] then return redis.call(\"del\",KEYS[1]) else return 0 end",
					1, key, value);
		} catch (Exception e) {
			logger.error("{}Jedis Release Lock Exception", logid, e);
		}
		logger.info("{}释放分布式锁{}:{}结果:{}", logid, key, value, returnValue);
		return returnValue;
	}
}
