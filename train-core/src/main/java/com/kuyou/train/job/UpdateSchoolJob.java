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
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * UpdateSchoolJob：更新学校数据
 *
 * @author taokai3
 * @date 2018/10/31
 */
@Slf4j
@Component("updateSchoolJob")
public class UpdateSchoolJob {

    private static final String SCHOOL_NAME_URL = "https://kyfw.12306.cn/otn/userCommon/schoolNames";

    @Resource
    protected JedisClient dataJedisClient;

    @MDCLog
    @Scheduled(cron = "0 0 22 * * ?")
    public void updateSchool() {
        try {
            String queryResult = CrawlerUtils.httpPost(SCHOOL_NAME_URL, null, "provinceCode=11&_json_att=", 6000);
            JSONObject resultJson = JSONObject.parseObject(queryResult);
            JSONArray dataArray = resultJson.getJSONArray("data");

            Map<String, String> schoolName2Code = new HashMap<>(16);
            Map<String, String> schoolCode2Name = new HashMap<>(16);
            for (int i = 0; i < dataArray.size(); i++) {
                JSONObject dataJson = dataArray.getJSONObject(i);
                String name = dataJson.getString("chineseName");
                String code = dataJson.getString("stationTelecode");
                schoolName2Code.put(name, code);
                schoolCode2Name.put(code, name);
            }

            // 填充到redis中
            dataJedisClient.del(KeyConstant.SCHOOL_NAME2CODE);
            String hmset = dataJedisClient.hmset(KeyConstant.SCHOOL_NAME2CODE, schoolName2Code);
            log.info("学校数据更新结果name-code:{}", hmset);
            dataJedisClient.del(KeyConstant.SCHOOL_CODE2NAME);
            hmset = dataJedisClient.hmset(KeyConstant.SCHOOL_CODE2NAME, schoolCode2Name);
            log.info("学校数据更新结果code-name:{}", hmset);
        } catch (Exception e) {
            log.info("UpdateSchoolJob.updateSchool 异常:{}", e.getClass().getSimpleName(), e);
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        UpdateSchoolJob updateSchoolJob = new UpdateSchoolJob();
        updateSchoolJob.updateSchool();
    }
}
