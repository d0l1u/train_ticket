package com.l9e.transaction.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.l9e.common.BaseController;
import com.l9e.transaction.service.JoinUsService;
import com.l9e.transaction.service.RegisterService;
import com.l9e.transaction.vo.AccountVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.transaction.vo.RegisterVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.ExcelUtil;
import com.l9e.util.PageUtil;

/**
 * 注册管理
 * @author hejh
 *
 */

@Controller
@RequestMapping("/register")
public class RegisterController extends BaseController  {
	
	private static final Logger logger = Logger.getLogger(RegisterController.class);
	
	@Resource
	private RegisterService registerService;
	@Resource
	private JoinUsService joinUsService ;
	
	@RequestMapping("/queryRegisterPage.do")
	public String queryRegisterPage(HttpServletRequest request, HttpServletResponse response){
		return "redirect:/register/queryRegisterList.do?regist_status=44";
	}
		
	/**
	 * 查询列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryRegisterList.do")
	public String queryRegisterList(HttpServletRequest request, HttpServletResponse response){

		/*************************查询条件***************************/
		String register_id = this.getParam(request, "register_id");
		String user_name = this.getParam(request, "user_name");
		String ids_card = this.getParam(request, "ids_card");
		String user_phone = this.getParam(request, "user_phone");
		String regist_phone = this.getParam(request, "regist_phone");
		String account_name = this.getParam(request, "account_name");
		String account_pwd = this.getParam(request, "account_pwd");
		String user_id = this.getParam(request, "user_id");
		String create_time = this.getParam(request, "create_time");
		String start_create_time = this.getParam(request, "start_create_time");
		String end_create_time = this.getParam(request, "end_create_time");
		String opt_person = this.getParam(request, "opt_person");
		//String account_source = this.getParam(request, "account_source");
		List<String> sourceList = this.getParamToList(request, "account_source");
		List<String> statusList = this.getParamToList(request, "regist_status");
		List<String> outputList = this.getParamToList(request, "is_output");
		String mail_163 = this.getParam(request, "mail_163");
		String mail_19trip = this.getParam(request, "mail_19trip");
		String mail_qita = this.getParam(request, "mail_qita");
		String mail = this.getParam(request, "mail");
		
		String start_regist_time = this.getParam(request, "start_regist_time");
		String end_regist_time = this.getParam(request, "end_regist_time");
		/*************************创建Map***************************/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("register_id", register_id);
		paramMap.put("user_name", user_name);
		paramMap.put("ids_card", ids_card);
		paramMap.put("user_phone", user_phone);
		paramMap.put("regist_phone", regist_phone);
		paramMap.put("account_name", account_name);
		paramMap.put("account_pwd", account_pwd);
		paramMap.put("user_id", user_id);
		paramMap.put("create_time", create_time);
		paramMap.put("opt_person", opt_person);
		paramMap.put("start_create_time", start_create_time);
		paramMap.put("end_create_time", end_create_time);
		paramMap.put("regist_status", statusList);
		paramMap.put("is_output", outputList);
		paramMap.put("account_source", sourceList);
		paramMap.put("mail_163", mail_163);
		paramMap.put("mail_19trip", mail_19trip);
		paramMap.put("mail_qita", mail_qita);
		paramMap.put("mail", mail);
		paramMap.put("mail_set", 2);
		if(!"".equals(mail_163) || !"".equals(mail_19trip) || !"".equals(mail_qita)){
			paramMap.put("mail_set", -1);
		}
		paramMap.put("start_regist_time", start_regist_time);
		paramMap.put("end_regist_time", end_regist_time);
		
		/*************************分页条件***************************/
		int totalCount = registerService.queryRegisterListCount(paramMap);//总条数	
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
			
		/*************************执行查询***************************/
		List<Map<String,Object>> registerList = registerService.queryRegisterList(paramMap);
		
