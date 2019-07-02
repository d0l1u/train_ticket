package com.l9e.servlet;

import com.l9e.common.BaseServlet;
import com.l9e.common.TrainConsts;
import com.l9e.service.RobotServiceImp;
import com.l9e.service.SysSettingServiceImpl;
import com.l9e.util.*;
import com.l9e.vo.*;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询余票主控路口,19emobile查询去除动卧字段
 */
public class MobileQueryTicketServlet extends HttpServlet {
    private static final long serialVersionUID = 3264299111876887524L;

    private static String ctrip_data_url = ConfigUtil.getValue("ctrip_data_url");
    private static Logger logger = Logger.getLogger(MobileQueryTicketServlet.class);
    RobotServiceImp robotServiceImp = new RobotServiceImp();
    SysSettingServiceImpl sysSettingServiceImpl = new SysSettingServiceImpl();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }


    @Override
    public void init() throws ServletException {
        logger.info("init channel_robot selected num!");
    }

    /**
     * 查询余票入口
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        long a = System.currentTimeMillis();
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");

        /**分发参数*/
        String channel = req.getParameter("channel");
        logger.info("*****mobile*****" + "channel渠道名称：" + channel);
        String type = req.getParameter("type");


        /**业务参数*/
        String from_station = req.getParameter("from_station");
        String arrive_station = req.getParameter("arrive_station");
        String travel_time = req.getParameter("travel_time");
        String purpose_codes = req.getParameter("purpose_codes");//学生(0X00)、普通(ADULT)的余票查询,
        String train_code = req.getParameter("train_code");

        //查询余票
        String check_spare_num = req.getParameter("check_spare_num");
        String alter_refund = req.getParameter("alter_refund");
        StringBuffer log_req = new StringBuffer();

        //日志
        log_req.append("channel[").append(channel).append("]").append("param:")
                .append("travel_time[").append(travel_time).append("],from_station[")
                .append(URLDecoder.decode(from_station, "utf-8")).append("],arrive_station[")
                .append(URLDecoder.decode(arrive_station, "utf-8")).append("],purpose_codes[")
                .append(purpose_codes).append("],").append("type:").append(type).append("train_code:").append(train_code);

        logger.info(log_req.toString());

        Map<String, String> param = new HashMap<String, String>();
        param.put("from_station", from_station);
        param.put("arrive_station", arrive_station);
        param.put("travel_time", travel_time);
        param.put("purpose_codes", purpose_codes);
        if (StringUtils.isNotEmpty(train_code)) {
            param.put("train_code", train_code);//携程可选参数，查询单个车次
        } else {
            param.put("train_code", "");
        }

        param.put("method", "DGTrain");
        String key = getFileName(param) + "noDW";//不包含动卧字段的json
        param.put("channel", channel);
        Object memcache = MemcachedUtil.getInstance().getAttribute(key);

        if ("true".equals(check_spare_num)) {
            memcache = null;
        }

        if ("true".equals(alter_refund)) {
            memcache = null;
        }

        if (null == memcache) {
            String queryTicketLine = this.getTrainSystemSetting("queryTicketLine");//1.代表查询12306  2 .查询库进行数据伪造 ,[查询系统异常,让其可以订票] 3.携程查询
            logger.info("查询余票线路,queryTicketLine::" + queryTicketLine);
            String jsonStr = TrainConsts.NO_DATAS;
            if ("1".equals(queryTicketLine)) { //查询12306
                //java查询
                jsonStr = queryTrainInfo(param);
                if (TrainConsts.STATION_ERROR.equals(jsonStr)) {
                    write2Response(resp, TrainConsts.STATION_ERROR);
                    return;
                }

                if (TrainConsts.NO_ROBOT.equals(jsonStr) || TrainConsts.ERROR.equals(jsonStr)) {
                    jsonStr = queryTrainInfo(param);
                    if (TrainConsts.NO_ROBOT.equals(jsonStr) || TrainConsts.ERROR.equals(jsonStr)) {
                        jsonStr = queryTrainInfo(param);
                    }
                }

                if (TrainConsts.NO_ROBOT.equals(jsonStr) || TrainConsts.ERROR.equals(jsonStr)) {
                    logger.info(param + ",重试3次,查询车次失败,查询数据库车次:" + jsonStr);
                    boolean ysFlag = false;
                    if ("ADULT".equals(purpose_codes)) {//成人预售30天
                        ysFlag = CalendarUtil.checkNowDateInterval(DateUtil.stringToDate(travel_time, DateUtil.DATE_FMT1), 30);
                    } else if ("0X00".equals(purpose_codes)) {//学生预售60天
                        ysFlag = CalendarUtil.checkNowDateInterval(DateUtil.stringToDate(travel_time, DateUtil.DATE_FMT1), 60);
                    } else {
                        ysFlag = CalendarUtil.checkNowDateInterval(DateUtil.stringToDate(travel_time, DateUtil.DATE_FMT1), 30);
                    }

                    if (ysFlag) {//在预售期内
                        queryTicketLine = "2";
                        logger.info("查询机器人失败，查询数据库的数据，不缓存！！！,queryTicketLine::" + queryTicketLine);
                        jsonStr = queryTrainInfoNew(param);// 查询数据库,进行匹配
                    } else {
                        jsonStr = TrainConsts.NO_DATAS;
                    }

                }

            } else if ("2".equals(queryTicketLine)) { //查询库进行数据伪造
                logger.info("----不查询12306----,查询数据库,进行匹配,数据伪造！");
                try {
                    jsonStr = queryTrainInfoNew(param);// 查询数据库,进行匹配
                } catch (Exception e) {
                    logger.info("查询数据库发生异常！！！", e);
                }
            } else if ("3".equals(queryTicketLine)) {//查询携程的数据接口
                logger.info("-------查询携程余票-----------------------");
                param.put("ctripChannel", channel);
                jsonStr = queryTrainInfoCtrip(param);

            }

            if (jsonStr == null || TrainConsts.NO_ROBOT.equals(jsonStr) || TrainConsts.ERROR.equals(jsonStr)) {
                write2Response(resp, TrainConsts.NO_DATAS);//所有错误全部转换成，未获取到数据
                return;
            }

            try {

                if (jsonStr.contains("\"code\":\"000\"") && "1".equals(queryTicketLine)) {//从12306查询的余票信息，需要缓存
                    logger.info(param + ",12306数据查询成功");
                    long startFile = System.currentTimeMillis();
                    String fileDir = "//data//cache_files//" + travel_time;//不含动卧字段
                    String fileName = Md5Encrypt.md5(key, "gbk") + ".txt";
                    String filePath = fileDir + "//" + fileName;
                    logger.info("---------------cache_files is filePath=" + filePath);
                    //创建文件保存接口返回数据
                    FileUtil.removeFile(filePath);
                    boolean isSucess = FileUtil.createFile(fileDir, fileName, jsonStr, "UTF-8");
                    //文件生成成功则把文件名写入Memcache
                    if (isSucess) {
                        MemcachedUtil.getInstance().setAttribute(key, filePath, 1 * 60 * 1000);
                        logger.info(param.get("from_station") + param.get("arrive_station") + param.get("travel_time") + "," + "," + "文件缓存耗时:" + (System.currentTimeMillis() - startFile) / 1000f + "s");
                    }
                } else {
                    logger.info("queryTicketLine::" + queryTicketLine + ",查询的返回数据：" + jsonStr);
                }
                long b = System.currentTimeMillis();
                logger.info(param.get("from_station") + param.get("arrive_station") + param.get("travel_time") + "," + (b - a) / 1000f + "秒");
                //返回查询结果
                write2Response(resp, jsonStr);
            } catch (Exception e) {
                logger.info("保存查询信息为缓存文件异常！", e);
                write2Response(resp, "error");
            }
        } else {
            long start = System.currentTimeMillis();
            String filePath = (String) memcache;
            String fileContent = FileUtil.readFile(filePath, "UTF-8");
            StringBuffer file_str = new StringBuffer();
            file_str.append(param.get("from_station") + param.get("arrive_station") + param.get("travel_time") + ",读文件耗时[").append(System.currentTimeMillis() - start).append("]ms");
            logger.info(file_str.toString());
            write2Response(resp, fileContent);
        }

    }

    //查询携程的余票数据
    public String queryTrainInfoCtrip(Map<String, String> param) {
        String jsonStr = TrainConsts.NO_DATAS;
        StringBuffer postParam = new StringBuffer();
        try {
            postParam.append("travel_time=").append(param.get("travel_time"))
                    .append("&from_station=").append(URLEncoder.encode(param.get("from_station"), "utf-8"))
                    .append("&arrive_station=").append(URLEncoder.encode(param.get("arrive_station"), "utf-8"))
                    .append("&train_code=").append(param.get("train_code"))
                    .append("&channel=").append(param.get("ctripChannel"))
                    .append("&type=").append("yupiao");
        } catch (UnsupportedEncodingException e) {
            logger.info("***", e);
            jsonStr = TrainConsts.ERROR;
        }

        logger.info("请求携程服务接口：" + ctrip_data_url + "?" + postParam.toString());
        jsonStr = HttpUtil.sendByPost(ctrip_data_url, postParam.toString(), "utf-8");
        logger.info("携程接口返回数据：" + jsonStr);

        logger.info("*****去除动卧信息后的字符串*****");
        if (jsonStr.contains("\"code\":\"000\"")) {//查询成功
            JSONObject obj = JSONObject.fromObject(jsonStr);
            JSONArray arr = obj.getJSONArray("datajson");
            for (int i = 0; i < arr.size(); i++) {
                JSONObject json = new JSONObject();
                json = arr.getJSONObject(i);
                json.remove("dwx");
                json.remove("dws");
                json.remove("dw_num");
            }
            return obj.toString();
        }

        return jsonStr;
    }


    //查询数据库，获取车次和票价
    public String queryTrainInfoNew(Map<String, String> param) throws UnsupportedEncodingException {
        // TODO Auto-generated method stub
        try {
            QueryControll qc = new QueryControll();
            OuterSoukdNewData osnd = new OuterSoukdNewData();
            StringBuffer params = new StringBuffer();

            String from_station = BaseServlet.stationName.get(param
                    .get("from_station"));
            String arrive_station = BaseServlet.stationName.get(param
                    .get("arrive_station"));
            String purpose_codes = param.get("purpose_codes");
            if (null == from_station) {
                logger.info("station is error: from_station:"
                        + URLDecoder.decode(param.get("from_station"), "utf-8"));
                return TrainConsts.STATION_ERROR;
            }
            if (null == arrive_station) {
                logger.info("station is error: arrive_station:"
                        + URLDecoder.decode(param.get("arrive_station"),
                        "utf-8"));
                return TrainConsts.STATION_ERROR;
            }
            params.append("travel_time=").append(param.get("travel_time"))
                    .append("&from_station=").append(from_station)
                    .append("&arrive_station=").append(arrive_station);

            if (StringUtils.isNotEmpty(purpose_codes)) {
                if ("0X00".equals(purpose_codes)) {
                    params.append("&purpose_codes=").append(purpose_codes);
                } else if ("ADULT".equals(purpose_codes)) {
                    params.append("&purpose_codes=").append(purpose_codes);
                } else {
                    logger.info("purpose_codes,参数错误：" + purpose_codes);
                    return TrainConsts.ERROR;
                }
            }
            logger.info("database program query :"
                    + URLDecoder.decode(params.toString(), "utf-8"));

            logger.info("java program query :"
                    + URLDecoder.decode(params.toString(), "utf-8"));

            logger.info("---------查询数据库，获取车次，然后拼接票价----------");

            String jsonStr = qc.dataBaseQueryData(param);

            logger.info("根据车站请求查询车次余票接口的返回结果jsonStr为：" + jsonStr);
            if (TrainConsts.ERROR.equals(jsonStr)
                    || jsonStr.equals(TrainConsts.NO_DATAS)) {
                return jsonStr;
            }
            ObjectMapper mapper = new ObjectMapper();
            TrainNewMidData train_data = mapper.readValue(jsonStr.toString(),
                    TrainNewMidData.class);
            if (train_data != null && train_data.getDatas().size() > 0) {
                osnd.setCode("000");
                osnd.setDatajson(train_data.getDatas());
                trainInfoAppendPrice(param, osnd);

                robotServiceImp.querySystemSetting("sys_weather_book");
                if ("0".equals(robotServiceImp.getSetting_value())) {
                    for (TrainNewData data : osnd.getDatajson()) {
                        data.setWz_num("-");
                        data.setYz_num("-");
                        data.setYw_num("-");
                        data.setRz_num("-");
                        data.setRw_num("-");
                        data.setZy_num("-");
                        data.setZe_num("-");
                        data.setGw_num("-");
                        data.setTdz_num("-");
                        data.setTz_num("-");
                        data.setSwz_num("-");
                        data.setGr_num("-");
                        data.setDw_num("-");
                    }
                }
                if (train_data.getDatas().size() > 0) {
                    JSONObject json_str = JSONObject.fromObject(osnd);
                    //老版服务处理
                    logger.info("******去除动卧信息，保持原有接口的使用*****");
                    JSONArray arr = json_str.getJSONArray("datajson");
                    JSONObject json = new JSONObject();
                    for (int i = 0; i < arr.size(); i++) {
                        json = arr.getJSONObject(i);
                        json.remove("dwx");
                        json.remove("dws");
                        json.remove("dw_num");
                    }

                    return json_str.toString();

                } else {
                    return TrainConsts.ERROR;
                }
            } else {
                return TrainConsts.ERROR;
            }
        } catch (Exception e) {
            logger.info("java程序查询余票异常！", e);
            return TrainConsts.ERROR;
        }

    }

    //机器人新接口，java版
    public String queryTrainInfo(Map<String, String> param) {
        MobileQueryControll qc = new MobileQueryControll();
        String jsonStr = TrainConsts.NO_DATAS;
        //从内存队列取机器人
        WorkerVo robot = null;
        try {
            robot = getWorkerByQueue();
            if (null == robot) {
                robot = getWorkerByQueue();
            }
            if (null == robot) {
                logger.info("*****从队列获取四次,没有获取到机器*******");
                jsonStr = TrainConsts.ERROR;
            } else {
                StringBuffer buff = new StringBuffer();
                buff.append("travel_time=").append(param.get("travel_time"))
                        .append("|from_station=")
                        .append(param.get("from_station"))
                        .append("|arrive_station=")
                        .append(param.get("arrive_station"))
                        .append("|worker_ext=").append(robot.getWorker_ext());

                logger.info("queryTrainInfo信息：" + buff.toString());
                jsonStr = qc.javaQueryData(param, robot);
            }
        } catch (Exception e) {
            logger.info("获取查询机器失败", e);
            jsonStr = TrainConsts.ERROR;
        }
        return jsonStr;
    }


    /**
     * 从内存队列取机器人,取两次
     *
     * @return
     * @throws RepeatException
     * @throws DatabaseException
     */
    public WorkerVo getWorkerByQueue() throws RepeatException, DatabaseException {
        WorkerVo robot = WorkerQueue.getInstance().poll();
        if (null != robot) {
            return robot;
        } else {
            return WorkerQueue.getInstance().poll();
        }
    }

    /**
     * 拼接车次信息票价
     *
     * @throws DatabaseException
     * @throws RepeatException
     */
    public void trainInfoAppendPrice(Map<String, String> paramMap, OuterSoukdNewData osnd) throws Exception {
        List<TrainNewDataFake> list = new ArrayList<TrainNewDataFake>();
        List<TrainNewData> list_train = osnd.getDatajson();
        if (osnd != null && list_train != null
                && list_train.size() > 0) {
            List<TrainNewData> new_list = new ArrayList<TrainNewData>();
            robotServiceImp.queryProperTrainNewData(paramMap);
            list = robotServiceImp.getList();
            TrainNewDataFake tndf = null;
            boolean exist = false;
            for (TrainNewData trainNewData : list_train) {
                trainNewData.initPrice();
                for (int i = 0; i < list.size(); i++) {
                    String[] arrCc = list.get(i).getCc().split("/");
                    String trainCode = trainNewData.getStation_train_code();
                    int len = arrCc.length;
                    for (int m = 0; m < len; m++) {
                        if (arrCc[m].equals(trainCode)) {
                            if (list.get(i).getFz().equals(trainNewData.getFrom_station_name()) &&
                                    list.get(i).getDz().equals(trainNewData.getTo_station_name())) {
                                tndf = list.get(i);
                                if (!"-".equals(tndf.getYz())) {
                                    trainNewData.setYz(tndf.getYz());
                                    exist = true;
                                }
                                if (!"-".equals(tndf.getRz())) {
                                    trainNewData.setRz(tndf.getRz());
                                    exist = true;
                                }
                                if (!"-".equals(tndf.getYws())) {
                                    trainNewData.setYws(tndf.getYws());
                                    exist = true;
                                }
                                if (!"-".equals(tndf.getYwz())) {
                                    trainNewData.setYwz(tndf.getYwz());
                                    exist = true;
                                }
                                if (!"-".equals(tndf.getYwx())) {
                                    trainNewData.setYwx(tndf.getYwx());
                                    exist = true;
                                }
                                if (!"-".equals(tndf.getRws())) {
                                    trainNewData.setRws(tndf.getRws());
                                    exist = true;
                                }
                                if (!"-".equals(tndf.getRwx())) {
                                    trainNewData.setRwx(tndf.getRwx());
                                    exist = true;
                                }
                                if (!"-".equals(tndf.getRz1())) {
                                    trainNewData.setZy(tndf.getRz1());
                                    exist = true;
                                }
                                if (!"-".equals(tndf.getRz2())) {
                                    trainNewData.setZe(tndf.getRz2());
                                    exist = true;
                                }
                                if (!"0".equals(tndf.getSwz())) {
                                    trainNewData.setSwz(tndf.getSwz());
                                    exist = true;
                                }
                                if (!"-".equals(tndf.getTdz())) {
                                    trainNewData.setTdz(tndf.getTdz());
                                    exist = true;
                                }
                                if (!"-".equals(tndf.getGws())) {
                                    trainNewData.setGws(tndf.getGws());
                                    exist = true;
                                }
                                if (!"-".equals(tndf.getGwx())) {
                                    trainNewData.setGwx(tndf.getGwx());
                                    exist = true;
                                }
                                if (exist) {
                                    new_list.add(trainNewData);
                                }
                            }
                        }
                    }
                    if (exist) {
                        break;
                    }
                }
                if (!exist) {

                    Map<String, String> train_info = new HashMap<String, String>();
                    train_info.put("train_no", trainNewData.getTrain_no());
                    train_info.put("from_station_no", trainNewData.getFrom_station_no());
                    train_info.put("to_station_no", trainNewData.getTo_station_no());
                    train_info.put("seat_types", trainNewData.getSeat_types());
                    train_info.put("train_date", paramMap.get("travel_time"));
                    train_info.put("train_code", trainNewData.getStation_train_code());
                    train_info.put("from_station_name", trainNewData.getFrom_station_name());
                    train_info.put("to_station_name", trainNewData.getTo_station_name());
                    logger.info("code:" + trainNewData.getStation_train_code() + trainNewData.getFrom_station_name() + "/" + trainNewData.getTo_station_name());
                    try {
                        robotServiceImp.addWaitPrice(train_info);
                    } catch (Exception e) {
                        logger.info("插入待查询票价表异常");
                    }
                } else {
                    exist = false;
                }
            }
            for (TrainNewDataFake train : list) {
                String[] arrCc = train.getCc().split("/");
                int len = arrCc.length;
                boolean exist_train = false;
                if (len > 1) {
                    for (int m = 0; m < len; m++) {
                        for (int i = 0; i < list_train.size(); i++) {
                            String trainCode = list_train.get(i).getStation_train_code();
                            if (arrCc[m].equals(trainCode)) {
                                exist_train = true;
                                break;
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < list_train.size(); i++) {
                        String trainCode = list_train.get(i).getStation_train_code();
                        if (train.getCc().equals(trainCode)) {
                            exist_train = true;
                            break;
                        }
                    }
                }
                if (!exist_train) {
                    try {
                        robotServiceImp.addDeletePrice(train);
                        ;
                    } catch (Exception e) {
                        logger.info("插入待删除票价表异常");
                    }
                }
            }
            osnd.setDatajson(new_list);
        }
    }


    /**
     * 获取文件名(eg:all_北京_上海_2013-5-22)
     *
     * @return
     */
    protected String getFileName(Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
        sb.append("javarobot_").append(map.get("from_station"))
                .append("_")
                .append(map.get("arrive_station"))
                .append("_")
                .append(map.get("travel_time"))
                .append(map.get("method"));
        return sb.toString();
    }


    /**
     * 查询余票线路选择
     *
     * @param key
     * @return
     */
    public String getTrainSystemSetting(String key) {
        String queryTicketLine = null;//1.代表查询12306  2 .查询库进行数据伪造 3.携程数据
        if (null == MemcachedUtil.getInstance().getAttribute(key)) {
            try {
                int status = sysSettingServiceImpl.querySysVal(key);
                logger.info("***" + status);
            } catch (RepeatException e) {
                logger.info("*********", e);
            } catch (DatabaseException e) {
                logger.info("********", e);
            }
            queryTicketLine = sysSettingServiceImpl.getSysVal();
            MemcachedUtil.getInstance().setAttribute(key, queryTicketLine, 30 * 1000);
        } else {
            queryTicketLine = (String) MemcachedUtil.getInstance().getAttribute(key);
        }
        logger.info("queryTicketLine:::" + queryTicketLine);
        return queryTicketLine;
    }

    /**
     * 值写入response
     *
     * @param response
     * @param StatusStr
     */
    public void write2Response(HttpServletResponse response, String StatusStr) {
        try {
            response.getWriter().write(StatusStr);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
