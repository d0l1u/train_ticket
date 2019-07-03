package com.l9e.transaction.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.JoinUsService;
import com.l9e.transaction.vo.AgentVo;
import com.l9e.util.MD5;
import com.l9e.util.MemcachedUtil;

/**
 * 登录
 * @author zhangjun
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {
	protected static final Logger logger = Logger.getLogger(LoginController.class);
	
	@Resource
	private JoinUsService joinUsService;
		
	@RequestMapping("/trainIndex.jhtml")
	public void login(HttpServletRequest request, 
			HttpServletResponse response){
		String terminal = this.getParam(request, "terminal");
		String timestamp = this.getParam(request, "timestamp");
		String version = this.getParam(request, "version");
		String hmac = this.getParam(request, "hmac");
		String eop_order_url = this.getParam(request, "eop_order_url");
		String agentId = this.getParam(request, "agentId");
		String agentName = this.getParam(request, "agentName");
		String storeName = this.getParam(request, "storeName");
		String address = this.getParam(request, "address");
		String provinceId = this.getParam(request, "provinceId");
		String cityId = this.getParam(request, "cityId");
		String districtId = this.getParam(request, "districtId");
		String agentLevel = this.getParam(request, "agentLevel");
		
		logger.info("terminal="+terminal+"&timestamp="+timestamp+"&version="+version+"&hmac="+hmac
				+"&eop_order_url="+eop_order_url+"&agentId="+agentId+"&name="+agentName
				+"&storeName="+storeName+"&address="+address+"&provinceId="+provinceId
				+"&cityId="+cityId+"&districtId="+districtId+"&agentLevel="+agentLevel);
		
		String book_day = commonService.querySysSettingByKey("book_day_num");//官网预售天数
		String svip_book_day = commonService.querySysSettingByKey("svip_book_day");//svip预售天数
		
		// 验证参数是否为空
		if(StringUtils.isEmpty(terminal) || StringUtils.isEmpty(timestamp) || StringUtils.isEmpty(version)
				|| StringUtils.isEmpty(hmac) || StringUtils.isEmpty(eop_order_url) || StringUtils.isEmpty(agentId)
				|| StringUtils.isEmpty(agentName) || StringUtils.isEmpty(provinceId) 
				|| StringUtils.isEmpty(cityId)) {
			logger.info("输入参数错误或为空。");
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code", "PARAM_EMPTY");
			returnJson.put("message", "请求的必填参数不能为空！");
			returnJson.put("book_day", book_day);//官网预收天数
			returnJson.put("svip_book_day", svip_book_day);//svip预售天数
			printJson(response,returnJson.toString());
			return;
		}
		
		logger.info("解码前eop_order_url="+eop_order_url);
		try {
			eop_order_url = URLDecoder.decode(eop_order_url, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			logger.error("eop_order_url解码失败", e1);
		}
		logger.info("解码后eop_order_url="+eop_order_url);
		
		//验签
		String signKey="50036056ad6654b63c241e8a13949a05";
		String data = terminal+ timestamp+ version+ eop_order_url+ agentId+ agentName+
			storeName+address+provinceId+ cityId+districtId+agentLevel+signKey;
		String hmac2 = encodeMD5Hex(data);
		
		String hmac1 = "";
		try {
			hmac1 = MD5.getMd5(data, "utf-8");
		} catch (Exception e) {
			logger.error("生成签名hmac1失败", e);
		}
		logger.info("hmac="+hmac+"&hmac1="+hmac1+"&hmac2="+hmac2);
		if(!hmac1.equalsIgnoreCase(hmac)){
			logger.info("验签失败！");
			JSONObject returnJson = new JSONObject();
			returnJson.put("return_code", "SIGN_FAIL");
			returnJson.put("message", "验签失败！");
			returnJson.put("book_day", book_day);//官网预收天数
			returnJson.put("svip_book_day", svip_book_day);//svip预售天数
			printJson(response,returnJson.toString());
			return;
		}
		
		AgentVo agentVo = joinUsService.queryAgentInfo(agentId);
		
		//代理商未注册或者状态不是审核通过则跳转我要加盟
		if(agentVo == null || StringUtils.isEmpty(agentVo.getEstate()) 
				|| !TrainConsts.AGENT_ESTATE_PASSED.equals(agentVo.getEstate())){
//			logger.info("代理商需要申请开通火车票业务，agentId="+agentId);
//			JSONObject returnJson = new JSONObject();
//			returnJson.put("return_code", "FORBIDDEN");
//			returnJson.put("message", "使用PC端申请开通火车票业务后方可使用APP购票！");
//			printJson(response,returnJson.toString());
//			return;
			agentVo=new AgentVo();
			agentVo.setUser_id(agentId);
			agentVo.setUser_name(agentName);
			agentVo.setShop_name(storeName);
			agentVo.setCity_id(cityId);
			agentVo.setDistrict_id(districtId);
			agentVo.setProvince_id(provinceId);
			agentVo.setUser_level(agentLevel);
			agentVo.setUser_address(address);
			agentVo.setEstate("33");
			joinUsService.addAgentInfo(agentVo);
		}

		LoginUserInfo loginUser = new LoginUserInfo();
		loginUser.setEop_order_url(eop_order_url);
		loginUser.setAgentId(agentId);
		loginUser.setAgentName(agentName);
		loginUser.setEopAgentLevel(agentLevel);
		loginUser.setProvinceId(provinceId);
		loginUser.setCityId(cityId);
		loginUser.setDistrictId(agentLevel);
		loginUser.setTerminal("mobile");
		//用户信息放入session
		//request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER, loginUser);
		//用户信息放入memcache
		String lvcode = encodeMD5Hex(TrainConsts.INF_LOGIN_USER + System.currentTimeMillis() + agentId);
		logger.info("登陆验证lvcode=" + lvcode);
		
		MemcachedUtil.getInstance().setAttribute(lvcode, loginUser, 1000*60*30);
		
		logger.info("代理商登录火车票成功，agentId="+agentId);
		JSONObject returnJson = new JSONObject();
		returnJson.put("return_code", "SUCCESS");
		returnJson.put("message", "接入火车票业务成功！");
		returnJson.put("lvcode", lvcode);
		returnJson.put("book_day", book_day);//官网预收天数
		returnJson.put("svip_book_day", svip_book_day);//svip预售天数
		printJson(response,returnJson.toString());
		return;
		
	}
	
	public static String encodeMD5Hex(String data){
		return DigestUtils.md5Hex(data);
	}
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		
		String str = "terminal=mobile&timestamp=20140410053415&version=1.0.0&hmac=2fab77af0d4768ef245a030e328d29ac&eop_order_url=http://192.168.63.137:11330/eopInforms.do?sso_session_id=sso_account_1321_60935_20140410093628&agentId=DL20130318103126100937&name=18618000304&provinceId=110000&cityId=110100&agentLevel=3";
		
		System.out.println(encodeMD5Hex(str));
		try {
			System.out.println(MD5.getMd5(str, "utf-8"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String aaa = "http://www.baidu.com?eopInforms.do?sso_session_id=sso_account_1321_60935_20140410093628&agent_id=10001&agent_name=aaa";
		
		
		try {
			aaa = URLEncoder.encode(aaa,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(aaa);
		
		System.out.println(URLDecoder.decode(aaa,"utf-8"));
		
	}
}