		/*************************request绑定***************************/
		request.setAttribute("registerList", registerList);
		request.setAttribute("register_id", register_id);
		request.setAttribute("user_name", user_name);
		request.setAttribute("ids_card", ids_card);
		request.setAttribute("user_phone", user_phone);
		request.setAttribute("regist_phone", regist_phone);
		request.setAttribute("account_name", account_name);
		request.setAttribute("account_pwd", account_pwd);
		request.setAttribute("user_id", user_id);
		request.setAttribute("create_time", create_time);
		request.setAttribute("opt_person", opt_person);
		request.setAttribute("start_create_time", start_create_time);
		request.setAttribute("end_create_time", end_create_time);
		request.setAttribute("isShowList", 1);
		request.setAttribute("StatusStr", statusList.toString());
		request.setAttribute("sourceStr", sourceList.toString());
		request.setAttribute("is_outputStr", outputList.toString());
		request.setAttribute("regist_status", RegisterVo.getRegist_Status());
		request.setAttribute("account_source", RegisterVo.getAccount_Source());
		request.setAttribute("is_output", RegisterVo.getIs_Output());
		request.setAttribute("mail_163", mail_163);
		request.setAttribute("mail_19trip", mail_19trip);
		request.setAttribute("mail_qita", mail_qita);
		request.setAttribute("mail", mail);
		
