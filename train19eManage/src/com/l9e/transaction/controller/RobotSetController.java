package com.l9e.transaction.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import com.l9e.transaction.service.RobotSettingService;
import com.l9e.transaction.service.WorkerService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.RobotSettingVo;
import com.l9e.transaction.vo.WorkerVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.MobileMsgUtilNew;
import com.l9e.util.PageUtil;
/**
 *机器管理
 * @author zhangjc
 *
 */
@Controller
@RequestMapping("/robotSet")
public class RobotSetController extends BaseController{

	private static final Logger logger = Logger.getLogger(RobotSetController.class);
	@Resource
	private RobotSettingService robotSettingService;
	@Resource
	private	WorkerService workerService;
	@Resource
	private MobileMsgUtilNew mobileMsgUtilNew;
	
	
	@RequestMapping("/getRobotSetting.do")
	public String getSystemSetting(HttpServletRequest request,
			HttpServletResponse response) {

		return  "redirect:/robotSet/queryRobotSetting.do";
	}
	
	/**
	 * 查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryRobotSetting.do")
	public String queryRobotSetting(HttpServletRequest request,
			HttpServletResponse response){
		List<String> robot_statusList = this.getParamToList(request, "robot_status");
		String robot_name = this.getParam(request, "robot_name");
		List<String> robot_type = this.getParamToList(request, "robot_type");
		
		//查询参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(robot_name.trim().length()>0){
			paramMap.put("robot_name", robot_name);
		}else{
			paramMap.put("robot_status", robot_statusList);
			
			if(!"".equals(robot_type)){
				for(String str :robot_type){
					paramMap.put(str, str);
				}
			}
			paramMap.put("robot_setList", 2);
			if(robot_type.size()>0){
				paramMap.put("robot_setList", -1);
			}
		}
		int totalCount = robotSettingService.queryRobotSystemSettingCount(paramMap);// 总条数
		// 分页
		PageVo page = PageUtil.getInstance().paging(request, 10, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());// 每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());// 每页显示的条数
		
		List<RobotSettingVo>robot_channelList=robotSettingService.getRobotSystemSetting(paramMap);
		for(int i=0;i<robot_channelList.size();i++){
			String robot_id =robot_channelList.get(i).getRobot_id();
			List<WorkerVo> workerList = workerService.queryWorkerByRobotId(robot_id);
			for(int j=0;j<workerList.size();j++){
				WorkerVo worker = workerList.get(j);
				String type = worker.getWorker_type();
				if("11".equals(type)){
					robot_channelList.get(i).setRobot_money_status(worker.getWorker_status());
					robot_channelList.get(i).setRobot_money_proxy(worker.getProxy_status());
					robot_channelList.get(i).setRobot_money_num(worker.getSpare_thread());
					robot_channelList.get(i).setRobot_money_stopReason(worker.getStop_reason());
				}
				if("10".equals(type)){
					robot_channelList.get(i).setRobot_register_status(worker.getWorker_status());
					robot_channelList.get(i).setRobot_register_proxy(worker.getProxy_status());
					robot_channelList.get(i).setRobot_register_num(worker.getSpare_thread());
					robot_channelList.get(i).setRobot_register_stopReason(worker.getStop_reason());
				}
				if("1".equals(type)){
					robot_channelList.get(i).setRobot_book_status(worker.getWorker_status());
					robot_channelList.get(i).setRobot_book_proxy(worker.getProxy_status());
					robot_channelList.get(i).setRobot_book_num(worker.getSpare_thread());
					robot_channelList.get(i).setRobot_book_stopReason(worker.getStop_reason());
				}
				if("3".equals(type)){
					robot_channelList.get(i).setRobot_pay_status(worker.getWorker_status());
					robot_channelList.get(i).setRobot_pay_proxy(worker.getProxy_status());
					robot_channelList.get(i).setRobot_pay_num(worker.getSpare_thread());
					robot_channelList.get(i).setRobot_pay_stopReason(worker.getStop_reason());
					String zhifubao=robotSettingService.queryZhifubaoByWorkerId(worker.getWorker_id());	
					if(StringUtils.isEmpty(zhifubao)){
						zhifubao ="";
					}else if(zhifubao.length()==21){
						zhifubao ="01";
					}else{
						zhifubao = zhifubao.substring(13,15);
					}
						robot_channelList.get(i).setZhifubao(zhifubao);
				}
				if("5".equals(type)){
					robot_channelList.get(i).setRobot_check_status(worker.getWorker_status());
					robot_channelList.get(i).setRobot_check_proxy(worker.getProxy_status());
					robot_channelList.get(i).setRobot_check_num(worker.getSpare_thread());
					robot_channelList.get(i).setRobot_check_stopReason(worker.getStop_reason());
				}
				if("6".equals(type)){
					robot_channelList.get(i).setRobot_cancel_status(worker.getWorker_status());
					robot_channelList.get(i).setRobot_cancel_proxy(worker.getProxy_status());
					robot_channelList.get(i).setRobot_cancel_num(worker.getSpare_thread());
					robot_channelList.get(i).setRobot_cancel_stopReason(worker.getStop_reason());
				}
				if("7".equals(type)){
					robot_channelList.get(i).setRobot_endorse_status(worker.getWorker_status());
					robot_channelList.get(i).setRobot_endorse_proxy(worker.getProxy_status());
					robot_channelList.get(i).setRobot_endorse_num(worker.getSpare_thread());
					robot_channelList.get(i).setRobot_endorse_stopReason(worker.getStop_reason());
				}
				if("8".equals(type)){
					robot_channelList.get(i).setRobot_refund_status(worker.getWorker_status());
					robot_channelList.get(i).setRobot_refund_proxy(worker.getProxy_status());
					robot_channelList.get(i).setRobot_refund_num(worker.getSpare_thread());
					robot_channelList.get(i).setRobot_refund_stopReason(worker.getStop_reason());
				}
				if("9".equals(type)){
					robot_channelList.get(i).setRobot_query_status(worker.getWorker_status());
					robot_channelList.get(i).setRobot_query_proxy(worker.getProxy_status());
					robot_channelList.get(i).setRobot_query_num(worker.getSpare_thread());
					robot_channelList.get(i).setRobot_query_stopReason(worker.getStop_reason());
				}
				if("13".equals(type)){
					robot_channelList.get(i).setRobot_delete_status(worker.getWorker_status());
					robot_channelList.get(i).setRobot_delete_proxy(worker.getProxy_status());
					robot_channelList.get(i).setRobot_delete_num(worker.getSpare_thread());
					robot_channelList.get(i).setRobot_delete_stopReason(worker.getStop_reason());
				}
				if("14".equals(type)){
					robot_channelList.get(i).setRobot_enroll_status(worker.getWorker_status());
					robot_channelList.get(i).setRobot_enroll_proxy(worker.getProxy_status());
					robot_channelList.get(i).setRobot_enroll_num(worker.getSpare_thread());
					robot_channelList.get(i).setRobot_enroll_stopReason(worker.getStop_reason());
				}
				if("15".equals(type)){
					robot_channelList.get(i).setRobot_activate_status(worker.getWorker_status());
					robot_channelList.get(i).setRobot_activate_proxy(worker.getProxy_status());
					robot_channelList.get(i).setRobot_activate_num(worker.getSpare_thread());
					robot_channelList.get(i).setRobot_activate_stopReason(worker.getStop_reason());
				}
				
			}
		}
		for(int i=0;i<robot_channelList.size();i++){
			String robot_channel=robot_channelList.get(i).getRobot_channel();
			if(robot_channel!=null){
			String robot_id1 =robot_channelList.get(i).getRobot_id();
			String a[] = robot_channel.split(",");
			List<String> list=new ArrayList<String>();
			for(int j=0;j<a.length;j++){
				list.add(a[j]);
			}
			
			request.setAttribute("robot_channelStr"+robot_id1, robot_id1+list.toString());
			}
		}
		
		List<String> robot_statusStr = this.getParamToList(request, "robot_status"); 
		request.setAttribute("robotSystemSetting", robot_channelList);
		request.setAttribute("robot_channelList", RobotSettingVo.getRobot_channelList());
		request.setAttribute("robot_statusList", RobotSettingVo.getRobot_statusList());
		request.setAttribute("stopReason", RobotSettingVo.getRobot_stopReson());
		request.setAttribute("isShowList", 1);
		request.setAttribute("proxyStatus", RobotSettingVo.getProxyStatus());
		if(robot_name.trim().length()>0){
			request.setAttribute("robot_name", robot_name);
		}else{
			request.setAttribute("robot_statusStr", robot_statusStr.toString());
			request.setAttribute("robot_type", robot_type.toString());
			request.setAttribute("robot_status", robot_statusStr);
		}
		return "robotSet/robotSetList";
	}
	
	//判断机器人是否存在
	@RequestMapping("/queryRobot_name.do")
	public String queryRobot_name(HttpServletRequest request ,HttpServletResponse response){
		String robot_name = this.getParam(request, "robot_name") ;
		//String robotName = robotSettingService.queryRobot_name(robot_name);
		System.out.println("robot_name="+robot_name);
		String result = null;
		if(StringUtils.isEmpty( robotSettingService.queryRobot_name(robot_name) )){
			result = "yes";
		}else{
			result = "no";
		}
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//判断机器人功能是否存在
	@RequestMapping("/queryRobot.do")
	public String queryRobot(HttpServletRequest request ,HttpServletResponse response){
		String robot_name = this.getParam(request, "robot_name");
		String robot_setList = this.getParam(request, "robot_setLIst");
		String robotId =robotSettingService.queryRobot_id(robot_name);
		 RobotSettingVo robotSettingVo = robotSettingService.getRobotSystemSettingById(robotId);
		 String type=null;
		 if(robot_setList.equals("10")){
			 type = robotSettingVo.getRobot_register();
		 }
		String result = null;
		
		System.out.println("robot_name="+robot_name+"robot_setList="+robot_setList+"robotId="+robotId+"type="+type);
		
		if(StringUtils.isEmpty( type )){
			result = "yes";
		}else{
			result = "no";
		}
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//跳转到增加机器人页面
	@RequestMapping("/turnToAddNewRobotURLPage.do")
	public String turnToAddNewRobotURLPage(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("robot_setList", RobotSettingVo.getRobot_setList());
		
		return "robotSet/robotSetAdd";
	}
	//增加机器人
	@RequestMapping("/addRobotSetting.do")
	public String addRobotSetting(HttpServletRequest request, HttpServletResponse response) {
		String robotName=request.getParameter("robotName");
		if(StringUtils.isEmpty( robotSettingService.queryRobot_name(robotName))){
		RobotSettingVo robotSettingVo = new RobotSettingVo();
		WorkerVo worker = new WorkerVo();
		String robot_id = CreateIDUtil.createID("SS");
		robotSettingVo.setRobot_id(robot_id);
		robotSettingVo.setRobot_name(this.getParam(request, "robot_name"));
		worker.setRobot_id(robot_id);
		robotSettingVo.setRobot_status("2");
		robotSettingVo.setRobot_desc(this.getParam(request, "robot_desc"));
		Map<String, String> log = new HashMap<String, String>();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		String msn = this.getParam(request, "msn");
		StringBuilder content = new StringBuilder();
		content.append(opt_person + "添加的新机器人:"+this.getParam(request, "robot_name")+"并添加了新功能,");
		worker.setWorker_name(this.getParam(request, "robot_name"));
		String workerType=this.getParam(request, "robot_setList");
		//赋值
		if("11".equals(workerType)){
			robotSettingVo.setRobot_money("2");
		}else if("10".equals(workerType)){
			robotSettingVo.setRobot_register("2");
		}else if("1".equals(workerType)){
			robotSettingVo.setRobot_book("2");
		}else if("3".equals(workerType)){
			robotSettingVo.setRobot_pay("2");
		}else if("5".equals(workerType)){
			robotSettingVo.setRobot_check("2");
		}else if("6".equals(workerType)){
			robotSettingVo.setRobot_cancel("2");
		}else if("7".equals(workerType)){
			robotSettingVo.setRobot_endorse("2");
		}else if("8".equals(workerType)){
			robotSettingVo.setRobot_refund("2");
		}else if("9".equals(workerType)){
			robotSettingVo.setRobot_query("2");
		}else if("13".equals(workerType)){
			robotSettingVo.setRobot_delete("2");
		}else if("14".equals(workerType)){
			robotSettingVo.setRobot_enroll("2");
		}else if("15".equals(workerType)){
			robotSettingVo.setRobot_activate("2");
		}
		content.append(robotSettingVo.getRobot_setList().get(workerType));
		
		log.put("content", content.toString());
		log.put("opt_person", opt_person);
		log.put("robot_name", robotSettingVo.getRobot_name());
		robotSettingService.addRobot(robotSettingVo, log);
		worker.setOpt_name(opt_person);
		worker.setWorker_status("22");
		worker.setWorker_type(workerType);
		worker.setWorker_ext(this.getParam(request, "robot_text"));
		worker.setWorker_priority(this.getParam(request, "priority"));
		worker.setWorker_region(this.getParam(request, "worker_region"));
		worker.setWorker_vendor(this.getParam(request, "worker_vendor"));
		worker.setWorker_describe(this.getParam(request, "worker_describe"));
		worker.setWorker_language_type(this.getParam(request, "worker_language"));
		worker.setPublic_ip(this.getParam(request, "public_ip"));
		
		logger.info("setWorker_region:"+worker.getWorker_region()+",worker_language:"+worker.getWorker_language_type()
				+",public_ip:"+worker.getPublic_ip());
		
		workerService.insertWorker(worker);
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("opt_person", opt_person);
		map.put("content", "添加账号："+robotSettingVo.getRobot_name());
		workerService.insertLog(map);
		
		}else{
			RobotSettingVo robotSettingVo = new RobotSettingVo();
			WorkerVo worker = new WorkerVo();
			String robot_id =robotSettingService.queryRobot_id(robotName);
			String robot_name =robotSettingService.queryRobot_name(robotName);
			worker.setRobot_id(robot_id);
			worker.setWorker_name(robot_name);
			String workerType=this.getParam(request, "robot_setList");
			StringBuilder content = new StringBuilder();
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			if("11".equals(workerType)){
				robotSettingVo.setRobot_money("2");
			}else if("10".equals(workerType)){
				robotSettingVo.setRobot_register("2");
			}else if("1".equals(workerType)){
				robotSettingVo.setRobot_book("2");
			}else if("3".equals(workerType)){
				robotSettingVo.setRobot_pay("2");
			}else if("5".equals(workerType)){
				robotSettingVo.setRobot_check("2");
			}else if("6".equals(workerType)){
				robotSettingVo.setRobot_cancel("2");
			}else if("7".equals(workerType)){
				robotSettingVo.setRobot_endorse("2");
			}else if("8".equals(workerType)){
				robotSettingVo.setRobot_refund("2");
			}else if("9".equals(workerType)){
				robotSettingVo.setRobot_query("2");
			}else if("13".equals(workerType)){
				robotSettingVo.setRobot_delete("2");
			}else if("14".equals(workerType)){
				robotSettingVo.setRobot_enroll("2");
			}else if("15".equals(workerType)){
				robotSettingVo.setRobot_activate("2");
			}
			content.append(robotSettingVo.getRobot_setList().get(workerType));
			log.put("content",content.toString());
			log.put("opt_person", opt_person);
			log.put("robot_name", robot_name);
			log.put("robot_id", robot_id);
			robotSettingVo.setRobot_id(robot_id);
			robotSettingVo.setRobot_desc(this.getParam(request, "robot_desc"));
			robotSettingService.updateRobot(robotSettingVo, log);
			worker.setOpt_name(opt_person);
			worker.setWorker_status("22");
			worker.setWorker_type(workerType);
			worker.setWorker_ext(this.getParam(request, "robot_text"));
			worker.setWorker_priority(this.getParam(request, "priority"));	
			worker.setWorker_region(this.getParam(request, "worker_region"));
			worker.setWorker_vendor(this.getParam(request, "worker_vendor"));
			worker.setWorker_describe(this.getParam(request, "worker_describe"));
			worker.setWorker_language_type(this.getParam(request, "worker_language"));
			worker.setPublic_ip(this.getParam(request, "public_ip"));
			
			logger.info("setWorker_region:"+worker.getWorker_region()+",worker_language:"+worker.getWorker_language_type()
					+",public_ip:"+worker.getPublic_ip());
			
			workerService.insertWorker(worker);
			
			Map<String,String> map = new HashMap<String,String>();
			map.put("opt_person", opt_person);
			map.put("content", "添加账号："+robot_name);
			workerService.insertLog(map);
		}
		//return "redirect:/robotSet/queryRobotSetting.do";老版机器人页面
		return  "redirect:/robotSetNew/queryRobotSetList.do?worker_type=1";//新版机器人页面
		
	}
	
	
	//切换机器人运行状态
	@RequestMapping("/changeNewRobotURLStatus.do")
	public String changeNewRobotURLStatus(HttpServletRequest request, HttpServletResponse response){
		String robotId=request.getParameter("robotId");
		String status=request.getParameter("status");
		
		 RobotSettingVo robotSettingVo = robotSettingService.getRobotSystemSettingById(robotId);
		 String msn = this.getParam(request, "msn");
		 StringBuilder content = new StringBuilder();
		 Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			String robot_name=robotSettingVo.getRobot_name();
			String pageIndex = this.getParam(request, "pageIndex");
		if(status.equals("1")){
			System.out.println("~~~~~~~~~~~~~~~~"+msn);
			if(msn.equals("1")){//用户点击【停用并发送短信】
				//发送审核通过短信功能
				String smnContent = "";//短信内容
				String phone=robotSettingService.getphoneList();
				String[] phones = phone.split("@@");
//				String[] phones = {"15201169346", "13718235385", "18511833526"};
				for(int i=0; i<phones.length;i++){
					String userPhone = phones[i];
					if(!StringUtils.isEmpty(userPhone)){
						smnContent =robotSettingVo.getRobot_name()+"出现异常，"+opt_person+"停用，请尽快处理！";
						try {
							mobileMsgUtilNew.send(userPhone.trim(), smnContent, "22");//短信通过企信通发送
						} catch (Exception e) {
							logger.error("短信发送异常", e);
						}
					 }
					String log2 = "【停用并发送短信】发给用户："+userPhone;
					System.out.println(log2);
					logger.info(log2);
				}
				content.append("点击了【停用并发送短信】机器人，短信内容："+smnContent);
			}else{//用户点击【停用】
				content.append("点击了【停用】机器人");
			}
			robotSettingVo.setRobot_status("2");
			List<WorkerVo> workerList = workerService.queryWorkerByRobotId(robotId);
			for(int i=0;i<workerList.size();i++){
				WorkerVo worker = workerList.get(i);
				worker.setWorker_status("22");
				workerService.updateWorkerByRobotId(worker);
			}
			if(robotSettingVo.getRobot_money() != null){
				robotSettingVo.setRobot_money("2");
			}
			
			if(robotSettingVo.getRobot_register() != null){
				robotSettingVo.setRobot_register("2");
			}
			if(robotSettingVo.getRobot_book() != null){
				robotSettingVo.setRobot_book("2");
			}
			if(robotSettingVo.getRobot_pay() != null){
				robotSettingVo.setRobot_pay("2");
			}
			if(robotSettingVo.getRobot_check() != null){
				robotSettingVo.setRobot_check("2");
			}
			if(robotSettingVo.getRobot_cancel() != null){
				robotSettingVo.setRobot_cancel("2");
			}
			if(robotSettingVo.getRobot_endorse() != null){
				robotSettingVo.setRobot_endorse("2");
			}
			if(robotSettingVo.getRobot_refund() != null){
				robotSettingVo.setRobot_refund("2");
			}
			if(robotSettingVo.getRobot_query() != null){
				robotSettingVo.setRobot_query("2");
			}
			if(robotSettingVo.getRobot_delete() != null){
				robotSettingVo.setRobot_delete("2");
			}
			if(robotSettingVo.getRobot_enroll() != null){
				robotSettingVo.setRobot_enroll("2");
			}
			if(robotSettingVo.getRobot_activate() != null){
				robotSettingVo.setRobot_activate("2");
			}
			if(robotSettingVo.getRobot_channel() != null){
				robotSettingVo.setRobot_channel("".toString());
			}
		}
		else if(status.equals("2")){
			content.append("点击了【置为备用】机器人");
			robotSettingVo.setRobot_status("3");
		}
		else if(status.equals("3")){
			content.append("点击了【启用】机器人");
			robotSettingVo.setRobot_status("1");
		}
		log.put("content", content.toString());
		log.put("opt_person", opt_person);
		log.put("robot_name", robot_name);
		log.put("robot_id", robotId);
		robotSettingVo.setOpt_name(opt_person);
		robotSettingService.updateRobotStatus(robotSettingVo, log);
		String str = "";
		String robot_type=request.getParameter("robot_type");
		if("".equals(robot_type)||robot_type==null){
			str = "";
		}else{
			String[] arr = robot_type.split(",");
			for(int i=0;i<arr.length;i++){
				str += "&robot_type="+arr[i];
			}
		}
		return "redirect:/robotSet/queryRobotSetting.do?pageIndex="+pageIndex+str;
	}
	
	//跳转到修改页面
	@RequestMapping("/turnToUpdateRobotPage.do")
	public String turnToUpdateRobotPage(HttpServletRequest request, HttpServletResponse response) {
		String robotId=request.getParameter("robotId");
		//查询参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("robot_id", robotId) ;
		
		List<Map<String, Object>> WorkerList = workerService.queryWorkerList2(paramMap);
		for(int i=0;i<WorkerList.size();i++){
			Integer worker_id=(Integer) WorkerList.get(i).get("worker_id");
			if((WorkerList.get(i).get("worker_type")).equals(3)){
			String zhifubao=robotSettingService.queryZhifubaoByWorkerId(worker_id.toString());
				if(StringUtils.isEmpty(zhifubao)){
					zhifubao = "";
				}else if(zhifubao.length()==21){
					zhifubao ="01";
				}else{
					zhifubao = zhifubao.substring(13,15);
				}
				request.setAttribute("zhifubao", zhifubao);
			}
		}
		request.setAttribute("workerType", WorkerVo.getWorkerType());
		request.setAttribute("workerList", WorkerList);
		String zhanghao = robotSettingService.queryZhanghaoList();
		Map<String, String> zhanghaoMap = new LinkedHashMap<String, String>();
		for(int i=0; i<zhanghao.split("@@").length;i++){
			zhanghaoMap.put(zhanghao.split("@@")[i],zhanghao.split("@@")[i]);
		}
		request.setAttribute("zhanghaoList", zhanghaoMap);
		String robot_type=request.getParameter("robot_type");
		String robot_name_search = request.getParameter("robot_name");
		String pageIndex = this.getParam(request, "pageIndex");
		request.setAttribute("robot_type", robot_type);
		request.setAttribute("robot_name_search", robot_name_search);
		request.setAttribute("pageIndex", pageIndex);
		return "robotSet/robotSetUpdate";
	}
	
	//修改机器人信息
	@RequestMapping("/updateRobotUrl.do")
	public String updateRobotUrl(HttpServletRequest request, HttpServletResponse response) {
		String workerId=request.getParameter("workerId");
		WorkerVo worker=workerService.queryWorkerByWorkerId(workerId);
		String zhifubao=robotSettingService.queryZhifubaoByWorkerId(workerId);
		String robotId = worker.getRobot_id();
		String worker_ext = this.getParam(request, "worker_ext_"+workerId);
		String robot_con_timeout = this.getParam(request, "robot_con_timeout_"+workerId);
		String robot_read_timeout = this.getParam(request, "robot_read_timeout_"+workerId);
		String worker_priority = this.getParam(request, "worker_priority_"+workerId);
		String spare_thread = this.getParam(request, "spare_thread_"+workerId);
		String zhifubaoNew  = this.getParam(request, "zhifubao");
		String pageIndex = this.getParam(request, "pageIndex");
		Map<String, String> log = new HashMap<String, String>();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		StringBuilder content = new StringBuilder();
		String s=worker.getWorker_type();
		int i=Integer.parseInt(s);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String zhifubaoUpdate = "";
		if(StringUtils.isEmpty(zhifubaoNew)){
			zhifubaoUpdate = "";
		}else if("01".equals(zhifubaoNew)){
			zhifubaoUpdate = "huochepiao19e@163.com";
		}else{
			zhifubaoUpdate = "huochepiao19e"+zhifubaoNew+"@163.com";
		}
		paramMap.put("zhifubaoNew", zhifubaoUpdate);
		paramMap.put("zhifubao", zhifubao);
		content.append("更新机器人信息"+worker.getWorker_name()+"功能,【"+worker.getWorkerType().get(i)+"】");
		if(!worker_ext.equals(worker.getWorker_ext())){
			content.append("【请求网址："+worker_ext+"更换前为："+worker.getWorker_ext()+"】");
		}
		if(!worker_priority.equals(worker.getWorker_priority())){
			content.append("【优先级："+worker_priority+"更换前为："+worker.getWorker_priority()+"】");
		}
		if(!robot_con_timeout.equals(worker.getRobot_con_timeout())){
			content.append("【连接超时时间："+robot_con_timeout+"更换前为："+worker.getRobot_con_timeout()+"】");
		}
		if(!robot_read_timeout.equals(worker.getRobot_read_timeout())){
			content.append("【读取超时时间："+robot_read_timeout+"更换前为："+worker.getRobot_read_timeout()+"】");
		}
		if(!spare_thread.equals(worker.getSpare_thread())){
			content.append("【剩余空闲："+spare_thread+"更换前为："+worker.getSpare_thread()+"】");
		}
		String zhifubao_new = "";
		if(!StringUtils.isEmpty(zhifubao)){
			zhifubao_new = zhifubao.split("@")[0];
		}
//		logger.info("1111111111111"+zhifubao_new+"@@@@"+!zhifubaoUpdate.equals(zhifubao_new));
		if(!zhifubaoUpdate.equals(zhifubao_new)){
			if("".equals(zhifubaoUpdate)){
				content.append("【该机器人解除绑定支付宝账号："+zhifubao+"】");
				paramMap.put("workerId", workerId) ;
				workerService.updateRobotZfb(paramMap);
			}else{
				int bfZhifubao=robotSettingService.queryZhifubaoCount(paramMap);
				if(bfZhifubao>0){
					content.append("【支付宝账号："+zhifubaoUpdate+"更换前为："+zhifubao+"】");
					paramMap.put("workerId", workerId) ;
					workerService.updateRobotZfb(paramMap);
				}
			}
		}
		worker.setWorker_ext(worker_ext);
		worker.setWorker_priority(worker_priority);
		worker.setRobot_con_timeout(robot_con_timeout);
		worker.setRobot_read_timeout(robot_read_timeout);
		worker.setSpare_thread(spare_thread);
		
		log.put("content", content.toString());
		log.put("opt_person", opt_person);
		workerService.updateRobotUrl(worker);
		workerService.insertLog(log);
		String robot_type=request.getParameter("robot_type");
		String robot_name_search = request.getParameter("robot_name_search");
		return "redirect:/robotSet/turnToUpdateRobotPage.do?pageIndex="+pageIndex+"&robotId="+robotId+"&robot_type="+robot_type+"&robot_name="+robot_name_search;
	}
	
	//判断支付宝账号是否已绑定两个账号
	@RequestMapping("/queryZhifubao.do")
	public String queryZhifubao(HttpServletRequest request ,HttpServletResponse response){
		String zhifubao  = this.getParam(request, "zhifubao");
		String result = null;
//		StringBuffer zhifubaoNew = new StringBuffer();
		Map<String, Object> paramMap = new HashMap<String, Object>();
//		if("01".equals(zhifubao)){
//			zhifubaoNew.append("huochepiao19e@163.com");
//		}else{
//			zhifubaoNew.append("huochepiao19e").append(zhifubao);
//		}
//		System.out.println(zhifubaoNew);
//		paramMap.put("zhifubaoNew", zhifubaoNew.toString());
		paramMap.put("zhifubaoNew", zhifubao);
		if(robotSettingService.queryZhifubaoCount(paramMap)==0){
			result = "yes";
		}else{
			result = "no";
		}
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//修改机器人查询渠道渠道
	@RequestMapping("/changeNewRobotChannel.do")
	public String changeNewRobotChannel(HttpServletRequest request, HttpServletResponse response) {
		String robotId = request.getParameter("robotId");
		RobotSettingVo robotSettingVo = robotSettingService.getRobotSystemSettingById(robotId);
		List<String> robot_channelList = this.getParamToList(request, "robot_channel_"+robotId);
		String robot_channel = "";
		for(int i=0;i<robot_channelList.size();i++){
			robot_channel += (robot_channelList.get(i)+",");
		}
		if(robot_channel.length()!=0){
			robot_channel = robot_channel.substring(0, robot_channel.length()-1);
		}
		robotSettingVo.setRobot_channel(robot_channel.toString());
		Map<String, String> log = new HashMap<String, String>();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		String robot_name=robotSettingVo.getRobot_name();
			log.put("content",opt_person +  "更改了渠道，更改后的值为:"+robot_channel.toString());
			log.put("opt_person", opt_person);
			log.put("robot_name", robot_name);
			log.put("robot_id", robotId);
			robotSettingVo.setOpt_name(opt_person);
			robotSettingService.updateRobotStatus(robotSettingVo, log);
			String str = "";
			String robot_type=request.getParameter("robot_type");
			String pageIndex=request.getParameter("pageIndex");
			String robot_name_search = request.getParameter("robot_name");
			if("".equals(robot_type)||robot_type==null){
				str = "";
			}else{
				String[] arr = robot_type.split(",");
				for(int i=0;i<arr.length;i++){
					str += "&robot_type="+arr[i];
				}
			}
			return "redirect:/robotSet/queryRobotSetting.do?pageIndex="+pageIndex+"&robot_name="+robot_name_search+str;
		}
	
	//日志
	@RequestMapping("/queryRobotSetList.do")
	public String queryRobotSetList(HttpServletRequest request, HttpServletResponse response){
		String robot_id= request.getParameter("robot_id");
		Map<String,Object>paramMap = new HashMap<String,Object>();
		/******************分页条件开始********************/
		int totalCount = robotSettingService.queryRobotSetListCount2(robot_id);
		PageVo page = PageUtil.getInstance().paging(request, 10, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		paramMap.put("robot_id", robot_id);
		/******************操作********************/
		List<Map<String,Object>> robotList = robotSettingService.queryRobotSetList(paramMap);
		request.setAttribute("robotList", robotList);
		request.setAttribute("robot_id", robot_id);
		request.setAttribute("isShowList", 1);
		return "robotSet/robotHistory";
	}
	
