import com.l9e.transaction.dao.CtripDao;
import com.l9e.transaction.vo.ProxyEntity;
import com.l9e.transaction.vo.TrainCtrip;
import com.l9e.transaction.vo.TrainSeat;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpRequest;
import com.l9e.util.SeatType;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 查询携程的车次信息
 *
 * @author guona
 */
public class CtripTrainNumber {
    public static Logger logger = Logger.getLogger(CtripTrainNumber.class);
    public static String CTRIP_NUMBER_URL = "http://trains.ctrip.com/TrainBooking/Ajax/SearchListHandler.ashx";
    public ProxyEntity entity = new ProxyEntity();
    private CtripDao ctripDao = new CtripDao();

    public void getCtripTrainNumber(int threadId, int number) {
        int threadNum = threadId;
        while (true) {
            //	List<Map<String, String>> zmStartList = ctripDao.queryZmById(threadId);
            List<Map<String, String>> zmStartList = ctripDao.queryZmByCCStatus("00");//获取一个未查询的始发站,多个线程竞争

            if (null==zmStartList){
               // logger.info("没有未更新的车站，结束");
                continue;
            }

            List<Map<String, String>> zmStartList_new = new ArrayList<Map<String, String>>();
            for (Map<String, String> map : zmStartList) {

                int count = ctripDao.updateZmCCStatus(Integer.valueOf(String.valueOf(map.get("id"))));
                logger.info("id:" + String.valueOf(map.get("id")) + ",cout:" + count);
                if (count > 0) {
                    zmStartList_new.add(map);
                }
            }
            if (zmStartList_new.size() > 0) {
                updateTrainNumber(threadNum, threadId, zmStartList_new);
                threadId += number;
            } else {
                //logger.info(zmStartList+"当前车站未获取到~~~~");
            }



        }
    }

    //ctrip查询车次信息
    public void updateTrainNumber(int num, int threadId, List<Map<String, String>> zmStartList) {
        int index = 0;
        String logPre = "线程" + num + ",[ctrip车次]";
        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());//2016-01-07
        String query_date = DateUtil.dateAddDays(today, "15");
        String query_back_date = DateUtil.dateAddDays(today, "17");

