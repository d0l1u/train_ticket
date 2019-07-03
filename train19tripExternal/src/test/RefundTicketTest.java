package test;

import com.l9e.util.DateUtil;
import com.l9e.util.Md5Encrypt;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author meizs
 * @create 2018-05-18 16:57
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/context/applicationContext.xml", "classpath:springMVC.xml"})
@WebAppConfiguration
public class RefundTicketTest {

    protected Logger logger = Logger.getLogger(this.getClass());

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    public static void main(String[] args) {

    }

    @Before
    public void setUp() {

        mockMvc = webAppContextSetup(webApplicationContext).build();


    }

    @Test
    //post get ,form
    public void demo1() {

        RequestBuilder requestBuilder =
                post("/externalInterface.do")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .characterEncoding("UTF-8");
        // .param("", "")
        // .param("", "");
        try {
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        } catch (Exception e) {
            logger.info(e);
        }
    }

    @Test
    //post json
    public void demo2() {

        String paramString = "";
        RequestBuilder requestBuilder = post("/trainAccount/contact/query")
                .contentType(new MediaType("application", "json"))
                .characterEncoding("UTF-8").content(paramString);
        try {
            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        } catch (Exception e) {
            logger.info(e);
        }

    }

    @Test
    //退票申请
    public void refund() {

        Map<String, String> map = new HashMap<String, String>();
        map.put("terminal", "PC");
        map.put("merchant_id", "301049");
        map.put("timestamp", DateUtil.dateToString(new Date(), DateUtil.DATE_FMT2));
        map.put("type", "refundTicket");
        map.put("version", "1.0.0");
        map.put("spare_pro1", "");
        map.put("spare_pro2", "");
        map.put("json_param", "{\"comment\":\"退票3\",\"merchant_order_id\":\"510183118902384\",\"order_id\":\"EXHC180506101834764\",\"refund_picture_url\":\"\",\"refund_result_url\":\"http://www.ichumenr.com/train/new_url\",\"request_id\":\"510183118902384-6\",\"refund_type\":\"part\",\"refundinfo\":[{\"id_type\":\"2\",\"ticket_type\":\"0\",\"user_ids\":\"220324196003052929\",\"user_name\":\"王雅环\"}]}");
        String source_str = map.get("terminal") + map.get("merchant_id") + map.get("timestamp") + map.get("type") + map.get("version") + map.get("spare_pro1") + map.get("spare_pro2") + map.get("json_param");
        String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(source_str + "e924994174527de01db510a38a0eb240", "", "utf-8");
        map.put("hmac", hmac_19);


        RequestBuilder requestBuilder =
                post("/externalInterface.do")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .characterEncoding("UTF-8");


        MockHttpServletRequestBuilder rq = (MockHttpServletRequestBuilder) requestBuilder;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            rq.param(entry.getKey(), entry.getValue());
        }

        try {
            String result = mockMvc.perform(rq)
                    .andExpect(status().isOk()) // 返回的状态是200
                    .andDo(print()) // 打印出请求和相应的内容
                    .andReturn().getResponse().getContentAsString(); // 将相应的数据转换为字符串

            System.out.println(result);

        } catch (Exception e) {
            logger.info(e);
        }

    }

    @Test
    public void testQuery() throws UnsupportedEncodingException, Exception {

        String from_station = "北京";
        String arrive_station = "上海";

        String travel_time = "2018-05-22";
        String terminal = "PC";
        String merchant_id = "kuyoutest";
        String timestamp = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT2);

        String type = "queryLeftTicket";
        String version = "1.0.0";

        String md_str = terminal + merchant_id + timestamp + type + version + travel_time + from_station + arrive_station;

        String hmac_19 = Md5Encrypt.getKeyedDigestFor19Pay(md_str + "e7bf4932d2166d1c87ab4313c4ef37b0", "", "utf-8");

        System.out.println(hmac_19);

        StringBuilder params = new StringBuilder();

        //*************************************************************//


        String responseString = mockMvc
                .perform(
                        post("/externalInterface.do")
                                // 请求的url,请求的方法是get
                                .contentType(
                                        MediaType.APPLICATION_FORM_URLENCODED)
                                .characterEncoding("UTF-8")
                                .param("terminal", terminal)
                                .param("merchant_id", merchant_id)
                                .param("timestamp", timestamp)
                                .param("version", version).param("type", type)
                                .param("travel_time", travel_time)
                                .param("from_station", from_station)
                                .param("arrive_station", arrive_station)
                                .param("hmac", hmac_19))
                .andExpect(status().isOk()) // 返回的状态是200
                .andDo(print()) // 打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString(); // 将相应的数据转换为字符串
        System.out.println("--------返回的json = " + responseString);

    }


}
