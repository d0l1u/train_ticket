package com.l9e.transaction.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.CommonService;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.MobileMsgUtil;
import com.l9e.util.XmlUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
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
	@Resource
	private MobileMsgUtil mobileMsgUtil;

	@RequestMapping("/main_no.jhtml")
	public String mainHandler(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("查询测试");
		logger.info("请求地址" + request.getRemoteAddr());
		System.out.println(request.getServletPath());
		System.out.println(request.getServerPort());
		System.out.println(request.getServerName());

		request.setAttribute("msg", "i am testing");
		logger.info("i am testing");
		
		mobileMsgUtil.send("13716579764", "我是中国人");
		return "test";
	}
	
	@RequestMapping("/test.jhtml")
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
		String aaa = "<?xml version=\"1.0\" encoding=\"gb2312\" ?><OTResponse><RangeCode>直通</RangeCode><StartCity>北京</StartCity><Data><ID>1</ID><TrainCode>G102</TrainCode></Data><Data><ID>1</ID><TrainCode>G102</TrainCode></Data></OTResponse>";
		
		OTResponse ot = XmlUtil.toBean(aaa, OTResponse.class);

		System.out.println(ot.getRangeCode());
		System.out.println(MemcachedUtil.getInstance().setAttribute("user", "zhangjun", 60*60));
		System.out.println(MemcachedUtil.getInstance().getAttribute("user"));
		System.out.println(MemcachedUtil.getInstance().getAttribute("user"));
		System.out.println(MemcachedUtil.getInstance().getAttribute("user"));
	}
	
	@XStreamAlias("OTResponse")
	class OTResponse {
		String RangeCode;
		String StartCity;
		
		@XStreamImplicit(itemFieldName="Data")  
		List<Data> dataList;

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

		public List<Data> getDataList() {
			return dataList;
		}

		public void setDataList(List<Data> dataList) {
			this.dataList = dataList;
		}


	}
	
	@XStreamAlias("Data")
	class Data {
		String ID;
		String TrainCode;
		public String getID() {
			return ID;
		}
		public void setID(String id) {
			ID = id;
		}
		public String getTrainCode() {
			return TrainCode;
		}
		public void setTrainCode(String trainCode) {
			TrainCode = trainCode;
		}

	}
}
