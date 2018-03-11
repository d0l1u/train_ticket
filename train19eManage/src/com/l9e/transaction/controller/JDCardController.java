package com.l9e.transaction.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.JDCardService;
import com.l9e.transaction.vo.JdVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.PageUtil;

/**
 * 京东预付卡控制层
 * @author wangsf01
 *
 */

@Controller
@RequestMapping("jdCard")
public class JDCardController extends BaseController{
	
	private static final Logger logger = 
		Logger.getLogger(JDCardController.class);
	
	@Resource
	private JDCardService jdCardService;

	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryJDCardPage.do")
	public String queryJDCardPage(HttpServletRequest request,
			HttpServletResponse response){

		return "redirect:/jdCard/queryJDCardList.do?card_status=00";
	}
	
	/**
	 * 查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryJDCardList.do")
	public String queryJDCardList(HttpServletRequest request,
			HttpServletResponse response){
		/******************查询条件********************/
		String card_no=this.getParam(request, "card_no");//京东预付卡卡号
		String beginBalance=this.getParam(request, "beginBalance");
		String endBalance=this.getParam(request, "endBalance");
		
		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
		if(StringUtils.isEmpty(begin_info_time)){
			Calendar theCa = Calendar.getInstance(); 
			theCa.setTime(new Date());  
			theCa.add(theCa.DATE, -90); 
			Date date = theCa.getTime();
			DateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
			String querydate=dff.format(date);
			begin_info_time = querydate;
		}
		
		String end_info_time = this.getParam(request, "end_info_time");//结束时间

		List<String> card_StatusList = this.getParamToList(request, "card_status");//预付卡状态

		List<String> card_status = new ArrayList<String>(card_StatusList);
		if(card_no.trim().length()<=0){
			request.setAttribute("card_status", card_status.toString());
		}
	
		/**
		 * 查询出预付卡的总金额和总余额
		 */
		List<Map<String,BigDecimal>> jdCardMoneyList = jdCardService.queryJDCardMoney();

		String total_money="";
		String total_balance="";
		
		
		Map<String,BigDecimal> moneyMap =new HashMap<String,BigDecimal>();

		moneyMap = jdCardMoneyList.get(0);
		if(moneyMap.get("total_money") != null){
		total_money = Double.toString(moneyMap.get("total_money").doubleValue());
		}
		
		if(moneyMap.get("total_balance") != null){
		total_balance = Double.toString(moneyMap.get("total_balance").doubleValue());
		}

		
		/******************查询Map********************/
		Map<String,Object> paramMap = new HashMap<String,Object>();
		if(card_no.trim().length()>0){
		paramMap.put("card_no", card_no);
		}else{
		paramMap.put("beginBalance", beginBalance==""?null:new Integer(beginBalance));
		paramMap.put("endBalance", endBalance==""?null:new Integer(endBalance));
		paramMap.put("begin_info_time", begin_info_time);//开始时间
		paramMap.put("end_info_time", end_info_time);//结束时间
		paramMap.put("card_status", card_status);
		}
		/******************分页条件开始********************/
		int totalCount = jdCardService.queryJDCardCounts(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/******************查询开始********************/
		List<Map<String,String>> jdCardList = jdCardService.queryJDCardList(paramMap);
		
		/******************Request绑定开始********************/
		request.setAttribute("total_money", total_money);
		request.setAttribute("total_balance", total_balance);
		request.setAttribute("jdCardList", jdCardList);
		if(card_no.trim().length()>0){
			request.setAttribute("card_no", card_no);
		}else{
			request.setAttribute("beginBalance", beginBalance==""?null:new Integer(beginBalance));
			request.setAttribute("endBalance", endBalance==""?null:new Integer(endBalance));
			request.setAttribute("begin_info_time", begin_info_time);
			request.setAttribute("end_info_time", end_info_time);

		}
		request.setAttribute("jdCardStatus", JdVo.getJdCardStatus());

		request.setAttribute("isShowList", 1);

		Calendar theCa2 = Calendar.getInstance(); 
		theCa2.add(java.util.Calendar.HOUR_OF_DAY, +2); //把时间设置为当前时间+2小时
		//theCa2.add(java.util.Calendar.MINUTE, +30); //把时间设置为当前时间+30分钟
		Date date2 = theCa2.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String querydate2=df.format(date2);
		Calendar theCa4 = Calendar.getInstance(); 
		theCa4.add(java.util.Calendar.HOUR_OF_DAY, +4);  //把时间设置为当前时间+4小时
		//theCa4.add(java.util.Calendar.MINUTE, +90); //把时间设置为当前时间+90分钟
		Date date4 = theCa4.getTime();
		String querydate4=df.format(date4);
		request.setAttribute("now2", querydate2);
		request.setAttribute("now4", querydate4);

		return "jdCard/jdCardList";
	}
	
	/**
	 * 进入增加页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addJdCardPage.do")
	public String addJdCardPage( HttpServletRequest request,HttpServletResponse response){
		return "jdCard/jdCardAdd";
	}
	
	/**
	 * 添加预付卡信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addJdCard.do")
	public void addJdCard(HttpServletRequest request,HttpServletResponse response){
		String result="yes";
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_person =loginUserVo.getReal_name();//获取当前登录的人
		String card_no = this.getParam(request, "card_no");
		String card_pwd = this.getParam(request, "card_pwd");
		String card_money = this.getParam(request, "card_money");//余额
		String card_amount = this.getParam(request, "card_amount");//面值
		String become_due_time = this.getParam(request, "become_due_time");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("card_no", card_no);
		paramMap.put("card_pwd", card_pwd);
		paramMap.put("card_money", card_money);
		paramMap.put("card_amount", card_amount);
		paramMap.put("become_due_time", become_due_time);
		paramMap.put("card_status", "00");
		
		try{
			
		jdCardService.addJDCardInfo(paramMap);
		
		}catch(Exception e){
			result="no";
			logger.error(e);
		}

		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 进入修改页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateJdCardPage.do")
	public String updateJdCardPage( HttpServletRequest request,HttpServletResponse response){
		String card_id = this.getParam(request, "card_id");
		Map<String, Object> jdCardInfo = jdCardService.queryJDCardById(Integer.valueOf(card_id));
		request.setAttribute("jdCardInfo", jdCardInfo);
		return "jdCard/updateJDCard";
	}
	
	/**
	 * 修改预付卡信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateJdCard.do")
	public void updateJdCard(HttpServletRequest request,HttpServletResponse response){
		String result="yes";
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_person =loginUserVo.getReal_name();//获取当前登录的人
		String card_id = this.getParam(request, "card_id");
		String card_no = this.getParam(request, "card_no");
		String card_pwd = this.getParam(request, "card_pwd");
		String card_money = this.getParam(request, "card_money");//余额
		String card_amount = this.getParam(request, "card_amount");//面值
		String become_due_time = this.getParam(request, "become_due_time");
		
		Map<String, String> paramMap = new HashMap<String, String>();
		
		paramMap.put("card_no", card_no);
		paramMap.put("card_pwd", card_pwd);
		paramMap.put("card_money", card_money);
		paramMap.put("card_amount", card_amount);
		paramMap.put("become_due_time", become_due_time);
		paramMap.put("card_id",card_id);

		
		try{
			
		jdCardService.updateJDCardInfo(paramMap);
		
		}catch(Exception e){
			result="no";
			logger.error(e);
		}

		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
