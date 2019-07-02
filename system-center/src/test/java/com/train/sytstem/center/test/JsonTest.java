package com.train.sytstem.center.test;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.Date;

/**
 * JsonTest
 *
 * @author taokai3
 * @date 2018/7/4
 */
public class JsonTest {

    String str = "{\n" + "\t\"recordCount\": \"5\",\n" + "\t\"postalcode\": \"\",\n" +
            "\t\"passenger_type_name\": \"成人\",\n" + "\t\"born_date\": \"2018-06-08 00:00:00\",\n" +
            "\t\"delete_time\": \"2018/12/05\",\n" + "\t\"passenger_id_type_code\": \"1\",\n" + "\t\"code\": \"4\",\n" +
            "\t\"sex_code\": \"M\",\n" + "\t\"passenger_type\": \"1\",\n" + "\t\"country_code\": \"CN\",\n" +
            "\t\"passenger_id_no\": \"342723197207160752\",\n" + "\t\"first_letter\": \"ZHL\",\n" +
            "\t\"passenger_flag\": \"0\",\n" + "\t\"sex_name\": \"男\",\n" + "\t\"address\": \"\",\n" +
            "\t\"email\": \"\",\n" + "\t\"isUserSelf\": \"N\",\n" + "\t\"phone_no\": \"\",\n" +
            "\t\"passenger_name\": \"章海龙\",\n" + "\t\"total_times\": \"99\",\n" +
            "\t\"passenger_id_type_name\": \"二代身份证\",\n" + "\t\"mobile_no\": \"\"\n" + "}";

    @Test
    public void parse() {
        JSONObject json = JSONObject.parseObject(str);
        Date delete_time = json.getDate("delete_time");
        System.err.println(delete_time);
    }

}
