package com.train.robot.method;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.train.robot.util.HangtianUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * HangtianMethod
 * @author daiqinghua
 * @date 2018/6/21
 */
public class HangtianMethod {

    private String logid;

    private Logger logger = LoggerFactory.getLogger(HangtianMethod.class);

    public HangtianMethod(String logid) {
        this.logid = logid;
    }
    public HangtianMethod() {

    }

    //查询车次url
    @Value("${interface.searchUrl}")
    private String searchUrl;

    //下单查询订单等其他接口通用url
    @Value("${interface.orderUrl}")
    private String orderUrl;

    //下单占座回调地址
    @Value("${callback.trainOrder}")
    private String trainOrderCallback;

    //退票回调地址
    @Value("${callback.returnTicket}")
    private String returnTicketCallback;

    //改签回调地址
    @Value("${callback.changeTrain}")
    private String changeTrainCallback;

    //确认改签回调地址
    @Value("${callback.confirmChangeTrain}")
    private String confirmChangeTrainCallback;

    /**
     * 查询车次方法
     * @param method {train_query(有价格)，train_query_remain(无价格)}
     * @return json
     */
    public String searchTrain(String method,String trainDate,String fromStation,String toStation){
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("train_date",trainDate); //乘车日期（yyyy-MM-dd）
        paramsMap.put("from_station",fromStation); //出发站简码
        paramsMap.put("to_station",toStation);  //到达站简码
        paramsMap.put("purpose_codes","ADULT");//订票类别，固定值“ADULT”表示普通票
        return HangtianUtils.pushInterface(searchUrl,method,paramsMap);
    }

    /**
     * 占座接口
     * @return json串
     */
    public String trainOrder(JSONObject paramJson) {
        String method = "train_order";
        //json解析---12306帐号相关数据(accountJson)，预定车次相关数据(trainJson),预定人相关数据(passengersJson)
        JSONObject accountJson = paramJson.getJSONObject("account");
        JSONObject trainJson = paramJson.getJSONObject("data");
        JSONArray passengersJson = trainJson.getJSONArray("passengers");

        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("orderid", paramJson.getString("orderId")); //使用方订单号
        paramsMap.put("checi", trainJson.getString("trainCode"));//车次
        paramsMap.put("from_station_code", trainJson.getString("fromStationCode")); //出发站简码(非必选)
        paramsMap.put("from_station_name", trainJson.getString("fromStationName")); //出发站名称
        paramsMap.put("to_station_code", trainJson.getString("toStationCode"));  //到达站简码(非必选)
        paramsMap.put("to_station_name", trainJson.getString("toStationName"));  //到达站名称
        paramsMap.put("train_date", trainJson.getString("departureDate")); //乘车日期（yyyy-MM-dd）
        paramsMap.put("callbackurl", trainOrderCallback);//占座成功回调地址[选填]
        paramsMap.put("LoginUserName", accountJson.getString("username"));//12306用户名
        paramsMap.put("LoginUserPassword", accountJson.getString("password"));//12306密码
        paramsMap.put("is_accept_standing", trainJson.getBoolean("isAcceptStanding"));//是否要无座票，true要;false或者不传不要
        //        paramsMap.put("is_choose_seats", false);//是否需要选座
        //        paramsMap.put("choose_seats", "");//选座STR（比如：1A1D2B2C2F，就是选5个坐席），选座个数要与乘客数量应该一致
        //存放乘客信息(多条)
        JSONArray passengersArray = new JSONArray();
        for (Object object : passengersJson) {
            JSONObject passenger = (JSONObject) object;
            JSONObject passengerJson = new JSONObject();
            passengerJson.put("passengersename", passenger.getString("name"));//乘客姓名
            passengerJson.put("passportseno", passenger.getString("cardNo"));//乘客证件号
            //----获取证件类型进行转换----
            String cardType = passenger.getString("cardType");
            String cardResult, cardName;
            if ("2".equals(cardType)) {
                cardResult = "1";
                cardName = "二代身份证";
            } else if ("3".equals(cardType)) {
                cardResult = "C";
                cardName = "港澳通行证";
            } else if ("4".equals(cardType)) {
                cardResult = "G";
                cardName = "台湾通行证";
            } else if ("5".equals(cardType)) {
                cardResult = "B";
                cardName = "护照";
            } else {
                cardResult = cardType;
                cardName = "";
            }
            passengerJson.put("passporttypeseidname", cardName);//乘客证件类型名
            passengerJson.put("passporttypeseid", cardResult);//乘客证件类型CODE
            passengerJson.put("passengerid", passenger.getString("cpId"));//乘客唯一标识（字符串内为随机长数字）
            //----获取坐席类型进行格式转换
            int seats = Integer.parseInt(passenger.getString("seatType"));
            String seatName = "";
            switch (seats) {
                case 0:
                    seatName = "商务座";
                    break;
                case 1:
                    seatName = "特等座";
                    break;
                case 2:
                    seatName = "一等座";
                    break;
                case 3:
                    seatName = "二等座";
                    break;
                case 4:
                    seatName = "高级软卧";
                    break;
                case 5:
                    seatName = "软卧";
                    break;
                case 6:
                    seatName = "硬卧";
                    break;
                case 7:
                    seatName = "软座";
                    break;
                case 8:
                    seatName = "硬座";
                    break;
            }
            passengerJson.put("zwname", seatName);//坐席名
            passengerJson.put("zwcode", HangtianUtils.seatCode(seats));//坐席类型CODE
            passengerJson.put("price", 0);//票价(这里必填，不了解则传0)
            //-----获取票类型进行转换-------
            String ticket_type = passenger.getString("ticketType");
            passengerJson.put("piaotype", HangtianUtils.convetTicketType(ticket_type));//票类型(成人票、儿童票之类)
            passengerJson.put("cxin", "");//传空字符串
            passengersArray.add(passengerJson);
        }
        paramsMap.put("passengers", passengersArray);//乘客信息
        return HangtianUtils.pushInterface(orderUrl, method, paramsMap);
    }

