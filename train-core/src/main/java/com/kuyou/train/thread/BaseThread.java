package com.kuyou.train.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kuyou.train.common.constant.KeyConstant;
import com.kuyou.train.common.jedis.JedisClient;
import com.kuyou.train.common.util.DateUtil;
import com.kuyou.train.common.util.HttpUtil;
import com.kuyou.train.common.util.MD5Util;
import com.kuyou.train.common.util.SpringContextUtil;
import com.kuyou.train.entity.dto.TrainTimeDto;
import com.kuyou.train.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.Date;

/**
 * BaseThread
 *
 * @author taokai3
 * @date 2018/11/2
 */
@Slf4j
public abstract class BaseThread<T> implements Runnable {

    private static final int TRAIN_TIME_EXPIRE = 60 * 60 * 24 * 7;

    protected ChangeService changeService = SpringContextUtil.getBean(ChangeService.class);
    protected ChangeCpService changeCpService = SpringContextUtil.getBean(ChangeCpService.class);
    protected OrderCpService orderCpService = SpringContextUtil.getBean(OrderCpService.class);
    protected WorkerService workerService = SpringContextUtil.getBean(WorkerService.class);
    protected HistoryService historyService = SpringContextUtil.getBean(HistoryService.class);
    protected RefundService refundService = SpringContextUtil.getBean(RefundService.class);
    protected AccountService accountService = SpringContextUtil.getBean(AccountService.class);
    protected HthyService hthyService = SpringContextUtil.getBean(HthyService.class);
    protected NotifyService notifyService = SpringContextUtil.getBean(NotifyService.class);
    protected OrderService orderService = SpringContextUtil.getBean(OrderService.class);
    protected SettingService settingService = SpringContextUtil.getBean(SettingService.class);
    protected TrainOptlogService trainOptlogService = SpringContextUtil.getBean(TrainOptlogService.class);
    protected JedisClient dataJedisClient = SpringContextUtil.getBean("dataJedisClient", JedisClient.class);
    protected JedisClient orderJedisClient = SpringContextUtil.getBean("orderJedisClient", JedisClient.class);


    @Override
    public void run() {
        try {
            //设置mdc日志
            String logid = String.valueOf(System.nanoTime());
            logid = logid.substring(logid.length() - 6);
            MDC.put("LOGID", logid);

            //执行方法
            execute();
        } catch (Exception e) {
            log.info("Run方法异常", e);
        } finally {
            MDC.clear();
        }
    }

    /**
     * 线程执行方法
     */
    public abstract void execute();

    /**
     * 获取具体的出发时间和到达时间
     *
     * @param trainCode
     * @param fromDate
     * @param fromTime
     * @param toTime
     * @param fromStationName
     * @param toStationName
     * @return
     */
    protected TrainTimeDto trainTime(String trainCode, Date fromDate, Date fromTime, Date toTime,
            String fromStationName, String toStationName) {
        String key = KeyConstant.getKey(KeyConstant.TRAIN_TIME, trainCode, fromStationName, toStationName);
        if (fromTime != null && toTime != null) {
            TrainTimeDto trainTimeDto = new TrainTimeDto(fromTime, toTime);

            //计算出发时间，和历时
            trainTimeDto.mathRunTime(fromTime, toTime);

            //缓存至Redis，并设置超时时间
            try {
                boolean setResult = dataJedisClient.set(key, trainTimeDto.toString());
                log.info("设置列车时间key:{}, value:{}, Result:{}", key, trainTimeDto, setResult);
                dataJedisClient.expire(key, TRAIN_TIME_EXPIRE);
            } catch (Exception e) {
                log.info("设置列车时间key:{}, value:{}异常", key, trainTimeDto, e);
            }
            return trainTimeDto;
        }

        String trainTimeResult = dataJedisClient.get(key);
        log.info("根据{}查询列车时间结果:{}", key, trainTimeResult);
        TrainTimeDto trainTimeDto = null;
        if (StringUtils.isNotBlank(trainTimeResult)) {
            //反序列化获取时间
            trainTimeDto = JSON.parseObject(trainTimeResult, TrainTimeDto.class);
        } else {
            log.info("查询携程获取具体时间");
            // 从携程获取车次信息
            String timeStamp = Long.toString(System.currentTimeMillis() / 1000);
            // 从携程获取车次信息
            StringBuffer sb = new StringBuffer();
            sb.append("From=").append(fromStationName).append("&To=").append(toStationName).append("&DepartDate=")
                    .append(DateUtil.format(fromDate)).append("&TrainNo=").append(trainCode).append("&User=19e")
                    .append("&timeStamp=").append(timeStamp).append("&Sign=")
                    .append(MD5Util.md5(timeStamp + "7a692b08bb10a9c0681cc54697e8447d", "utf-8"));
            String result = new HttpUtil()
                    .doHttpPost("http://m.ctrip.com/restapi/soa2/12976/json/SearchS2S", sb.toString(), 3000, false);
            if (StringUtils.isNotBlank(result)) {
                JSONObject resultJson = JSON.parseObject(result);
                JSONArray trainArray = resultJson.getJSONArray("Trains");
                JSONObject trainJson = null;
                if (trainArray != null && trainArray.size() != 0) {
                    trainJson = trainArray.getJSONObject(0);
                    if (trainJson != null) {
                        trainTimeDto = new TrainTimeDto();
                        String startTimeStr = trainJson.getString("StartTime");
                        Integer diffTime = trainJson.getInteger("DurationMinutes");
                        log.info("Ctrip响应{}的出发时间:{}, 历时:{}分钟", key, startTimeStr, diffTime);
                        trainTimeDto.setRunTime(diffTime);
                        trainTimeDto.setFromTimeStr(startTimeStr);

                        //缓存时间
                        try {
                            dataJedisClient.set(key, trainTimeDto.toString());
                            dataJedisClient.expire(key, TRAIN_TIME_EXPIRE);
                        } catch (Exception e) {
                            log.info("设置列车时间key:{}, value:{}异常", key, trainTimeDto, e);
                        }
                    }
                }
            }
        }
        if (trainTimeDto == null) {
            log.info("获取列车时间为空,trainCode:{}, fromStation:{}, toStation:{}", trainCode, fromStationName, toStationName);
            throw new RuntimeException("获取列车时间为空");
        }

        //计算出发时间，到达时间
        trainTimeDto.mathTime(fromDate);

        return trainTimeDto;
    }


    protected void bookLog(String orderId, String status, String message) {
        historyService.insertBookLog(orderId, message);
        orderService.updateStatus(orderId, status);
    }

    protected void changeIdLog(Integer changeId, String orderId, String status, String message) {
        historyService.insertChangeLog(orderId, changeId, message);
        changeService.updateStatusById(changeId, status);
    }
}