	//停用机器人跳转到选择停用原因页面
	@RequestMapping("/chooseStopReason.do")
	public String chooseStopReason(HttpServletRequest request, HttpServletResponse response){
		String robotId=request.getParameter("robotId");
		String workerType=request.getParameter("workerType");
		String msn = this.getParam(request, "msn");
		String robot_type = this.getParam(request, "robot_type");
		request.setAttribute("robotId", robotId);
		request.setAttribute("workerType", workerType);
		request.setAttribute("msn", msn);
		request.setAttribute("robot_type", robot_type);
		return "robotSet/robotStopReason";
	}
	
	//切换机器人各功能状态
	@RequestMapping("/changeRobotAction.do")
	public String changeRobotAction(HttpServletRequest request, HttpServletResponse response){
		String robotId=request.getParameter("robotId");
		RobotSettingVo robotSettingVo = robotSettingService.getRobotSystemSettingById(robotId);
		String workerType=request.getParameter("workerType");
		String stop_reason=request.getParameter("stop_reason");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("robot_id", robotId) ;
		paramMap.put("worker_type", workerType);
		WorkerVo worker=workerService.queryWorkerByRobotType(paramMap);
		Map<String, String> log = new HashMap<String, String>();
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		String robot_name=robotSettingVo.getRobot_name();
		String msn = this.getParam(request, "msn");
		String actionName = RobotSettingVo.getRobot_setList().get(workerType.trim());
		StringBuilder content = new StringBuilder();
		String getAction="",setAction="";
		
		if("11".equals(workerType)){
			getAction = robotSettingVo.getRobot_money();
		}else if("10".equals(workerType)){
			getAction = robotSettingVo.getRobot_register();
		}else if("1".equals(workerType)){
			getAction = robotSettingVo.getRobot_book();
		}else if("3".equals(workerType)){
			getAction = robotSettingVo.getRobot_pay();
		}else if("5".equals(workerType)){
			getAction = robotSettingVo.getRobot_check();
		}else if("6".equals(workerType)){
			getAction = robotSettingVo.getRobot_cancel();
		}else if("7".equals(workerType)){
			getAction = robotSettingVo.getRobot_endorse();
		}else if("8".equals(workerType)){
			getAction = robotSettingVo.getRobot_refund();
		}else if("9".equals(workerType)){
			getAction = robotSettingVo.getRobot_query();
		}else if("13".equals(workerType)){
			getAction = robotSettingVo.getRobot_delete();
		}else if("14".equals(workerType)){
			getAction = robotSettingVo.getRobot_enroll();
		}else if("15".equals(workerType)){
			getAction = robotSettingVo.getRobot_activate();
		}
		if(getAction.equals("1")){
			if("1".equals(msn)){//用户点击【停用并发送短信】
				//发送审核通过短信功能
				String smnContent = "";//短信内容
				String phone=robotSettingService.getphoneList();
				String[] phones = phone.split("@@");
				for(int i=0; i<phones.length;i++){
					String userPhone = phones[i];
					if(!StringUtils.isEmpty(userPhone)){
						//Robot_type机器人robot_name出现异常，user_name停用，请尽快处理！
						smnContent =robotSettingVo.getRobot_name()+actionName+"功能出现异常："+robotSettingVo.getRobot_stopReson().get(stop_reason)+"，"+opt_person+"停用，请尽快处理！";
						//String smnContent = "【19e火车票】您好，"+user+"点击了停用机器人:"+workerMap.get("worker_name");
						try {
							mobileMsgUtilNew.send(userPhone.trim(), smnContent, "22");//短信通过企信通发送
						} catch (Exception e) {
							logger.error("短信发送异常", e);
						}
					 }
					String log2 = "【停用并发送短信】发给用户："+userPhone;
					System.out.println(log2);
					logger.info(log2);
				}
				content.append("点击了【停用并发送短信】"+actionName+"功能，短信内容："+smnContent);
			}else{//用户点击【停用】
				content.append("点击了【停用】"+actionName+"功能");
			}
				setAction="2";
				worker.setWorker_status("22");
				worker.setStop_reason(stop_reason);
		}
		else if(getAction.equals("2")){
				content.append("点击了【置为备用】"+actionName+"功能");
				setAction="3";
				worker.setWorker_status("33");
		}
		else if(getAction.equals("3")){
				content.append("点击了【启用】"+actionName+"功能");
				setAction="1";
				worker.setWorker_status("00");
				worker.setStop_reason("00");
		}
		//赋值
		if("11".equals(workerType)){
			robotSettingVo.setRobot_money(setAction);
		}else if("10".equals(workerType)){
			robotSettingVo.setRobot_register(setAction);
		}else if("1".equals(workerType)){
			robotSettingVo.setRobot_book(setAction);
		}else if("3".equals(workerType)){
			robotSettingVo.setRobot_pay(setAction);
		}else if("5".equals(workerType)){
			robotSettingVo.setRobot_check(setAction);
		}else if("6".equals(workerType)){
			robotSettingVo.setRobot_cancel(setAction);
		}else if("7".equals(workerType)){
			robotSettingVo.setRobot_endorse(setAction);
		}else if("8".equals(workerType)){
			robotSettingVo.setRobot_refund(setAction);
		}else if("9".equals(workerType)){
			robotSettingVo.setRobot_query(setAction);
		}else if("13".equals(workerType)){
			robotSettingVo.setRobot_delete(setAction);
		}else if("14".equals(workerType)){
			robotSettingVo.setRobot_enroll(setAction);
		}else if("15".equals(workerType)){
			robotSettingVo.setRobot_activate(setAction);
		}
		log.put("content", opt_person + content.toString());
		log.put("opt_person", opt_person);
		log.put("robot_name", robot_name);
		log.put("robot_id", robotId);
		robotSettingVo.setOpt_name(opt_person);
		robotSettingService.updateRobotStatus(robotSettingVo, log);
		workerService.updateRobotStatus(worker,log);
		String str = "";
		String robot_type=request.getParameter("robot_type");
		String pageIndex=request.getParameter("pageIndex");
		String robot_name_search = request.getParameter("robot_name");
		if("".equals(robot_type)||robot_type==null){
			str = "";
		}else{
			String[] arr = robot_type.split(",");
			for(int i=0;i<arr.length;i++){
				str += "&robot_type="+arr[i];
			}
		}
		return "redirect:/robotSet/queryRobotSetting.do?pageIndex="+pageIndex+"&robot_name="+robot_name_search+str;
	}

