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

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.RobotSetNewService;
import com.l9e.transaction.service.RobotSettingService;
import com.l9e.transaction.service.WorkerService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.RobotSettingVo;
import com.l9e.transaction.vo.WorkerVo;
import com.l9e.util.HttpPostRobotUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.HttpUtilNew;
import com.l9e.util.PageUtil;
/**
 *机器管理
 * @author zhangjc02
 *
 */
@Controller
@RequestMapping("/robotSetNew")
public class RobotSetNewController extends BaseController{

	private static final Logger logger = Logger.getLogger(RobotSetNewController.class);
	@Resource
	private RobotSetNewService robotSetNewService;
	@Resource
	private RobotSettingService robotSettingService;
	
	@Resource
	private WorkerService workerService;
	/**
	 * 跳转到机器人页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/gotoRobotSetPage.do")
	public String getSystemSetting(HttpServletRequest request,
			HttpServletResponse response) {

		return  "redirect:/robotSetNew/queryRobotSetList.do?worker_type=1";
	}
	
	/**
	 * 查询机器人列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryRobotSetList.do")
	public String queryRobotSetting(HttpServletRequest request,
			HttpServletResponse response){
		String worker_type = this.getParam(request, "worker_type");
		String worker_name = this.getParam(request, "worker_name");
		List<String> workerStatusList = this.getParamToList(request, "worker_status");
		List<String> stopReasonList = this.getParamToList(request, "stop_reason");
		List<String> workerRegionList = this.getParamToList(request, "worker_region");
		List<String> workerVendorList = this.getParamToList(request, "worker_vendor");
		List<String> workerLanguageList = this.getParamToList(request, "worker_language");
		//查询参数
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("worker_type",worker_type);
	
		if(worker_name.trim().length()>0){
			paramMap.put("worker_name",worker_name);
		}else{
			paramMap.put("worker_status",workerStatusList);
			paramMap.put("stop_reason",stopReasonList);
			paramMap.put("worker_region",workerRegionList);
			paramMap.put("worker_vendor",workerVendorList);
			paramMap.put("worker_language",workerLanguageList);
		}
		int totalCount = robotSetNewService.queryRobotSetCount(paramMap);// 总条数
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());// 每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());// 每页显示的条数
		List<Map<String,String>> robotList=robotSetNewService.queryRobotSetList(paramMap);
		
		request.setAttribute("robotList", robotList);
		request.setAttribute("isShowList", 1);
		request.setAttribute("worker_type", worker_type);
		request.setAttribute("workerType", WorkerVo.getWorkerType());
		request.setAttribute("workerStatus", WorkerVo.getWorkerStatus());
		request.setAttribute("stopReason", WorkerVo.getStopReason());
		request.setAttribute("worker_regionList", WorkerVo.getWorkerRegion());
		request.setAttribute("worker_vendorList", WorkerVo.getWorkerVendor());
		request.setAttribute("worker_languageList", WorkerVo.getWorkerLanguage());


		if(worker_name.trim().length()>0){
			request.setAttribute("worker_name", worker_name);
		}else{
			request.setAttribute("workerStatusStr", workerStatusList.toString());
			request.setAttribute("stopReasonStr", stopReasonList.toString());
			request.setAttribute("workerVendorStr", workerVendorList.toString());
			request.setAttribute("workerRegionStr", workerRegionList.toString());
			request.setAttribute("workerLanguageStr", workerLanguageList.toString());
		}
		
		//支付机器人显示支付宝信息
		if("3".equals(worker_type)){
			Map<Integer, String> robotMap = new HashMap<Integer, String>();
			for(Map<String, String> workerMap : robotList){
				String worker_id = String.valueOf(workerMap.get("worker_id"));
				String zhifubao=robotSettingService.queryZhifubaoByWorkerId(worker_id);
				if(StringUtils.isEmpty(zhifubao)){
					zhifubao = "空";
				}else if("huochepiao19e@163.com".equals(zhifubao)){
					zhifubao ="19e01";
				}else if("huochepiaokuyou@163.com".equals(zhifubao)){
					zhifubao ="kuyou01";//huochepiao19e12@163.com
				}else if(zhifubao.length()==23){
					zhifubao = zhifubao.substring(10,16);   //19e12
				}else if(zhifubao.length()==15){//jp02@19e.com.cn
					 zhifubao =zhifubao.substring(0,4);  //jp02
				}else{//huochepiaokuyou19@19e.com.cn
					zhifubao = zhifubao.substring(10,17); //kuyou19
				}
				robotMap.put(Integer.parseInt(worker_id), zhifubao);
			}
			request.setAttribute("robotMap", robotMap);
		}
		return "robotSetNew/robotSetNewList";
	}
	
	/**
	 * 修改机器人状态
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/changeStatus.do")
	public void changeStatus(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		String worker_status = this.getParam(request, "worker_status");
		String worker_id = this.getParam(request, "worker_id");
		String stop_reason = "00";
		if("22".equals(worker_status)){
			stop_reason = this.getParam(request, "stop_reason");
		}
		String result = "failure";
		Map<String, String> worker = new HashMap<String, String>();
		worker.put("worker_id",worker_id);
		worker.put("worker_status",worker_status);
		worker.put("opt_person",opt_person);
		Map<String, String> log = new HashMap<String, String>();
		String content = "";
		if("22".equals(worker_status)){
			worker.put("stop_reason",stop_reason);
			content = opt_person+"点击了【停用】停用原因："+WorkerVo.getStopReason().get(stop_reason);
		}else if("33".equals(worker_status)){
//			worker.put("stop_reason",stop_reason);
			content = opt_person+"点击了【备用】";
		}else{
			worker.put("stop_reason",stop_reason);
			content = opt_person+"点击了【启用】";
		}
		log.put("content", content);
		log.put("opt_person", opt_person);
		log.put("worker_id", worker_id);
		try {
			int count = robotSetNewService.changeStatus(worker,log);
			if(count>0){
				result = "success";
			}
		} catch (Exception e) {
			logger.error("修改机器人状态异常："+e);
		}
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//查询操作日志
	@RequestMapping("/queryWorkerLog.do")
	@ResponseBody
	public void queryWorkerLog(HttpServletResponse response,HttpServletRequest request){
		String worker_id = this.getParam(request,"worker_id");
		List<Map<String, Object>> history = robotSetNewService.queryWorkerLog(worker_id);
		JSONArray jsonArray = JSONArray.fromObject(history);  
		response.setCharacterEncoding("utf-8");
		try {
			response.getWriter().write(jsonArray.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 跳转到机器人明细页面
	 */
	@RequestMapping("/queryRobotSetInfo.do")
	public String queryRobotSetInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String worker_id = this.getParam(request, "worker_id");
		Map<String, String> worker = robotSetNewService.queryRobotSetInfo(worker_id);
		List<Map<String, Object>> history = robotSetNewService.queryWorkerLog(worker_id);
		List<Map<String, Object>> report = robotSetNewService.queryReportLog(worker_id);
		
