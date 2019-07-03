import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l9e.transaction.dao.MidwayDao;
import com.l9e.transaction.dao.StationDao;
import com.l9e.transaction.dao.TrainDao;
import com.l9e.transaction.vo.Midway;
import com.l9e.transaction.vo.Station;
import com.l9e.transaction.vo.Train;
import com.l9e.util.HttpUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 更新途经站信息
 * t_sinfo表
 *
 * @author guona
 */
public class MidwayManager {

//	public static String QUERY_MIDWAY_RUL = "https://kyfw.12306.cn/otn/czxx/queryByTrainNo?";

    public static String QUERY_MIDWAY_RUL = "http://118.190.27.106:18001/servlet/queryMidStation?";
    private static Logger logger = Logger.getLogger(MidwayManager.class);
    private MidwayDao midwayDao = new MidwayDao();
    private StationDao stationDao = new StationDao();
    private TrainDao trainDao = new TrainDao();

    public static void main(String[] args) {

        //System.getProperties().setProperty("http.proxyHost", "192.168.65.126");
        //System.getProperties().setProperty("http.proxyPort", "3128");

        new MidwayManager().updateMidwayOfLccc();

    }

    public static String getDayWord(int day) {

        String dayWord = "";
        if (day == 1) {
            dayWord = "当日";
        } else if (day == 2) {
            dayWord = "第2日";
        } else if (day == 3) {
            dayWord = "第3日";
        } else if (day == 4) {
            dayWord = "第4日";
        } else if (day == 5) {
            dayWord = "第5日";
        }

        return dayWord;
    }

