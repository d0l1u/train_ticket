package com.train.robot.util;

import com.alibaba.fastjson.JSONObject;
import com.train.commons.util.DateUtil;
import com.train.commons.util.MD5Util;
import com.train.commons.worm.client.HttpWormClient;
import com.train.commons.worm.client.HttpWormResponse;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * HangtianUtils-航天华有接口配套util
 * @author daiqinghua
 * @date 2018/6/21
 */
public class HangtianUtils
{
    @Value("${interface.partnerId}")
    private static String partnerId;//航天华有分配帐号
    @Value("${interface.key}")
    private static String key; //航天华有分配key
    private static HttpWormClient client = new HttpWormClient();

    /**
     * 发送请求
     * @return 航天华有返回字符串
     */
    public static String pushInterface(String url,String method,Map<String,Object> paramsMap){
        String parameters = "jsonStr="+buildJson(method,paramsMap);
        System.out.println("拼接json参数："+parameters);
        try {
            HttpWormResponse response = client.doPost(url,getHeader(),parameters);
            System.out.println("返回值:"+ response.getResponseContent());
            return response.getResponseContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 拼接请求参数
     * @return json串s
     */
    private static String buildJson(String method,Map<String,Object> paramsMap){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("partnerid",partnerId);
        jsonObject.put("method",method);
        //获取当前时间戳
        String reqTimes = DateUtil.formatDate(new Date(),"yyyyMMddHHmmss");
        jsonObject.put("reqtime", reqTimes);
        //签名加密
        String sign = MD5Util.md5(partnerId+method+reqTimes+MD5Util.md5(key).toLowerCase()).toLowerCase();
        jsonObject.put("sign",sign);
        jsonObject.putAll(paramsMap);
        return jsonObject.toJSONString();
    }

    /**
     * 请求头设置
     * @return map
     */
    private static Map<String,String> getHeader(){
        Map<String,String> map = new HashMap<>();
        map.put("Content-Type","application/x-www-form-urlencoded");
        return map;
    }

    /**
     * 车票类型转换
     * @return
     */
    public static String convetTicketType(String ticket_type){
        String type ="";
        if ("0".equals(ticket_type)) {
            type = "1";
        } else if ("1".equals(ticket_type)) {
            type = "2";
        } else if ("3".equals(ticket_type)) {
            type = "3";
        } else if ("2".equals(ticket_type)) {
            type = "4";
        } else {
            type = ticket_type;
        }
        return  type;
    }
    /**
     * 坐席类型转换
     * @param seats
     * @return
     */
    public static String seatCode(int seats){
        String seatResult = "";
        switch (seats) {
            case 0:
                seatResult = "9";
                break;
            case 1:
                seatResult = "P";
                break;
            case 2:
                seatResult = "M";
                break;
            case 3:
                seatResult = "O";
                break;
            case 4:
                seatResult = "6";
                break;
            case 5:
                seatResult = "4";
                break;
            case 6:
                seatResult = "3";
                break;
            case 7:
                seatResult = "2";
                break;
            case 8:
                seatResult = "1";
                break;
            case 9:
                seatResult = "O";
                break;
            case 11:
                seatResult = "5";
                break;
            case 16:
                seatResult = "A";
                break;
            case 20:
                seatResult = "F";
                break;
        }
        return seatResult;
    }

}
