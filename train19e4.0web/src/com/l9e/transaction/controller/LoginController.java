package com.l9e.transaction.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiexun.iface.sign.InjectSign;
import com.jiexun.iface.sign.SignService;
import com.jiexun.iface.util.ASPUtil;
import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.SystemConfInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.JoinUsService;
import com.l9e.transaction.vo.AgentVo;

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
	
	@Value("#{propertiesReader[ASP_VERIFY_KEY]}")
	private String asp_verify_key;//验签key
	
/*	@Value("#{propertiesReader[ASP_BIZ_ID]}")
	private String asp_biz_id;//业务id
	
	@Value("#{propertiesReader[EOP_NOTICE_URL]}")
	private String eop_notice_url;//eop公告地址
*/	
	@Value("#{propertiesReader[iframeSetHeightPath]}")
	private String iframeSetHeightPath;//iframe高度设置路径
	
	@RequestMapping("/trainIndex.jhtml")
	public String login(HttpServletRequest request, 
			HttpServletResponse response){
		
		SignService ss = new InjectSign();
		logger.info("[验签key]asp_verify_key="+asp_verify_key);
		ASPUtil.sign(request, asp_verify_key, ss);
		if (!"SUCCESS".equalsIgnoreCase(ss.getSignResult())) {
			// 验签错误
			logger.info("[接入EOP]验签结果为"+ss.getSignResult());
			InjectSign is = (InjectSign) ss;
			logger.info("接收协议参数，" + "service：["
					+ is.getService() + "]," + "sign：[" + is.getSign() + "],"
					+ "sign_type：[" + is.getSign_type() + "]," + "timestamp：["
					+ is.getTimestamp() + "]," + "data_type：["
					+ is.getData_type() + "]," + "partner_id：["
					+ is.getPartner_id() + "]," + "input_charset：["
					+ is.getInput_charset() + "]," + "version：["
					+ is.getVersion() + "]," + "接收业务参数，" + "eop_order_url：["
					+ is.getEop_order_url() + "]," + "agent_id：["
					+ is.getAgent_id() + "]," + "agent_name：["
					+ is.getAgent_name() + "]," + "agent_level：["
					+ is.getAgent_level() + "]," + "agent_province：["
					+ is.getAgent_province() + "]," + "agent_city：["
					+ is.getAgent_city() + "]," + "agent_district：["
					+ is.getAgent_district() + "]," + "terminal：["
					+ is.getTerminal() + "]");
			request.setAttribute("errMsg", "系统错误，请稍后再试！");
			return "/common/error";
		}

		InjectSign is = (InjectSign) ss;

		logger.info("19e门户进入ASP首页面，" + "接收协议参数，" + "service：["
				+ is.getService() + "]," + "sign：[" + is.getSign() + "],"
				+ "sign_type：[" + is.getSign_type() + "]," + "timestamp：["
				+ is.getTimestamp() + "]," + "data_type：["
				+ is.getData_type() + "]," + "partner_id：["
				+ is.getPartner_id() + "]," + "input_charset：["
				+ is.getInput_charset() + "]," + "version：["
				+ is.getVersion() + "]," + "接收业务参数，" + "eop_order_url：["
				+ is.getEop_order_url() + "]," + "agent_id：["
				+ is.getAgent_id() + "]," + "agent_name：["
				+ is.getAgent_name() + "]," + "agent_level：["
				+ is.getAgent_level() + "]," + "agent_province：["
				+ is.getAgent_province() + "]," + "agent_city：["
				+ is.getAgent_city() + "]," + "agent_district：["
				+ is.getAgent_district() + "]," + "terminal：["
				+ is.getTerminal() + "]");
		
		logger.info("dealerinfo|id="+is.getAgent_id()+"&name="+is.getAgent_name()
				+"&provinceId="+is.getAgent_province()+"&cityId="+is.getAgent_city());

		// 验证参数是否为空
		if (StringUtils.isEmpty(is.getAgent_city())
				|| StringUtils.isEmpty(is.getAgent_province())
				|| StringUtils.isEmpty(is.getAgent_id())) {
			logger.info("EOP进入ASP系统，参数不正确：" + "用户ID：["
					+ is.getAgent_city() + "]," + "省份编号：["
					+ is.getAgent_province() + "],"
					+ "城市编号：[" + is.getAgent_id() + "]");

			request.setAttribute("errMsg", "系统错误，请稍后再试！");
			return "/common/error";
		}

		LoginUserInfo loginUser = new LoginUserInfo();
		loginUser.setEop_order_url(is.getEop_order_url());
		loginUser.setAgentId(is.getAgent_id());
		loginUser.setAgentName(is.getAgent_name());
		loginUser.setEopAgentLevel(is.getAgent_level());
		loginUser.setProvinceId(is.getAgent_province());
		loginUser.setCityId(is.getAgent_city());
		loginUser.setDistrictId(is.getAgent_district());
		loginUser.setTerminal(is.getTerminal());
		//用户信息放入session
		request.getSession().setAttribute(TrainConsts.INF_LOGIN_USER, loginUser);
		
		request.getSession().setAttribute("iframeSetHeightPath", iframeSetHeightPath);
		
/*		Map<String, String> oldAreaMap = commonService.queryOldAreaInfo(is.getAgent_province());
		StringBuffer ad = new StringBuffer();
		ad.append(eop_notice_url)
		  .append("?provinceId=")
		  .append(oldAreaMap.get("area_oldno"))
		  .append("&serviceId=")
		  .append(asp_biz_id);
			
		request.getSession().setAttribute("eopAdUrl", ad.toString());//EOP公告
*/		
		//系统配置信息
		SystemConfInfo sysConf = this.getSysConf(is.getAgent_province());
		
		//request.getSession().setAttribute(TrainConsts.INF_SYS_CONF, sysConf);
		
		//区域未开通则跳转到未开通页面
		if(StringUtils.isEmpty(sysConf.getIs_open())
				|| TrainConsts.AREA_OPEN_NO.equals(sysConf.getIs_open())){
			request.getSession().setAttribute("menuDisplay", "bookOnly");
			request.getSession().setAttribute("showJm_Gg", "2");//不显示
			return "redirect:/buyTicket/bookIndex.jhtml";
		}else{
			request.getSession().setAttribute("showJm_Gg", "1");//显示
		}
		
		AgentVo agentVo = joinUsService.queryAgentInfo(is.getAgent_id());
		
		//代理商未注册或者状态不是审核通过则跳转我要加盟
		if(agentVo == null || StringUtils.isEmpty(agentVo.getEstate()) 
				|| !TrainConsts.AGENT_ESTATE_PASSED.equals(agentVo.getEstate())){
			
			request.getSession().setAttribute("menuDisplay", "bookAndJoin");//只显示我要加盟菜单
			return "redirect:/joinUs/joinIndex.jhtml";
		}else if(agentVo.getUser_level().equals("3")){//对外商户账号--世纪
			request.getSession().setAttribute("menuDisplay", "shiji");//显示世纪菜单
			return "redirect:/extShiji/queryExtShijiOrderList.jhtml";
		}
		
		request.getSession().setAttribute("menuDisplay", "all");//显示全部菜单
		
		
		if(agentVo == null || StringUtils.isEmpty(agentVo.getEstate()))//未开通
			request.getSession().setAttribute("status", "nozhuce");
		if(TrainConsts.AGENT_ESTATE_PASSED.equals(agentVo.getEstate()))//审核通过
			request.getSession().setAttribute("status", "passed");
		if(TrainConsts.AGENT_ESTATE_NOT.equals(agentVo.getEstate()))//审核未通过
			request.getSession().setAttribute("status", "nopass");
		if(TrainConsts.AGENT_ESTATE_WAITING.equals(agentVo.getEstate()))//等待审核
			request.getSession().setAttribute("status", "waiting");
		
		return "redirect:/buyTicket/bookIndex.jhtml";
	}
}
