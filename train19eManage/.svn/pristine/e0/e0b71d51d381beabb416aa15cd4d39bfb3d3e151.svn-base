package com.l9e.transaction.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.l9e.transaction.service.ElongExcelService;
import com.l9e.transaction.vo.ElongExcelVo;
import com.l9e.transaction.vo.ElongVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.CommStr;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.PageUtil;

/**
 * 代理管理表格下载列表
 * @author zhangjc02
 *
 */
@Controller
@RequestMapping("/elongExcel")
public class ElongExcelController extends BaseController {
	private static final Logger logger = Logger.getLogger(ElongBookController.class);
	@Resource
	private ElongExcelService elongExcelService;
	
	@RequestMapping("/queryExcelList.do")
	public String queryExcelList(HttpServletRequest request,
			HttpServletResponse response) {
		
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		
		List<String> channelList = this.getParamToList(request,"channel");
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		paramMap.put("channel", channelList);
		
		int totalCount = elongExcelService.getExcelListCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());
		paramMap.put("pageSize", page.getEveryPageRecordCount());
		List<Map<String,Object>> excelList = elongExcelService.getExcelList(paramMap);
			
		request.setAttribute("isShowList", 1);
		request.setAttribute("excelList", excelList);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("channelStr", channelList.toString());
		request.setAttribute("Channel", ElongVo.getQunarChannel());
			return "elongExcel/excelList";
	}

	
	@RequestMapping("/deleteExcelById.do")
	public void deleteExcelById(HttpServletRequest request, HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String user = loginUserVo.getReal_name();//获取当前登录人
		String excel_id = this.getParam(request, "excel_id");
		String excel_url = this.getParam(request, "excel_url");
		elongExcelService.deleteExcelById(request,response,excel_id);
		logger.info(user + "点击了【下载表格】中删除按钮");
		
		File file=new File(excel_url); 
		if(file.exists()){
       		if(file.getParentFile().isDirectory()){
          		file.delete();
          		logger.info("删除文件："+file);
          		file.getParentFile().delete();
          		logger.info("删除文件目录："+file.getParentFile());
          	}
        }
	}
	
	
	@SuppressWarnings("static-access")
	private String getDate(){
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		theCa.add(theCa.DATE, -1); 
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String querydate=df.format(date);
		return querydate;
	}
	
	//下载
	@RequestMapping("/uploadExcel.do")
	public void uploadExportexcel(HttpServletRequest request,
			HttpServletResponse response) {
		LoginUserVo loginUserVo = (LoginUserVo) request.getSession().getAttribute("loginUserVo");
		String opt_name = loginUserVo.getReal_name();//得到操作人的姓名
		logger.info(opt_name+"点击了【表格下载】的‘下载’");
		/******************************页面表单参数********************************/
		String path = this.getParam(request, "excel_url");
		logger.info("执行文件下载"+path);
		// ************取得文件的路径和文件名***************//
		path = CommStr.javaEncoder(path);
		String fileName = path.substring(path.lastIndexOf(File.separator) + 1);
		// ************判断文件是否存在********************//
		File file = new File(path);
		if (!file.exists()) {
			logger.error("文件下载失败：文件或路径错误");
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
		} catch (IOException e) {
			logger.error("文件下载失败：" + e.getMessage());
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
				logger.error(ex.getMessage());
			}
		}
	}

	
	/*
	 * 上传同程对账表格
	 */
	//查询页面
	@RequestMapping("/tcAddCheckPage.do")
	public String tcAddCheckPage(HttpServletRequest request,HttpServletResponse response){
		Calendar calendar = Calendar.getInstance();
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String begin_info_time = df.format(date);
		String alert = this.getParam(request, "alert");
		return "redirect:/elongExcel/tcAddCheckList.do?begin_info_time="+begin_info_time+"&notify_status=00&alert="+alert;
	}
	@RequestMapping("/tcAddCheckList.do")
	public String tcAddCheckList(HttpServletRequest request,HttpServletResponse response){
		String begin_info_time = this.getParam(request, "begin_info_time");
		String end_info_time = this.getParam(request, "end_info_time");
		String order_id = this.getParam(request, "order_id");
		String alert = this.getParam(request, "alert");
		List<String> notifyList = this.getParamToList(request,"notify_status");
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("begin_info_time", begin_info_time);
		paramMap.put("end_info_time", end_info_time);
		paramMap.put("notify_status", notifyList);
		paramMap.put("order_id", order_id);
		int totalCount = elongExcelService.getTcCheckCount(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());
		paramMap.put("pageSize", page.getEveryPageRecordCount());
		List<Map<String,Object>> checkList = elongExcelService.getTcCheckList(paramMap);
			
		request.setAttribute("isShowList", 1);
		request.setAttribute("checkList", checkList);
		request.setAttribute("alert", alert);
		request.setAttribute("order_id", order_id);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("notifyStr", notifyList.toString());
		request.setAttribute("notifyStatus", ElongExcelVo.getNotifyStatus());
		request.setAttribute("checkType", ElongExcelVo.getSettlementType());
		return  "tcCheck/tcCheckList";
	}
	//跳转导入页面
	@RequestMapping("/tcAddCheckAdds.do")
	public String tcAddCheckAdds(HttpServletRequest request,HttpServletResponse response){
		return  "tcCheck/tcCheckAdds";
	}
	//Excel批量导入
	@RequestMapping("/tcAddCheck.do")
	public String tcAddCheck(HttpServletRequest request,HttpServletResponse response)throws IOException{
		logger.info("上传同程对账表格start");
		try {
			request.setCharacterEncoding("utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		LoginUserVo loginUserVo = (LoginUserVo)request.getSession().getAttribute("loginUserVo");
		String opt_person = loginUserVo.getReal_name();
		String path = "";
		path = this.getParam(request, "file");
		 // 检查
		String newPath = null;
		//上传文件
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile upfile = multipartRequest.getFile("excelFile");
		if(!upfile.isEmpty()){
			String fileSuffix = upfile.getOriginalFilename().substring(upfile.getOriginalFilename().lastIndexOf("."));
			String filename = upfile.getOriginalFilename();
			logger.info("filename="+filename);
			String today = DateUtil.dateToString(new Date(), "yyyy-MM-dd");
			String excel_id = "excel"+DateUtil.dateToString(new Date(), "yyyyMMdd");
			Map<String,String> excel = new HashMap<String,String>();
			excel.put("today", today);
			excel.put("excel_id", excel_id);
			excel.put("filename", filename);
			List<String> filenameList = elongExcelService.getFileNameById(excel);
			logger.info("filenameList.contains(filename)="+filenameList.contains(filename));
			if(filenameList.toString().trim().contains(filename.trim())){
				logger.info("filename="+filename+":已经导入，勿重复导入！");
				return "redirect:/elongExcel/tcAddCheckPage.do?alert=1";
			}else{
				logger.info("filename="+filename+":首次导入，添加导入日志！");
				 elongExcelService.addFileNameLogs(excel);
			}
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
		logger.info("path是："+path); 
		File file=new File(newPath); 
		logger.info("file===="+file+"=======file.exists==="+file.exists()); 
        if (!file.exists()) {
            throw new FileNotFoundException("传入的文件不存在：" + path);
        }
        if (!(path.endsWith("xls") || path.endsWith("xlsx"))) {
            throw new FileFormatException("传入的文件不是excel");
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
           
            int sheetNumber =workbook.getNumberOfSheets();
            logger.info("sheet个数="+sheetNumber);
            for(int s=0;s<sheetNumber;s++){
            	logger.info("读取："+workbook.getSheetName(s));
	            Sheet sheet = workbook.getSheetAt(s);
	            int firstRowIndex = sheet.getFirstRowNum();
	            int lastRowIndex = sheet.getLastRowNum();
	            // 读取数据行
	            String content1="";
	            for (int rowIndex = firstRowIndex + 1; rowIndex <= lastRowIndex; rowIndex++) {
	                Row currentRow = sheet.getRow(rowIndex);// 当前行
	                    Cell cell1 = currentRow.getCell(0);
	                    content1 = this.getCellValue(cell1, true);
//	      		Map<String,Object> pMap = new HashMap<String,Object>();
//	      		pMap.put("order_id", content1);
	          		if((!content1.equals(""))&&content1!=null){
	          			
	          			String trade_date = this.getCellValue(currentRow.getCell(5), true);
	          			String settlement_date=this.getCellValue(currentRow.getCell(6), true);
	          			
	          			String trade_date_format = this.transNumToDateStr(trade_date);
	          			String settlement_date_format = this.transNumToDateStr(settlement_date);
	          			
	          			
//	          			"供应商订单号","12306订单号","结算类型","结算金额","张数","交易时间","结算归属日期","供应商","余额"
	               		Map<String,Object> paramMap = new HashMap<String,Object>();
	                  	paramMap.put("order_id", content1);
	               		paramMap.put("out_ticket_billno", this.getCellValue(currentRow.getCell(1), true));
	               		String value =this.getCellValue(currentRow.getCell(2), true);
	               		paramMap.put("settlement_type", this.getTypeKey(value));
	               		paramMap.put("amount", this.getCellValue(currentRow.getCell(3), true));
	               		paramMap.put("quantity", this.getCellValue(currentRow.getCell(4), true));
	               		paramMap.put("trade_date", trade_date_format);
	               		paramMap.put("settlement_date", settlement_date_format);
	               		paramMap.put("channel", "10");
	               		paramMap.put("account_balance", this.getCellValue(currentRow.getCell(8), true));
	               		paramMap.put("create_time", "now()");
	               		paramMap.put("opt_person", opt_person);
	               			try {
	               				elongExcelService.tcAddCheck(paramMap);
	               			} catch (Exception e) {
	               				logger.error("插入elong_refundstation失败order_id："+content1);
	               			}
	              	}
	            }
            }
            logger.info("批量导入完成");
        } catch (Exception e) {
        	logger.error("批量导入异常");
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
          		logger.info("删除文件："+file);
          		file.getParentFile().delete();
          		logger.info("删除文件目录："+file.getParentFile());
          	}
        }
       	return "redirect:/elongExcel/tcAddCheckPage.do";
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
    
    //时间转换
    public String transNumToDateStr(String value) throws Exception{
	    double _value = Double.parseDouble(value);
	    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Integer _min = (int) Math.floor((_value - (int)_value)*86400);
	    long diffsecondes = (long)((((int)_value - 25569)*24-8)*3600+_min)*1000;
	    String format = sdf.format(diffsecondes);
	    return format;
	}
    
    
    /**
     * 取type
     */
    private String getTypeKey(String value) {
        if (value == null) {
            return "";
        }
        Map<Integer, String> Type = ElongExcelVo.getSettlementType();
   		if(Type.containsValue(value)){
   			String key ="";
   			for(int k =1;k<=Type.size();k++){
   				if(value.equals(Type.get(k))){
   					key = String.valueOf(k);
   				}
   			}
   			return key.trim();
   		}else{
   			return "";
   		}
    }
}
