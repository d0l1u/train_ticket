package com.l9e.transaction.controller;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiexun.iface.bean.OrderBean;
import com.jiexun.iface.util.ASPUtil;
import com.l9e.common.BaseController;
import com.l9e.common.LoginUserInfo;
import com.l9e.common.SystemConfInfo;
import com.l9e.common.TrainConsts;
import com.l9e.transaction.service.JoinUsService;
import com.l9e.transaction.service.UserInfoService;
import com.l9e.transaction.vo.AgentRegistInfo;
import com.l9e.transaction.vo.AgentRegistVo;
import com.l9e.transaction.vo.AgentVo;
import com.l9e.transaction.vo.OrderInfo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.ProductVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.PageUtil;

/**
 * 我要加盟
 * @author zhangjun
 *
 */
@Controller
@RequestMapping("/joinUs")
public class JoinUsController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(JoinUsController.class);
	
	private static final int PAGE_SIZE = 10;//每页显示的条数
	
	@Resource
	private JoinUsService joinUsService;
	
	@Value("#{propertiesReader[ASP_ID]}")
	private String asp_id;
	
	@Value("#{propertiesReader[ASP_BIZ_ID]}")
	private String asp_biz_id;//业务id
	
	@Value("#{propertiesReader[ASP_PRODUCT_ID]}")
	private String asp_product_id;//商品ID
	
	@Value("#{propertiesReader[ASP_VERIFY_KEY]}")
	private String asp_verify_key;//验签key
	
	@Value("#{propertiesReader[PAY_RESULT_BACK_NOTIFY_URL]}")
	private String pay_result_back_notify_url;//支付结果后台通知地址
	
	@Value("#{propertiesReader[PAY_RESULT_FRONT_NOTIFY_URL]}")
	private String pay_result_front_notify_url;//支付结果前台页面地址
	
	@Value("#{propertiesReader[ORDER_DETAIL_URL]}")
	private String order_detail_url;//订单详情连接地址
	
	@Value("#{propertiesReader[REFUND_RESULT_NOTIFY_URL]}")
	private String refund_result_notify_url;//退款完成通知地址
	
	/**
	 * 进入个人中心首页
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/joinIndex.jhtml")
	public String joinIndex(HttpServletRequest request, 
			HttpServletResponse response){
		
		LoginUserInfo loginUser = this.getLoginUser(request);
		String cityId = loginUser.getCityId();
		String agentId = loginUser.getAgentId();
		String agentName = loginUser.getAgentName();
		
		/**处理代理商的金牌银牌铜牌数据，得到代理商等级*/
		List<Map<String,String>> userInfoList=joinUsService.queryAgentRegisterInfo(agentId);
		request.setAttribute("registList", userInfoList);
		String agentLevel=this.getAgentLevel(userInfoList);
		request.setAttribute("agentLevel", agentLevel);
		
		
		AgentVo agentVo = joinUsService.queryAgentInfo(agentId);
		if(agentVo==null){
			//未开通
			request.setAttribute("realPass", "0");
		}else{
			//已开通
			request.setAttribute("realPass", "1");
		}
		if(agentVo == null){//无代理商信息则显示注册页面
			List<Map<String, String>> districtList = joinUsService.queryDistrictList(cityId);
			request.setAttribute("agentName", agentName);
			request.setAttribute("districtList", districtList);
			return "join/joinInput";
			
		}else if(TrainConsts.AGENT_ESTATE_NEED_PAY.equals(agentVo.getEstate())){//需要付费
			ProductVo productVo = new ProductVo();
			productVo.setType(TrainConsts.PRODUCT_TYPE_0);
			List<ProductVo> productList = commonService.queryProductInfoList(productVo);
			request.setAttribute("productList", productList);
			request.setAttribute("agentVo", agentVo);//代理商信息
			request.setAttribute("saleTypeMap", TrainConsts.getSaleType());//计费方式
			request.setAttribute("shopTypeMap", TrainConsts.getShopType());//店铺类别
			return "join/buyProduct";
			
		}else if(TrainConsts.AGENT_ESTATE_WAITING.equals(agentVo.getEstate())){//等待审核
			request.setAttribute("pay_status", "SUCCESS");
			this.queryOwnProductInfo(request, agentVo);
			return "join/verifying";
			
		}else if(TrainConsts.AGENT_ESTATE_NOT.equals(agentVo.getEstate())){//审核未通过
			this.queryOwnProductInfo(request, agentVo);
			this.queryJmOrderStatus(request, agentVo);
			return "join/verifyed";
			
		}else if(TrainConsts.AGENT_ESTATE_PASSED.equals(agentVo.getEstate())){//审核通过
			request.setAttribute("pay_status", "SUCCESS");
			this.queryOwnProductInfo(request, agentVo);
			return "join/verifyed";
			
		}else if(TrainConsts.AGENT_ESTATE_NEED_REPAY.equals(agentVo.getEstate())){//需要续费
			ProductVo productVo = new ProductVo();
			productVo.setType(TrainConsts.PRODUCT_TYPE_0);
			List<ProductVo> productList = commonService.queryProductInfoList(productVo);
			request.setAttribute("productList", productList);
			request.setAttribute("agentVo", agentVo);//代理商信息
			request.setAttribute("shopTypeMap", TrainConsts.getShopType());//店铺类别
			request.setAttribute("saleTypeMap", TrainConsts.getSaleType());//计费方式
			request.setAttribute("buyType", "repay_sys");//被动续费
			return "join/buyProduct";
		}
		return null;
	}
	
	/**
	 * 	跳转到个人信息页面
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping("/agentInfoIndex.jhtml")
	public String gotoAgentInfoPage(HttpServletRequest request,HttpServletResponse response){
		logger.info("跳转到个人信息页面");
		LoginUserInfo loginUser = this.getLoginUser(request);
		String agentId = loginUser.getAgentId();
		
		AgentVo agentVo = joinUsService.queryAgentInfo(agentId);
		
		if(agentVo==null){
			//无数据未开通
			request.setAttribute("realPass", "0");
			return "join/joinInput";
		}else{
			//有数据
			request.setAttribute("realPass", "1");
			/**处理代理商的金牌银牌铜牌数据，得到代理商等级*/
			List<Map<String,String>> userInfoList=joinUsService.queryAgentRegisterInfo(agentId);
			request.setAttribute("registList", userInfoList);
			String agentLevel=this.getAgentLevel(userInfoList);
			
			request.setAttribute("agentEstateMap", TrainConsts.getAgentEstate());
			request.setAttribute("agentLevel", agentLevel);
			request.setAttribute("agentVo", agentVo);
			return "join/agentInfo";
		}
	
		
	}
	
	//跳转到实名认证页面
	@RequestMapping("/realNameAuth.jhtml")
	public String gotoRealNamePage(HttpServletRequest request, 
			HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		String agentId = loginUser.getAgentId();
		
		//用来判断是否是从我要加盟页面点下一步跳转过来的，0表示是 
		String realName=this.getParam(request, "realName");
		
		//用来判断代理商是否已经开通
		AgentVo agentVo = joinUsService.queryAgentInfo(agentId);
		if(agentVo==null){
			//无数据未开通
			request.setAttribute("realPass", "0");
		}else{
			//有数据
			request.setAttribute("realPass", "1");
		}
		request.setAttribute("agentVo", agentVo);
		/**
		AgentVo agentVo = joinUsService.queryAgentInfo(agentId);
		if(agentVo == null){//无代理商信息则显示注册页面
			List<Map<String, String>> districtList = joinUsService.queryDistrictList(cityId);
			request.setAttribute("agentName", agentName);
			request.setAttribute("districtList", districtList);
			return "join/joinInput";
		}else if(TrainConsts.AGENT_ESTATE_NEED_PAY.equals(agentVo.getEstate())){//需要付费
			ProductVo productVo = new ProductVo();
			productVo.setType(TrainConsts.PRODUCT_TYPE_0);
			List<ProductVo> productList = commonService.queryProductInfoList(productVo);
			request.setAttribute("productList", productList);
			request.setAttribute("agentVo", agentVo);//代理商信息
			request.setAttribute("saleTypeMap", TrainConsts.getSaleType());//计费方式
			request.setAttribute("shopTypeMap", TrainConsts.getShopType());//店铺类别
			return "join/buyProduct";
		}else if(TrainConsts.AGENT_ESTATE_WAITING.equals(agentVo.getEstate())){//等待审核
			request.setAttribute("pay_status", "SUCCESS");
			this.queryOwnProductInfo(request, agentVo);
			return "join/verifying";
		}else if(TrainConsts.AGENT_ESTATE_NOT.equals(agentVo.getEstate())){//审核未通过
			this.queryOwnProductInfo(request, agentVo);
			this.queryJmOrderStatus(request, agentVo);
			return "join/verifyed";
		}
		*/
		List<Map<String,String>> userInfoList=joinUsService.queryAgentRegisterInfo(agentId);
		request.setAttribute("registList", userInfoList);
		/**
		 * 处理代理商的金牌银牌铜牌数据，实现每次跳转到实名认证页面都刷新其等级
		 */
		Map<String,String> numMap=this.getAgentData(userInfoList);
		request.getSession().setAttribute("numMap", numMap);
		//end
		if("0".equals(realName)){
			return "join/joinUsRealName";
		}else{
			return "join/realNameAuthentication";
		}
	
	}
	/**
	 * 进入主动续费页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/gotoRepayPage.jhtml")
	public String gotoRepayPage(HttpServletRequest request, 
			HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		String agentId = loginUser.getAgentId();
		
		AgentVo agentVo = joinUsService.queryAgentInfo(agentId);
		ProductVo productVo = new ProductVo();
		productVo.setType(TrainConsts.PRODUCT_TYPE_0);
		List<ProductVo> productList = commonService.queryProductInfoList(productVo);
		request.setAttribute("productList", productList);
		request.setAttribute("agentVo", agentVo);//代理商信息
		request.setAttribute("shopTypeMap", TrainConsts.getShopType());//店铺类别
		request.setAttribute("saleTypeMap", TrainConsts.getSaleType());//计费方式
		request.setAttribute("buyType", "repay_hum");//主动续费
		return "join/buyProduct";
	}
	
	/**
	 * 查询已购买产品信息
	 * @param agentVo
	 */
	private void queryOwnProductInfo(HttpServletRequest request,
			AgentVo agentVo){
		//非免费用户则查询购买的产品信息
		if(agentVo != null && !StringUtils.isEmpty(agentVo.getProduct_id()) 
				&& !TrainConsts.USER_LEVEL_FREE.equals(agentVo.getUser_level())){
			ProductVo productVo = new ProductVo();
			productVo.setProduct_id(agentVo.getProduct_id());
			List<ProductVo> productList = commonService.queryProductInfoList(productVo);
			if(productList != null && productList.size() > 0){
				request.setAttribute("productVo", productList.get(0));
				request.setAttribute("saleTypeMap", TrainConsts.getSaleType());//计费方式
			}
		}
		request.setAttribute("agentEstateMap", TrainConsts.getAgentEstate());//审核状态
		request.setAttribute("agentVo", agentVo);//代理商信息
		request.setAttribute("shopTypeMap", TrainConsts.getShopType());//店铺类别
	}
	
	/**
	 * 查询加盟订单状态
	 * @param request
	 * @param agentVo
	 */
	private void queryJmOrderStatus(HttpServletRequest request,
			AgentVo agentVo){
		String pay_status = "FAIL";
		//非免费用户需查询退款结果
		if(!TrainConsts.USER_LEVEL_FREE.equals(agentVo.getUser_level()) 
				&& !StringUtils.isEmpty(agentVo.getProduct_id()) 
				&& !StringUtils.isEmpty(agentVo.getJm_order_id())){
			Map<String, String> map = new HashMap<String, String>();
			map.put("order_id", agentVo.getJm_order_id());
			map.put("user_id", agentVo.getUser_id());
			map.put("product_id", agentVo.getProduct_id());
			Map<String, String> jmOrder = joinUsService.queryJmOrderInfo(map);
			String order_status = jmOrder.get("order_status");
			
			//加盟状态 00、预下单 11、支付成功 12、EOP发货完成 22、支付失败 33、等待退款 34、EOP正在退款 44、退款成功 55、退款失败
			if(jmOrder == null || StringUtils.isEmpty(order_status)){
				logger.info("代理商加盟订单信息不一致，存在异常，user_id="+ agentVo.getUser_id());
			}else if("11".equals(order_status) || "12".equals(order_status)){
				pay_status = "SUCCESS";
			}else if("33".equals(order_status) || "34".equals(order_status)){
				pay_status = "REFUNDING";
			}else if("44".equals(order_status)){
				pay_status = "REFUNDED";
			}
		}
		request.setAttribute("pay_status", pay_status);
	}
	
	/**
	 * 注册用户信息
	 * @param agentVo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addJoinInfo.jhtml")
	public String addJoinInfo(AgentVo agentVo, HttpServletRequest request, 
			HttpServletResponse response){
		
		LoginUserInfo loginUser = this.getLoginUser(request);
		String provinceId = loginUser.getProvinceId();
		String cityId = loginUser.getCityId();
		String agentId = loginUser.getAgentId();
		String shop_name = loginUser.getAgentName();
		
		//系统配置信息
		SystemConfInfo sysConf = this.getSysConf(provinceId);
		
		//地区是否需要付费（默认付费）
		if(StringUtils.isEmpty(sysConf.getIs_cost()) 
				|| TrainConsts.AREA_COST_YES.equals(sysConf.getIs_cost())){
			agentVo.setEstate(TrainConsts.AGENT_ESTATE_NEED_PAY);//需要付费
		}else{
			agentVo.setUser_level(TrainConsts.USER_LEVEL_FREE);
			agentVo.setEstate(TrainConsts.AGENT_ESTATE_WAITING);//等待审核
		}
		agentVo.setProvince_id(provinceId);
		agentVo.setCity_id(cityId);
		agentVo.setUser_id(agentId);
		agentVo.setShop_name(shop_name);//店铺名称
		
		String shop_short_name = this.getParam(request, "shop_short_name");
		agentVo.setShop_short_name("定制短信(左侧说明)".equals(shop_short_name)?"":shop_short_name);
		
		joinUsService.addAgentInfo(agentVo);
		
		if(TrainConsts.AGENT_ESTATE_NEED_PAY.equals(agentVo.getEstate())){//需要付费
			return "redirect:/joinUs/goToBuyProduct.jhtml";
		}else{//等待审核
			//return "redirect:/joinUs/joinIndex.jhtml";
			//加盟时跳转到实名认证页面
			return "redirect:/joinUs/realNameAuth.jhtml?realName=0";
		}
	}
	
	/**
	 * 增加实名验证联系人
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/submitRegistInfo.jhtml")
	public void submitRegistInfo(AgentRegistInfo registInfo,HttpServletRequest request, 
			HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		String agentId = loginUser.getAgentId();
		List<AgentRegistVo> registInfoList = registInfo.getRegistInfoList();
		PrintWriter out = null;
		try{
			out = response.getWriter();
			if(registInfoList!=null){
				for(AgentRegistVo registVo:registInfoList){
					if("".equals(registVo.getUser_name()) && "".equals(registVo.getIds_card())
							&& "".equals(registVo.getUser_phone())){
						continue;
					}
					if(registVo == null){
						continue;
					}
					registVo.setUser_id(agentId);
					AgentRegistVo oldRegisterVo = new AgentRegistVo();
					Map<String, String> map = new HashMap<String,String>();
					if("".equals(registVo.getRegist_id()) || registVo.getRegist_id()==null){
						map.put("regist_id", registVo.getRegist_id());
						map.put("user_id", agentId);
						map.put("ids_card", registVo.getIds_card());
					}else{
						map.put("regist_id", registVo.getRegist_id());
						map.put("user_id", agentId);
					}
					oldRegisterVo = joinUsService.queryAgentRegistInfo(map);
					if(oldRegisterVo==null){
						joinUsService.addAgentRegistInfo(registVo);
					}else{
						joinUsService.updateAgentRegistInfo(registVo);

					}
				}
			}
			JSONObject json = new JSONObject();
			json.put("result", "SUCCESS");
			out.write(json.toString());
		}catch(Exception e){
			logger.error("提交实名验证联系人异常", e);
			out.write("FAILURE");
		}
	}
	
	/**
	 * 当用户刚开始加盟时增加实名验证联系人，添加完成后跳转到显示用户加盟信息界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/firstSubmitRegistInfo.jhtml")
	public String submitRegistInfos(AgentRegistInfo registInfo,HttpServletRequest request, 
			HttpServletResponse response){
		logger.info("刚开始加盟时添加实名验证联系人！");
		LoginUserInfo loginUser = this.getLoginUser(request);
		String agentId = loginUser.getAgentId();
		List<AgentRegistVo> registInfoList = registInfo.getRegistInfoList();
		try{
			if(registInfoList!=null){
				for(AgentRegistVo registVo:registInfoList){
					if("".equals(registVo.getUser_name()) && "".equals(registVo.getIds_card())
							&& "".equals(registVo.getUser_phone())){
						continue;
					}
					if(registVo == null){
						continue;
					}
					registVo.setUser_id(agentId);
					AgentRegistVo oldRegisterVo = new AgentRegistVo();
					Map<String, String> map = new HashMap<String,String>();
					if("".equals(registVo.getRegist_id()) || registVo.getRegist_id()==null){
						map.put("regist_id", registVo.getRegist_id());
						map.put("user_id", agentId);
						map.put("ids_card", registVo.getIds_card());
					}else{
						map.put("regist_id", registVo.getRegist_id());
						map.put("user_id", agentId);
					}
					oldRegisterVo = joinUsService.queryAgentRegistInfo(map);
					if(oldRegisterVo==null){
						joinUsService.addAgentRegistInfo(registVo);
					}else{
						joinUsService.updateAgentRegistInfo(registVo);

					}
				}
			}
		}catch(Exception e){
			logger.error("提交实名验证联系人异常", e);
		}
		return "redirect:/joinUs/joinIndex.jhtml";

	}
	
	
	//删除实名验证联系人
	@RequestMapping("/deleteRegistInfo.jhtml")
	public void deleteRegistInfo(HttpServletRequest request, 
			HttpServletResponse response){
		PrintWriter out = null;
		try{
			out = response.getWriter();
			LoginUserInfo loginUser = this.getLoginUser(request);
			String agentId = loginUser.getAgentId();
			String regist_id = request.getParameter("regist_id");
			Map<String, String> map = new HashMap<String,String>();
			map.put("user_id", agentId);
			map.put("regist_id", regist_id);
			joinUsService.deleteAgentRegistInfo(map);
			out.write("SUCCESS");
		}catch(Exception e){
			logger.error("删除实名验证联系人异常！", e);
			out.write("FAILURE");
		}
	}
	
	/**
	 * 进入购买产品页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/goToBuyProduct.jhtml")
	public String goToBuyProduct(HttpServletRequest request, 
			HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		String agentId = loginUser.getAgentId();
		AgentVo agentVo = joinUsService.queryAgentInfo(agentId);
		ProductVo productVo = new ProductVo();
		productVo.setType(TrainConsts.PRODUCT_TYPE_0);
		List<ProductVo> productList = commonService.queryProductInfoList(productVo);
		request.setAttribute("productList", productList);
		request.setAttribute("saleTypeMap", TrainConsts.getSaleType());//计费方式
		request.setAttribute("agentVo", agentVo);
		request.setAttribute("shopTypeMap", TrainConsts.getShopType());//店铺类别
		return "join/buyProduct";
		
	}
	
	/**
	 * 生成购买产品订单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/createJmOrder.jhtml")
	public String createJmOrder(HttpServletRequest request, 
			HttpServletResponse response){
		
		LoginUserInfo loginUser = this.getLoginUser(request);
		String agentId = loginUser.getAgentId();
		String eop_order_url = loginUser.getEop_order_url();//eop下单地址
		String productId = this.getParam(request, "productId");
		
		ProductVo productVo = new ProductVo();
		productVo.setProduct_id(productId);
		List<ProductVo> productList = commonService.queryProductInfoList(productVo);
		
		String pay_money = String.valueOf(productList.get(0).getSale_price());
		Map<String, Object> jmMap = new HashMap<String, Object>();
		String asp_order_id = CreateIDUtil.createID("JM");
		jmMap.put("order_id", asp_order_id);
		jmMap.put("user_id", agentId);
		jmMap.put("product_id", productId);
		jmMap.put("order_status", "00");//预下单
		jmMap.put("pay_money", pay_money);//支付金额
		
		joinUsService.addJmOrder(jmMap);
		
		//EOP下单
		OrderBean bean = new OrderBean();
		bean.setAsp_verify_key(asp_verify_key);
		bean.setPartner_id(asp_id);
		bean.setAgent_id(agentId);
		bean.setAsp_order_id(asp_order_id);
		bean.setOrder_name("火车票加盟订单");
		bean.setBiz_id(asp_biz_id);
		bean.setGoods_id(asp_product_id);
		bean.setGoods_name(productList.get(0).getName());
		bean.setBuy_cnt("1");//eop商品数量
		bean.setGoods_price(pay_money);//eop商品单价
		bean.setOrder_price(pay_money);//eop订单售价
		bean.setAgent_get_monney("0");//网点分润
		
		//支付结果通知地址
		StringBuffer payResultNotifyUrl = new StringBuffer();
		payResultNotifyUrl.append(pay_result_back_notify_url)
						  .append("?asp_order_id=")
						  .append(asp_order_id)
						  .append("&asp_agent_id=")
						  .append(agentId)
						  .append("&asp_product_id=")
						  .append(productId)
						  .append("&asp_order_type=");
		String buyType = this.getParam(request, "buyType");
		if(!StringUtils.isEmpty(buyType) 
				&& "repay_sys".equals(buyType)){//被动续费购买产品
			payResultNotifyUrl.append("jmxf_sys");
		}else if("repay_hum".equals(buyType)){//主动续费购买产品
			payResultNotifyUrl.append("jmxf_hum");
		}else{
			payResultNotifyUrl.append("jm");//首次购买产品
		}
		
		
		//支付结果前台页面地址
		StringBuffer dealResultUrl = new StringBuffer();
		dealResultUrl.append(pay_result_front_notify_url)
					 .append("?asp_order_id=")
					 .append(asp_order_id)
					 .append("&asp_agent_id=")
					 .append(agentId)
					 .append("&asp_product_id=")
					 .append(productId)
					 .append("&asp_order_type=")
					 .append("jm");
		
		//订单详情连接地址
		StringBuffer orderDetailUrl = new StringBuffer();
		orderDetailUrl.append(order_detail_url)
					  .append("?asp_order_id=")
				      .append(asp_order_id);
		
		StringBuffer refundResultNotifyUrl = new StringBuffer();
		refundResultNotifyUrl.append(refund_result_notify_url)
							 .append("?asp_order_type=")
							 .append("jm");
		
		bean.setPay_result_notify_url(payResultNotifyUrl.toString());//支付结果通知地址
		bean.setDeal_result_url(dealResultUrl.toString());//支付结果前台页面地址
		bean.setOrder_detail_url(orderDetailUrl.toString());////订单详情连接地址
		bean.setAsp_refund_url(refundResultNotifyUrl.toString());
		
		StringBuffer eopOrderUrl = new StringBuffer();
		eopOrderUrl.append(eop_order_url)
				   .append((eop_order_url.indexOf("?")!=-1) ? "?data_type=JSON" : "&data_type=JSON");
		ASPUtil.createOrder(bean, eopOrderUrl.toString());
		
		if(StringUtils.isEmpty(bean.getResult_code())){//状态码为空
			logger.info("【下单接口】EOP返回的接口码为空，下单失败！" + "ASP订单号" + asp_order_id);
			
		}else if("SUCCESS".equalsIgnoreCase(bean.getResult_code())){//下单成功
			logger.info("【下单接口】EOP下单成功，EOP订单号" + bean.getEop_order_id());
			Map<String, String> eopInfo = new HashMap<String, String>();
			eopInfo.put("eop_order_id", bean.getEop_order_id());//EOP订单号
			eopInfo.put("pay_url", bean.getPay_url());//支付地址
			eopInfo.put("query_result_url", bean.getQuery_result_url());//支付结果查询地址
			eopInfo.put("asp_order_id", asp_order_id);//ASP订单号
			
			commonService.addOrderEopInfo(eopInfo);//添加EOP下单信息
			
		}else if("AGENT_LOGIN_OUT".equalsIgnoreCase(bean.getResult_code())){//代理商登录失效
			logger.info("【下单接口】代理商登录失效，下单失败！" + "ASP订单号" + asp_order_id);
			Map<String, String> errMap = new HashMap<String, String>();
			errMap.put("AGENT_LOGIN_OUT", "AGENT_LOGIN_OUT");//代理商登录失效
			return "common/sessionTimeOut";
			
		}else{//其他
			logger.info("【下单接口】错误码为" + bean.getResult_code() + "，下单失败！" + "ASP订单号" + asp_order_id);
		}
		
		
		return "redirect:/joinUs/jmOrderConfirm.jhtml?asp_order_id="
				+asp_order_id+"&productId="+productId;
	}
	
	/**
	 * 加盟下单重定向查询
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/jmOrderConfirm.jhtml")
	public String jmOrderConfirm(HttpServletRequest request, 
			HttpServletResponse response){
		LoginUserInfo loginUser = this.getLoginUser(request);
		String agentId = loginUser.getAgentId();
		String productId = this.getParam(request, "productId");
		String asp_order_id = this.getParam(request, "asp_order_id");
		
		AgentVo agentVo = joinUsService.queryAgentInfo(agentId);
		
		ProductVo productVo = new ProductVo();
		productVo.setProduct_id(productId);
		List<ProductVo> productList = commonService.queryProductInfoList(productVo);
		Map<String, String> eopInfo = commonService.queryEopInfo(asp_order_id);
		
		request.setAttribute("productList", productList);
		request.setAttribute("saleTypeMap", TrainConsts.getSaleType());//计费方式
		request.setAttribute("agentEstateMap", TrainConsts.getAgentEstate());//审核状态
		request.setAttribute("agentVo", agentVo);//代理商信息
		request.setAttribute("shopTypeMap", TrainConsts.getShopType());//店铺类别
		request.setAttribute("pay_url", eopInfo.get("pay_url"));//支付地址
		return "join/buyProduct";
	}
	
	
	/**
	 * 根据代理商的联系人情况，返回其铜牌金牌银牌数量的map，
	 * @param request
	 * @param userInfoList
	 * @return
	 */

	private Map<String,String> getAgentData(List<Map<String,String>> userInfoList){
		//已通过数量
		int passNum =0;
		//审核中数量
		int waitNum=0;
		//未通过数量
		int noNum=0;
		String passName="";
		String waitName="";
		String noName="";
		if(userInfoList!=null && userInfoList.size()>0){
			//弹窗实名标记
			for(int i=0;i<userInfoList.size();i++){
				String registStatus=userInfoList.get(i).get("regist_status").trim();
				if("22".equals(registStatus)){
					if(passName.length()==0){
						passName=userInfoList.get(i).get("user_name").trim();
					}
					passNum=passNum+1;
				}else if("00".equals(registStatus)|| "11".equals(registStatus)||"55".equals(registStatus)||"44".equals(registStatus)){
					if(waitName.length()==0){
						waitName=userInfoList.get(i).get("user_name").trim();
					}
					waitNum=waitNum+1;
				}else if("33".equals(registStatus)){
					if(noName.length()==0){
						noName=userInfoList.get(i).get("user_name").trim();
					}
					noNum=noNum+1;
				}
			}
		}else{
			logger.info("该代理商还未添加联系人！");
		}
		Map<String,String> numMap=new HashMap<String,String>();
		numMap.put("passNum", passNum+"");
		numMap.put("waitNum", waitNum+"");
		numMap.put("noNum", noNum+"");
		numMap.put("passName", passName);
		numMap.put("waitName", waitName);
		numMap.put("noName", noName);
		return numMap;
	}
	
	/**
	 * 根据代理商的联系人情况，返回他的用户等级，0 铜牌 1 银牌 2 金牌 3未通过 4 审核中 5 未认证
	 * @param request
	 * @param userInfoList
	 * @return
	 */

	private String getAgentLevel(List<Map<String,String>> userInfoList){
		//已通过数量
		int passNum =0;
		//审核中数量
		int waitNum=0;
		//未通过数量
		int noNum=0;
		if(userInfoList!=null && userInfoList.size()>0){
			//弹窗实名标记
			for(int i=0;i<userInfoList.size();i++){
				String registStatus=userInfoList.get(i).get("regist_status").trim();
				if("22".equals(registStatus)){
					passNum=passNum+1;
				}else if("00".equals(registStatus)|| "11".equals(registStatus)||"55".equals(registStatus)||"44".equals(registStatus)){
					waitNum=waitNum+1;
				}else if("33".equals(registStatus)){
					noNum=noNum+1;
				}
			}
		}else{
			logger.info("该代理商还未添加联系人！");
			return "5";
		}
		if(passNum>0){
			if(passNum==1){
				return "0"; //铜牌
			}else if(passNum>=2 && passNum<=4){
				return "1"; //银牌
			}else {
				return "2"; //金牌
			}
		}else if(noNum>0){
			return "3"; //未通过
		}else if(waitNum>0){
			return "4"; //审核中
		}else{
			return "";
		}
	}
	
}
