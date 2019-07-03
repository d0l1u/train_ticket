package com.l9e.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.l9e.common.BaseController;
import com.l9e.common.TrainConsts;
import com.l9e.entity.*;
import com.l9e.util.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;

/**
 * @author meizs 携程数据接口
 */
@Controller
public class CtripDataInerface extends BaseController {

    //private static final Logger logger = Logger.getLogger(CtripDataInerface.class);//log4j
    private static final Logger logger = LoggerFactory.getLogger(CtripDataInerface.class);//logback

    @Value("${ctrip.SearchS2SUrl}")
    private String SearchS2SUrl;

    @Value("${ctrip.StopStationUrl}")
    private String StopStationUrl;

    @Value("${ctrip.signKey}")
    private String signKey;

    @Value("${ctrip.user}")
    private String user;

    public static void main(String[] args) {

    }

    @RequestMapping("/ctripDataInterface.do")
    public void ctripDataInterfaceMain(HttpServletRequest request, HttpServletResponse response) {
        logger.info("请求参数列表：" + getFullURL(request));
        String type = this.getParam(request, "type");
        logger.info("请求数据接口的类型type:" + type);
        try {
            if ("yupiao".equals(type)) {
                queryLeftTicket(request, response);// 查询余票
            } else if ("stopStation".equals(type)) {
                querySubwayStation(request, response);//查询经停站
            } else {
                logger.info("hcp对外接口-非法的type接口参数 ：type=" + type);
                logger.info("请求参数异常type:" + type);
                printJson(response, TrainConsts.ERROR);
            }

        } catch (IOException e) {
            logger.info("queryLeftTicket:", e);
            printJson(response, TrainConsts.ERROR);
        }

    }

