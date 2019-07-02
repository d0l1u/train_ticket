package com.kuyou.train.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kuyou.train.common.constant.KeyConstant;
import com.kuyou.train.common.jedis.JedisClient;
import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.common.util.CrawlerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 更新城市数据
 *
 * @author taokai3
 */
@Slf4j
@Component("updateCityJob")
public class UpdateCityJob {

    private static final String CITY_URL = "https://kyfw.12306.cn/otn/userCommon/allCitys";

    @Resource
    protected JedisClient dataJedisClient;

    @MDCLog
    @Scheduled(cron = "0 0 22 * * ?")
    public void updateCity() {
        try {
            String queryResult = CrawlerUtils.httpPost(CITY_URL, null, "station_name=&_json_att=", 6000);
            JSONObject resultJson = JSONObject.parseObject(queryResult);
            JSONArray dataArray = resultJson.getJSONArray("data");

            Map<String, String> cityName2Code = new HashMap<>(16);
            Map<String, String> cityCode2Name = new HashMap<>(16);
            for (int i = 0; i < dataArray.size(); i++) {
                JSONObject dataJson = dataArray.getJSONObject(i);
                String name = dataJson.getString("chineseName");
                String code = dataJson.getString("stationTelecode");
                cityName2Code.put(name, code);
                cityCode2Name.put(code, name);
            }

            // 填充到redis中
            dataJedisClient.del(KeyConstant.CITY_NAME2CODE);
            String hmset = dataJedisClient.hmset(KeyConstant.CITY_NAME2CODE, cityName2Code);
            log.info("城市数据更新结果name-code:{}", hmset);
            dataJedisClient.del(KeyConstant.CITY_CODE2NAME);
            hmset = dataJedisClient.hmset(KeyConstant.CITY_CODE2NAME, cityCode2Name);
            log.info("城市数据更新结果code-name:{}", hmset);
        } catch (Exception e) {
            log.info("UpdateCityJob.updateCity 异常:{}", e.getClass().getSimpleName(), e);
        }
    }

    public static void main(String[] args) {
        UpdateCityJob updateCityJob = new UpdateCityJob();
        updateCityJob.updateCity();
    }
}
