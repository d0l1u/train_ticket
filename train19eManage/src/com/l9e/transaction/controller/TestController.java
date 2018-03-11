package com.l9e.transaction.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.CommonService;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * test
 * @author
 */
@Controller("TestController")
@RequestMapping("/testController")
public class TestController extends BaseController{

	private static final Logger logger = Logger.getLogger(TestController.class);

	@Resource
	private CommonService commonService;

	@RequestMapping("/main.do")
	public String mainHandler(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("查询测试");
		logger.info("请求地址" + request.getRemoteAddr());

		logger.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + commonService.query());

		request.setAttribute("msg", "i am testing");
		logger.info("i am testing");
		return "test";
	}
	
	@RequestMapping("/test.do")
	public ModelAndView test(String goods, String person){
		System.out.println(goods);
		System.out.println(person);
		ModelAndView mav = new ModelAndView("test");
		mav.addObject("msg", "ModelAndView");
		return mav;
	}

	public static void main(String[] args) throws Exception {
		// System.getProperties().setProperty("http.proxyHost", "192.168.63.177"
		// );
		// System.getProperties().setProperty("http.proxyPort", "10032" );
		//
		// String url =
		// "http://webservice.soukd.com/SKTrain.Asp?FromCity=北京&ToCity=上海&sDate=2013-05-20&UserID=19e&CheckCode=57342AA7715832648A5DD9CC18F2DA96";
		// String result = HttpUtil.sendByGet(url, "gbk");
		//		
		String aaa = "<?xml version=\"1.0\" encoding=\"gb2312\" ?><OTResponse><RangeCode>直通</RangeCode><StartCity>北京</StartCity></OTResponse>";
		XStream xstream = new XStream(new DomDriver());

		xstream.alias("OTResponse", OTResponse.class);

		OTResponse ot = (OTResponse) (xstream.fromXML(aaa));
		System.out.println(ot.getRangeCode());
	}

	class OTResponse {
		String RangeCode;
		String StartCity;

		public String getRangeCode() {
			return RangeCode;
		}

		public void setRangeCode(String rangeCode) {
			RangeCode = rangeCode;
		}

		public String getStartCity() {
			return StartCity;
		}

		public void setStartCity(String startCity) {
			StartCity = startCity;
		}
	}
}
