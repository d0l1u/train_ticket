package com.l9e.transaction.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.vo.QunarOrderPack;
import com.l9e.transaction.vo.QunarResult;
import com.l9e.util.HttpUtil;

/**
 * test
 * @author
 */
@Controller("TestController")
@RequestMapping("/testController")
public class TestController extends BaseController{

	private static final Logger logger = Logger.getLogger(TestController.class);


	@RequestMapping("/queryOrders.jhtml")
	public String queryOrders(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("查询测试");
		
		//临时测试代理
		System.getProperties().setProperty("http.proxyHost", "192.168.63.132");
		System.getProperties().setProperty("http.proxyPort", "6789" );
		
		String merchantCode = "xyxhc";
		String md5Key = "616951661AE746158A78A3E3ABBD97A3";
		String type = "WAIT_TICKET";
		
		String hMac = DigestUtils.md5Hex(md5Key+merchantCode+type).toUpperCase();
		
		String reqUrl ="http://api.pub.train.dev.qunar.com/api/pub/QueryOrders.do?merchantCode=xyxhc&type=WAIT_TICKET&HMAC="+hMac;
		
		String jsonStr = HttpUtil.sendByGet(reqUrl, "GBK", "10000", "30000");
		System.out.println(jsonStr);

		
		
		return null;
	}
	
	/**
	 * 查询订单
	 */
/*	public static void main(String[] args) throws Exception {
		//临时测试代理
		System.getProperties().setProperty("http.proxyHost", "192.168.63.132");
		System.getProperties().setProperty("http.proxyPort", "6789" );
		
		String merchantCode = "xyxhc";
		String md5Key = "616951661AE746158A78A3E3ABBD97A3";
		String type = "WAIT_TICKET";
		
		String hMac = DigestUtils.md5Hex(md5Key+merchantCode+type).toUpperCase();
		
		String reqUrl ="http://api.pub.train.dev.qunar.com/api/pub/QueryOrders.do?merchantCode=xyxhc&type=WAIT_TICKET&HMAC="+hMac;
		
		String jsonStr = HttpUtil.sendByGet(reqUrl, "UTF-8", "10000", "30000");
		System.out.println(jsonStr);
		
		//String jsonStr = "{\"data\":[{\"arrStation\":\"深圳北\",\"dptStation\":\"北京西\",\"extSeat\":[{\"3\":1483}],\"orderDate\":\"2013-07-04 16:01:39\",\"orderNo\":\"xyxhc130704160139004\",\"passengers\":[{\"certNo\":\"512021200809081627\",\"certType\":\"1\",\"name\":\"雷雨婷\"}],\"seat\":{\"4\":940},\"ticketPay\":0.01,\"trainNo\":\"G71\",\"trainStartTime\":\"2013-07-23 08:00\"},{\"arrStation\":\"长沙南\",\"dptStation\":\"北京西\",\"extSeat\":[{\"4\":651}],\"orderDate\":\"2013-07-15 14:20:14\",\"orderNo\":\"xyxhc130715142014002\",\"passengers\":[{\"certNo\":\"140224198805069013\",\"certType\":\"1\",\"name\":\"刘晓敏\"}],\"seat\":{\"3\":1040},\"ticketPay\":0.01,\"trainNo\":\"G83\",\"trainStartTime\":\"2013-07-23 09:00\"},{\"arrStation\":\"石景山南\",\"dptStation\":\"北京西\",\"extSeat\":[],\"orderDate\":\"2013-07-17 18:14:29\",\"orderNo\":\"xyxhc130717181429003\",\"passengers\":[{\"certNo\":\"11\",\"certType\":\"B\",\"name\":\"啊\"}],\"seat\":{\"1\":1.5},\"ticketPay\":0.01,\"trainNo\":\"6437\",\"trainStartTime\":\"2013-08-05 17:45\"},{\"arrStation\":\"石景山南\",\"dptStation\":\"北京西\",\"extSeat\":[],\"orderDate\":\"2013-07-17 18:22:20\",\"orderNo\":\"xyxhc130717182220004\",\"passengers\":[{\"certNo\":\"11\",\"certType\":\"B\",\"name\":\"啊\"}],\"seat\":{\"1\":1.5},\"ticketPay\":0.01,\"trainNo\":\"6437\",\"trainStartTime\":\"2013-08-05 17:45\"},{\"arrStation\":\"杭州\",\"dptStation\":\"北京南\",\"extSeat\":[],\"orderDate\":\"2013-08-20 16:00:56\",\"orderNo\":\"xyxhc13082016005600a\",\"passengers\":[{\"certNo\":\"522723198102145095\",\"certType\":\"1\",\"name\":\"韩鹤轩\"}],\"seat\":{\"4\":631},\"ticketPay\":631,\"trainNo\":\"G37\",\"trainStartTime\":\"2013-09-03 14:05\"},{\"arrStation\":\"杭州\",\"dptStation\":\"北京南\",\"extSeat\":[],\"orderDate\":\"2013-08-20 16:41:22\",\"orderNo\":\"xyxhc13082016412200d\",\"passengers\":[{\"certNo\":\"522723198102145095\",\"certType\":\"1\",\"name\":\"韩鹤轩\"}],\"seat\":{\"4\":631},\"ticketPay\":631,\"trainNo\":\"G37\",\"trainStartTime\":\"2013-09-03 14:05\"},{\"arrStation\":\"石景山南\",\"dptStation\":\"北京西\",\"extSeat\":[],\"orderDate\":\"2013-08-28 14:30:31\",\"orderNo\":\"xyxhc130828143031001\",\"passengers\":[{\"certNo\":\"130922198703166414\",\"certType\":\"1\",\"name\":\"刘广泽\"}],\"seat\":{\"1\":1.5},\"ticketPay\":1,\"trainNo\":\"6437\",\"trainStartTime\":\"2013-08-29 17:45\"}],\"ret\":true,\"total\":7}";
		
		ObjectMapper mapper = new ObjectMapper();
		QunarOrderPack orderPack = mapper.readValue(jsonStr, QunarOrderPack.class);
		System.out.println(orderPack.getData().size());
	}*/
	