    /**
     * 确认占座
     * @return json
     */
    public String confirmOrder(String orderId,String transactionId){
        String method = "train_confirm";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("orderid", orderId); //使用方订单号
        paramsMap.put("transactionid", transactionId);//交易单号
        return HangtianUtils.pushInterface(orderUrl, method, paramsMap);
    }

    /**
     * 查询订单
     * @return  json
     */
    public String queryOrder(String orderId,String transactionId) {
        String method = "train_query_info";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("orderid", orderId); //使用方订单号
        paramsMap.put("transactionid", transactionId);//交易单号
        return HangtianUtils.pushInterface(orderUrl, method, paramsMap);
    }

    /**
     * 取消订单
     * @return json
     */
    public String cancelOrder(String orderId,String transactionId){
        String method = "train_cancel";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("orderid", orderId); //使用方订单号
        paramsMap.put("transactionid", transactionId);//交易单号
        paramsMap.put("callbackurl", "");//回调地址(可为空)
        return HangtianUtils.pushInterface(orderUrl, method, paramsMap);
    }

    /**
     * 取消夜间订单
     * @return json
     */
    public String cancelNightOrder(String orderId){
        String method = "train_cancel_order_night";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("orderid", orderId); //使用方订单号
        return HangtianUtils.pushInterface(orderUrl, method, paramsMap);
    }
    /**
     * 线上退票
     * @return json
     */
    public String returnTicket(String orderId,String transactionId,String orderNumber){
        String method = "return_ticket";
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("orderid", orderId); //使用方订单号
        paramsMap.put("transactionid", transactionId);//航天华有订单号
        paramsMap.put("ordernumber", orderNumber);//取票单号
        String reqTimes = String.valueOf(Calendar.getInstance().getTimeInMillis());
        paramsMap.put("reqtoken", reqTimes);//请求特征（唯一）
        paramsMap.put("callbackurl", returnTicketCallback);//异步通知接口回调地址
        paramsMap.put("LoginUserName", "");//12306用户名
        paramsMap.put("LoginUserPassword", "");//12306密码
        //存放乘客信息(多条)
        JSONArray passengersArray = new JSONArray();
        JSONObject passengerJson = new JSONObject();
        passengerJson.put("ticket_no", "EE569774714010076");//车票的票号 （唯一）
        passengerJson.put("passengername", "张华平");//乘客姓名
        passengerJson.put("passportseno", "512501197911047780");//乘客证件号
        passengerJson.put("passporttypeseid", "1");//乘客证件类型CODE
        passengersArray.add(passengerJson);
        paramsMap.put("tickets", passengersArray);//车票信息
        return HangtianUtils.pushInterface(orderUrl, method, paramsMap);
    }