    //判断两个时间的大小
    public static int compare_date(String DATE1, String DATE2) throws Exception {
        DateFormat df = new SimpleDateFormat("HH:mm");//24小时制(HH)
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                logger.info(DATE1 + ">" + DATE2);
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                logger.info(DATE1 + "<" + DATE2);
                return -1;
            } else {
                logger.info(DATE1 + "=" + DATE2);
                return 0;
            }
        } catch (Exception exception) {
            logger.info("时间转换异常：", exception);
            throw new Exception("由上级处理");
        }
        //	return 0;
    }

    /**
     * @param startName 起始站
     * @param MidName   途径站
     * @return 获取里程
     */
    public String getDistance(String startName, String MidName) {

        if (startName.equals(MidName)) {
            return "0";
        }
        try {
            Thread.sleep(500);
            String url = "http://search.huochepiao.com/juli/";
            String txtChuFa = URLEncoder.encode(startName, "GBK");
            String txtDaoDa = URLEncoder.encode(MidName, "GBK");
            url = url + "?chuFa=" + txtChuFa + "&daoDa=" + txtDaoDa
                    + "&Submit=%C0%EF%B3%CC%B2%E9%D1%AF";
            String backHtml = HttpUtil.sendByGet(url, "GBK", "300000", "300000");
            String indexPoint = "<div align=\"center\"><h1>";
            if (backHtml != null && backHtml.indexOf(indexPoint) > 0) {
                String lichengStr = backHtml.substring(backHtml
                        .indexOf(indexPoint) + 24);
                String licheng = lichengStr.substring(0, lichengStr.indexOf("公里"));
                logger.info("startName：" + startName + ",MidName: " + MidName + ",距离：" + licheng);
                return licheng;
            }
        } catch (Exception e) {
            // TODO: handle exception
            logger.info("获取异常：" + e.getMessage());
        }
        return null;
    }

    /**
     * @param startName 起始站
     * @param MidName   途径站
     * @return 获取里程
     */
    public String getDistance1(String startName, String MidName) {

        String distance = null;

        if (startName.equals(MidName)) {
            return "0";
        }

        try {
            Thread.sleep(1000);

            String url = "http://search.huochepiao.com/juli/";
            String txtChuFa = URLEncoder.encode(startName, "GBK");
            String txtDaoDa = URLEncoder.encode(MidName, "GBK");
            url = url + "?chuFa=" + txtChuFa + "&daoDa=" + txtDaoDa + "&Submit=%C0%EF%B3%CC%B2%E9%D1%AF";

            String backHtml = HttpUtil.sendByGet(url, "GBK", "300000", "300000");
            String indexPoint = "<div align=\"center\"><h1>";
            if (backHtml != null && backHtml.indexOf(indexPoint) > 0) {
                String lichengStr = backHtml.substring(backHtml.indexOf(indexPoint) + 24);
                String licheng = lichengStr.substring(0, lichengStr.indexOf("公里"));
                logger.info(licheng);
                distance = licheng;
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return distance;
    }

    /**
     * @param startName 起始站
     * @param MidName   途径站
     * @return 获取里程
     */
    public Map<String, String> getDistance(String cc) {

        try {
            Thread.sleep(1000);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String url = "http://search.huochepiao.com/checi/" + cc;
        String backHtml = HttpUtil.sendByGet(url, "GBK", "300000", "300000");
        String indexPoint = "<tr bgcolor=\"#ffffff\" onMouseOver=\"this.bgColor='#eeeeff';\" onMouseOut=\"this.bgColor='#ffffff';\">";
        if (backHtml != null && backHtml.indexOf(indexPoint) > 0) {
            String[] arrayStr = backHtml.split(indexPoint);
            Map<String, String> lichengMap = new HashMap<String, String>();

            for (int i = 1; i < arrayStr.length; i++) {
                String str = arrayStr[i];
                if (str != null && !"".equals(str)) {
                    logger.info(str);
                    String tempStr = str.substring(str.indexOf("a href"), str.indexOf("</a>"));
                    String key = tempStr.substring(tempStr.indexOf(">") + 1);
                    String[] centerStr = str.split("<td align=\"center\">");
                    String value = centerStr[8].substring(0, centerStr[8].indexOf("<"));
                    lichengMap.put(key, value);
                }

            }

            return lichengMap;

        } else {
            logger.info("途径站：" + cc + "，查询异常，为空");
            return null;
        }

    }

    public void updateMidwayOfLccc() {
        Map<String, String> trainMap = new HashMap<String, String>();
        trainMap.put("status", Train.NEEDUPDATE);//02、不存在火车需要更新
        List<Train> trainList = trainDao.queryTrainfromLccc(trainMap);
        logger.info("需要更新的车次共：" + trainList.size());
        for (Train train : trainList) {
            logger.info("train----" + train.getStartStation() + "/" + train.getTrainCode());
            String startStation = train.getStartStation();//始发站
            String endStation = train.getEndStation();//终点站
            Map<String, String> queryMap = new HashMap<String, String>();

            //查询是否有该车站--始发站  start~
            queryMap.put("station_name", startStation);
            List<Station> stationList = stationDao.queryStationFromZm(queryMap);
            if (stationList.size() == 0) {
                logger.info("数据库中没有查到车站名为[" + startStation + "]的起始站，需要更新！");
                continue;
            }
            String startStationCode = stationList.get(0).getStationCode();//始发站简码
            train.setStartStationCode(startStationCode);
            //查询是否有该车站--始发站  end~
            //查询是否有该车站--终点站  start~
            queryMap.put("station_name", endStation);
//			stationList = stationDao.queryStation(queryMap);
            stationList = stationDao.queryStationFromZm(queryMap);
            if (stationList.size() == 0) {
                logger.info("数据库中没有查到车站名为[" + endStation + "]的终点站，需要更新！");
                continue;
            }
            String endStationCode = stationList.get(0).getStationCode();
            train.setEndStationCode(endStationCode);
            //查询是否有该车站--终点站  end~

            List<Midway> midwayList = httpsMidwayList(train);
            //List<Midway> insertMidwayList = new ArrayList<Midway>();

            //********// 不执行更新,直接删除，再插入
            if (null != midwayList && !midwayList.isEmpty()) {
                Map<String, String> delteMap = new HashMap<String, String>();

                List<String> ccList = new ArrayList<String>();
                if (train.getTrainCode().indexOf("/") != -1) {
                    ccList.add(train.getTrainCode().split("/")[0]);
                    ccList.add(train.getTrainCode().split("/")[1]);
                } else {
                    ccList.add(train.getTrainCode());
                }

                for (String cc : ccList) {
                    delteMap.put("trainCode", cc);
                    int count = midwayDao.deleteMidwayByCheci(delteMap);
                    if (count == 0) {
                        midwayDao.deleteMidwayByCheci(delteMap);
                    }
                }


                for (Midway midway : midwayList) {
                    logger.info("起始站：" + startStation + "，途径站：" + midway.getStationName() + ",距离：" + midway.getDistance());
                    if (null == midway.getDistance() || "".equals(midway.getDistance())) {
                        String distance = this.getDistance(startStation, midway.getStationName());
                        midway.setDistance(distance);
                    }
                    logger.info("途径站信息：" + midway);

                }
            }

            if (null != midwayList && !midwayList.isEmpty()) {
                midwayDao.insertMidwayBybach(midwayList);  //批量插入
            } else {
                logger.info("从12306获取失败，无途径站信息！");
            }

            //********//

			/*for (Midway midway : midwayList) {
				Map<String, Object> midwayMap = new HashMap<String, Object>();
			    midwayMap.put("stationName", midway.getStationName());
				midwayMap.put("stationNo", midway.getStationNo());
				midwayMap.put("trainCode", midway.getTrainCode().trim());
				logger.info("midwayMap----"+midwayMap);
				//查询途经站表t_sinfo里是否有该车次信息
				List<Midway> queryMidwayList = midwayDao.queryMidwayFromSinfo(midwayMap);


				logger.info("起始站："+startStation+"，途径站："+midway.getStationName());
				String distance=this.getDistance(startStation,midway.getStationName());
				midway.setDistance(distance);
				logger.info("途径站信息："+midway);

				if (queryMidwayList.size() <= 0) {
					logger.info("数据库内没有车次[" + midway.getTrainCode() + "]的第"
							+ midway.getStationNo() + "个途经站， 需插入数据库");
				//	 insertMidwayList.add(midway);
					midwayDao.insertMidwayIntoSinfo(midway);

				} else {

					Midway queryMidway = queryMidwayList.get(0);
					if (!midway.getArriveTime().equals(
							queryMidway.getArriveTime())
							|| !midway.getStartTime().equals(
									queryMidway.getStartTime())
							|| !midway.getStopoverTime().equals(
									queryMidway.getStopoverTime())
							|| !midway.getStationName().equals(
									queryMidway.getStationName())
							||  !midway.getDistance().equals(
									queryMidway.getDistance())) {
						logger.info("数据库内车次[" + midway.getTrainCode() + "]的第"
								+ midway.getStationNo() + "个站的数据需要更新为startTime:"
								+ midway.getStartTime() + "#arriveTime:"
								+ midway.getArriveTime() + "#stopoverTime:"
								+ midway.getStopoverTime() + "#stationName:"
								+ midway.getStationName());
						midwayDao.updateMidwayOfSinfo(midway);

					}
				}




				// for(Midway insertMidway : insertMidwayList) {

				// }
				// midwayDao.insertMidwayIntoSinfo(insertMidwayList);
			}*/

        }

        logger.info("需要更新的车次共：" + trainList.size() + ",执行完成");
    }

    //查询该车次的途经站信息
    public List<Midway> httpsMidwayList(Train train) {

        List<Midway> midwayList = new ArrayList<Midway>();
        String url = getQueryUrl(train);
        String midwayString = HttpUtil.sendByPost(url, "", "utf-8");
        //暂停2s,防止IP被封
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        logger.info("查询途径站--：" + midwayString);
        ObjectMapper objMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        String startTime = "";//始发站开车时间
        String beforeTime = "";
        String beforeTimeTemp = "";
        int day = 1;//1：当日、2：第2日、3：第3日、4：第4日  ,costday=day-1
        try {
            jsonNode = objMapper.readTree(midwayString);
            JsonNode midwayListNode = jsonNode.get("data").get("data");
            Iterator<JsonNode> midwayListNodes = midwayListNode.iterator();

            String cc = null;
            if (train.getTrainCode().indexOf("/") != -1) {
                cc = train.getTrainCode().split("/")[0];//一个车次
            } else {
                cc = train.getTrainCode();
            }

            Map<String, String> lichengMap = this.getDistance(cc);
            logger.info("车次:" + cc + "里程Map:" + lichengMap);

            while (midwayListNodes.hasNext()) {

                JsonNode midwayNode = midwayListNodes.next();
                Midway midway = new Midway();


                if (train.getTrainCode().indexOf("/") != -1) {

                    String[] czcc = train.getTrainCode().split("/"); //拼接车次，D6617/D6616

                    Midway midway1 = new Midway();//第一个车次
                    Midway midway2 = new Midway();//第二个车次

                    midway1.setTrainCode(czcc[0]);
                    //{"arrive_time":"10:36","station_name":"锦州南","start_time":"10:37","stopover_time":"1分钟","station_no":"04","isEnabled":false}
                    midway1.setArriveTime(midwayNode.get("arrive_time").asText());//到达时间
                    midway1.setStartTime(midwayNode.get("start_time").asText());//开车时间
                    midway1.setStationNo(midwayNode.get("station_no").asInt());//第几个途经站
                    midway1.setStationName(midwayNode.get("station_name").asText());//站名


                    midway2.setTrainCode(czcc[1]);
                    //{"arrive_time":"10:36","station_name":"锦州南","start_time":"10:37","stopover_time":"1分钟","station_no":"04","isEnabled":false}
                    midway2.setArriveTime(midwayNode.get("arrive_time").asText());//到达时间
                    midway2.setStartTime(midwayNode.get("start_time").asText());//开车时间
                    midway2.setStationNo(midwayNode.get("station_no").asInt());//第几个途经站
                    midway2.setStationName(midwayNode.get("station_name").asText());//站名


                    String stopoverTime = midwayNode.get("stopover_time").asText().replace("分钟", "分");//停留时间
                    if (stopoverTime.contains("-")) {
                        stopoverTime = "--";
                    }
                    midway1.setStopoverTime(stopoverTime);
                    midway2.setStopoverTime(stopoverTime);
                    JsonNode station_train_code=midwayNode.get("station_train_code");
                    if ("----".equals(midway1.getArriveTime())||null!=station_train_code) { //起始站
                        startTime = midway1.getStartTime();
                        midway1.setArriveTime(startTime);
                        midway1.setCosttime("当日");
                        midway1.setCostday("0");
                        midway2.setArriveTime(startTime);
                        midway2.setCosttime("当日");
                        midway2.setCostday("0");
                    } else {
                        //判断第几日到达该途经站
                        if ("".equals(beforeTime) || "".equals(midway1.getArriveTime())) {
                            logger.info("出发到达时间：为空");
                        }
                        int beforeDay = compare_date(beforeTime, midway1.getArriveTime());//与上一站时间的差

                        if (beforeDay > 0) {
                            day = day + 1;
                        }
                        midway1.setCostday(String.valueOf(day - 1));
                        midway2.setCostday(String.valueOf(day - 1));
                        midway1.setCosttime(getDayWord(day));
                        midway2.setCosttime(getDayWord(day));

                    }

                    String distance = null;
                    if (lichengMap != null && !lichengMap.isEmpty()) {

                        for (Map.Entry<String, String> entry : lichengMap.entrySet()) {
                            logger.info("key= " + entry.getKey() + " and value= " + entry.getValue());
                            if (midwayNode.get("station_name").asText().equals(entry.getKey())) {
                                distance = entry.getValue();
                            }
                        }
                        logger.info("[途径站]：" + midwayNode.get("station_name").asText() + "，[距离]:" + distance);

                    }

                    if (distance != null) {
                        midway1.setDistance(distance);
                        midway2.setDistance(distance);
                    }

                    midwayList.add(midway1);
                    midwayList.add(midway2);

                    beforeTime = "----".equals(midway1.getArriveTime()) ? midway1.getStartTime() : midway1.getArriveTime();//上一站的到达时间

                } else {

                    midway.setTrainCode(train.getTrainCode());
                    //{"arrive_time":"10:36","station_name":"锦州南","start_time":"10:37","stopover_time":"1分钟","station_no":"04","isEnabled":false}
                    midway.setArriveTime(midwayNode.get("arrive_time").asText());//到达时间
                    midway.setStartTime(midwayNode.get("start_time").asText());//开车时间
                    midway.setStationNo(midwayNode.get("station_no").asInt());//第几个途经站
                    midway.setStationName(midwayNode.get("station_name").asText());//站名
                    String stopoverTime = midwayNode.get("stopover_time").asText().replace("分钟", "分");//停留时间
                    if (stopoverTime.contains("-")) {
                        stopoverTime = "--";
                    }
                    midway.setStopoverTime(stopoverTime);
                    JsonNode station_train_code=midwayNode.get("station_train_code");
                    if ("----".equals(midway.getArriveTime())||null!=station_train_code) { //起始站
                        startTime = midway.getStartTime();
                        midway.setArriveTime(startTime);
                        midway.setCosttime("当日");
                        midway.setCostday("0");
                    } else {
                        //判断第几日到达该途经站
                        if ("".equals(beforeTime) || "".equals(midway.getArriveTime())) {
                            logger.info("出发到达时间：为空");
                        }
                        int beforeDay = compare_date(beforeTime, midway.getArriveTime());//与上一站时间的差
                        logger.info(beforeTime + "------" + midway.getArriveTime());
                        if (beforeDay > 0) {
                            day = day + 1;
                            logger.info("******" + day);
                        }

                        logger.info(day);

                        midway.setCostday(String.valueOf(day - 1));
                        midway.setCosttime(getDayWord(day));
                    }

                    String distance = null;
                    if (null != lichengMap && !lichengMap.isEmpty()) {

                        for (Map.Entry<String, String> entry : lichengMap.entrySet()) {
                            logger.info("key= " + entry.getKey() + " and value= " + entry.getValue());
                            if (midwayNode.get("station_name").asText().equals(entry.getKey())) {
                                distance = entry.getValue();
                            }
                        }
                        logger.info("[途径站]：" + midwayNode.get("station_name").asText() + "，[距离]:" + distance);

                    }

                    if (null != distance) {
                        midway.setDistance(distance);
                    }

                    midwayList.add(midway);
                    beforeTime = "----".equals(midway.getArriveTime()) ? midway.getStartTime() : midway.getArriveTime();//上一站的到达时间

                }

            }
        } catch (JsonProcessingException e) {
            logger.info("解析车次字符串发成异常：" + e.getMessage(), e);
            return null;
        } catch (IOException e) {
            logger.info("解析车次字符串发成异常：" + e.getMessage(), e);
            return null;
        } catch (Exception e) {
            logger.info("解析车次字符串发成异常：" + e.getMessage(), e);
            return null;
        }
        return midwayList;
    }

    private String getQueryUrl(Train train) {
        return MidwayManager.QUERY_MIDWAY_RUL + "train_no="
                + train.getTrainNo() + "&from_station_telecode="
                + train.getStartStationCode() + "&to_station_telecode="
                + train.getEndStationCode() + "&depart_date="
                + train.getStartDate();
    }


    private String getQueryUrl(Map<String, String> map) {
        return MidwayManager.QUERY_MIDWAY_RUL + "train_no="
                + map.get("train_no") + "&from_station_telecode="
                + map.get("start_station_code") + "&to_station_telecode="
                + map.get("end_station_code") + "&depart_date="
                + map.get("start_date");
    }
}