		request.setAttribute("start_regist_time", start_regist_time);
		request.setAttribute("end_regist_time", end_regist_time);
		return "register/registerList";
	}
	
	/**
	 * 查询明细
	 * @param request
	 * @return
	 */
	@RequestMapping("queryRegisterInfo.do")
	public String queryRegisterInfo(HttpServletRequest request, HttpServletResponse response){
		
		/*********************查询条件************************/
		String regist_id = this.getParam(request, "regist_id");
		String isShow=this.getParam(request, "isShow");
		logger.info("【查询明细】queryRegisterInfo.do 【注册ID："+regist_id+"】");
		
		/*********************Map************************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("regist_id", regist_id);;
		
		/*********************执行service************************/
		List<Map<String,Object>>registerInfoList = registerService.queryRegisterInfo(paramMap);
		Map<String,Object>registerInfo = registerInfoList.get(0);
		
		/*********************request绑定************************/
		request.setAttribute("registerInfo", registerInfo);
		request.setAttribute("regist_status", RegisterVo.getRegist_Status());
		request.setAttribute("account_source", RegisterVo.getAccount_Source());
		request.setAttribute("is_output", RegisterVo.getIs_Output());
		if(StringUtils.isEmpty(isShow)){
			request.setAttribute("isShow", "1");
		}else{
			request.setAttribute("isShow", isShow);
		}
		return "register/registerInfo";
	}
	
	//查询明细
	@RequestMapping("/queryRegisterFailInfo.do")
	@ResponseBody
	public void queryRegisterFailInfo(HttpServletResponse response,HttpServletRequest request){
		String regist_id = this.getParam(request, "regist_id");
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("regist_id", regist_id);;
		List<Map<String,Object>>registerInfoList = registerService.queryRegisterInfo(paramMap);
		//System.out.println("~~~~~~~~~~~"+regist_id+"~~~~~~~~~~~~~~~"+registerInfoList.get(0).get("fail_reason"));
		JSONArray jsonArray = JSONArray.fromObject(registerInfoList);  
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


	/**
	 * 更改12306账号和密码
	 * @param request
	 * @param response
	 */
	@RequestMapping("save12306Register.do")
	public void save12306Register(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_person =loginUserVo.getReal_name();//获取当前登录的人
		String flag = "error";
		String regist_id = this.getParam(request, "regist_id");
		String account_name = this.getParam(request, "account_name");//更改后的账号和密码
		String account_pwd = this.getParam(request, "account_pwd");
		String account_name1 = this.getParam(request, "account_name1");//更改前的账号和密码
		String account_pwd1 = this.getParam(request, "account_pwd1");
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("regist_id", regist_id);
		paramMap.put("account_name", account_name);
		paramMap.put("account_pwd", account_pwd);
		paramMap.put("opt_person", opt_person);
		registerService.update12306Register(paramMap);
		flag = "success";
		logger.info(opt_person+"更改了12306账号和密码，更改前为："+account_name1+","+account_pwd1+"更改后为："+account_name+","+account_pwd);
		try {
			response.getWriter().write(flag);
			response.getWriter().flush();
			response.getWriter().close() ;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 更改状态为成功
	 * @param request
	 * @return
	 */
	@RequestMapping("updateRegisterSuccess.do")
	public String updateRegisterSuccess(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_person =loginUserVo.getReal_name();//获取当前登录的人
		/*********************查询条件************************/
		String regist_id = this.getParam(request, "regist_id");
		/*********************Map************************/
		Map<String,Object>updateMap = new HashMap<String,Object>();
		updateMap.put("regist_id", regist_id);
		updateMap.put("opt_person", opt_person);
		/*********************执行service************************/
		registerService.updateRegisterSuccess(updateMap);
		//registerService.addAccountInfo(updateMap);
		
		String user_id = this.getParam(request, "user_id");
		Map<String,Object>updateGrade = new HashMap<String,Object>();
		updateGrade.put("user_id", user_id);
		int totalCount = joinUsService.ueryUserRegistInfoCount(updateGrade);//注册信息总条数
		List<Map<String, String>> userRegistInfo = joinUsService.queryUserRegistInfo(updateGrade);//注册信息明细
		String user_status = null;
		int passNum = 0;
		int waitNum = 0;
		int nopassNum = 0;
	    for(int i=0; i<totalCount; i++){
	    	user_status = userRegistInfo.get(i).get("regist_status");
	    	if(user_status.equals("22")){
	    		passNum = passNum+1;
	    	}
	    	if(user_status.equals("00") || user_status.equals("11") ||user_status.equals("44") ||user_status.equals("55")){
	    		waitNum = waitNum+1;
	    	}
	    	if(user_status.equals("33")){
	    		nopassNum = nopassNum+1;
	    	}
	    }
	    if(passNum==0 && waitNum==0 && nopassNum==0){
	    	updateGrade.put("agent_grade", "44");
	    	registerService.updateRegisterSuccessGrade(updateGrade);
	    }
	    if(passNum != 0){
	    	if(passNum == 1 ){
	    		updateGrade.put("agent_grade", "33");
		    	registerService.updateRegisterSuccessGrade(updateGrade);
	    	}if(passNum >=5 ){
	    		updateGrade.put("agent_grade", "11");
		    	registerService.updateRegisterSuccessGrade(updateGrade);
	    	}if(passNum>=2 && passNum<=4){
	    		updateGrade.put("agent_grade", "22");
		    	registerService.updateRegisterSuccessGrade(updateGrade);
	    	}
	    }
	    if(passNum==0 && waitNum!=0){
	    	updateGrade.put("agent_grade", "44");
	    	registerService.updateRegisterSuccessGrade(updateGrade);
	    }
	    if(passNum==0 && waitNum==0 && nopassNum!=0){
	    	updateGrade.put("agent_grade", "44");
	    	registerService.updateRegisterSuccessGrade(updateGrade);
	    }
		
		return "redirect:/register/queryRegisterList.do";
	}
	
	/**
	 * 更改状态为失败
	 * @param request
	 * @return
	 */
	@RequestMapping("updateRegisterFail.do")
	public String updateRegisterFail(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_person =loginUserVo.getReal_name();//获取当前登录的人
		String regist_id = this.getParam(request, "regist_id");
		String fail_reason = this.getParam(request, "fail_reason");
		Map<String,Object>updateMap = new HashMap<String,Object>();
		updateMap.put("regist_id", regist_id);
		updateMap.put("fail_reason", fail_reason);
		updateMap.put("opt_person", opt_person);
		registerService.updateRegisterFail(updateMap);
		return "redirect:/register/queryRegisterList.do";
	}
	
	/**
	 * 更改状态为需要核验
	 * @param request
	 * @return
	 */
	@RequestMapping("updateRegisterCheck.do")
	public String updateRegisterCheck(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_person =loginUserVo.getReal_name();//获取当前登录的人
		String regist_id = this.getParam(request, "regist_id");
		String regist_status = this.getParam(request, "regist_status");
		Map<String,Object>updateMap = new HashMap<String,Object>();
		updateMap.put("regist_id", regist_id);
		updateMap.put("regist_status", regist_status);
		updateMap.put("opt_person", opt_person);
		registerService.updateRegisterCheck(updateMap);
		return "redirect:/register/queryRegisterList.do";
	}

	/**
	 * 进入添加页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toAddRegister.do")
	public String toAddRegister(HttpServletRequest request, HttpServletResponse response){
		return "register/registerAdd";
	}
	
	/**
	 * 添加注册页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/addRegister.do")
	public String addRegister(HttpServletRequest request, HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_person =loginUserVo.getReal_name();//获取当前登录的人
		request.setAttribute("opt_person", loginUserVo.getReal_name());
		
		String user_name = this.getParam(request, "user_name");
		String ids_card = this.getParam(request, "ids_card");
		String regist_status = "00";
		String user_phone = this.getParam(request, "user_phone");
		String user_id = this.getParam(request, "user_id");
		String account_name = this.getParam(request, "account_name");
		String account_pwd = this.getParam(request, "account_pwd");
		/***************************Map存储并修改*****************************/
		Map<String,String> add_Map = new HashMap<String,String>();
		add_Map.put("user_name", user_name);
		add_Map.put("ids_card", ids_card);
		add_Map.put("opt_person", opt_person);
		add_Map.put("regist_status", regist_status);
		add_Map.put("user_phone", user_phone);
		add_Map.put("user_id", user_id);
		add_Map.put("account_name",account_name);
		add_Map.put("account_pwd", account_pwd);
		add_Map.put("account_source", "3");
		registerService.addRegister(add_Map);
		return "redirect:/register/queryRegisterList.do";
	}
	
	//Excel批量导入
	@RequestMapping("/addExcelRegister.do")
	public String addExcelRegister(HttpServletRequest request,HttpServletResponse response) throws IOException{
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_person = loginUserVo.getReal_name();
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
        String ids_card = null;
        String user_name = null;
        String regist_phone = null;
        String user_phone = null;
        String account_name = null;
        String account_pwd = null;
        String user_id = null;
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
          	String idcard = registerService.queryRegisterIdcard(content);//从数据库中查询该邮箱地址是否存在
          	if(StringUtils.isEmpty(idcard)){
          		if((!content.equals(""))&&content!=null){
             		ids_card = sheet.getCell(0,rownum).getContents().trim();//参数顺序为（列，行）
                 	user_name = sheet.getCell(1,rownum).getContents().trim();//getContents().trim()是获取单元格内的值并去空格
                 	regist_phone = sheet.getCell(2,rownum).getContents().trim();
               		user_phone = sheet.getCell(3,rownum).getContents().trim();
               		user_id = sheet.getCell(4,rownum).getContents().trim();
               		account_name = sheet.getCell(5,rownum).getContents().trim();
               		account_pwd = sheet.getCell(6,rownum).getContents().trim();
                 		
               		Map<String,String> paramMap = new HashMap<String,String>();
                  	paramMap.put("ids_card", ids_card);
                  	paramMap.put("user_name", user_name);
                  	paramMap.put("regist_phone", regist_phone);
               		paramMap.put("user_phone", user_phone);
               		paramMap.put("user_id", user_id);
               		paramMap.put("account_name", account_name);
               		paramMap.put("account_pwd",account_pwd);
               		paramMap.put("opt_person", opt_person);
               		paramMap.put("account_source", "2");
               		paramMap.put("regist_status", "00");
               		registerService.addRegister(paramMap);
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
		return "redirect:/register/queryRegisterList.do";	
	}
	
	//查看身份证号是否存在
	@RequestMapping("/queryRegisterIdcard.do")
	@ResponseBody
	public void queryRegisterIdcard(HttpServletRequest request ,HttpServletResponse response){
		String ids_card = this.getParam(request, "ids_card");						
		String idcard = registerService.queryRegisterIdcard(ids_card);//从数据库中查询该邮箱地址是否存在
		String result = null;
		if(StringUtils.isEmpty(idcard)){
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
	
	/**
	 * 跳转到文件上传页面
	 */
	@RequestMapping("/uploadFile.do")
	public String uploadFile(HttpServletResponse response,
			HttpServletRequest request) {
		return "register/registerAdds";
	}
	
	//导出excel
	@RequestMapping("/exportexcel.do")
	public String exportExcel(HttpServletRequest request,
			HttpServletResponse response) {
		/*************************查询条件***************************/
		String register_id = this.getParam(request, "register_id");
		String user_name = this.getParam(request, "user_name");
		String ids_card = this.getParam(request, "ids_card");
		String user_phone = this.getParam(request, "user_phone");
		String account_name = this.getParam(request, "account_name");
		String account_pwd = this.getParam(request, "account_pwd");
		String user_id = this.getParam(request, "user_id");
		String create_time = this.getParam(request, "create_time");
		String start_create_time = this.getParam(request, "start_create_time");
		String end_create_time = this.getParam(request, "end_create_time");
		String opt_person = this.getParam(request, "opt_person");
		//String account_source = this.getParam(request, "account_source");
		List<String> sourceList = this.getParamToList(request, "account_source");
		List<String> statusList = this.getParamToList(request, "regist_status");
		List<String> outputList = this.getParamToList(request, "is_output");
		String mail_163 = this.getParam(request, "mail_163");
		String mail_19trip = this.getParam(request, "mail_19trip");
		String mail_qita = this.getParam(request, "mail_qita");
		String mail = this.getParam(request, "mail");
		/*************************创建Map***************************/
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("register_id", register_id);
		paramMap.put("user_name", user_name);
		paramMap.put("ids_card", ids_card);
		paramMap.put("user_phone", user_phone);
		paramMap.put("account_name", account_name);
		paramMap.put("account_pwd", account_pwd);
		paramMap.put("user_id", user_id);
		paramMap.put("create_time", create_time);
		paramMap.put("opt_person", opt_person);
		paramMap.put("start_create_time", start_create_time);
		paramMap.put("end_create_time", end_create_time);
		paramMap.put("regist_status", statusList);
		paramMap.put("is_output", outputList);
		paramMap.put("account_source", sourceList);
		paramMap.put("mail_163", mail_163);
		paramMap.put("mail_19trip", mail_19trip);
		paramMap.put("mail_qita", mail_qita);
		paramMap.put("mail", mail);
		paramMap.put("mail_set", 2);
		if(!"".equals(mail_163) || !"".equals(mail_19trip) || !"".equals(mail_qita)){
			paramMap.put("mail_set", -1);
		}

		List<Map<String, String>> reslist = registerService.queryRegisterExcel(paramMap);
		
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("user_name"));
			linkedList.add(m.get("ids_card"));
			linkedList.add(m.get("user_phone"));
			linkedList.add(m.get("user_id"));
			linkedList.add(m.get("account_name"));
			linkedList.add(m.get("account_pwd"));
			linkedList.add(m.get("mail"));
			linkedList.add(m.get("pwd"));
			linkedList.add(m.get("create_time"));
			linkedList.add(RegisterVo.getAccount_Source().get(m.get("account_source")));
			linkedList.add(m.get("opt_person"));
			linkedList.add(RegisterVo.getRegist_Status().get(m.get("regist_status")));
			linkedList.add( RegisterVo.getIs_Output().get(m.get("is_output")));
			list.add(linkedList);
		}
		String title = "火车票账号注册明细";

		String date = createDate(start_create_time, end_create_time);
		String filename = "火车票注册管理.xls";
		String[] secondTitles = { "序号", "姓名","身份证号", "联系电话", "代理商账号",
				 "12306账号", "12306密码", "邮箱 ", "邮箱密码", "创建时间", "来源","操作人","订单状态","使用情况"};
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, date,
				secondTitles, list, request, response);

		return null;
	}
	private String createDate(String start_create_time, String end_create_time) {
		String date = "日期：";
		SimpleDateFormat ss = new SimpleDateFormat("yyyy-MM-dd");
		if (start_create_time.equals(end_create_time)
				|| start_create_time == end_create_time) {
			if (start_create_time == null || "".equals(start_create_time)) {
				date += ss.format(new Date());
			} else {
				date += start_create_time;
			}
		} else {
			if (start_create_time == null || "".equals(start_create_time)) {
				if (end_create_time == null || "".equals(end_create_time)) {
					date += ss.format(new Date()) + "之前";
				} else {
					date += end_create_time + "之前";
				}
			} else {
				if (end_create_time == null || "".equals(end_create_time)) {
					date += start_create_time + "-------" + ss.format(new Date());
				} else {
					date += start_create_time + "-------" + end_create_time;
				}
			}
		}
		return date;
	}
	
}