    /**
     * 车票改签/变更到站
     * @return json
     */
    public String changeTrain(JSONObject paramJson){
        String method = "train_request_change";
        //json解析---12306帐号相关数据(accountJson)，预定车次相关数据(trainJson),预定人相关数据(passengersJson)
        JSONObject accountJson = paramJson.getJSONObject("account");
        JSONObject trainJson = paramJson.getJSONObject("data");
        JSONArray passengersJson = trainJson.getJSONArray("passengers");

        Map<String,Object> paramsMap = new HashMap<>();
        //--------------------------改签原票信息-----------------
        paramsMap.put("orderid",trainJson.getString("orderId")); //使用方订单号
        paramsMap.put("transactionid",trainJson.getString("transactionId"));//航天华有订单号
        paramsMap.put("ordernumber",trainJson.getString("sequence"));//取票单号
//        String reqTimes = String.valueOf(Calendar.getInstance().getTimeInMillis());
        paramsMap.put("reqtoken",trainJson.getString("orderId"));//请求特征（唯一）
        paramsMap.put("LoginUserName",accountJson.getString("username"));//12306用户名
        paramsMap.put("LoginUserPassword",accountJson.getString("password"));//12306密码
        String oldZwcode = "";
        String changeZwcode = "";
        //存放乘客信息(多条)
        JSONArray passengersArray = new JSONArray();
        for (Object object : passengersJson) {
            JSONObject passenger = (JSONObject) object;
            JSONObject passengerJson = new JSONObject();
            passengerJson.put("passengersename", passenger.getString("name"));//乘客姓名
            passengerJson.put("passportseno", passenger.getString("cardNo"));//乘客证件号
            //----获取证件类型进行转换----
            String cardType = passenger.getString("cardType");
            String cardResult;
            if ("2".equals(cardType)) {
                cardResult = "1";
            } else if ("3".equals(cardType)) {
                cardResult = "C";
            } else if ("4".equals(cardType)) {
                cardResult = "G";
            } else if ("5".equals(cardType)) {
                cardResult = "B";
            } else {
                cardResult = cardType;
            }
            passengerJson.put("passporttypeseid", cardResult);//乘客证件类型CODE
            //-----获取票类型进行转换-------
            String ticket_type = passenger.getString("ticketType");
            passengerJson.put("piaotype", HangtianUtils.convetTicketType(ticket_type));//票类型(成人票、儿童票之类)
            passengerJson.put("old_ticket_no", passenger.getString("subSequence"));
            passengersArray.add(passengerJson);
            //----获取坐席类型进行格式转换
            int seats = Integer.parseInt(passenger.getString("seatType"));
            int newSeats = Integer.parseInt(passenger.getString("newSeatType"));
            oldZwcode = HangtianUtils.seatCode(seats);
            changeZwcode = HangtianUtils.seatCode(newSeats);
        }
        paramsMap.put("ticketinfo",passengersArray);//车票信息

        paramsMap.put("old_zwcode",oldZwcode);//坐席类型CODE
        //------------------------改签信息---------------------------------
        paramsMap.put("callbackurl",changeTrainCallback);//异步通知接口回调地址
        paramsMap.put("isTs",false);//是否变更到站，true/false
        paramsMap.put("from_station_code", trainJson.getString("newFromStationCode")); //出发站简码(非必选)
        paramsMap.put("from_station_name", trainJson.getString("newFromStationName")); //出发站名称
        paramsMap.put("to_station_code", trainJson.getString("newToStationCode"));  //到达站简码(非必选)
        paramsMap.put("to_station_name", trainJson.getString("newToStationName"));  //到达站名称
        paramsMap.put("isasync","Y");//异步改签，固定值（Y）
        paramsMap.put("change_zwcode", changeZwcode);//坐席类型CODE
        paramsMap.put("change_checi",trainJson.getString("newTrainCode"));//改签车次
        paramsMap.put("change_datetime",trainJson.getString("newDepartureDate")+" 00:00:00");//改签发车时间，例：yyyy-MM-dd HH:mm:ss
        return HangtianUtils.pushInterface(orderUrl,method,paramsMap);
    }

    /**
     * 取消改签
     * @return json
     */
    public String cancelChangeTrain(String orderId,String transactionId){
        String method = "train_cancel_change";
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("orderid",orderId); //使用方订单号
        paramsMap.put("transactionid",transactionId);//交易单号
//        String reqTimes = String.valueOf(Calendar.getInstance().getTimeInMillis());
        paramsMap.put("reqtoken",orderId);//请求特征（唯一）
        return HangtianUtils.pushInterface(orderUrl,method,paramsMap);
    }

    /**
     * 确认改签
     * @return json
     */
    public String confirmChangeTrain(String orderId,String transactionId){
        String method = "train_confirm_change";
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("orderid",orderId); //使用方订单号
        paramsMap.put("transactionid",transactionId);//交易单号
        paramsMap.put("callbackurl",confirmChangeTrainCallback);//异步通知接口回调地址
        paramsMap.put("isasync","Y");
//        String reqTimes = String.valueOf(Calendar.getInstance().getTimeInMillis());
        paramsMap.put("reqtoken",orderId);//请求特征（唯一）
        return HangtianUtils.pushInterface(orderUrl,method,paramsMap);
    }

    public String getLogid() {
        return logid;
    }

    public void setLogid(String logid) {
        this.logid = logid;
    }
}
