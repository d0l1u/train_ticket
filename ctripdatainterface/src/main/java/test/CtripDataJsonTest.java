package test;

import com.l9e.util.HttpUtil;
import com.l9e.util.Md5Encrypt;
import com.l9e.util.UrlFormatUtil;

import java.util.LinkedHashMap;
import java.util.Map;

public class CtripDataJsonTest {

    /*
     * user-19e 密钥-7a692b08bb10a9c0681cc54697e8447d
     *
     */
    public static void main(String[] args) {


        //queryLeftTicket();

        //queryStopStation();

    }

    public static void queryStopStation() {
        Map<String, String> paramMap = new LinkedHashMap<String, String>();
        paramMap.put("DepartDate", "2018-06-10");
        paramMap.put("TrainNo", "K1523");
        paramMap.put("User", "19e");
        String timeStamp = Long.toString(System.currentTimeMillis() / 1000);
        String md5Str = timeStamp + "7a692b08bb10a9c0681cc54697e8447d";
        String sign = Md5Encrypt.md5(md5Str, "utf-8");
        paramMap.put("TimeStamp", timeStamp);
        paramMap.put("Sign", sign);
        String params = "";
        try {
            params = UrlFormatUtil.CreateUrl(paramMap, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(params);
        String result = HttpUtil.sendByPost("http://m.ctrip.com/restapi/soa2/12976/json/GetStopStations", params, "300000", "300000", "utf-8");
        //String result=HttpUtil.sendByGet("http://m.ctrip.com/restapi/soa2/12976/json/SearchS2S"+"?"+params, "30000","30000","utf-8");
        System.out.println(result);
    }

    public static void queryLeftTicket() {
        Map<String, String> paramMap = new LinkedHashMap<String, String>();

        paramMap.put("From", "上海");
        paramMap.put("To", "重庆北");
        paramMap.put("DepartDate", "2018-02-27");
        paramMap.put("User", "19e");
        //paramMap.put("TrainNo", "D952");

        String TimeStamp = Long.toString(System.currentTimeMillis() / 1000);
        String md5Str = TimeStamp + "7a692b08bb10a9c0681cc54697e8447d";
        String sign = Md5Encrypt.md5(md5Str, "utf-8");
        paramMap.put("TimeStamp", TimeStamp);
        paramMap.put("Sign", sign);
        String params = "";
        try {
            params = UrlFormatUtil.CreateUrl(paramMap, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(params);
        String result = HttpUtil.sendByPost("http://m.ctrip.com/restapi/soa2/12976/json/SearchS2S", params, "300000", "300000", "utf-8");
        //String result=HttpUtil.sendByGet("http://m.ctrip.com/restapi/soa2/12976/json/SearchS2S"+"?"+params, "30000","30000","utf-8");
        System.out.println(result);
    }


}