	//切换机器人自动切换代理
	@RequestMapping("/channgeProxy.do")
	public String channgeProxy(HttpServletRequest request, HttpServletResponse response){
			String robotId=request.getParameter("robotId");
			String workerType=request.getParameter("workerType");
			String cope = request.getParameter("cope");
			String robot_name = request.getParameter("robot_name");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("robot_id", robotId) ;
			paramMap.put("worker_type", workerType);
			WorkerVo worker=workerService.queryWorkerByRobotType(paramMap);
			worker.setProxy_status(cope);

			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			RobotSettingVo robotSettingVo = robotSettingService.getRobotSystemSettingById(robotId);
			String status = "";
			if("00".equals(cope)){
				status = "开启";
			}else if("22".equals(cope)){
				status = "关闭";
			}
			String type = RobotSettingVo.getRobot_setList().get(workerType);
			log.put("content", opt_person +":"+ status + "【"+type+"】功能的自动切换代理！");
			log.put("opt_person", opt_person);
			log.put("robot_name", robotSettingVo.getRobot_name());
			log.put("robot_id", robotId);
			robotSettingVo.setOpt_name(opt_person);
			robotSettingService.updateRobotStatus(robotSettingVo, log);
			workerService.updateRobotStatus(worker,log);
			String str = "";
			String robot_type=request.getParameter("robot_type");
			String pageIndex=request.getParameter("pageIndex");
			if("".equals(robot_type)||robot_type==null){
				str = "";
			}else{
				String[] arr = robot_type.split(",");
				for(int i=0;i<arr.length;i++){
					str += "&robot_type="+arr[i];
				}
			}
			return "redirect:/robotSet/queryRobotSetting.do?pageIndex="+pageIndex+"&robot_name="+robot_name+str;
	}
	
	
}
