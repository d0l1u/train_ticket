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
import com.l9e.transaction.service.MeituanStationService;
import com.l9e.transaction.vo.MeituanVo;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.transaction.vo.PageVo;
import com.l9e.util.CreateIDUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.ExcelUtil;
import com.l9e.util.PageUtil;

/**
 * 失败订单统计
 * @author zhangjc
 *
 */
@Controller
@RequestMapping("/meituanStation")
public class MeituanStationController extends BaseController{
	private static final Logger logger = Logger.getLogger(MeituanStationController.class);
	@Resource
	private MeituanStationService meituanStationService;
	/**
	 * 查询页面
	 */
	@RequestMapping("/queryMeituanStationPage.do")
	public String queryMeituanStationPage(HttpServletResponse response ,HttpServletRequest request){
		Calendar theCa = Calendar.getInstance(); 
		theCa.setTime(new Date());  
		theCa.add(theCa.DATE, -7); 
		Date date = theCa.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String querydate=df.format(date);
		return "redirect:/meituanStation/queryMeituanStationList.do?begin_info_time="+querydate;
	}

	/**
	 * 查询列表
	 */
	@RequestMapping("/queryMeituanStationList.do")
	public String queryMeituanStationList(HttpServletRequest request,
			HttpServletResponse response){
		logger.info("【进入车站退票手动导入页面】queryMeituanStationList.do");
		/******************查询条件********************/
		String begin_info_time = this.getParam(request, "begin_info_time");//开始时间
		String end_info_time = this.getParam(request, "end_info_time");//结束时间
		
		/******************查询Map********************/
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("begin_info_time", begin_info_time);//开始时间
		paramMap.put("end_info_time", end_info_time);//结束时间
		/******************分页条件开始********************/
		int totalCount = meituanStationService.queryMeituanStationCounts(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		
		/******************查询开始********************/
		List<Map<String,String>> meituanStationList = meituanStationService.queryMeituanStationList(paramMap);
		
		/******************Request绑定开始********************/
		request.setAttribute("meituanStationList", meituanStationList);
		request.setAttribute("begin_info_time", begin_info_time);
		request.setAttribute("end_info_time", end_info_time);
		request.setAttribute("isShowList", 1);
		return  "meituanStation/meituanStationList";
	}

	/**
	 * 查询列表
	 */
	@RequestMapping("/queryMeituanStationInfo.do")
	public String queryMeituanStationInfo(HttpServletRequest request,HttpServletResponse response){
		logger.info("【进入车站退票管理手动导入明细】queryMeituanStationInfo.do");
		String refund_seq = this.getParam(request, "refund_seq");
		String order_id = this.getParam(request, "order_id");
		String begin_time = this.getParam(request, "begin_time");
		List<String> order_status = this.getParamToList(request, "order_status");
		
		Map<String,Object>paramMap = new HashMap<String,Object>();
		paramMap.put("refund_seq", refund_seq);
		paramMap.put("order_id", order_id);
		paramMap.put("order_status", order_status);
		/******************分页条件开始********************/
		int totalCount = meituanStationService.queryMeituanStationInfoCounts(paramMap);
		PageVo page = PageUtil.getInstance().paging(request, 20, totalCount);
		paramMap.put("everyPagefrom", page.getFirstResultIndex());//每页开始的序号
		paramMap.put("pageSize", page.getEveryPageRecordCount());//每页显示的条数
		/******************查询开始********************/
		List<Map<String,String>> meituanStationList = meituanStationService.queryMeituanStationInfoList(paramMap);
		
		/******************Request绑定开始********************/
		request.setAttribute("meituanStationList", meituanStationList);
		request.setAttribute("order_status", order_status.toString());
		request.setAttribute("orderStatus", MeituanVo.getOrderStatus());
		request.setAttribute("refund_seq", refund_seq);
		request.setAttribute("order_id", order_id);
		request.setAttribute("isShowList", 1);
		request.setAttribute("begin_time", begin_time);
		return  "meituanStation/meituanStationInfo";
	}

	//跳转到Excel批量导入页面
	@RequestMapping("/meituanAddRefundsPage.do")
	public String meituanAddRefundsPage(HttpServletRequest request,HttpServletResponse response){
		return  "meituanStation/meituanStationAdds";
	}
	//Excel批量导入
	@RequestMapping("/meituanAddRefunds.do")
	public String meituanAddRefunds(HttpServletRequest request,HttpServletResponse response)throws IOException{
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
            Sheet sheet = workbook.getSheetAt(0);
            int firstRowIndex = sheet.getFirstRowNum();
            int lastRowIndex = sheet.getLastRowNum();
            // 读取数据行
            String content1="";
            String refund_seq=CreateIDUtil.createID("RS");
            for (int rowIndex = firstRowIndex + 1; rowIndex <= lastRowIndex; rowIndex++) {
                Row currentRow = sheet.getRow(rowIndex);// 当前行
                    Cell cell1 = currentRow.getCell(0);
                    content1 = this.getCellValue(cell1, true);
      		Map<String,Object> pMap = new HashMap<String,Object>();
      		pMap.put("order_id", content1);
      		int counts = meituanStationService.queryCounts(pMap);//从数据库中查询该订单号是否已经导入
          		if((!content1.equals(""))&&content1!=null){
               		Map<String,Object> paramMap = new HashMap<String,Object>();
                  	paramMap.put("order_id", content1);
               		paramMap.put("opt_person", opt_person);
               		paramMap.put("refund_seq", refund_seq);
               		paramMap.put("create_time", "now()");
               		if(counts==0){
               			paramMap.put("order_status", "11");
               		}else{
               			paramMap.put("order_status", "22");
               		}
               			try {
               				meituanStationService.addMeituanStation(paramMap);
               			} catch (Exception e) {
               				logger.error("插入meituan_refundstation失败order_id："+content1);
               			}
              	}
          }
            
            Map<String,Object> paramMap1 = new HashMap<String,Object>();
	      	List<String> list1=new ArrayList<String>();
	      	list1.add("11");
	   		paramMap1.put("refund_seq", refund_seq);
	   		paramMap1.put("order_status", list1);
	   		int success_num=meituanStationService.queryCounts(paramMap1);
	   		Map<String,Object> paramMap2 = new HashMap<String,Object>();
	   		paramMap2.put("refund_seq", refund_seq);
	   		List<String> list2=new ArrayList<String>();
	      	list2.add("22");
	   		paramMap2.put("order_status", list2);
	   		int again_num=meituanStationService.queryCounts(paramMap2);
	      	Map<String,Object> paramMap = new HashMap<String,Object>();
	      	paramMap.put("order_id", content1);
	   		paramMap.put("opt_person", opt_person);
	   		paramMap.put("refund_seq", refund_seq);
       		paramMap.put("create_time", "now()");
       		paramMap.put("count", lastRowIndex);
       		paramMap.put("success_num",success_num);
       		paramMap.put("again_num",again_num);
       		paramMap.put("fail_num",lastRowIndex-success_num-1);
       		try {
   				meituanStationService.addMeituanStationTj(paramMap);
   			} catch (Exception e) {
   				logger.info("插入meituan_refundstationTj失败");
   			}
//            
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
       	return "redirect:/meituanStation/queryMeituanStationPage.do";
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
	@RequestMapping("/exportMeituanStationexcel.do")
	public String exportMeituanStationexcel(HttpServletRequest request,
			HttpServletResponse response) {
		String order_id = this.getParam(request, "order_id");
		String refund_seq = this.getParam(request, "refund_seq");
		String begin_time = this.getParam(request, "begin_time");
		List<String> order_status = this.getParamToList(request, "order_status");
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("order_id", order_id);
		map.put("refund_seq", refund_seq);
		map.put("order_status", order_status);
		List<Map<String, String>> reslist = meituanStationService.queryMeituanStationInfoExcel(map);
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		for (Map<String, String> m : reslist) {
			LinkedList<String> linkedList = new LinkedList<String>();
			linkedList.add(m.get("order_id"));
			linkedList.add(m.get("cp_id"));
			linkedList.add(String.valueOf( m.get("from_time")));
			String buy_money =String.valueOf( m.get("buy_money"));
			linkedList.add(buy_money);
			linkedList.add(String.valueOf(m.get("ticket_num")));
			linkedList.add(m.get("user_name"));
			linkedList.add("美团");
			
			int refundFinishCount=meituanStationService.queryRefundFinishCount(m.get("order_id"));
				if(refundFinishCount!=0){
					linkedList.add("退款完成");
				}else{
					linkedList.add(" ");
				}
			list.add(linkedList);
		}

		String title = "美团手动导入数据明细";
		String filename = "美团手动导入数据.xls";
		String[] secondTitles = { "序号",  "订单号 " , "车票号" , "发车时间","票价" , "票数", "乘客姓名" ,"渠道","是否退款"};
		HSSFWorkbook book = ExcelUtil.createExcel(filename, title, begin_time,
				secondTitles, list, request, response);

		return null;
	}
	
	

}
