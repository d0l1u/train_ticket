package com.kuyou.train.job;

import com.kuyou.train.common.constant.KeyConstant;
import com.kuyou.train.common.jedis.JedisClient;
import com.kuyou.train.common.log.MDCLog;
import com.kuyou.train.common.util.CrawlerUtils;
import com.kuyou.train.common.util.RegexUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 更新车站数据
 * 
 * @author taokai3
 *
 */
@Slf4j
@Component("updateStationJob")
public class UpdateStationJob {

    /**
     * 余票初始化页面
     */
    private static final String LEFTTICKET_INIT_URL = "https://kyfw.12306.cn/otn/leftTicket/init";
    private static final Pattern STATION_VERSION_PATTERN = Pattern.compile("station_name.js.station_version=([^\"]+)");

    /**
     * 车站数据请求路径
     */
    private static final String STATION_URL = "https://kyfw.12306.cn/otn/resources/js/framework/station_name.js?station_version=%s";
    private static final Pattern STATION_DATA_PATTERN = Pattern.compile("'([^']+)");

    @Resource
    protected JedisClient dataJedisClient;

    @MDCLog
    @Scheduled(cron = "0 0 22 * * ?")
    public void updateStation() {
        try {
            // 获取12306车站数据
            String initResponse = CrawlerUtils.httpGet(LEFTTICKET_INIT_URL, null, 6000);
            String version = RegexUtil.matcher(initResponse, STATION_VERSION_PATTERN);
            log.info("车站版本号:{}", version);

            // 解析车站数据
            String stationResponse = CrawlerUtils.httpGet(String.format(STATION_URL, version), null, 6000);

            String stationStr = RegexUtil.matcher(stationResponse, STATION_DATA_PATTERN);
            String[] stationArr = stationStr.split("@");
            Map<String, String> stationName2Code = new HashMap<>(16);
            Map<String, String> stationCode2Name = new HashMap<>(16);
            for (String station : stationArr) {
                if (StringUtils.isBlank(station)) {
                    continue;
                }
                // bjb|北京北|VAP|beijingbei|bjb|0
                String[] arr = station.split("\\|");
                String name = arr[1];
                String code = arr[2];
                stationName2Code.put(name, code);
                stationCode2Name.put(code, name);
            }
            //log.info("stationNameMapCode:{}", stationName2Code);
            //log.info("stationCodeMapName:{}", stationCode2Name);

            // 填充到redis中
            dataJedisClient.del(KeyConstant.STATION_NAME2CODE);
            String hmset = dataJedisClient.hmset(KeyConstant.STATION_NAME2CODE, stationName2Code);
            log.info("车站数据更新结果name-code:{}", hmset);
            dataJedisClient.del(KeyConstant.STATION_CODE2NAME);
            hmset = dataJedisClient.hmset(KeyConstant.STATION_CODE2NAME, stationCode2Name);
            log.info("车站数据更新结果code-name:{}", hmset);
        } catch (Exception e) {
            log.info("UpdateStationJob.updateStation 异常:{}", e.getClass().getSimpleName(), e);
        }
    }

    public static void main(String[] args) {
        UpdateStationJob updateStationJob = new UpdateStationJob();
        updateStationJob.updateStation();
    }
}
