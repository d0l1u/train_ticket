package com.l9e.transaction.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.QuestionOrderService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.QuestionVo;
import com.l9e.util.CommStr;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.PageUtil;

@Controller
@RequestMapping("/questionOrder")
public class QuestionOrderController extends BaseController {
	private static final Logger logger = Logger.getLogger(FileController.class);
	@Resource
	private QuestionOrderService questionOrderService;
	
		//查询问题
		@RequestMapping("/gototrain_question_order.do")
		public String gototrain_question_order(HttpServletRequest request, HttpServletResponse response){
			String question_order_id=this.getParam(request, "question_order_id");
			String question_answer=this.getParam(request, "question_answer");
			String question_assigner=this.getParam(request, "question_assigner");
			String question_solve=this.getParam(request, "question_solve");
			String begin_answer_time=this.getParam(request, "begin_answer_time");
			String end_answer_time=this.getParam(request, "end_answer_time");
			String begin_solve_time=this.getParam(request, "begin_solve_time");
			String end_solve_time=this.getParam(request, "end_solve_time");
			List<String> statusList=this.getParamToList(request, "question_status");
			Map<String, Object> paramMap=new HashMap<String, Object>();
			paramMap.put("question_order_id", question_order_id);
			paramMap.put("question_answer", question_answer);
			paramMap.put("question_assigner", question_assigner);
			paramMap.put("question_solve", question_solve);
			paramMap.put("begin_answer_time", begin_answer_time);
			paramMap.put("end_answer_time", end_answer_time);
			paramMap.put("begin_solve_time", begin_solve_time);
			paramMap.put("end_solve_time", end_solve_time);
			paramMap.put("question_status", statusList);
			int totalCount = questionOrderService.querytrain_question_orderCount(paramMap);//总条数	
			//分页
			PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
			paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
			paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
			List<Map<String,Object>> train_question_orderList=questionOrderService.querytrain_question_order(paramMap);
			request.setAttribute("train_question_orderList", train_question_orderList);
			request.setAttribute("isShowList", 1);
			request.setAttribute("question_order_id", question_order_id);
			request.setAttribute("question_answer", question_answer);
			request.setAttribute("question_assigner", question_assigner);
			request.setAttribute("question_solve", question_solve);
			request.setAttribute("begin_answer_time", begin_answer_time);
			request.setAttribute("end_answer_time", end_answer_time);
			request.setAttribute("begin_solve_time", begin_solve_time);
			request.setAttribute("end_solve_time", end_solve_time);
			request.setAttribute("totalCount", totalCount);
			request.setAttribute("statusList", QuestionVo.getQuestion_status());
			request.setAttribute("question_statusStr", statusList.toString());
			return "questionOrder/questionOrderList";
		}
		//增加问题
		@RequestMapping("/addtrain_question_order.do")
		public String insertFile(HttpServletResponse response,HttpServletRequest request) {
			String question_order_id=this.getParam(request, "question_order_id");
			String question_theme=this.getParam(request, "question_theme");
			String question_desc=this.getParam(request, "question_desc");
			String question_assigner=this.getParam(request, "question_assigner");
			String question_pic=this.getParam(request, "question_pic");
			System.out.println("#########"+question_pic);
			System.out.println("question_order_id"+question_order_id+"question_theme"+question_theme+
					"question_desc"+question_desc+"question_assigner"+question_assigner);
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, Object> paramMap=new HashMap<String, Object>();
			paramMap.put("question_order_id", question_order_id);
			paramMap.put("question_pic", question_pic);
			paramMap.put("question_theme", question_theme);
			paramMap.put("question_desc", question_desc);
			paramMap.put("question_assigner", question_assigner);
			paramMap.put("question_answer", opt_person);
			paramMap.put("question_status", "11");
			paramMap.put("question_answer_time", 1);
			String question_seq =CreateIDUtil.createID("QQ");
			paramMap.put("question_seq",question_seq);//自动生成以TK开头的问题流水号
			questionOrderService.addtrain_question_order(paramMap);
			Map<String, String> log = new HashMap<String, String>();
			log.put("question_seq", question_seq);
			log.put("content", "添加新的问题,【"+question_theme+"】");
			log.put("opt_person", opt_person);
			questionOrderService.addLog(log);
			return "redirect:/questionOrder/gototrain_question_order.do";
		}
		
