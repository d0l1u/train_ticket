package com.l9e.transaction.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.MailService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.MailVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.CommStr;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.ExcelUtil;
import com.l9e.util.PageUtil;
/**
 * 邮箱管理
 * @author guona
 *
 */
@Controller
@RequestMapping("/mail")
public class MailContorller extends BaseController {
	private static final Logger logger = Logger.getLogger(MailContorller.class);
	@Resource
	private MailService mailService;
	
	/**
	 * 进入查询页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryMailPage.do")
	public String queryMailPage(HttpServletRequest request, HttpServletResponse response){
		request.setAttribute("statusStr", MailVo.getMailStatus());
		request.setAttribute("isOpenStr", MailVo.getMailIsOpen());
		return "redirect:/mail/queryMailList.do?status=0";
	}
	
	/**
	 * 进入查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/queryMailList.do")
	public String queryMailList(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//获得操作人姓名
		//查询参数
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		String address = this.getParam(request, "address");
		List<String> statusList = this.getParamToList(request, "status");
		List<String> isOpenList = this.getParamToList(request, "is_open");
		String mail_163 = this.getParam(request, "mail_163");
		String mail_19trip = this.getParam(request, "mail_19trip");
		String mail_qita = this.getParam(request, "mail_qita");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		paramMap.put("address", address.toString());
		paramMap.put("status", statusList);
		paramMap.put("is_open", isOpenList);
		paramMap.put("mail_163", mail_163);
		paramMap.put("mail_19trip", mail_19trip);
		paramMap.put("mail_qita", mail_qita);
		paramMap.put("mail_set", 2);
		if(!"".equals(mail_163) || !"".equals(mail_19trip) || !"".equals(mail_qita)){
			paramMap.put("mail_set", -1);
		}

		int totalCount = mailService.queryMailListCount(paramMap);//总条数	
		//分页
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		List<Map<String, String>> mailList = mailService.queryMailList(paramMap);
		
		request.setAttribute("isShowList", 1);
		request.setAttribute("statusStr", MailVo.getMailStatus());
		request.setAttribute("isOpenStr", MailVo.getMailIsOpen());
		request.setAttribute("mailList", mailList);
		request.setAttribute("status", statusList);
		request.setAttribute("is_open", isOpenList);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("address", address);
		request.setAttribute("mail_163", mail_163);
		request.setAttribute("mail_19trip", mail_19trip);
		request.setAttribute("mail_qita", mail_qita);
		return "mail/mailList";
	}
	
	/**
	 * 进入增加邮箱页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toAddMail.do")
	public String toAddMail(HttpServletRequest request, HttpServletResponse response){
		return "mail/mailAdd";
	}
	
	/**
	 * 增加邮箱页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addMail.do")
	public String addMail(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();
		request.setAttribute("opt_name", loginUserVo.getReal_name());
		String address = this.getParam(request, "address");
		String pwd = this.getParam(request, "pwd");
		/***************************Map存储并修改*****************************/
		Map<String,String> add_Map = new HashMap<String,String>();
		add_Map.put("address", address);
		add_Map.put("pwd", pwd);
		add_Map.put("opt_name", opt_name);
		mailService.addMail(add_Map);
		return "redirect:/mail/queryMailList.do";
	}
	
	/**
	 * 进入更新页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toUpdateMail.do")
	public String toUpdateMail(HttpServletRequest request,HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String user_Session_level = loginUserVo.getUser_level();
		String mail_id = this.getParam(request, "mail_id");//mail_id
		
		Map<String, String> mail = mailService.queryMailModify(mail_id);
		
		request.setAttribute("mail_id", mail_id);
		request.setAttribute("mail", mail);
		return "mail/mailModify";
	}
	
	/**
	 * 更新账号信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateMail.do")
	public String updateMail(HttpServletRequest request,HttpServletResponse response){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date = df.format(new Date());
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();
		request.setAttribute("opt_name", loginUserVo.getReal_name());
		String mail_id = this.getParam(request, "mail_id");
		String address = this.getParam(request, "address");
		String pwd = this.getParam(request, "pwd");
		logger.info(date+":"+loginUserVo.getReal_name()+"修改邮箱，邮箱ID为"+mail_id);
		/***************************Map存储并修改*****************************/
		Map<String,String>update_Map = new HashMap<String,String>();
		update_Map.put("mail_id", mail_id);
		update_Map.put("address", address);
		update_Map.put("pwd", pwd);
		update_Map.put("opt_name", opt_name);
		mailService.updateMail(update_Map);
		return "redirect:/mail/queryMailList.do";
	}
	
	/**
	 * 启用邮箱信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateMailOpen.do")
	public String updateMailOpen(HttpServletRequest request,HttpServletResponse response){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date = df.format(new Date());
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();
		request.setAttribute("opt_name", loginUserVo.getReal_name());
		String mail_id = this.getParam(request, "mail_id");
		String status = "1";
		logger.info(date+":"+loginUserVo.getReal_name()+"启用邮箱，邮箱ID为"+mail_id);
		/***************************Map存储并修改*****************************/
		Map<String,String>update_Map = new HashMap<String,String>();
		update_Map.put("mail_id", mail_id);
		update_Map.put("status", status);
		update_Map.put("opt_name", opt_name);
		mailService.updateMail(update_Map);
		return "redirect:/mail/queryMailList.do";
	}
	
	/**
	 * 停用邮箱信息
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/updateMailStop.do")
	public String updateMailStop(HttpServletRequest request,HttpServletResponse response){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		String date = df.format(new Date());
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();
		request.setAttribute("opt_name", loginUserVo.getReal_name());
		String mail_id = this.getParam(request, "mail_id");
		String status = "0";
		logger.info(date+":"+loginUserVo.getReal_name()+"启用邮箱，邮箱ID为"+mail_id);
		/***************************Map存储并修改*****************************/
		Map<String,String>update_Map = new HashMap<String,String>();
		update_Map.put("mail_id", mail_id);
		update_Map.put("status", status);
		update_Map.put("opt_name", opt_name);
		mailService.updateMail(update_Map);
		return "redirect:/mail/queryMailList.do";
	}
	
	
	@RequestMapping("/addExcelMail.do")
	public String addExcelMail(HttpServletRequest request,HttpServletResponse response) throws IOException{
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();
		String path = "";
			//path.replaceAll("/", File.separator);
		path = this.getParam(request, "file");
		String tipUrl = null;//小票地址
		String newPath = null;
		//上传文件
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		//CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile("file");
		MultipartFile upfile = multipartRequest.getFile("excelFile");
		if(!upfile.isEmpty()){
			String fileSuffix = upfile.getOriginalFilename().substring(upfile.getOriginalFilename().lastIndexOf("."));

			if(".xls".equalsIgnoreCase(fileSuffix)){
				String fileName = CreateIDUtil.createID("XLS") + fileSuffix;
				String prePath = multipartRequest.getSession().getServletContext().getRealPath("/upload");
				String date = DateUtil.dateToString(new Date(), "yyyyMMdd");
				prePath = prePath + "/" + date;
		        File targetFile = new File(prePath, fileName);  
		        if(!targetFile.exists()){
		        	targetFile.mkdirs();
		        }
				upfile.transferTo(targetFile);
				tipUrl = multipartRequest.getScheme() + "://"
					+ multipartRequest.getServerName() + ":" + multipartRequest.getServerPort()
					+ "/upload/" + date + "/" + fileName;
				newPath = prePath + "/" + fileName; 
				logger.info("tipUrl:"+tipUrl);
			}
		}
		logger.info("path是："+path); 
		File file=new File(newPath); 
		logger.info("file===="+file+"=======file.exists==="+file.exists()); 
		Workbook book = null;
        int rownum = 1;
        String address = null;
        String pwd = null;
        	try {
				book = Workbook.getWorkbook(file);
				logger.info("book===="+book);
			} catch (BiffException e) {
				logger.info("BiffException===="+book);
				e.printStackTrace();
			} catch (IOException e) {
				logger.info("IOException===="+book);
				e.printStackTrace();
			}
        	Sheet sheet = book.getSheet(0);//工作簿是从0开始的
        	logger.info("sheet===="+sheet);
        	logger.info("sheet.getRows()===="+sheet.getRows());
          	for(rownum=1;rownum<sheet.getRows();rownum++){
          		Cell cell = sheet.getCell(0, rownum);
          		//if(cell.getType() != CellType.EMPTY){
          		String content = cell.getContents().trim();
          		String add = mailService.queryMailAddress(content);//从数据库中查询该邮箱地址是否存在
          		if(StringUtils.isEmpty(add)){
          			if((!content.equals(""))&&content!=null){
              			address = sheet.getCell(0,rownum).getContents().trim();//参数顺序为（列，行）
                  		pwd = sheet.getCell(1,rownum).getContents().trim();//getContents().trim()是获取单元格内的值并去空格
                  		Map<String,String> paramMap = new HashMap<String,String>();
                  		paramMap.put("address", address);
                  		paramMap.put("pwd", pwd);
                  		paramMap.put("opt_name", opt_name);
                  		mailService.addMail(paramMap);
              		}
          		}
          	}
          	book.close();
          	if(file.exists()){
          		if(file.getParentFile().isDirectory()){
          			file.delete();
          			logger.info("删除文件："+file);
          			file.getParentFile().delete();
          			logger.info("删除文件目录："+file.getParentFile());
          		}
          	}
		return "redirect:/mail/queryMailList.do";
	}
	
	//查看邮箱地址是否存在
	@RequestMapping("/queryMailAddress.do")
	@ResponseBody
	public void queryMailAddress(HttpServletRequest request ,HttpServletResponse response){
		String address = this.getParam(request, "address");						
		String add = mailService.queryMailAddress(address);//从数据库中查询该邮箱地址是否存在
		String result = null;
		if(StringUtils.isEmpty(add)){
			result = "no";
		}else{
			result = "yes";
		}
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//导出excel
	@RequestMapping("/exportexcel.do")
	public String exportExcel(HttpServletRequest request,
			HttpServletResponse response) {
		//查询参数
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		String address = this.getParam(request, "address");
		List<String> statusList = this.getParamToList(request, "status");
		List<String> isOpenList = this.getParamToList(request, "is_open");
		String mail_163 = this.getParam(request, "mail_163");
		String mail_19trip = this.getParam(request, "mail_19trip");
		String mail_qita = this.getParam(request, "mail_qita");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		paramMap.put("address", address.toString());
		paramMap.put("status", statusList);
		paramMap.put("is_open", isOpenList);
		paramMap.put("mail_163", mail_163);
		paramMap.put("mail_19trip", mail_19trip);
		paramMap.put("mail_qita", mail_qita);
		paramMap.put("mail_set", 2);
		if(!"".equals(mail_163) || !"".equals(mail_19trip) || !"".equals(mail_qita)){
			paramMap.put("mail_set", -1);
		}
		List<Map<String, String>> reslist = mailService.queryMailExcel(paramMap);
		
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("address"));
			linkedList.add(m.get("pwd"));
			linkedList.add(MailVo.getMailStatus().get(m.get("status")));
			linkedList.add(String.valueOf(m.get("create_time")));
			linkedList.add(String.valueOf(m.get("option_time")));
			linkedList.add(m.get("opt_name"));
			list.add(linkedList);
		}
		String title = "火车票邮箱管理明细";

		String date = createDate(begin_info_time, end_info_time);
		String filename = "火车票邮箱管理.xls";
		String[] secondTitles = { "序号", "邮箱地址","邮箱密码", "状态", "创建时间",
				 "操作时间", "操作人"};
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);

		return null;
	}
	private String createDate(String begin_info_time, String end_info_time) {
		String date = "日期：";
		SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
		if (begin_info_time.equals(end_info_time)
				|| begin_info_time == end_info_time) {
			if (begin_info_time == null || "".equals(begin_info_time)) {
				date += ss.format(new Date());
			} else {
				date += begin_info_time;
			}
		} else {
			if (begin_info_time == null || "".equals(begin_info_time)) {
				if (end_info_time == null || "".equals(end_info_time)) {
					date += ss.format(new Date()) + "之前";
				} else {
					date += end_info_time + "之前";
				}
			} else {
				if (end_info_time == null || "".equals(end_info_time)) {
					date += begin_info_time + "-------" + ss.format(new Date());
				} else {
					date += begin_info_time + "-------" + end_info_time;
				}
			}
		}
		return date;
	}
	
}
