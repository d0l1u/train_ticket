package test;

import com.l9e.util.HttpUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

/**
 * @author meizs
 */
public class QueyTicketUtil {
    private static String reqUrl = "http://127.0.0.1:18060/ctripDataInterface.do";
    // private static String reqUrl = "http://118.190.15.83:18060/ctripDataInterface.do";

    public static void main(String[] args) throws Throwable {

       queryTicket();

     //   queryStopStation();


    }

    public static void queryStopStation() {
        String channel = "ext_301088";
        String query_date = "2018-06-20";
        String train_code = "K1454";
        String type = "stopStation";
        StringBuffer params = new StringBuffer();
        params.append("channel=").append(channel)
                .append("&type=").append(type)
                .append("&query_date=").append(query_date)
                .append("&train_code=").append(train_code);

        String result = HttpUtil.sendByPost(reqUrl, params.toString(), "30000", "30000", "utf-8");
        System.out.println(result);
    }

    public static void queryTicket() throws UnsupportedEncodingException {
        String from_station = "上海";
        String arrive_station = "重庆北";
        String travel_time = "2018-06-16";
        String purpose_codes = "ADULT";
        String type = "yupiao";
        Random rand = new Random();
        //String channel="ext"+rand.nextInt()*100000;
        String channel = "ext-1458698400";
        StringBuffer params = new StringBuffer();
        params.append("channel=").append(channel)
                .append("&type=").append(type)
                .append("&from_station=").append(URLEncoder.encode(from_station, "utf-8"))
                .append("&arrive_station=").append(URLEncoder.encode(arrive_station, "utf-8"))
                .append("&travel_time=").append(travel_time)
                .append("&purpose_codes=").append(purpose_codes);
        //.append("&train_code=").append("D956");


        String result = HttpUtil.sendByPost(reqUrl, params.toString(), "30000", "30000", "utf-8");
        System.out.println(result);
    }

}