	/**
	 * 获取退票订单
	 */
	public static void main(String[] args) throws Exception {
		String str = "{\"errCode\":\"300\",\"errMsg\":\"同意退款操作失败!请输入退款金额;order:sjysz141231152607b71\",\"ret\":false}";
		ObjectMapper mapper = new ObjectMapper();
		QunarResult rs = mapper.readValue(str, QunarResult.class);
		System.out.println(rs.getErrMsg());
		//临时测试代理
		System.getProperties().setProperty("http.proxyHost", "192.168.63.132");
		System.getProperties().setProperty("http.proxyPort", "6789" );
		
		String merchantCode = "xyxhc";
		String md5Key = "616951661AE746158A78A3E3ABBD97A3";
		String type = "APPLY_REFUND";
		
		String hMac = DigestUtils.md5Hex(md5Key+merchantCode+type).toUpperCase();
		
		String reqUrl ="http://api.pub.train.dev.qunar.com/api/pub/QueryOrders.do?merchantCode=xyxhc&type=APPLY_REFUND&HMAC="+hMac;
		
		String jsonStr = HttpUtil.sendByGet(reqUrl, "UTF-8", "10000", "30000");
		System.out.println(jsonStr);
		
		//String jsonStr = "{\"data\":[{\"arrStation\":\"深圳北\",\"dptStation\":\"北京西\",\"extSeat\":[{\"3\":1483}],\"orderDate\":\"2013-07-04 16:01:39\",\"orderNo\":\"xyxhc130704160139004\",\"passengers\":[{\"certNo\":\"512021200809081627\",\"certType\":\"1\",\"name\":\"雷雨婷\"}],\"seat\":{\"4\":940},\"ticketPay\":0.01,\"trainNo\":\"G71\",\"trainStartTime\":\"2013-07-23 08:00\"},{\"arrStation\":\"长沙南\",\"dptStation\":\"北京西\",\"extSeat\":[{\"4\":651}],\"orderDate\":\"2013-07-15 14:20:14\",\"orderNo\":\"xyxhc130715142014002\",\"passengers\":[{\"certNo\":\"140224198805069013\",\"certType\":\"1\",\"name\":\"刘晓敏\"}],\"seat\":{\"3\":1040},\"ticketPay\":0.01,\"trainNo\":\"G83\",\"trainStartTime\":\"2013-07-23 09:00\"},{\"arrStation\":\"石景山南\",\"dptStation\":\"北京西\",\"extSeat\":[],\"orderDate\":\"2013-07-17 18:14:29\",\"orderNo\":\"xyxhc130717181429003\",\"passengers\":[{\"certNo\":\"11\",\"certType\":\"B\",\"name\":\"啊\"}],\"seat\":{\"1\":1.5},\"ticketPay\":0.01,\"trainNo\":\"6437\",\"trainStartTime\":\"2013-08-05 17:45\"},{\"arrStation\":\"石景山南\",\"dptStation\":\"北京西\",\"extSeat\":[],\"orderDate\":\"2013-07-17 18:22:20\",\"orderNo\":\"xyxhc130717182220004\",\"passengers\":[{\"certNo\":\"11\",\"certType\":\"B\",\"name\":\"啊\"}],\"seat\":{\"1\":1.5},\"ticketPay\":0.01,\"trainNo\":\"6437\",\"trainStartTime\":\"2013-08-05 17:45\"},{\"arrStation\":\"杭州\",\"dptStation\":\"北京南\",\"extSeat\":[],\"orderDate\":\"2013-08-20 16:00:56\",\"orderNo\":\"xyxhc13082016005600a\",\"passengers\":[{\"certNo\":\"522723198102145095\",\"certType\":\"1\",\"name\":\"韩鹤轩\"}],\"seat\":{\"4\":631},\"ticketPay\":631,\"trainNo\":\"G37\",\"trainStartTime\":\"2013-09-03 14:05\"},{\"arrStation\":\"杭州\",\"dptStation\":\"北京南\",\"extSeat\":[],\"orderDate\":\"2013-08-20 16:41:22\",\"orderNo\":\"xyxhc13082016412200d\",\"passengers\":[{\"certNo\":\"522723198102145095\",\"certType\":\"1\",\"name\":\"韩鹤轩\"}],\"seat\":{\"4\":631},\"ticketPay\":631,\"trainNo\":\"G37\",\"trainStartTime\":\"2013-09-03 14:05\"},{\"arrStation\":\"石景山南\",\"dptStation\":\"北京西\",\"extSeat\":[],\"orderDate\":\"2013-08-28 14:30:31\",\"orderNo\":\"xyxhc130828143031001\",\"passengers\":[{\"certNo\":\"130922198703166414\",\"certType\":\"1\",\"name\":\"刘广泽\"}],\"seat\":{\"1\":1.5},\"ticketPay\":1,\"trainNo\":\"6437\",\"trainStartTime\":\"2013-08-29 17:45\"}],\"ret\":true,\"total\":7}";
		
		QunarOrderPack orderPack = mapper.readValue(jsonStr, QunarOrderPack.class);
		System.out.println(orderPack.getData().size());
	}
	
