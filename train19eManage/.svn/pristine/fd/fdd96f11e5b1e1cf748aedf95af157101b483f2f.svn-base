package com.l9e.transaction.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.BalanceAccountService;
import com.l9e.transaction.vo.AppUserVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.CommStr;
import com.l9e.util.PageUtil;
/**
 * 对账管理 主控器
 * */
@Controller
@RequestMapping("/balance")
public class BalanceAccountController extends BaseController{

	private static final Logger logger = Logger.getLogger(AppUserController.class);

	@Resource
	private BalanceAccountService balanceAccountService;
	
		/**
		 * 查询支付宝账单页面
		 * @param request
		 * @param response
		 * @return
		 */
		@RequestMapping("/queryBalanceAccountList.do")
		public String queryBalanceAccountList(HttpServletRequest request,HttpServletResponse response)throws IOException{
			String begin_info_time = this.getParam(request, "begin_info_time");
			String end_info_time = this.getParam(request, "end_info_time");
			String sh_order_id = this.getParam(request, "sh_order_id");
			String orderIdIsNotNull = this.getParam(request, "orderIdIsNotNull");
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("begin_info_time", begin_info_time);
			paramMap.put("end_info_time", end_info_time);
			paramMap.put("sh_order_id",sh_order_id);
			request.setAttribute("begin_info_time", begin_info_time);
			request.setAttribute("end_info_time", end_info_time);
			request.setAttribute("end_info_time", end_info_time);
			if(sh_order_id==null||sh_order_id.length()==0){
				paramMap.put("orderIdIsNotNull", "1".equals(orderIdIsNotNull)?"isNull":"");
			}
			int pageCount = balanceAccountService.queryBalanceAccountListCount(paramMap);// 总条数
			// 分页
			PageVo page = PageUtil.getInstance().paging(request, 20, pageCount);
			paramMap.put("everyPagefrom", page.getFirstResultIndex());// 每页开始的序号
			paramMap.put("pageSize", page.getEveryPageRecordCount());// 每页显示的条数
			List<Map<String, String>> balanceAccountList = balanceAccountService
			.queryBalancAccountList(paramMap);
			
			request.setAttribute("balanceAccountList", balanceAccountList);
			request.setAttribute("isShowList", 1);
			
			
			request.setAttribute("begin_info_time",begin_info_time);
			request.setAttribute("end_info_time",end_info_time);
			request.setAttribute("sh_order_id",sh_order_id);
			request.setAttribute("orderIdIsNotNull","".equals(orderIdIsNotNull)?"2":orderIdIsNotNull);
			request.setAttribute("allCount",pageCount);
			
			
			return "balancAccount/balancAccountList";
		}
		

	/**
	 * 匹配退款
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/matchRefund.do")
	public String matchRefund(HttpServletRequest request,HttpServletResponse response)throws IOException{
			balanceAccountService.matchRefund(request,response);
			return "redirect:/balance/queryfileList.do";
	}
	
	/**
	 * 查询系统订单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryOrderList.do")
	public String queryOrderList(HttpServletRequest request,HttpServletResponse response)throws IOException{
		balanceAccountService.queryOrderList(request,response);
		return "redirect:/balance/queryfileList.do";
	}
	

	/**
	 * 人工更新订单号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateBalancAccount.do")
	public void updateBalancAccount(HttpServletRequest request,HttpServletResponse response)throws IOException{
		balanceAccountService.updateBalancAccount(request,response);
	}
	/**
	 * 上传支付宝账单文件
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/uploadExcel.do")
	public String uploadExcel(MultipartHttpServletRequest request,HttpServletResponse response)throws IOException{
		String uploadStr="";
		try {
			uploadStr = balanceAccountService.addTxt(request, response);
		} catch (Exception e) {
			uploadStr=e.getMessage();
		}
		return "redirect:/balance/queryfileList.do?uploadStr="+uploadStr;
	}
	
	
	/**
	 * 查询订单匹配页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryfileList.do")
	public String queryfileList(HttpServletRequest request,HttpServletResponse response)throws IOException{
		logger.info("执行queryFileList.do");
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		String uploadStr=this.getParam(request, "uploadStr");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		int pageCount = balanceAccountService.queryFileCount(paramMap);// 总条数
		// 分页
		PageVo page = PageUtil.getInstance().paging(request, 20, pageCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());// 每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());// 每页显示的条数
		List<Map<String, Object>> fileList = balanceAccountService
				.queryFileList(paramMap);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("fileList", fileList);
		request.setAttribute("isShowList", 1);
		request.setAttribute("uploadStr", "".equals(uploadStr)?"success":uploadStr);
		if("queryOrder".equals(this.getParam(request, "formType"))){
			balanceAccountService.queryOrderList(request,response);
		}
		// return "redirect:/hcStat/queryHcStatPage.do";
		return "balancAccount/fileList";
	}
	
	/**
	 * 更新订单号
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateOrderId.do")
	public void updateOrderId(HttpServletRequest request,HttpServletResponse response)throws IOException{
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", request.getParameter("id"));
		paramMap.put("order_id", request.getParameter("order_id"));
		balanceAccountService.updateOrderId(request,response, paramMap);
	}
	
	
	/**
	 * 文件下载
	 */
	@RequestMapping("/fileDown.do")
	public void fileDown(HttpServletRequest request,
			HttpServletResponse response) {
		String id = this.getParam(request, "id");
		String dirPath = balanceAccountService.queryFilepath(id);
		logger.info("执行文件下载"+dirPath);
		dirPath = CommStr.javaEncoder(dirPath);
		System.out.println(dirPath);
		String fileName = dirPath.substring(dirPath.lastIndexOf(File.separator) + 1);
		File file = new File(dirPath);
		if (!file.exists()) {
			System.out.println("文件下载失败：文件或路径错误");
		}
		long fileLength = file.length();
		String length = String.valueOf(fileLength);
		try {
			fileName = URLEncoder.encode(fileName,"utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setContentType("application/octet-stream");
		response.setHeader("Content-disposition", "attachment;filename="
				+ fileName);
		response.setHeader("Content_Length", length);
		FileInputStream input = null;
		ServletOutputStream output = null;
		try {
			input = new FileInputStream(file);
			output = response.getOutputStream();
			byte[] block = new byte[1024];
			int len = 0;
			while ((len = input.read(block)) != -1) {
				output.write(block, 0, len);
			}
			
		} catch (IOException e) {
			System.out.println("文件下载失败：" + e.getMessage());
		} finally {
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
	
	/**
	 *  将选中的文件信息删除
	 *  删除csv同时 删除相关的txt文件
	 */
	@RequestMapping("/fileDelete.do")
	public String fileDelete(HttpServletResponse response,HttpServletRequest request) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String username = loginUserVo.getReal_name();
		String id = this.getParam(request, "id");
		String dirPath = balanceAccountService.queryFilepath(id);
		String dirPath2=(dirPath.substring(0,dirPath.lastIndexOf("."))+".txt").replaceAll("未匹配订单_", "");
		File file = new File(dirPath);
		if(file.exists()){
			file.delete();
		}
		balanceAccountService.deleteFile(id);
		logger.info(username+"将文件信息删除,路径是:"+dirPath);
		if(dirPath.indexOf("未匹配订单_")>0){
			File file2 = new File(dirPath2);
			if(file2.exists()){
				file2.delete();
			}
			logger.info(username+"将文件信息删除,路径是:"+dirPath2);
		}
		return "redirect:/balance/queryfileList.do";
	}

}