		List<Map<String, Object>> zhanghaoList = robotSetNewService.queryZhanghaoList();
		request.setAttribute("zhanghaoList", zhanghaoList);
		request.setAttribute("worker_vendorList", WorkerVo.getWorkerVendor());
		request.setAttribute("worker_regionList", WorkerVo.getWorkerRegion());
		request.setAttribute("worker_languageList", WorkerVo.getWorkerLanguage());
		
		String zhifubao=robotSettingService.queryZhifubaoByWorkerId(worker_id);
		String com_no=workerService.queryComNo(worker_id);

		request.setAttribute("zhifubao", zhifubao);
		request.setAttribute("com_no", com_no);
		
		request.setAttribute("worker", worker);
		request.setAttribute("history", history);
		request.setAttribute("report", report);
		
		request.setAttribute("workerType", WorkerVo.getWorkerType());
		request.setAttribute("workerStatus", WorkerVo.getWorkerStatus());
		return "robotSetNew/robotSetNewInfo";
	}
	
	/**
	 * 跳转到机器人验证码页面
	 */
	@RequestMapping("/queryRobotCodeInfo.do")
	public String queryRobotCodeInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String worker_id = this.getParam(request, "worker_id");
		Map<String, String> worker = robotSetNewService.queryRobotSetInfo(worker_id);
		
		List<Map<String, Object>> zhanghaoList = robotSetNewService.queryZhanghaoList();
		request.setAttribute("zhanghaoList", zhanghaoList);
		
		String zhifubao=robotSettingService.queryZhifubaoByWorkerId(worker_id);
		request.setAttribute("zhifubao", zhifubao);
		
		request.setAttribute("worker", worker);
		
		request.setAttribute("workerType", WorkerVo.getWorkerType());
		request.setAttribute("workerStatus", WorkerVo.getWorkerStatus());
		return "robotSetNew/robotVerificationCodeInfo";
	}
	
	
	/**
	 * 更新订单信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateRobotSetInfo.do")
	public void updateAcquireInfo(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		String worker_id=request.getParameter("worker_id");
		Map<String, String> worker = robotSetNewService.queryRobotSetInfo(worker_id);
		String zhifubao=robotSettingService.queryZhifubaoByWorkerId(worker_id);
		String worker_ext = this.getParam(request, "worker_ext");
		String robot_con_timeout = this.getParam(request, "robot_con_timeout");
		String robot_read_timeout = this.getParam(request, "robot_read_timeout");
		String worker_priority = this.getParam(request, "worker_priority");
		String spare_thread = this.getParam(request, "spare_thread");
		String app_valid=this.getParam(request, "app_valid");
		String pay_device_type=this.getParam(request, "pay_device_type");
		String zhifubaoNew  = this.getParam(request, "zhifubao");
		String com_no  = this.getParam(request, "com_no");
		String com_no_orgin=workerService.queryComNo(worker_id);
		String worker_region  = this.getParam(request, "worker_region");
		String worker_vendor  = this.getParam(request, "worker_vendor");
		String worker_describe  = this.getParam(request, "worker_describe");
		String worker_language  = this.getParam(request, "worker_language");
		String public_ip  = this.getParam(request, "public_ip");
		String worker_name  = this.getParam(request, "worker_name");
		
		Map<String, String> log = new HashMap<String, String>();
		StringBuilder content = new StringBuilder();
		
		content.append("更新机器人信息"+worker.get("worker_name")+"功能,");
		if(!worker_name.equals(String.valueOf(worker.get("worker_name")))){
			content.append("【机器人名称："+worker_name+"更换前为："+String.valueOf(worker.get("worker_name"))+"】");
		}
		if(!worker_ext.equals(String.valueOf(worker.get("worker_ext")))){
			content.append("【请求网址："+worker_ext+"更换前为："+String.valueOf(worker.get("worker_ext"))+"】");
		}
		if(!worker_priority.equals(String.valueOf(worker.get("worker_priority")))){
			content.append("【优先级："+worker_priority+"更换前为："+String.valueOf(worker.get("worker_priority"))+"】");
		}
		if(!robot_con_timeout.equals(String.valueOf(worker.get("robot_con_timeout")))){
			content.append("【连接超时时间："+robot_con_timeout+"更换前为："+String.valueOf(worker.get("robot_con_timeout"))+"】");
		}
		if(!robot_read_timeout.equals(String.valueOf(worker.get("robot_read_timeout")))){
			content.append("【读取超时时间："+robot_read_timeout+"更换前为："+String.valueOf(worker.get("robot_read_timeout"))+"】");
		}
		if(!spare_thread.equals(String.valueOf(worker.get("spare_thread")))){
			content.append("【剩余空闲："+spare_thread+"更换前为："+String.valueOf(worker.get("spare_thread"))+"】");
		}
		if(!app_valid.equals(String.valueOf(worker.get("app_valid")))){
			content.append("【名单类型："+app_valid+"更换前为："+String.valueOf(worker.get("app_valid"))+"】");
		}
		if(!worker_region.equals(String.valueOf(worker.get("worker_region")))){
			content.append("【机器区域："+worker_region+"更换前为："+String.valueOf(worker.get("worker_region"))+"】");
		}
		if(!pay_device_type.equals(String.valueOf(worker.get("pay_device_type")))){
			content.append("【支付类型："+pay_device_type+"更换前为："+String.valueOf(worker.get("pay_device_type"))+"】");
		}
		if(!com_no.equals(String.valueOf(com_no_orgin))){
			content.append("【猫池com端口："+com_no+"更换前为："+String.valueOf(com_no_orgin)+"】");
		}
		if(!worker_vendor.equals(String.valueOf(worker.get("worker_vendor")))){
			content.append("【机器提供商："+worker_vendor+"更换前为："+String.valueOf(worker.get("worker_vendor"))+"】");
		}
		if(!worker_describe.equals(String.valueOf(worker.get("worker_describe")))){
			content.append("【机器描述："+worker_describe+"更换前为："+String.valueOf(worker.get("worker_describe"))+"】");
		}
		if(!worker_language.equals(String.valueOf(worker.get("worker_language_type")))){
			content.append("【机器语言："+worker_language+"更换前为："+String.valueOf(worker.get("worker_language_type"))+"】");
		}	
		if(!public_ip.equals(String.valueOf(worker.get("public_ip")))){
			content.append("【公网IP："+public_ip+"更换前为："+String.valueOf(worker.get("public_ip"))+"】");
		}
		
		
		if("3".equals(String.valueOf(worker.get("worker_type")))){
			Map<String, Object> paramMap = new HashMap<String, Object>();
			Map<String, Object> paMap = new HashMap<String, Object>();
			paramMap.put("zhifubaoNew", zhifubaoNew);
			paramMap.put("zhifubao", zhifubao);
			
			/*if(StringUtils.isNotEmpty(com_no)){*/
				paMap.put("com_no", com_no);
				paMap.put("worker_id", worker_id);

				try {
					//更新短信猫口
					workerService.updateComNo(paMap);
				} catch (Exception e) {
					logger.info("更新短信猫口失败",e);
				}
			/*}*/
				
			if(!zhifubaoNew.equals(zhifubao)){
				if("".equals(zhifubaoNew)){
					content.append("【该机器人解除绑定支付宝账号："+zhifubao+"】");
					paramMap.put("workerId", worker_id) ;
					workerService.updateRobotZfb(paramMap);
				}else{
					int bfZhifubao=robotSettingService.queryZhifubaoCount(paramMap);
					if(bfZhifubao>0){
						content.append("【支付宝账号："+zhifubaoNew+"更换前为："+zhifubao+"】");
						paramMap.put("workerId", worker_id) ;
						workerService.updateRobotZfb(paramMap);
					}
				}
			}
		}
		worker.put("worker_name",worker_name);
		worker.put("worker_ext",worker_ext);
		worker.put("spare_thread",spare_thread);
		worker.put("worker_priority",worker_priority);
		worker.put("robot_con_timeout",robot_con_timeout);
		worker.put("robot_read_timeout",robot_read_timeout);
		worker.put("opt_name", opt_person);
		worker.put("app_valid", app_valid);
		worker.put("pay_device_type", pay_device_type);
		worker.put("worker_region", worker_region);
		worker.put("worker_vendor", worker_vendor);
		worker.put("worker_describe", worker_describe);
		worker.put("worker_language", worker_language);
		worker.put("public_ip", public_ip);
		
		log.put("content", content.toString());
		log.put("opt_person", opt_person);
		log.put("worker_id", worker_id);
		int count = robotSetNewService.changeStatus(worker,log);
		if(count>0) logger.info("修改机器人信息成功！");
	}
	
	//判断短信猫口是否被使用
	@RequestMapping("/queryComNo.do")
	public String queryComNo(HttpServletRequest request ,HttpServletResponse response){
		String com_no  = this.getParam(request, "com_no");
		String result = null;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("com_no", com_no);
		
		if(workerService.queryComNoCount(paramMap)<=1){
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
	
	
	/**
	 * 添加验证码信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addRobotSetCodeInfo.do")
	public String addVerificationCodeInfo(HttpServletRequest request,HttpServletResponse response){
		String worker_id=request.getParameter("worker_id");
		String verification_code = this.getParam(request, "verification_code");
		String result = "failure";
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("worker_id", worker_id);
		paramMap.put("verification_code", verification_code);
		
		try {
			//查询worker_id十分钟内是否有相同验证码
			List<Map<String, Object>> time_list = robotSetNewService.queryVerificationCode(paramMap);
			if(time_list.isEmpty()){
				paramMap.put("create_time", "now()");
				try {
					robotSetNewService.addVerificationCode(paramMap);
					result = "success";
				} catch (Exception e) {
					logger.error("添加验证码失败！"+e.getMessage());
				}
			}else {
				//10分钟内不能添加相同验证码
				result = "wrong";
			}
		} catch (Exception e) {
			logger.error("查询验证码失败！"+e.getMessage());
		}
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "robotSetNew/robotVerificationCodeInfo";
	}
	
	
	
	
	/**
	 * 批量修改支付宝账号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/changeAlipayAccount.do")
	public void changeAlipayAccount(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		String workerIdStr = this.getParam(request, "workerIdStr");
		String result = "failure";
		
		try {
			String[] arr = workerIdStr.split(",");
			String dec = "";
			for (int i = 0; i < arr.length; i++){
		        if (i == 0){
		            dec += arr[arr.length-1]+ "," ;
		        }else{
		            dec += arr[i-1] + ",";
		        }
		    }
			String[] workerIdArr = dec.split(",");
			
			for(int i=0;i<arr.length + 1;i++){
				String change_worker_id="";
				String worker_id = "";
				
				if(i==0){
					 change_worker_id="9999";//随意设置一个temp值
					 worker_id = arr[i];
				}else if(i==arr.length){
					 change_worker_id=workerIdArr[0];
					 worker_id="9999";//将temp值改回
				}else{
					  change_worker_id = workerIdArr[i];
					  worker_id = arr[i];
				}
				
				Map<String, String> worker = new HashMap<String, String>();
				worker.put("worker_id",worker_id);
				worker.put("change_worker_id",change_worker_id);
				worker.put("opt_person",opt_person);
				try {
					 //切换账号类型为App支付
				//	 robotSetNewService.changeAlipayAccountType(worker); //目前只有PC端java支付
					 //切换账号
					 robotSetNewService.changeAlipayAccount(worker);
				} catch (Exception e) {
					logger.error("批量更换支付宝账号异常：+workerId:"+worker_id+"|"+e);
				}
			}
			result = "success";
		} catch (Exception e2) {
			logger.error("批量更换支付宝账号异常："+e2);
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
		 * 批量修改机器人状态
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("/changeWorkerStatus.do")
		public void changeWorkerStatus(HttpServletRequest request,
				HttpServletResponse response){
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			String worker_status = this.getParam(request, "worker_status");
			String stop_reason = "00";
			if("22".equals(worker_status)){
				stop_reason = this.getParam(request, "stop_reason");
			}
			String workerIdStr = this.getParam(request, "workerIdStr");
			String result = "failure";
			try {
				String[] arr = workerIdStr.split(",");
				for(int i=0;i<arr.length;i++){
					String worker_id = arr[i];
					Map<String, String> worker = new HashMap<String, String>();
					worker.put("worker_id",worker_id);
					worker.put("worker_status",worker_status);
					worker.put("opt_person",opt_person);
					Map<String, String> log = new HashMap<String, String>();
					String content = "";
					if("22".equals(worker_status)){
						worker.put("stop_reason",stop_reason);
						content = opt_person+"点击了【批量停用】停用原因："+WorkerVo.getStopReason().get(stop_reason);
					}else if("33".equals(worker_status)){
						worker.put("stop_reason",stop_reason);
						content = opt_person+"点击了【批量备用】";
					}else{
						worker.put("stop_reason",stop_reason);
						content = opt_person+"点击了【批量启用】";
					}
					log.put("content", content);
					log.put("opt_person", opt_person);
					log.put("worker_id", worker_id);
					try {
						int count = robotSetNewService.changeStatus(worker,log);
					} catch (Exception e) {
						logger.error("修改机器人状态异常：+workerId:"+worker_id+"|"+e);
					}
				}
				result = "success";
			} catch (Exception e2) {
				logger.error("批量修改机器人状态异常："+e2);
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
		 * 删除机器人
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("/deleteByWorkId.do")
		public void deleteByWorkId(HttpServletRequest request,
				HttpServletResponse response){
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			String worker_id = this.getParam(request, "worker_id");
			String result = "failure";
			if(!StringUtils.isEmpty(worker_id)){
				Map<String, String> worker = new HashMap<String, String>();
				worker.put("worker_id",worker_id);
				worker.put("opt_person",opt_person);
				Map<String, String> log = new HashMap<String, String>();
				String content = opt_person+"点击了【删除】worker_id："+worker_id;
				log.put("content", content);
				log.put("opt_person", opt_person);
				log.put("worker_id", worker_id);
				try {
					robotSetNewService.deleteByWorkId(worker,log);
					result = "success";
				} catch (Exception e) {
					logger.error("修改机器人状态异常："+e);
				}
			}
			try {
				response.getWriter().write(result);
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		/*
		 * 修改机器12306的host文件
		 */
		@RequestMapping("/modify12306Host.do")
		public void modify12306Host(HttpServletRequest request, HttpServletResponse response){
			
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
			String opt_person = loginUserVo.getReal_name();//当前登录人
			String work_ext = this.getParam(request, "work_ext");
			String worker_id = this.getParam(request, "worker_id");
			work_ext = work_ext.replaceAll("\\s*", "");//去除空格
			String  ip= this.getParam(request, "ip").trim();
			
			logger.info("work_ext->"+work_ext+";worker_id:"+worker_id+";ip:"+ip);
			
			String result = "failure";
			
			if(!StringUtils.isEmpty(worker_id)){
				Map<String, String> worker = new HashMap<String, String>();
				worker.put("worker_id",worker_id);
				worker.put("work_ext",work_ext);
				worker.put("ip",ip);
				
				Map<String, String> log = new HashMap<String, String>();
				String content = opt_person+"点击了【HOST】worker_id："+worker_id+",修改DNS:"+ip;
				log.put("content", content);
				log.put("opt_person", opt_person);
				log.put("worker_id", worker_id);
				try {
					//请求机器人
					String status=robotSetNewService.modify12306Host(worker,log); //记录操作日志
					result = status;
				
				} catch (Exception e) {
					result = "FAILURE";
					logger.info("修改机器12306的host文件："+e);
				}
			}
			
			try {
				response.getWriter().write(result);
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		//跳转到增加机器人页面
		@RequestMapping("/turnToAddNewRobotURLPage.do")
		public String turnToAddNewRobotURLPage(HttpServletRequest request, HttpServletResponse response) {
			request.setAttribute("robot_setList", WorkerVo.getWorkerType());
			request.setAttribute("worker_vendorList", WorkerVo.getWorkerVendor());
			request.setAttribute("worker_regionList", WorkerVo.getWorkerRegion());
			request.setAttribute("worker_languageList", WorkerVo.getWorkerLanguage());
			return "robotSetNew/robotSetNewAdd";
		}
		
		

}