		//修改问题
		@RequestMapping("/updatetrain_question_order.do")
		public String updatetrain_question_order(HttpServletRequest request, HttpServletResponse response){
			String question_id=this.getParam(request, "question_id");
			String question_order_id=this.getParam(request, "question_order_id");
			String question_theme=this.getParam(request, "question_theme");
			String question_desc=this.getParam(request, "question_desc");
			String question_assigner=this.getParam(request, "question_assigner");
			String question_reply=this.getParam(request, "question_reply");
			String question_seq=this.getParam(request, "question_seq");
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, Object> paramMap=new HashMap<String, Object>();
			paramMap.put("question_id", question_id);
			paramMap.put("question_order_id", question_order_id);
			paramMap.put("question_theme", question_theme);
			paramMap.put("question_desc", question_desc);
			paramMap.put("question_assigner", question_assigner);
			paramMap.put("question_reply", question_reply);
			paramMap.put("question_answer", opt_person);
			HashMap<String, String> questionOrderInfo = questionOrderService.getquestionOrderInfo(question_id);
			if(!"".equals(question_reply) && (!question_reply.equals(questionOrderInfo.get("question_reply")))){
					paramMap.put("question_solve", opt_person);
					paramMap.put("question_solve_time", 1);
					paramMap.put("question_status", "44");
			}
			questionOrderService.updatetrain_question_order(paramMap);
			Map<String, String> log = new HashMap<String, String>();
			log.put("question_seq", question_seq);
			String content="";
			if (!"".equals(question_order_id) && !question_order_id.equals(questionOrderInfo.get("question_order_id")))
				content+="订单号:"+questionOrderInfo.get("question_order_id")+"修改为"+question_order_id+"。";
			if (!question_theme.equals(questionOrderInfo.get("question_theme")))
				content+="标题:"+questionOrderInfo.get("question_theme")+"修改为"+question_theme+"。";
			if (!"".equals(question_assigner) && !question_assigner.equals(questionOrderInfo.get("question_assigner")))
				content+="指定人:"+questionOrderInfo.get("question_assigner")+"修改为"+question_assigner+"。";
			if (!"".equals(question_desc) && !question_desc.equals(questionOrderInfo.get("question_desc")))
				content+="详细:"+questionOrderInfo.get("question_desc")+"修改为"+question_desc+"。";
			if (!"".equals(question_reply)&& !question_reply.equals(questionOrderInfo.get("question_reply")))
				content+="答复:"+questionOrderInfo.get("question_reply")+"修改为"+question_reply+"。";
			log.put("content", content);
			log.put("opt_person", opt_person);
			if(!"".equals(content))
			questionOrderService.addLog(log);
			return "redirect:/questionOrder/gototrain_question_order.do?question_status=11&question_status=33";
		}
		
		//修改问题状态
		@RequestMapping("/updateQuestionStatus.do")
		public void updateQuestionStatus(HttpServletRequest request, HttpServletResponse response){
			String question_id=this.getParam(request, "question_id");
			String question_seq=this.getParam(request, "question_seq");
			String question_status=this.getParam(request, "question_status");
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			Map<String, Object> paramMap=new HashMap<String, Object>();
			paramMap.put("question_id", question_id);
			paramMap.put("question_status", question_status);
			paramMap.put("question_solve_time", 1);
			if("22".equals(question_status)){
			paramMap.put("question_solve", opt_person);
			}
			questionOrderService.updatequestion_status(request,response,paramMap);
			HashMap<String, String> questionOrderInfo = questionOrderService.getquestionOrderInfo(question_id);
			Map<String, String> log = new HashMap<String, String>();
			log.put("question_seq", question_seq);
			if("22".equals(question_status))
			log.put("content",opt_person+ "点击了解决问题,【"+questionOrderInfo.get("question_theme")+"】");
			if("33".equals(question_status))
			log.put("content",opt_person+ "点击了问题重现,【"+questionOrderInfo.get("question_theme")+"】");
			log.put("opt_person", opt_person);
			questionOrderService.addLog(log);
			//return "redirect:/questionOrder/gototrain_question_order.do";
		}
		
		//删除问题
		@RequestMapping("/deletetrain_question_order.do")
		public String deletetrain_question_order(HttpServletRequest request, HttpServletResponse response){
			String question_id=this.getParam(request, "question_id");
			String question_seq=this.getParam(request, "question_seq");
			Map<String, Object> paramMap=new HashMap<String, Object>();
			paramMap.put("question_id", question_id);
			questionOrderService.deletetrain_question_order(paramMap);
			Map<String, String> log = new HashMap<String, String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
			String opt_person = loginUserVo.getReal_name();//当前登录人 
			log.put("content", "删除提交问题,question_seq="+question_seq);
			log.put("opt_person", opt_person);
			log.put("question_seq", question_seq);
			questionOrderService.addLog(log);
			return "redirect:/questionOrder/gototrain_question_order.do";
		}
		//跳转到增加问题页面
		@RequestMapping("/turntoAddQuestionPage.do")
		public String turntoAddQuestionPage(HttpServletRequest request, HttpServletResponse response){
			String question_pic=this.getParam(request, "question_pic");
			String fileName=this.getParam(request, "fileName");
			request.setAttribute("fileName", fileName);
			request.setAttribute("question_pic", question_pic);
			return "questionOrder/addQuestionOrder";
		}
		