    /**
     * 查询途经站
     *
     * @param request
     * @param response
     */
    public void querySubwayStation(HttpServletRequest request, HttpServletResponse response) {

        logger.info("查询途经站信息接口-调用接口开始");
        String channel = this.getParam(request, "channel");
        String ymd = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
        //统计每个渠道，每天查询途径站的次数
        String countSinfoSortSet = "query_countSinfo_zset_" + ymd;
        double add_flag = RedisUtil.getInstance().sortSet().zincrby(countSinfoSortSet, 1, channel);
        //RedisUtil.getInstance().keys().expire(countSinfoSortSet, 60 * 60 * 24 * 10);//缓存10天
        logger.info(countSinfoSortSet + ":" + add_flag);

        String train_code = this.getParam(request, "train_code");
        String queryDate = "";
        String train_date = this.getParam(request, "train_date");

        if (StringUtils.isNotEmpty(train_date)) {
            queryDate = train_date;
        } else {
            queryDate = DateUtil.dateToString(DateUtil.dateAddDays(new Date(), 10), DateUtil.DATE_FMT1);//系统接口没有时间，这里提供一个日期用于查询途经站
        }

        logger.info("传入的参数：：" + channel + "," + train_code + "," + queryDate);
        Map<String, String> paramMap = new LinkedHashMap<String, String>();
        paramMap.put("DepartDate", queryDate);
        paramMap.put("TrainNo", train_code);
        paramMap.put("User", user);
        String TimeStamp = Long.toString(System.currentTimeMillis() / 1000);
        String md5Str = TimeStamp + signKey;
        String sign = Md5Encrypt.md5(md5Str, "utf-8");
        paramMap.put("TimeStamp", TimeStamp);
        paramMap.put("Sign", sign);
        String params = "";

        String key = "stopStation_" + queryDate + "_" + train_code;//途经站缓存key

        String redisStr = RedisUtil.getInstance().strings().get(key);

        if (StringUtils.isNotEmpty(redisStr)) {
            logger.info("缓存不为空读取：" + channel + "," + train_code + "," + queryDate + ",redisStr:" + redisStr);
            printJson(response, redisStr);
            return;
        }

        try {
            params = UrlFormatUtil.CreateUrl(paramMap, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        long a = System.currentTimeMillis();
        String result = HttpUtil.sendByPost(StopStationUrl, params, "300000", "300000", "utf-8");
        long b = System.currentTimeMillis();
        logger.info(channel + "," + train_code + "," + queryDate + ",耗时" + (b - a) / 1000f + "s,请求携程途经站数据：" + result);
        String dataJson = "";
        try {
            dataJson = ctripJsonTo19eSinfo(result, train_code);
        } catch (Exception e) {
            logger.info(channel + "," + train_code + "," + queryDate + ",查询异常", e);
            dataJson = TrainConsts.ERROR;
        }

        if (StringUtils.isEmpty(dataJson)) {
            printJson(response, TrainConsts.ERROR);
            return;
        }

        if (dataJson.contains("\"return_code\":\"000\"")) {
            a = System.currentTimeMillis();
            String status = RedisUtil.getInstance().strings().setEx(key, 10 * 1, dataJson); // 缓存10s
            b = System.currentTimeMillis();
            logger.info(channel + "," + train_code + "," + queryDate + ",redis缓存耗时" + (b - a) / 1000f + "秒"
                    + ",缓存结果:" + status);
        } else {
            logger.info(channel + "," + train_code + "," + queryDate + ",查询异常:" + dataJson);
        }

        printJson(response, dataJson);

        return;
    }

    /***
     * 携程途径站数据转换19e途径站格式
     * @param result
     */
    public String ctripJsonTo19eSinfo(String result, String train_code) {
        JSONObject resultObj = JSON.parseObject(result);
        String Ack = resultObj.getJSONObject("ResponseStatus").getString("Ack");
        String TimeStamp = resultObj.getJSONObject("ResponseStatus").getString("Timestamp");
        String Message = resultObj.getString("Message");
        JSONArray StopStations = resultObj.getJSONArray("StopStations");

        CtripResultData crd = new CtripResultData();

        crd.setResponseStatus(Ack);
        crd.setTimeStamp(TimeStamp);

        /**
         1 ,"Message":"invalid user:19e1"
         2 ,"Message":"invalid timeStamp:0"
         */
        if (StringUtils.contains(Message, "invalid")) {
            return TrainConsts.ERROR;//查询错误，未获取到途径站信息
        }

        if (StopStations.size() == 0) {
            return TrainConsts.NO_DATAS; // 没有查到车次，未获取到途径站信息
        }


        List<CtripStopStation> listCtripStation = new ArrayList<>();
        for (int i = 0; i < StopStations.size(); i++) {
            JSONObject station = StopStations.getJSONObject(i);
            CtripStopStation ctripStation = new CtripStopStation();
            ctripStation.setStationNo(station.getString("StationNo"));
            ctripStation.setStationName(station.getString("StationName"));
            ctripStation.setStartTime(station.getString("StartTime"));
            ctripStation.setArrivalTime(station.getString("ArrivalTime"));
            ctripStation.setStopMinutes(station.getString("StopMinutes"));
            listCtripStation.add(ctripStation);
        }

        Collections.sort(listCtripStation, new Comparator<CtripStopStation>() {//序号排序
            @Override
            public int compare(CtripStopStation o1, CtripStopStation o2) {

                int a = 0;
                int b = 0;
                try {
                    a = Integer.valueOf(o1.getStationNo());
                    b = Integer.valueOf(o2.getStationNo());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (a == b) {
                    return 0;
                } else if (a > b) {
                    return 1;
                } else if (a < b) {
                    return -1;
                }
                return 0;
            }
        });

        List<TrainStationVo> listStation = new ArrayList<>();
        String beforeTime = "";
        int day = 1;//1：当日、2：第2日、3：第3日、4：第4日  ,costday=day-1
        for (CtripStopStation cStation : listCtripStation) {
            TrainStationVo tvo = new TrainStationVo();
            tvo.setCheci(train_code);
            tvo.setStationno(cStation.getStationNo());
            tvo.setName(cStation.getStationName());
            tvo.setStarttime(cStation.getStartTime().equals("----") ? cStation.getArrivalTime() : cStation.getStartTime());
            tvo.setArrtime(cStation.getArrivalTime().equals("----") ? cStation.getStartTime() : cStation.getArrivalTime());
            tvo.setDistance("");//里程

            if (tvo.getArrtime().equals(tvo.getStarttime())) {
                tvo.setCosttime(DateUtil.getDayWord(day));
                tvo.setCostday(String.valueOf(day - 1));
            } else {
                try {
                    int beforeDay = DateUtil.compare_date(beforeTime, tvo.getArrtime());
                    if (beforeDay > 0) {
                        day = day + 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tvo.setCostday(String.valueOf(day - 1));
                tvo.setCosttime(DateUtil.getDayWord(day));
            }
            beforeTime = tvo.getArrtime();
            tvo.reSetInterval();//停车时间
            listStation.add(tvo);
        }

        JSONObject json = new JSONObject();
        json.put("return_code", "000");
        json.put("message", "");
        JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(listStation));
        json.put("train_stationinfo", jsonArray);

        return json.toJSONString();
    }

    /**
     * 查询余票
     *
     * @param request
     * @param response
     * @throws IOException
     */
    public void queryLeftTicket(HttpServletRequest request, HttpServletResponse response) throws IOException {

        logger.info("*********" + SearchS2SUrl + ",*********," + signKey);
        /** 分发参数 */
        String channel = request.getParameter("channel");
        String ymd = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
        String countLeftTicketSortSet = "query_countLeftTicket_zset_" + ymd;
        //统计每个渠道，每天查询的次数
        double add_flag = RedisUtil.getInstance().sortSet().zincrby(countLeftTicketSortSet, 1, channel);
      //  RedisUtil.getInstance().keys().expire(countLeftTicketSortSet, 60 * 60 * 24 * 10);//缓存10天
        logger.info(countLeftTicketSortSet + ":" + add_flag);

        /** 业务参数 */
        String from_station = request.getParameter("from_station");// 出发站
        String arrive_station = request.getParameter("arrive_station");// 到达站
        String travel_time = request.getParameter("travel_time");// 乘车日期,2017-10-26
        String purpose_codes = request.getParameter("purpose_codes");// 学生(0X00)、普通(ADULT)的余票查询
        String train_code = request.getParameter("train_code"); //车次，K1454
        train_code = StringUtils.isNotEmpty(train_code) ? train_code.trim() : "";

        // 查询余票
        String check_spare_num = request.getParameter("check_spare_num");
        String alter_refund = request.getParameter("alter_refund");
        StringBuffer log_req = new StringBuffer();
        // 日志
        log_req.append("channel[").append(channel).append("]").append("param:").append("travel_time[")
                .append(travel_time).append("],from_station[").append(URLDecoder.decode(from_station, "utf-8"))
                .append("],arrive_station[").append(URLDecoder.decode(arrive_station, "utf-8"))
                .append("],purpose_codes[").append(purpose_codes).append("]");
        logger.info(log_req.toString());

        Map<String, String> redisParam = new HashMap<String, String>();
        redisParam.put("from_station", from_station);
        redisParam.put("arrive_station", arrive_station);
        redisParam.put("travel_time", travel_time);
        redisParam.put("purpose_codes", purpose_codes);
        redisParam.put("train_code", train_code);
        redisParam.put("method", "DGTrain");

        String key = getFileName(redisParam);

        // 从redis获取缓存车次，缓存3分钟
        String redisStr = RedisUtil.getInstance().strings().get(key);
        if ("true".equals(check_spare_num)) {
            redisStr = null;
        }
        if ("true".equals(alter_refund)) {
            redisStr = null;
        }
        logger.info(from_station + arrive_station + travel_time + ",redisStr:" + redisStr);

        if ("null".equals(redisStr) || StringUtils.isEmpty(redisStr)) {
            logger.info(from_station + arrive_station + travel_time + ",没有缓存,查询携程数据");
            // 参数拼接
            Map<String, String> param = new HashMap<String, String>();
            param.put("From", from_station);
            param.put("To", arrive_station);
            param.put("DepartDate", travel_time);
            if (StringUtils.isNotEmpty(train_code)) {
                param.put("TrainNo", train_code);//查询单个车次
            }
            param.put("User", user);
            String TimeStamp = Long.toString(System.currentTimeMillis() / 1000);
            String md5Str = TimeStamp + signKey;
            String sign = Md5Encrypt.md5(md5Str, "utf-8");
            param.put("TimeStamp", TimeStamp);
            param.put("Sign", sign);

            logger.info(from_station + arrive_station + travel_time + ",生成参数map:" + param);
            String params = "";
            try {
                params = UrlFormatUtil.CreateUrl(param, "utf-8");
            } catch (Exception e) {
                logger.info("", e);
            }
            logger.info("post请求url:" + SearchS2SUrl + "参数：" + params);
            long a = System.currentTimeMillis();
            String result = HttpUtil.sendByPost(SearchS2SUrl, params, "30000", "30000", "utf-8");
            long b = System.currentTimeMillis();
            logger.info(from_station + arrive_station + travel_time + ",耗时" + (b - a) / 1000f + "s,请求携程余票数据：" + result);
            String dataJson = "";
            try {
                dataJson = dealCtripJsonTo19e(result);
            } catch (Exception e) {
                logger.info(from_station + arrive_station + travel_time + ",查询异常：" + dataJson, e);
                dataJson = TrainConsts.ERROR;
            }
            if (StringUtils.isEmpty(dataJson)) {
                printJson(response, TrainConsts.ERROR);
                return;
            }
            if (dataJson.contains("\"code\":\"000\"")) {
                a = System.currentTimeMillis();
                String status = RedisUtil.getInstance().strings().setEx(key, 60 * 1, dataJson); // 缓存3分钟
                b = System.currentTimeMillis();
                logger.info(from_station + arrive_station + travel_time + ",redis缓存耗时" + (b - a) / 1000f + "秒"
                        + ",缓存结果:" + status);
            } else {
                logger.info(from_station + arrive_station + travel_time + ",查询数据异常：" + dataJson);
            }
            printJson(response, dataJson);
        } else {
            logger.info(from_station + arrive_station + travel_time + ",有缓存,读取redis");
            printJson(response, redisStr);
        }

    }

    protected String getFileName(Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
        sb.append("javarobot_").append(map.get("from_station")).append("_").append(map.get("arrive_station"))
                .append("_").append(map.get("travel_time")).append(map.get("method")).append("train_code_").append(map.get("train_code"));
        return sb.toString();
    }

    /**
     * 将携程的车次信息转换成19e的先前的格式
     *
     * @param jsonStr
     * @return
     */
    public String dealCtripJsonTo19e(String jsonStr) {

        JSONObject resultObj = JSON.parseObject(jsonStr);
        String Ack = resultObj.getJSONObject("ResponseStatus").getString("Ack");
        String TicketLeftTime = resultObj.getString("TicketLeftTime");
        boolean IsRealTicket = resultObj.getBoolean("IsRealTicket");
        String TimeStamp = resultObj.getString("TimeStamp");
        String Message = resultObj.getString("Message");
        JSONArray Trains = resultObj.getJSONArray("Trains");

        CtripResultData crd = new CtripResultData();
        crd.setIsRealTicket(IsRealTicket);
        crd.setMessage(Message);
        crd.setResponseStatus(Ack);
        crd.setTimeStamp(TimeStamp);
        crd.setTicketLeftTime(TicketLeftTime);
        /**
         1 ,"Message":"龙猫 is not a station"
         2 ,"Message":"invalid user:19e1"
         3 ,"Message":"invalid timeStamp:0"
         */
        if (StringUtils.contains(Message, "invalid")) {
            return TrainConsts.ERROR;//查询错误，未获取到车次
        }
        if (StringUtils.contains(Message, "station")) {
            return TrainConsts.STATION_ERROR;//没有这个站点
        }

        if (Trains.size() == 0) {
            return TrainConsts.NO_DATAS; // 没有查到车次
        }

        List<CtripZhanZhanData> zzlist = new ArrayList<CtripZhanZhanData>();
        for (int i = 0; i < Trains.size(); i++) {

            JSONObject train = Trains.getJSONObject(i); // 车次
            CtripZhanZhanData czzd = new CtripZhanZhanData();
            czzd.setStartTime(train.getString("StartTime"));
            czzd.setArriveTime(train.getString("ArriveTime"));
            czzd.setFromStationName(train.getString("FromStationName"));
            czzd.setToStationName(train.getString("ToStationName"));
            czzd.setFromStationTypeName(train.getString("FromStationTypeName"));
            czzd.setToStationTypeName(train.getString("ToStationTypeName"));
            czzd.setTrainNo(train.getString("TrainNo"));
            czzd.setTrain12306No(train.getString("Train12306No"));
            czzd.setDurationMinutes(train.getInteger("DurationMinutes"));
            czzd.setDayDiff(train.getInteger("DayDiff"));
            czzd.setControlDay(train.getInteger("ControlDay"));
            czzd.setSaleTime(train.getString("SaleTime"));
            czzd.setToTelcode(train.getString("ToTelcode"));
            czzd.setFromTelcode(train.getString("FromTelcode"));
            czzd.setCanWebBuy(train.getString("CanWebBuy"));
            czzd.setIsSupportCard(train.getString("IsSupportCard"));

            JSONArray seatArr = train.getJSONArray("Seats");// 车次的坐席信息
            List<CtripSeatData> seatlist = new ArrayList<CtripSeatData>();
            for (int j = 0; j < seatArr.size(); j++) {
                CtripSeatData csd = new CtripSeatData();
                JSONObject seat = seatArr.getJSONObject(j);
                csd.setSeatName(seat.getString("SeatName"));
                csd.setPrice(seat.getDoubleValue("Price"));
                csd.setTicketLeft(seat.getIntValue("TicketLeft"));
                seatlist.add(csd);
            }

            czzd.setSeats(seatlist);

            zzlist.add(czzd);
        }
        crd.setTrains(zzlist);
        //********************************************************//
        List<CtripZhanZhanData> zzlistNew = crd.getTrains();
        List<TrainNewData> dataList = new ArrayList<TrainNewData>();
        for (CtripZhanZhanData czzd : zzlistNew) {

            TrainNewData trainNewdata = new TrainNewData();
            trainNewdata.setStation_train_code(czzd.getTrainNo());//车次号
            trainNewdata.setTrain_no(czzd.getTrain12306No());//12306内部车次号
            trainNewdata.setFrom_station_name(czzd.getFromStationName());//出发站名称
            trainNewdata.setTo_station_name(czzd.getToStationName());//到达站名称
            trainNewdata.setFrom_station_telecode(czzd.getFromTelcode());//出发站三字码
            trainNewdata.setTo_station_telecode(czzd.getToTelcode());//到达站三字码

            //防止字段丢失
            trainNewdata.setStart_station_name("");//起点站
            trainNewdata.setStart_station_telecode("");//起点站三字码
            trainNewdata.setEnd_station_name("");//终点站
            trainNewdata.setEnd_station_telecode("");//终点站三字码


            trainNewdata.setStart_time(czzd.getStartTime());//出发时间
            trainNewdata.setArrive_time(czzd.getArriveTime());//到达时间
            trainNewdata.setIs_support_card(czzd.getIsSupportCard());
            trainNewdata.setCanWebBuy(czzd.getCanWebBuy());//网购标记
            trainNewdata.setControl_day(String.valueOf(czzd.getControlDay()));//预售天数
            trainNewdata.setDay_difference(String.valueOf(czzd.getDayDiff()));//出发到达跨天数
            trainNewdata.setSale_time(czzd.getSaleTime());//预售当天开售时间
            trainNewdata.setLishiValue(String.valueOf(czzd.getDurationMinutes()));//耗时（分钟）
            String lishi = "";
            try {
                int lishiValue = Integer.valueOf(czzd.getDurationMinutes());
                int hour = lishiValue / 60;
                int min = lishiValue % 60;
                lishi = (hour < 10 ? ("0" + hour) : hour) + ":" + (min < 10 ? ("0" + min) : min);
            } catch (Exception e) {
                logger.info("", e);
            }
            trainNewdata.setLishi(String.valueOf(lishi));//历时时间
            trainNewdata.initPrice();
            trainNewdata.initYupiao();

            List<CtripSeatData> seatlist = czzd.getSeats();// 车次坐席

            for (CtripSeatData csd : seatlist) {
                double seat_price = csd.getPrice();
                String seat_name = csd.getSeatName();
                int seat_yupiao = csd.getTicketLeft();

                if ("一等座".equals(seat_name)) {
                    trainNewdata.setZy(Double.toString(seat_price));
                    trainNewdata.setZy_num(String.valueOf(seat_yupiao));
                } else if ("二等座".equals(seat_name)) {
                    trainNewdata.setZe(Double.toString(seat_price));
                    trainNewdata.setZe_num(String.valueOf(seat_yupiao));
                } else if ("商务座".equals(seat_name)) {
                    trainNewdata.setSwz(Double.toString(seat_price));
                    trainNewdata.setSwz_num(String.valueOf(seat_yupiao));
                } else if ("特等座".equals(seat_name)) {
                    trainNewdata.setTdz(Double.toString(seat_price));
                    trainNewdata.setTdz_num(String.valueOf(seat_yupiao));
                    trainNewdata.setTz_num(String.valueOf(seat_yupiao));
                } else if ("高级软卧下".equals(seat_name) || "高级动卧下".equals(seat_name)) {//携程某些车次（D952）：12306高级软卧对应携程高级动卧
                    trainNewdata.setGr_num(String.valueOf(seat_yupiao));
                    trainNewdata.setGw_num(String.valueOf(seat_yupiao));
                    trainNewdata.setGwx(Double.toString(seat_price));
                } else if ("高级软卧上".equals(seat_name) || "高级动卧上".equals(seat_name)) {//携程某些车次（D952）：12306高级软卧对应携程高级动卧
                    trainNewdata.setGr_num(String.valueOf(seat_yupiao));
                    trainNewdata.setGw_num(String.valueOf(seat_yupiao));
                    trainNewdata.setGws(Double.toString(seat_price));
                } else if ("软卧下".equals(seat_name)) {
                    trainNewdata.setRw_num(String.valueOf(seat_yupiao));
                    trainNewdata.setRwx(Double.toString(seat_price));
                } else if ("软卧上".equals(seat_name)) {
                    trainNewdata.setRw_num(String.valueOf(seat_yupiao));
                    trainNewdata.setRws(Double.toString(seat_price));
                } else if ("动卧下".equals(seat_name)) {
                    trainNewdata.setDw_num(String.valueOf(seat_yupiao));
                    trainNewdata.setDwx(Double.toString(seat_price));
                } else if ("动卧上".equals(seat_name)) {
                    trainNewdata.setDw_num(String.valueOf(seat_yupiao));
                    trainNewdata.setDws(Double.toString(seat_price));
                } else if ("硬卧下".equals(seat_name)) {
                    trainNewdata.setYwx(Double.toString(seat_price));
                    trainNewdata.setYw_num(String.valueOf(seat_yupiao));
                } else if ("硬卧中".equals(seat_name)) {
                    trainNewdata.setYwz(Double.toString(seat_price));
                    trainNewdata.setYw_num(String.valueOf(seat_yupiao));
                } else if ("硬卧上".equals(seat_name)) {
                    trainNewdata.setYws(Double.toString(seat_price));
                    trainNewdata.setYw_num(String.valueOf(seat_yupiao));
                } else if ("软座".equals(seat_name)) {
                    trainNewdata.setRz(Double.toString(seat_price));
                    trainNewdata.setRz_num(String.valueOf(seat_yupiao));
                } else if ("硬座".equals(seat_name)) {
                    trainNewdata.setYz(Double.toString(seat_price));
                    trainNewdata.setYz_num(String.valueOf(seat_yupiao));
                } else if ("无座".equals(seat_name)) {
                    trainNewdata.setWz_num(String.valueOf(seat_yupiao));
                    //logger.info("【"+seat_name+"】"+",不是坐席类型,[二等座,硬座]有无座");
                } else {
                    logger.info(seat_name + "该坐席没有匹配到");
                }
            }
            // 票价和余票数设置完成
            dataList.add(trainNewdata);
        }
        //**********************************************************//
        if (dataList.size() == 0) {
            return TrainConsts.NO_DATAS; // 没有查到车次
        }

        OuterSoukdNewData oskd = new OuterSoukdNewData();
        oskd.setErrInfo("");
        oskd.setSdate("");
        oskd.setStype("");
        oskd.setCode("000");
        oskd.setDatajson(dataList);
        String returnStr = JSON.toJSONString(oskd);

        return returnStr;
    }

}
