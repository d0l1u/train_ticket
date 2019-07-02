package com.train.sytstem.center.test;

import com.train.system.center.util.HttpUtil;

/**
 * HttpTest
 *
 * @author taokai3
 * @date 2018/6/27
 */
public class HttpTest {

	// @Test
	public void test() {
		String url = "http://127.0.0.1:18010/robot/book/pc";
		String parameter = "{\"orderId\":\"SJ9011806211941412755\",\"publicIp\":\"114.67.89.54\",\"account\":{\"username\":\"oxbp3388\",\"password\":\"vfug3338\"},\"data\":{\"trainCode\":\"K7906\",\"departureDate\":\"2018-07-19\",\"fromStationName\":\"包头\",\"toStationName\":\"包头东\",\"passengers\":[{\"passengerNo\":\"CP9011806211941412756\",\"name\":\"范凡\",\"cardType\":\"2\",\"cardNo\":\"130682198912230048\",\"ticketType\":\"0\",\"seatType\":\"8\"}]}}";

		String post = new HttpUtil().doHttpPost(url, parameter, 1000 * 60 * 3, true);
		System.out.println("post:" + post);
	}

}
