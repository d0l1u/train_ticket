package com.l9e.transaction.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.piccolo.io.FileFormatException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.l9e.common.BaseController;
import com.l9e.transaction.service.CheckPriceService;
import com.l9e.transaction.service.TrainSystemSettingService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.ExcelUtil;
import com.l9e.util.PageUtil;

/**
 * 失败订单统计
 * @author zhangjc02
 *
 */
@Controller
@RequestMapping("/checkPrice")
public class CheckPriceController extends BaseController{
	private static final Logger logger = Logger.getLogger(CheckPriceController.class);
	@Resource
	private CheckPriceService checkPriceService;
	@Resource 
	private TrainSystemSettingService trainSystemSettingService;
	/**
	 * 查询页面
	 */
	@SuppressWarnings("static-access")
	@RequestMapping("/queryCheckPricePage.do")
	public String queryCheckPricePage(HttpServletResponse response ,HttpServletRequest request){
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		theCa.add(theCa.DATE, 0); 
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String querydate=df.format(date);
		return "redirect:/checkPrice/queryCheckPriceList.do?begin_info_time="+querydate+"&check_status=C2&query_type=11";
	}

	/**
	 * 查询列表
	 */
	@RequestMapping("/queryCheckPriceList.do")
	public String queryCheckPriceList(HttpServletRequest request,
			HttpServletResponse response){
		/******************查询条件********************/
		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
		String end_info_time = this.getParam(request, "end_info_time");//结束时间
		String check_status = this.getParam(request, "check_status");
		String query_type = this.getParam(request, "query_type");
		String order_id = this.getParam(request, "order_id");
		List<String> channelList = this.getParamToList(request, "channel");
		
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_info_time", begin_info_time);//开始时间
		paramMap.put("end_info_time", end_info_time);//结束时间
		paramMap.put("check_status", check_status);
		paramMap.put("query_type", query_type);
		paramMap.put("order_id", order_id);
		paramMap.put("channel", channelList);
		/******************分页条件开始********************/ 
		int totalCount = checkPriceService.queryCheckPriceCounts(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/******************查询开始********************/
		List<Map<String,String>> checkPriceList = checkPriceService.queryCheckPriceList(paramMap);
		/******************Request绑定开始********************/
		
		request.setAttribute("checkPriceList", checkPriceList);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("check_status", check_status);
		request.setAttribute("query_type", query_type);
		request.setAttribute("order_id", order_id);
		request.setAttribute("channelList", channelList.toString());
		request.setAttribute("isShowList", 1);
		return  "checkPrice/checkPriceList";
	}

	//跳转到支付宝每日余额查询页面
	@RequestMapping("/queryAlipayBalance.do")
	public String alipayBalance(HttpServletRequest request,HttpServletResponse response){
		return  "checkPrice/checkPriceBalanceList";
	}
	
	/**
	 * 查询支付宝列表
	 */
	@RequestMapping("/queryAlipayBalancePage.do")
	public String queryAlipayBalanceList(HttpServletRequest request,
			HttpServletResponse response){
		/******************查询条件********************/
		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
		String end_info_time = this.getParam(request, "end_info_time");//结束时间
		String alipay_id = this.getParam(request, "alipay_id");
		if(Integer.parseInt(alipay_id)==0){
			alipay_id="";
		}else{
			alipay_id="huochepiaokuyou"+alipay_id+"@19e.com.cn";
		}
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_info_time", begin_info_time);//开始时间
		paramMap.put("end_info_time", end_info_time);//结束时间
		paramMap.put("alipay_id", alipay_id);
		
		/******************分页条件开始********************/
		int totalCount = checkPriceService.queryAlipayBalanceCounts(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/******************查询开始********************/
		List<Map<String,String>> checkPriceBalanceList = checkPriceService.queryAlipayBalanceList(paramMap);
		/******************Request绑定开始********************/
		request.setAttribute("checkPriceBalanceList", checkPriceBalanceList);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("alipay_id", alipay_id);
		request.setAttribute("isShowList", 1);
		return  "checkPrice/checkPriceBalanceList";
	}
	
	//导出支付宝账户余额excel
	@RequestMapping("/exportAlipayBalanceExcel.do")
	public String exportAlipayBalanceExcel(HttpServletRequest request,
			HttpServletResponse response) {
		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
		String end_info_time = this.getParam(request, "end_info_time");//结束时间
		String alipay_id = this.getParam(request, "alipay_id");
		if(Integer.parseInt(alipay_id)==0){
			alipay_id="";
		}else{
			alipay_id="huochepiaokuyou"+alipay_id+"@19e.com.cn";
		}
		/******************查询Map********************/
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("begin_info_time", begin_info_time);
		map.put("end_info_time", end_info_time);
		map.put("alipay_id", alipay_id);
		
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		List<Map<String, String>> checklist = checkPriceService.queryAlipayBalanceExcel(map);
		
		for (Map<String, String> m : checklist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(String.valueOf(m.get("pay_time")));
			linkedList.add(m.get("alipay_id"));
			linkedList.add(String.valueOf( m.get("account_balance")));
			list.add(linkedList);
		}
		String title = "支付宝账号每日余额";
		String filename = "支付宝账号每日余额.xls";
		String[] secondTitles = { "序号", "支付时间", "支付宝账号", "账户余额"};
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, begin_info_time,
				secondTitles, list, request, response);
		

		return null;
	}
	
	
	
	
	//跳转到Excel批量导入页面
	@RequestMapping("/addCheckPricePage.do")
	public String elongAddRefundsPage(HttpServletRequest request,HttpServletResponse response){
		String add_type = this.getParam(request, "add_type");
		String ticket_type = this.getParam(request, "ticket_type");
		request.setAttribute("ticket_type", ticket_type);
		request.setAttribute("add_type", add_type);
		return  "checkPrice/checkPriceAdds";
	}
	//Excel批量导入
	@RequestMapping("/addAlipayInfo.do")
	public String addAlipayInfo(HttpServletRequest request,HttpServletResponse response)throws IOException{
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_person = loginUserVo.getReal_name();
		/*String path = "";
		path = this.getParam(request, "file");
		 // 检查
		String newPath = null;*/
		//上传文件
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<MultipartFile> upfileList = multipartRequest.getFiles("excelFile");
		//对多个文件进行处理
		for(int i=0;i<upfileList.size();i++){
			//解压缩包内的文件名
			MultipartFile upfile = upfileList.get(i);
			String fileSuffix = upfile.getOriginalFilename().substring(upfile.getOriginalFilename().lastIndexOf("."));
			String fileName=upfile.getOriginalFilename();
			String prePath = multipartRequest.getSession().getServletContext().getRealPath("/upload");
			String date = DateUtil.dateToString(DateUtil.dateAddDays(new Date(), -1), "yyyy-MM-dd");
			//prePath = prePath + "/" + date;
			prePath="/data/train/upRar/"+date;
			if(!upfile.isEmpty()){
			if(".rar".equalsIgnoreCase(fileSuffix)  || ".zip".equalsIgnoreCase(fileSuffix)||".csv".equalsIgnoreCase(fileSuffix)){
				try {
			        File targetFile = new File(prePath, fileName);  
			        if(!targetFile.exists()){
			        	targetFile.mkdirs();
			        }
					upfile.transferTo(targetFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		if(".xls".equalsIgnoreCase(fileSuffix)  || ".xlsx".equalsIgnoreCase(fileSuffix)){
			String xlsFileName = CreateIDUtil.createID("XLS") + fileSuffix;
	        File targetFile = new File(prePath, xlsFileName);  
	        if(!targetFile.exists()){
	        	targetFile.mkdirs();
	        }
	        String newXlsPath = prePath + "/" + xlsFileName; 
			upfile.transferTo(targetFile);
			File xlsFile=new File(newXlsPath); 
			// 获取workbook对象
	        Workbook workbook = null;
	        InputStream is =null;
	        try {
	        	is= new FileInputStream(newXlsPath);
	            if (newXlsPath.endsWith("xls")) {
	                workbook = new HSSFWorkbook(is);
	            } else if (newXlsPath.endsWith("xlsx")) {
	                workbook = new XSSFWorkbook(is);
	            }
	            if(newXlsPath.endsWith("xls") || newXlsPath.endsWith("xlsx")){
		            Sheet sheet = workbook.getSheetAt(0);
		            int firstRowIndex = sheet.getFirstRowNum();
		            int lastRowIndex = sheet.getLastRowNum();
		            // 读取数据行
		            String bank_pay_seq="",alipay_type="";
		            String check_seq=CreateIDUtil.createID("CH");
		            for (int rowIndex = firstRowIndex + 1; rowIndex <= lastRowIndex; rowIndex++) {
		                Row currentRow = sheet.getRow(rowIndex);// 当前行
		                    Cell cell1 = currentRow.getCell(0);
		                    bank_pay_seq = this.getCellValue(cell1, true).trim();
		                    alipay_type = this.getCellValue(currentRow.getCell(4), true).trim();
		                    if("余额购票".equals(alipay_type))alipay_type = "11";
		                    else if("余额退款".equals(alipay_type))alipay_type = "22";
		                    else alipay_type = "";
		//            if(!StringUtils.isEmpty(alipay_type)&&("余额购票".equals(alipay_type) || "余额退款".equals(alipay_type))){
		              if(!StringUtils.isEmpty(alipay_type)){
		            	Map<String,Object> pMap = new HashMap<String,Object>();
		            	pMap.put("bank_pay_seq", bank_pay_seq);
		            	pMap.put("alipay_type", alipay_type);
		//            	int counts = checkPriceService.queryAlipayCounts(pMap);//从数据库中查询该订单号是否已经导入
		            	if((!bank_pay_seq.equals(""))&&bank_pay_seq!=null){
		            		Map<String,Object> paramMap = new HashMap<String,Object>();
		            		paramMap.put("bank_pay_seq", bank_pay_seq);//支付宝流水号
		            		paramMap.put("pay_time", this.getCellValue(currentRow.getCell(1), true).trim());//时间
		            		paramMap.put("refund_money", this.getCellValue(currentRow.getCell(2), true).trim());//收入金额（退款）
		            		paramMap.put("pay_money", this.getCellValue(currentRow.getCell(3), true).trim());//支出金额（出票）
		            		paramMap.put("alipay_type", alipay_type);//类型
		            		paramMap.put("alipay_id", this.getCellValue(currentRow.getCell(5), true));//支付宝账号
		            		
		            		paramMap.put("opt_person", opt_person);//操作人
		            		paramMap.put("check_seq", check_seq);//单次上传标识
		            		paramMap.put("create_time", "now()");//上传时间
		//            		if(counts!=0){
		//            			paramMap.put("add_status", "11");
		//            		}
		            		try {
		            			checkPriceService.addAlipayInfo(paramMap);
		            		} catch (Exception e) {
		            			logger.error("插入addAlipayInfo失败bank_pay_seq："+bank_pay_seq);
		            		}
		            	}
		            }
		        }
	       }
		} catch (Exception e) {
			logger.error("addAlipayInfo批量导入异常");
            e.printStackTrace();
		}finally{
			 if (is != null) {
	                try {
	                    is.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
		}
		 if(xlsFile.exists()){
	       		if(xlsFile.getParentFile().isDirectory()){
	       			xlsFile.delete();
	          		logger.info("addAlipayInfo删除文件："+xlsFile);
	          		xlsFile.getParentFile().delete();
	          		logger.info("addAlipayInfo删除文件目录："+xlsFile.getParentFile());
	          	}
	        }	
		}
	}
}
       	return "redirect:/checkPrice/queryCheckPricePage.do";
	}
	
	
	
	//Excel批量导入
	@RequestMapping("/updateOrderInfo.do")
	public String updateOrderInfo(HttpServletRequest request,HttpServletResponse response)throws IOException{
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_person = loginUserVo.getReal_name();
		String ticket_type = this.getParam(request, "ticket_type");
		String path = "";
		path = this.getParam(request, "file");
		 // 检查
		String newPath = null;
		//上传文件
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile upfile = multipartRequest.getFile("excelFile");
		if(!upfile.isEmpty()){
			String fileSuffix = upfile.getOriginalFilename().substring(upfile.getOriginalFilename().lastIndexOf("."));

			if(".xls".equalsIgnoreCase(fileSuffix)  || ".xlsx".equalsIgnoreCase(fileSuffix)){
				String fileName = CreateIDUtil.createID("XLS") + fileSuffix;
				String prePath = multipartRequest.getSession().getServletContext().getRealPath("/upload");
				String date = DateUtil.dateToString(new Date(), "yyyyMMdd");
				prePath = prePath + "/" + date;
		        File targetFile = new File(prePath, fileName);  
		        if(!targetFile.exists()){
		        	targetFile.mkdirs();
		        }
		        newPath = prePath + "/" + fileName; 
				upfile.transferTo(targetFile);
			}
		}
		logger.info("updateOrderInfo path是："+path); 
		File file=new File(newPath); 
		logger.info("updateOrderInfo:file===="+file+"=======file.exists==="+file.exists()); 
        if (!file.exists()) {
            throw new FileNotFoundException("updateOrderInfo传入的文件不存在：" + path);
        }
        if (!(path.endsWith("xls") || path.endsWith("xlsx"))) {
            throw new FileFormatException("updateOrderInfo传入的文件不是excel");
        }
        // 获取workbook对象
        Workbook workbook = null;
        InputStream is =null;
        try {
            is= new FileInputStream(newPath);
            if (newPath.endsWith("xls")) {
                workbook = new HSSFWorkbook(is);
            } else if (newPath.endsWith("xlsx")) {
                workbook = new XSSFWorkbook(is);
            }
            Sheet sheet = workbook.getSheetAt(0);
            int firstRowIndex = sheet.getFirstRowNum();
            int lastRowIndex = sheet.getLastRowNum();
            // 读取数据行
            String order_id="";
            for (int rowIndex = firstRowIndex + 1; rowIndex <= lastRowIndex; rowIndex++) {
                Row currentRow = sheet.getRow(rowIndex);// 当前行
                    Cell cell1 = currentRow.getCell(0);
                    order_id = this.getCellValue(cell1, true).trim();
              if(!StringUtils.isEmpty(order_id)){
            	Map<String,Object> pMap = new HashMap<String,Object>();
            	pMap.put("order_id", order_id);
            		Map<String,Object> paramMap = new HashMap<String,Object>();
            		paramMap.put("order_id", order_id);
            		paramMap.put("bank_pay_seq", this.getCellValue(currentRow.getCell(1), true).trim());//时间
            		paramMap.put("opt_person", opt_person);//操作人
            		paramMap.put("option_time", "now()");//上传时间
            		paramMap.put("ticket_type", ticket_type);
            		try {
            			int count = checkPriceService.updateOrderInfo(paramMap);
            			
            			if(count<=0) logger.error("修改updateOrderInfo失败order_id："+order_id+"|count:"+count);
            			else logger.info("修改updateOrderInfo-->order_id："+order_id +"|count:"+count);
            		} catch (Exception e) {
            			logger.error("修改updateOrderInfo失败order_id："+order_id+"|e:"+e);
            		}
            }
          }
            logger.info("updateOrderInfo批量完成");
        } catch (Exception e) {
        	logger.error("updateOrderInfo批量异常");
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(file.exists()){
       		if(file.getParentFile().isDirectory()){
          		file.delete();
          		logger.info("updateOrderInfo删除文件："+file);
          		file.getParentFile().delete();
          		logger.info("updateOrderInfo删除文件目录："+file.getParentFile());
          	}
        }
       	return "redirect:/checkPrice/queryCheckPricePage.do";
	}
        /**
         * 取单元格的值
         * @param cell 单元格对象
         * @param treatAsStr 为true时，当做文本来取值 (取到的是文本，不会把“1”取成“1.0”)
         * @return
         */
        private String getCellValue(Cell cell, boolean treatAsStr) {
            if (cell == null) {
                return "";
            }

            if (treatAsStr) {
                // 虽然excel中设置的都是文本，但是数字文本还被读错，如“1”取成“1.0”
                // 加上下面这句，临时把它当做文本来读取
                cell.setCellType(Cell.CELL_TYPE_STRING);
            }

            if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                return String.valueOf(cell.getBooleanCellValue()).trim();
            } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                return String.valueOf(cell.getNumericCellValue()).trim();
            } else {
                return String.valueOf(cell.getStringCellValue()).trim();
            }
        }
	
	
	//导出excel
	@RequestMapping("/exportCheckPriceExcel.do")
	public String exportCheckPriceexcel(HttpServletRequest request,
			HttpServletResponse response) {
		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
		String end_info_time = this.getParam(request, "end_info_time");//结束时间
		String check_status = this.getParam(request, "check_status");
		String query_type = this.getParam(request, "query_type");
		String order_id = this.getParam(request, "order_id");
		String channelList = this.getParam(request, "channel");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("begin_info_time", begin_info_time);
		map.put("end_info_time", end_info_time);
		map.put("check_status", check_status);
		map.put("query_type", query_type);
		map.put("order_id", order_id);
		map.put("channel", channelList);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		List<Map<String, String>> checklist = checkPriceService.queryCheckPriceExcel(map);
		if("11".equals(query_type)||"33".equals(query_type)){
			for (Map<String, String> m : checklist) {
				LinkedList<String> linkedList = new LinkedList<String>();
				linkedList.add(m.get("order_id"));
				linkedList.add(m.get("bank_pay_seq"));
				linkedList.add(String.valueOf( m.get("money")));
				linkedList.add(String.valueOf(m.get("alipay_money")));
				linkedList.add(String.valueOf(m.get("alipay_id")));
				linkedList.add(String.valueOf(m.get("ticket_time")));
				linkedList.add(String.valueOf(m.get("alipay_time")));
				list.add(linkedList);
			}
			String title = "导出出票对账数据明细";
			String filename = "导出出票对账数据.xls";
			if("33".equals(query_type)){
				title = "导出改签对账数据明细";
				filename = "导出改签对账数据.xls";
			}
			String[] secondTitles = { "序号", "订单号 ", "流水号", "总金额", "支付宝显示金额", "支付宝" , "出票时间", "支付宝时间"};
			@SuppressWarnings("unused")
			HSSFWorkbook book = ExcelUtil.createExcel(filename, title, begin_info_time,
					secondTitles, list, request, response);
		}else{
			for (Map<String, String> m : checklist) {
				LinkedList<String> linkedList = new LinkedList<String>();
				linkedList.add(m.get("bank_pay_seq"));
				linkedList.add(m.get("order_id"));
				linkedList.add(String.valueOf( m.get("refund_price")));
				linkedList.add(String.valueOf(m.get("refund_money")));
				linkedList.add(String.valueOf(m.get("alipay_id")));
				linkedList.add(String.valueOf(m.get("alipay_time")));
				list.add(linkedList);
			}
			String title = "导出退票对账数据明细";
			String filename = "导出退票对账数据.xls";
			String[] secondTitles = { "序号", "流水号", "订单号 ", "后台退款总金额", "支付宝退款总金额", "支付宝" , "支付宝时间"};
			@SuppressWarnings("unused")
			HSSFWorkbook book = ExcelUtil.createExcel(filename, title, begin_info_time,
					secondTitles, list, request, response);
		}

		return null;
	}
	
	//修改流水号
	@RequestMapping("/updateSeqById.do")
	public void updateSeqById(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		String order_id = this.getParam(request, "order_id");
		String bank_pay_seq = this.getParam(request, "bank_pay_seq");
		String ticket_type = this.getParam(request, "ticket_type");
		String result = "failure";
		Map<String, String> map = new HashMap<String, String>();
		map.put("order_id",order_id);
		map.put("opt_person",opt_person);
		map.put("bank_pay_seq",bank_pay_seq);
		map.put("ticket_type",ticket_type);
		try {
			int count = checkPriceService.updateSeqById(map);
			if(count>0){
				result = "success";
			}
		} catch (Exception e) {
			logger.error(" 修改流水号异常："+e);
		}
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//导出excel
	@RequestMapping("/exportAlipayExcel.do")
	public String exportAlipayExcel(HttpServletRequest request,
			HttpServletResponse response) {
		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
		String end_info_time = this.getParam(request, "end_info_time");//结束时间
		String check_status = this.getParam(request, "check_status");
		String query_type = this.getParam(request, "query_type");
		String order_id = this.getParam(request, "order_id");
		List<String> channelList = this.getParamToList(request, "channel");
		if("11".equals(query_type)||"33".equals(query_type))query_type = "11";
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("begin_info_time", begin_info_time);
		map.put("end_info_time", end_info_time);
		map.put("check_status", check_status);
		map.put("query_type", query_type);
		map.put("order_id", order_id);
		map.put("channel", channelList);
		List<Map<String, String>> alipaylist = checkPriceService.queryAlipayExcel(map);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : alipaylist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("bank_pay_seq"));
			linkedList.add(String.valueOf(m.get("pay_time")));
			linkedList.add(String.valueOf(m.get("refund_money")));
			linkedList.add(String.valueOf(m.get("pay_money")));
			String alipay_type =String.valueOf(m.get("alipay_type"));
			 if("11".equals(alipay_type))alipay_type = "余额购票";
             else if("22".equals(alipay_type))alipay_type = "余额退款";
             else alipay_type = "";
			linkedList.add(alipay_type);
			linkedList.add(String.valueOf(m.get("alipay_id")));
			list.add(linkedList);
		}
		String title = "未匹配支付宝账单明细";
		String filename = "支付宝数据.xls";
		String[] secondTitles = { "序号","流水号", "时间", "收入金额（退款）", "支出金额（出票）" , "类型", "支付宝账号"};
		@SuppressWarnings("unused")
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, begin_info_time,
				secondTitles, list, request, response);

		return null;
	}
	
	//更新退款总金额
	@RequestMapping("/updaterefund.do")
	public void updaterefund(HttpServletRequest request,
			HttpServletResponse response){
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		String result = "failure";
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Map<String, String>> list = checkPriceService.queryNeedRefund(map);
		try {
			logger.info("待更新的退款订单个数："+list.size());
			if(list.size()>0){
				int count =0;
				for(Map<String, String> t:list){
					Map<String, String> m = new HashMap<String, String>();
					m.put("order_id", t.get("order_id"));
					m.put("channel", t.get("channel"));
//					logger.info("channel="+t.get("channel"));
					String refund_price = checkPriceService.queryPriceByOrderId(m);
					m.put("refund_price", refund_price);
					m.put("opt_person", opt_person);
					int num = checkPriceService.updateRefundPrice(m);
					if(num>0){
						count++;
					}
				}
				logger.info("更新成功退款订单个数："+count);
				if(count>0){
					result = "success";
				}
			}else{
				result = "success";
			}
		} catch (Exception e) {
			logger.error("更新退款总金额异常："+e);
		}
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//跳转到查询支付宝上传情况的页面
	@RequestMapping("/queryUploadAlipay.do")
	public String queryUploadAlipay(HttpServletRequest request,HttpServletResponse response){
		String date = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
		return "redirect:/checkPrice/queryUploadAlipayList.do?date="+date+"&alipaySort=kuyou";

	}
	//查询支付宝上传情况
	@RequestMapping("/queryUploadAlipayList.do")
	public String queryUploadAlipayList(HttpServletRequest request,HttpServletResponse response){
		String alipaySort = request.getParameter("alipaySort");
		String date = request.getParameter("date");
		//在系统设置里面查询全部需要上传的支付宝文件名称
		String alipayNameAll =String.valueOf(trainSystemSettingService.querySystemRefundAndAlert(alipaySort+"_alipay")) ;
		List <Map<String,String>> result = new ArrayList<Map<String,String>>();
		if(alipayNameAll!=null){
			String []alipayList = alipayNameAll.split(",|，");
			//查询已上传的支付宝文件 check_price_alipayinfo
			if(alipaySort.equals("kuyou")){
				alipaySort="酷游";
			}
			Map<String,String> param = new HashMap<String,String>();
			param.put("date", date);
			param.put("alipaySort", alipaySort);
			List<String> uploadAlipay = checkPriceService.selectAlipay(param);
			for(int i=0;i<alipayList.length;i++){
				Map<String,String> everyAlipay = new HashMap<String,String>(); 
				if(uploadAlipay!=null&&uploadAlipay.size()>0){
					everyAlipay.put("alipayName", alipayList[i]);
					everyAlipay.put("status", "未上传");
					for(int j=0;j<uploadAlipay.size();j++){
						if((alipaySort+alipayList[i]).equals(uploadAlipay.get(j))){
							everyAlipay.put("status", "已上传");
							break;
						}
					}
				}else{
					everyAlipay.put("alipayName", alipayList[i]);
					everyAlipay.put("status", "未上传");
				}
				result.add(everyAlipay);
			}
			
		}else{
			logger.info(alipaySort+"没有配置的支付宝");
		}
		request.setAttribute("alipayList", result);
		request.setAttribute("date", date);
		request.setAttribute("alipaySort", alipaySort);
		return  "checkPrice/uploadAlipayList";
	}
	
	

	//删除选中订单
	@RequestMapping("/deleteTicket.do")
	public void deleteTicket(HttpServletRequest request,HttpServletResponse response){
		//获取id
		String check_id = this.getParam(request, "check_id");
		String ticket_type = this.getParam(request, "ticket_type");
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo"); 
		String opt_person = loginUserVo.getReal_name();//当前登录人 
		Map<String,Object> deleteMap = new HashMap<String,Object>(); 
		deleteMap.put("opt_person", opt_person);
		deleteMap.put("check_id", Integer.parseInt(check_id));
		deleteMap.put("ticket_type", ticket_type+"0");
		int del = checkPriceService.deleteTicketById(deleteMap);
		String result ="fail";
		if(del>0){
			result = "success";
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