	/**
	 * 出票结果
	 */
	/*public static void main(String[] args) throws Exception {
		//临时测试代理
		System.getProperties().setProperty("http.proxyHost", "192.168.63.132");
		System.getProperties().setProperty("http.proxyPort", "6789" );
		
		String merchantCode = "xyxhc";
		String md5Key = "616951661AE746158A78A3E3ABBD97A3";
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ticketNo", "E123456789");
		param.put("seatType", "4");
		param.put("seatNo", "5车厢2C座");
		param.put("price", 651.00);
		list.add(param);
		JSONArray jsonArray = JSONArray.fromObject(list);
		
		Map<String, Object> t = new HashMap<String, Object>();
		t.put("count", 1);

		t.put("tickets", jsonArray);
		
		JSONObject jsonObj = JSONObject.fromObject(t);
		System.out.println(jsonObj.toString());
		String tickets = jsonObj.toString();
		
		Map<String, String> map = new HashMap<String,String>();
		
		map.put("merchantCode", merchantCode);
		map.put("orderNo", "xyxhc130715142014002");
		map.put("opt", "CONFIRM");
		map.put("result", tickets);
		map.put("comment", "已经成功出票");
		String hMac = DigestUtils.md5Hex(md5Key+merchantCode+map.get("orderNo")+map.get("opt")+map.get("result")+map.get("comment")).toUpperCase();
		map.put("HMAC", hMac);
		System.out.println("hMac="+hMac);
		String reqParams = UrlFormatUtil.CreateUrl("", map, "", "UTF-8");
		System.out.println("reqParams="+reqParams);
		
		String rs = HttpUtil.sendByPost("http://api.pub.train.dev.qunar.com/api/pub/ProcessPurchase.do", reqParams, "UTF-8");
		System.out.println("rs="+rs);
	}*/
	
	/**
	 * 退票结果
	 * @throws Exception 
	 */
/*	public static void main(String[] args) throws Exception {
		//临时测试代理
		System.getProperties().setProperty("http.proxyHost", "192.168.63.132");
		System.getProperties().setProperty("http.proxyPort", "6789" );
		
		String merchantCode = "xyxhc";
		String md5Key = "616951661AE746158A78A3E3ABBD97A3";
		
		Map<String, String> map = new HashMap<String,String>();
		
		map.put("merchantCode", merchantCode);
		map.put("orderNo", "xyxhc130713200940002");
		map.put("opt", "AGREE");
		map.put("comment", "12306已经退票");
		map.put("reason", "12306已经退票");
		String hMac = DigestUtils.md5Hex(md5Key+merchantCode+map.get("orderNo")+map.get("opt")+map.get("comment")+map.get("reason")).toUpperCase();
		map.put("HMAC", hMac);
		System.out.println("hMac="+hMac);
		String reqParams = UrlFormatUtil.CreateUrl("", map, "", "UTF-8");
		System.out.println("reqParams="+reqParams);
		
		String rs = HttpUtil.sendByPost("http://api.pub.train.dev.qunar.com/api/pub/ProcessRefund.do", reqParams, "UTF-8");
		System.out.println("rs="+rs);		
	}*/
}