        //遍历所有的车站的信息
//		List<Map<String, String>> zmStartList = ctripDao.queryZmStartList(num);//始发站
        logger.info(logPre + "查询id:" + threadId + "的始发站信息...");
//		List<Map<String, String>> zmStartList = ctripDao.queryZmById(threadId);
        List<Map<String, String>> zmArrivalList = ctripDao.queryZmArrivalList();//到达站
        logger.info(logPre + "始发车站数据总共" + zmStartList.size() + "条~~~到达站共" + zmArrivalList.size() + "条...");
        Map<String, String> zmParamMap = null;
        for (Map<String, String> zmStartMap : zmStartList) {
            zmParamMap = new HashMap<String, String>();
            zmParamMap.put("query_date", query_date);
            zmParamMap.put("query_back_date", query_back_date);
            zmParamMap.put("DepartureCity", zmStartMap.get("pinyin"));//"DepartureCity":"linfen"
            zmParamMap.put("DepartureCityName", zmStartMap.get("name"));//DepartureCityName":"临汾"
            for (Map<String, String> zmEndMap : zmArrivalList) {
                zmParamMap.put("ArrivalCity", zmEndMap.get("pinyin"));//"ArrivalCity":"suzhou"
                zmParamMap.put("ArrivalCityName", zmEndMap.get("name"));//"ArrivalCityName":"苏州"

                //向ctrip发起请求，查询车次信息
                String result = queryCtripInfo(zmParamMap);
                sleep(1000); //暂停,防止IP被封

                //解析结果
                if (StringUtils.isNotEmpty(result) && result != null && !"".equals(result) && !"Exception".equals(result)) {
                    try {
                        JSONObject json = JSONObject.fromObject(result);
                        if (json.has("TrainItemsList")) {
                            JSONArray transitListArray = json.getJSONArray("TrainItemsList");
                            for (int i = 0; i < transitListArray.size(); i++) {
                                JSONObject jsonItem = JSONObject.fromObject(transitListArray.getString(i));
                                if (jsonItem.getBoolean("IsStartStation") && jsonItem.getBoolean("IsEndStation")) {
                                    TrainCtrip train = new TrainCtrip();
                                    train.setDeparture_city_name(json.getString("DepartureCityName"));
                                    train.setArrival_city_name(json.getString("ArrivalCityName"));
                                    train.setDeparture_date(query_date);
                                    train.setDcity_code(json.getString("DCityCode"));
                                    train.setAcity_code(json.getString("ACityCode"));
                                    train.setDcity_id(json.getString("DCityID"));
                                    train.setAcity_id(json.getString("ACityID"));
                                    train.setTrain_number(jsonItem.getString("TrainName"));
                                    train.setTrain_type(jsonItem.getString("TrainType"));
                                    train.setStart_station_name(jsonItem.getString("StartStationName"));
                                    train.setEnd_station_name(jsonItem.getString("EndStationName"));
                                    train.setStrat_time(jsonItem.getString("StratTime"));
                                    train.setEnd_time(jsonItem.getString("EndTime"));
                                    train.setTake_time(jsonItem.getString("TakeTime"));
                                    train.setTake_days(jsonItem.getInt("TakeDays") + "");
                                    train.setPre_sale_day(jsonItem.getInt("PreSaleDay") + "");
                                    int count = ctripDao.queryCtripIsExist(train);
                                    if (count > 0) {
                                        int flag = ctripDao.updateCtrip(train);
                                        logger.info(logPre + "更新" + flag + "条数据：" + train.getTrain_number());
                                    } else {
                                        ctripDao.addCtrip(train);
                                        logger.info(logPre + "新增1条数据：" + train.getTrain_number());
                                    }
                                }

                                //票价信息
                                if (jsonItem.has("SeatBookingItem")) {
                                    JSONArray seatListArray = jsonItem.getJSONArray("SeatBookingItem");
                                    TrainSeat seat = new TrainSeat();
                                    seat.setCc(jsonItem.getString("TrainName"));
                                    seat.setFz(jsonItem.getString("StartStationName"));
                                    seat.setDz(jsonItem.getString("EndStationName"));

                                    for (int j = 0; j < seatListArray.size(); j++) {
                                        JSONObject seatItem = JSONObject.fromObject(seatListArray.getString(j));
//						            	String seatCtrip = seatItem.getString("SeatName").trim();
                                        String seatTypeId = seatItem.getString("SeatTypeId");
                                        String seatCtrip = SeatType.getTitleByValue(seatTypeId);
                                        String price = seatItem.getString("Price");
                                        if ("无座".equals(seatCtrip)) {
                                            seat.setWz(price);
                                        } else if ("硬座".equals(seatCtrip)) {
                                            seat.setYz(price);
                                        } else if ("软座".equals(seatCtrip)) {
                                            seat.setRz(price);
                                        } else if ("硬卧".equals(seatCtrip)) {
                                            seat.setYwx(price);
                                        } else if ("软卧".equals(seatCtrip)) {
                                            seat.setRwx(price);
                                        } else if ("二等座".equals(seatCtrip)) {
                                            seat.setRz2(price);
                                        } else if ("一等座".equals(seatCtrip)) {
                                            seat.setRz1(price);
                                        } else if ("商务座".equals(seatCtrip)) {
                                            seat.setSwz(price);
                                        } else if ("特等座".equals(seatCtrip)) {
                                            seat.setTdz(price);
                                        } else if ("高级软卧".equals(seatCtrip)) {
                                            seat.setGwx(price);
                                        } else if ("动卧".equals(seatCtrip)) {
                                            seat.setDwx(price);
                                        }

                                        //G\K\C\Z\D\X\Y\T\S\L
                                        /*if("".equals(jsonItem.getString("TrainType"))){
						            		
						            	}*/
                                    }
                                    System.out.println(seat.getFz() + "-------------" + seat.getDz());
                                    int countSeat = ctripDao.querySeatIsExist(seat);
                                    if (countSeat > 0) {
                                        int flag = ctripDao.updateSeat(seat);
                                        logger.info("[坐席票价]更新" + flag + "条数据：" + seat.getCc());
                                    } else {
                                        seat.setXh(CreateIDUtil.createID(""));
                                        System.out.println("+++++++++++++++++++++" + seat.getXh());
                                        ctripDao.addSeat(seat);
                                        logger.info("[坐席票价]新增1条数据：" + seat.getCc());
                                    }
                                }

                                index++;
                                System.out.println("------------------" + index);
                            }
                        }
                    } catch (Exception e) {
                        logger.info(logPre + "解析结果exception, e=" + e.getMessage(), e);
                        e.printStackTrace();
                    }
                } else {
                    logger.info("代理IP:" + entity.getHost() + "起始站：" + zmParamMap.get("DepartureCityName") + ",到达站：" + zmParamMap.get("ArrivalCityName") + ",ctrip发起请求，查询车次信息,result" + result);
                    //将异常的存入临时表,重新跑一遍
                    if ("Exception".equals(result)) {
                        logger.info("代理IP:" + entity.getHost() + ",访问出现异常。");

                    }
                }
            }

            //更新t_ctrip_zm表的cc_status为11已查询
            Map<String, Object> zmMap = new HashMap<String, Object>();
            zmMap.put("id", zmStartMap.get("id"));
            zmMap.put("name", zmStartMap.get("name"));
            zmMap.put("cc_status", "11");
            int zmflag = ctripDao.updateZm(zmMap);
            logger.info(logPre + "更新zm" + zmflag + "条数据：" + zmMap);
        }
    }

    //向ctrip发起请求，查询车次信息:15天以后的
    private String queryCtripInfo(Map<String, String> zmParamMap) {
        String logPre = "[ctrip车次--发起请求]";
        String result = "";
        //{"IsAll":false,"Filter":"0","Catalog":"","IsGaoTie":false,"IsDongChe":false,"CatalogName":"",
        //"DepartureCity":"linfen","ArrivalCity":"suzhou","HubCity":"",
        //"DepartureCityName":"临汾","ArrivalCityName":"苏州","HubCityName":null,"DepartureDate":"2016-06-07",
        //"DepartureDateReturn":"2016-06-09","ArrivalDate":"","TrainNumber":""}
        JSONObject json = new JSONObject();
        json.put("IsBus", false);
        json.put("Filter", "0");
        json.put("Catalog", "");
        json.put("IsGaoTie", false);
        json.put("IsDongChe", false);
        json.put("CatalogName", "");
        json.put("DepartureCity", zmParamMap.get("DepartureCity"));//始发站城市拼音
        json.put("ArrivalCity", zmParamMap.get("ArrivalCity"));//终点站城市拼音
//		json.put("DepartureCity", "jinan");//始发站城市拼音
//		json.put("ArrivalCity", "beijing");//终点站城市拼音
        json.put("HubCity", "");
        json.put("DepartureCityName", zmParamMap.get("DepartureCityName"));//始发站城市名字
        json.put("ArrivalCityName", zmParamMap.get("ArrivalCityName"));//终点站城市名字
//		json.put("DepartureCityName", "济南");//始发站城市名字
//		json.put("ArrivalCityName", "北京");//终点站城市名字

//		json.put("HubCityName", null);
        json.put("DepartureDate", zmParamMap.get("query_date"));//出发日期
        json.put("DepartureDateReturn", zmParamMap.get("query_back_date"));//返程日期（无用）
        json.put("ArrivalDate", "");
        json.put("TrainNumber", "");
//		logger.info(logPre+"-----json="+json.toString()); 

        try {
//			Map<String, String> paramMap = new HashMap<String, String>();
//			paramMap.put("Action", "getSearchList");
//			paramMap.put("value", json.toString());
//			logger.info(logPre+"paramMap:"+paramMap);

            //result = HttpClientUtils.sendPostRequest(CTRIP_NUMBER_URL, paramMap, "utf-8");

//			String sendParams=UrlFormatUtil.CreateUrl("", paramMap, "utf-8");
//			logger.info(logPre+"sendParams---------------------------"+sendParams);
//			result = HttpUtil.sendByPost(CTRIP_NUMBER_URL, sendParams, "utf-8"); 
//			result = HttpRequest.sendGet(CTRIP_NUMBER_URL, "Action=getSearchList&value="+json.toString(),30000,entity);
            result = HttpRequest.sendPost(CTRIP_NUMBER_URL + "?Action=getSearchList", "value=" + json.toString(), 60000, entity);
//			logger.info(logPre+"请求通知接口返回：" + result);
        } catch (Exception e) {
            logger.info(logPre + "exception, e=" + e.getMessage(), e);
            e.printStackTrace();
        }

        return result;
    }


    public void sleep(int t) {

        try {
            Thread.sleep(t);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