		//下载图片
			@RequestMapping("/pictureDown.do")
			public void fileDown(HttpServletRequest request,
					HttpServletResponse response) {
				String question_id=this.getParam(request, "question_id");
				String pic=this.getParam(request, "pic");
				HashMap<String, String> questionOrderInfo = questionOrderService.getquestionOrderInfo(question_id);
				String question_picList =questionOrderInfo.get("question_pic");
				String question_seq =questionOrderInfo.get("question_seq");
				String[] arr = question_picList.split("@");
				String question_pic = "";
				question_pic= arr[Integer.valueOf(pic)];
				logger.info("执行文件下载"+question_pic);
				// ************取得文件的路径和文件名***************//
				question_pic = CommStr.javaEncoder(question_pic);
				System.out.println(question_pic);
				String fileName = question_pic.substring(question_pic.lastIndexOf(File.separator) + 1);
				// ************判断文件是否存在********************//
				File file = new File(question_pic);
				if (!file.exists()) {
					System.out.println("文件下载失败：文件或路径错误");
					//return false;
				}
				long fileLength = file.length();
				String length = String.valueOf(fileLength);
				try {
					fileName = URLEncoder.encode(fileName,"utf-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				// 设置返回文件的类型和头信息，application/octet-stream:文件类型的通用格式//
				response.setContentType("application/octet-stream");
				response.setHeader("Content-disposition", "attachment;filename="
						+ fileName);
				response.setHeader("Content_Length", length);
				FileInputStream input = null;
				ServletOutputStream output = null;
				try {
					// **************产生输入流和输出流*************//
					input = new FileInputStream(file);
					output = response.getOutputStream();
					byte[] block = new byte[1024];
					int len = 0;
					// **************开始下载文件*****************//
					while ((len = input.read(block)) != -1) {
						output.write(block, 0, len);
					}
					//return true;
					Map<String, String> log = new HashMap<String, String>();
					LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
					String opt_person = loginUserVo.getReal_name();//当前登录人 
					log.put("content", "下载了图片"+(Integer.valueOf(pic)+1)+",question_seq="+question_seq);
					log.put("opt_person", opt_person);
					log.put("question_seq", question_seq);
					questionOrderService.addLog(log);
				} catch (IOException e) {
					System.out.println("文件下载失败：" + e.getMessage());
					//return false;
				} finally {
					// *************关闭文件流****************//
					try {
						if (input != null) {
							input.close();
						}
						if (output != null) {
							output.flush();
							output.close();
						}
					} catch (IOException ex) {
						System.out.println(ex.getMessage());
					}
				}
			}
		
		
		//跳转到修改问题页面
		@RequestMapping("/turntoUpdateQuestionPage.do")
		public String turntoUpdateQuestionPage(HttpServletRequest request, HttpServletResponse response){
			String question_id=this.getParam(request, "question_id");
			String question_seq=this.getParam(request, "question_seq");
			String pageIndex=this.getParam(request, "pageIndex");
			HashMap<String, String> questionOrderInfo = questionOrderService.getquestionOrderInfo(question_id);
			List<Map<String, Object>> history = questionOrderService.queryHistroyByQuestion_id(question_seq);
			String question_picList =questionOrderInfo.get("question_pic");
			String[] arr = null;
			String amount ="0";
			if(!"".equals(question_picList) && question_picList!=null){
			arr = question_picList.split("@");
			amount =String.valueOf(arr.length);
			}
			request.setAttribute("questionOrderInfo", questionOrderInfo);
			request.setAttribute("amount",amount );
			request.setAttribute("history", history);
			request.setAttribute("pageIndex", pageIndex);
			return "questionOrder/questionOrderInfo";
		}
		
		
		//查询订单的操作日志
		@RequestMapping("/queryOrderOperHistory.do")
		@ResponseBody
		public void queryOrderOperHistory(HttpServletResponse response,HttpServletRequest request){
			String question_seq = this.getParam(request,"question_seq");
			List<Map<String, Object>> history = questionOrderService.queryHistroyByQuestion_id(question_seq);
			JSONArray jsonArray = JSONArray.fromObject(history);  
			response.setCharacterEncoding("utf-8");
			//System.out.println(jsonArray.toString());
			try {
				response.getWriter().write(jsonArray.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}
