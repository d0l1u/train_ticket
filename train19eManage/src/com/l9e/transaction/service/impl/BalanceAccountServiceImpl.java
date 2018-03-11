package com.l9e.transaction.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.l9e.transaction.dao.BalanceAccountDao;
import com.l9e.transaction.service.BalanceAccountService;
import com.l9e.transaction.vo.LoginUserVo;
import com.l9e.util.DateUtil;
import com.l9e.util.StringUtil;

@Service("balanceAccountService")
public class BalanceAccountServiceImpl implements BalanceAccountService{

	public static final String FILENAMEXLSX = "附加订单号.xlsx";
	@Resource 
	private BalanceAccountDao balanceAccountDao;
	
	private static Logger logger=Logger.getLogger(BalanceAccountServiceImpl.class);
	@Override
	public void uploadExcel(MultipartHttpServletRequest request,
			HttpServletResponse response) {
		MultipartFile upfile = request.getFile("excelFile");
		String tipUrl ;
		String fileName =null;
		if(!upfile.isEmpty()){
			fileName=upfile.getOriginalFilename();
			String fileSuffix =fileName.substring(fileName.lastIndexOf("."));
				if(".xlsx".equalsIgnoreCase(fileSuffix)){
					String prePath = request.getSession().getServletContext().getRealPath("/upload");
					File targetFile = new File(prePath,fileName);  
					if(!targetFile.exists()){
		        	targetFile.mkdirs();
					}
					try {
						upfile.transferTo(targetFile);
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					tipUrl = request.getScheme() + "://"
						+ request.getServerName() + ":" + request.getServerPort()
						+ "/upload/" + fileName;
					try {
						updateExcel(fileName,prePath,request);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		}
	}
	/**
	 *	对账管理 txt类型文件处理
	 * 	返回csv类型数据
	 * @throws Exception 
	 * */
	@Override
	public String addTxt(MultipartHttpServletRequest request,
			HttpServletResponse response) throws Exception {
		MultipartFile upfile = request.getFile("excelFile");
		String tipUrl ;
		String fileName =null;
		if(!upfile.isEmpty()){
			fileName=upfile.getOriginalFilename();
			String fileSuffix =fileName.substring(fileName.lastIndexOf("."));
			if(".txt".equalsIgnoreCase(fileSuffix)){
				String prePath = request.getSession().getServletContext().getRealPath("/upload");
				File targetFile = new File(prePath,fileName);  
				if(!targetFile.exists()){
	        	targetFile.mkdirs();
				}
				try {
					upfile.transferTo(targetFile);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				tipUrl = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ "/upload/" + fileName;
					String separator = File.separator;
					BufferedReader br;
					long start=System.currentTimeMillis();
					int num=1;
					try {
						//gbk读取
						InputStreamReader ins=new InputStreamReader(new FileInputStream(new File(prePath+separator+fileName)));
						br = new BufferedReader(ins);
						String s = null;
						String arr[]=null;
						String splitStr=null;
						FileWriter dzpp = new FileWriter(new File(prePath+separator+"未匹配订单_"+fileName.substring(0,fileName.lastIndexOf("."))+".csv"));
						logger.info(prePath+separator+fileName.substring(0,fileName.lastIndexOf("."))+".csv");
						logger.info("上传文件读取 耗费:"+(System.currentTimeMillis()-start)+"ms");
						start=System.currentTimeMillis();
						if((s = br.readLine())!=null){
							logger.info("读取第一行标题信息"+s);
							dzpp.write(s+"\n");
						}
						while((s = br.readLine())!=null){
							splitStr=s.replaceAll("\"", "");
							arr=splitStr.split(",");
							
							Map<String,String> param=new HashMap<String,String>();
							param.put("stream_id", arr[0].trim());
							param.put("cw_seq", arr[1].trim());
							param.put("yw_seq", arr[2].trim());
							param.put("sh_order_id", arr[3].trim());
							param.put("sp_name", arr[4].trim());
							param.put("sp_create_time", arr[5].trim());
							param.put("user_account", arr[6].trim());
							param.put("in_money", arr[7].trim());
							param.put("out_money", arr[8].trim());
							param.put("surplus_money",arr[9].trim());
							param.put("channel", arr[10].trim());
							//1在线交易  2交易退款   3充值    "交易退款".equals(arr[11].trim())?"2":"在线支付".equals(arr[11].trim())?"1":"3"
							param.put("business_status",arr[11].trim());
							param.put("remark", arr[12].trim());
							param.put("our_account", arr[13].trim());
							System.out.println(arr.length);
							String order_id="";
							if(arr.length==15) {
								//有订单号的 直接插入
								order_id= arr[14].trim();
							}else{
								//无订单号的匹配
								order_id=balanceAccountDao.queryOrderIdByPaySeq(arr[3].trim());
							}
							if(order_id==null||order_id.trim().length()==0){
								//未匹配的订单号 进行输出
								dzpp.write(s+"\n");
							}
							param.put("order_id",order_id==null?"":order_id.trim());
							balanceAccountDao.insertBalanceAccountOne(param);
							num++;
						}
						/**插入到文件管理的数据表中*/
						Map<String,String> paramMap=new HashMap<String,String>();
						LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
						.getAttribute("loginUserVo");
						String username = loginUserVo.getReal_name();
						fileName="未匹配订单_"+(fileName.substring(0,fileName.lastIndexOf("."))+".csv");
						paramMap.put("filename",fileName);
						paramMap.put("filepath", prePath+separator+fileName);
						paramMap.put("opt_name", username);
						balanceAccountDao.insertFile(paramMap);
						logger.info("生成文件:"+prePath+separator+fileName+"耗时"+(System.currentTimeMillis()-start)+"ms");
						
						dzpp.flush();
						dzpp.close();
						
						return "success";
					} catch (Exception e) {
						logger.info("插入第"+num+"条数据异常"+e);
						throw new Exception(num+"");
					}
			}else{
				return "typeError";
			}
		}
			return "uploadNull";
		
	}
	/**
	 *	对账管理 Excel类型文件处理
	 * 	返回Excel类型数据
	 *  只支持2007 .xlsx类型文件
	 * */
	private  void updateExcel(String fileName,String filePath,MultipartHttpServletRequest request)throws FileNotFoundException, IOException, InvalidFormatException  {
		String separator = File.separator;
		File file=new File(filePath+separator+fileName);
		try {
			OPCPackage r=OPCPackage.open(file);
			XSSFWorkbook  wb = new XSSFWorkbook(r);    //创建工作表
			XSSFSheet  st = wb.getSheetAt(0); //暂时只取一个窗口表格
			
			
			CellStyle style=wb.createCellStyle();
			style.setFillBackgroundColor(HSSFColor.YELLOW.index);
			style.setFillForegroundColor(HSSFColor.YELLOW.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			XSSFRow row=null;//行
			XSSFCell cell=null;//单元格
			System.out.println("/**更新Excel */");
			int rowsNums=st.getLastRowNum();
			for(int i=1;i<=rowsNums;i++){
				/**更新Excel */
				row=st.getRow(i);
				if(row!=null&&row.getCell(0)!=null){
					String orderId=balanceAccountDao.queryOrderIdByPaySeq(row.getCell(1).getStringCellValue());
					if(orderId!=null){
						row.getCell(row.getLastCellNum()-1).setCellValue(orderId);
					}else{
						Map<String,String> param=new HashMap<String,String>();
						param.put("pay_time", cellGetValue(row.getCell(2)));
						param.put("buy_money", cellGetValue(row.getCell(4)));
						orderId=balanceAccountDao.queryOrderIdByOrderInfo(param);
						row.getCell(row.getLastCellNum()-1).setCellStyle(style);
						row.getCell(row.getLastCellNum()-1).setCellValue(orderId==null?"null":orderId);
					}
				}
			}
			/**插入到文件管理的数据表中*/
			Map<String,String> paramMap=new HashMap<String,String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
			.getAttribute("loginUserVo");
			String username = loginUserVo.getReal_name();
			fileName="附加订单号_"+fileName;
			paramMap.put("filename", fileName);
			paramMap.put("filepath", filePath+separator+fileName);
			paramMap.put("opt_name", username);
			balanceAccountDao.insertFile(paramMap);
			
			FileOutputStream fOut = new FileOutputStream(filePath+separator+fileName);
			wb.write(fOut);
			fOut.flush();
			fOut.close();
			r.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	private String cellGetValue(XSSFCell cell){
		String value=""; 
		switch (cell.getCellType()) {
          case HSSFCell.CELL_TYPE_STRING:
              value = cell.getStringCellValue();
              break;
          case HSSFCell.CELL_TYPE_NUMERIC:
       	   if (HSSFDateUtil.isCellDateFormatted(cell)) {
       		   Date date = cell.getDateCellValue();
       		   if (date != null) {
       			   value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
       		   } else {
       			   value = "";
       		   }
       	   } else {
       		   value = new DecimalFormat("0").format(cell.getNumericCellValue());
       	   }
       	   break;
          case HSSFCell.CELL_TYPE_FORMULA:
          // 导入时如果为公式生成的数据则无值
          if (!cell.getStringCellValue().equals("")) {
             value = cell.getStringCellValue();
          } else {
             value = cell.getNumericCellValue() + "";
          }
              break;
          case HSSFCell.CELL_TYPE_BLANK:
              break;
          case HSSFCell.CELL_TYPE_ERROR:
       	   value = "";
       	   break;
          case HSSFCell.CELL_TYPE_BOOLEAN:
       	   value = (cell.getBooleanCellValue() == true ? "Y" : "N");
       	   break;
          default: value = "";
        }
		return value;
	}
	
	@Override
	public int queryBalanceAccountListCount(Map<String, Object> paramMap) {
		return balanceAccountDao.queryBalanceAccountListCount(paramMap);
	}
	@Override
	public void updateOrderId(HttpServletRequest request,
			HttpServletResponse response, Map<String, String> paramMap) {
		int count=	balanceAccountDao.updateOrderId(paramMap);
		if(count>0){
			write(response,"true");
		}else{
			write(response,"false");
		}
	}
	private void write(HttpServletResponse response, String wrStr) {
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(wrStr);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int queryFileCount(Map<String, Object> paramMap) {
		return balanceAccountDao.queryFileCount(paramMap);
	}
	@Override
	public List<Map<String, Object>> queryFileList(Map<String, Object> paramMap) {
		return balanceAccountDao.queryFileList(paramMap);
	}
	@Override
	public void deleteFile(String id) {
		balanceAccountDao.deleteFile(id);
	}
	@Override
	public String queryFilepath(String id) {
		return balanceAccountDao.queryFilepath(id);
	}
	@Override
	public void queryOrderList(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> param=new HashMap<String, String>();
		
		String buy_money=request.getParameter("buy_money");
		String out_ticket_time_begin=request.getParameter("out_ticket_time_begin");
		out_ticket_time_begin=out_ticket_time_begin.length()==16?
				out_ticket_time_begin+":00":out_ticket_time_begin.length()==13?out_ticket_time_begin+":00:00":out_ticket_time_begin;
		String minutes=request.getParameter("minutes");
		if(StringUtil.isNotEmpty(buy_money)&&StringUtil.isNotEmpty(out_ticket_time_begin)){
			String out_ticket_time_end=DateUtil.dateToString(DateUtil.dateAddMinutes(DateUtil.stringToDate(out_ticket_time_begin, "yyyy-MM-dd HH:mm:ss"), Integer.parseInt(minutes)),"yyyy-MM-dd HH:mm:ss");
			param.put("buy_money", buy_money);
			param.put("out_ticket_time_begin", out_ticket_time_begin);
			param.put("out_ticket_time_end", out_ticket_time_end);
			List<Map<String,String>> list=new ArrayList<Map<String,String>>();
			list=balanceAccountDao.queryOrderList(param);
			
			request.setAttribute("buy_money", buy_money);
			request.setAttribute("out_ticket_time_begin", out_ticket_time_begin);
			request.setAttribute("minutes", minutes);
			request.setAttribute("acquireList", list);
			
		}
	}
	@Override
	public void updateBalancAccount(HttpServletRequest request,
			HttpServletResponse response) {
		String pay_money=request.getParameter("pay_money");	
		String time_end=request.getParameter("pay_time");
		String time_start=DateUtil.dateToString(DateUtil.dateAddMinutes(DateUtil.stringToDate(time_end, "yyyy-MM-dd HH:mm:ss"), -3),"yyyy-MM-dd HH:mm:ss");
		String order_id=request.getParameter("order_id");
		Map <String,String>param=new HashMap<String,String>();
		param.put("pay_money", pay_money);
		param.put("time_start", time_start);
		param.put("time_end", time_end);
		param.put("order_id", order_id);
		int count=balanceAccountDao.selectUpdateCount(param);
		if(count==1){
			//更新成功
			balanceAccountDao.updateBalancAccount(param);
			write(response,"success");
		}else{
			//更新失败
			write(response,count+"");
		}
	}
	@Override
	public void matchRefund(HttpServletRequest request,
			HttpServletResponse response) {
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		String dateEnd=DateUtil.dateToString(new Date(), "yyyy-MM-dd");
		String dateStart=DateUtil.dateToString(DateUtil.dateAddDays(new Date(), -1), "yyyy-MM-dd");
		Map<String,String>param=new HashMap<String,String>();
		param.put("dateEnd", dateEnd);
		param.put("dateStart", dateStart);
		
		/*List<Map<String,Object>> listApp=balanceAccountDao.queryAppRefund(param);
		List<Map<String,Object>> listInner=balanceAccountDao.queryInnerRefund(param);
		List<Map<String,Object>> listElong=balanceAccountDao.queryElongRefund(param);
		List<Map<String,Object>> listExt=balanceAccountDao.queryExtRefund(param);*/
		List<Map<String,Object>> listHc=balanceAccountDao.queryHcRefund(param);
		List<Map<String,Object>> listQunar=balanceAccountDao.queryQunarRefund(param);
		
		/*if (listApp!=null) list.addAll(listApp);
		if (listInner!=null) list.addAll(listInner);
		if (listElong!=null) list.addAll(listElong);
		if (listExt!=null) list.addAll(listExt);*/
		if (listHc!=null) list.addAll(listHc);
		if (listQunar!=null) list.addAll(listQunar);
		
		String separator = File.separator;
		String prePath = request.getSession().getServletContext().getRealPath("/upload");
		String fileName=dateStart+"退款匹配结果.csv";
		try {
			//new File(prePath+separator+fileName)
			//FileWriter tkf= new FileWriter(new File(prePath+separator+fileName));
			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File(prePath+separator+fileName), true),"UTF-8");
			//FileWriter tkf= new FileWriter(osw);
			//**引入第一行标题*/
			osw.write("order_id,sys_refund_money,zfb_refund_money,match_status,refund_status"+"\n");
			for(Map<String,Object> map:list){
				StringBuffer sb=new StringBuffer();
				String order_id=map.get("order_id").toString();
				String zfbRefundMoney=balanceAccountDao.queryRefundMoney(order_id);
				zfbRefundMoney=zfbRefundMoney==null?"0":zfbRefundMoney;
				sb.append(order_id+",")
				.append(map.get("all_refund_money")+",")
				.append(zfbRefundMoney+",")
				.append(Double.parseDouble(map.get("all_refund_money").toString())==Double.parseDouble(zfbRefundMoney)?"true":"false"+",,"+"\n");
				osw.write(sb.toString());
				
				Map<String,String> updateParam=new HashMap<String,String>();
				updateParam.put("order_id", order_id);
				if(Double.parseDouble(map.get("all_refund_money").toString())==Double.parseDouble(zfbRefundMoney)){
					updateParam.put("refund_result", "2");//匹配
				}else{
					updateParam.put("refund_result", "3");//未匹配
				}
				balanceAccountDao.updateRefundResult(updateParam);
			}
		
			/**插入到文件管理的数据表中*/
			Map<String,String> paramMap=new HashMap<String,String>();
			LoginUserVo loginUserVo = (LoginUserVo) request.getSession()
			.getAttribute("loginUserVo");
			String username = loginUserVo.getReal_name();
			paramMap.put("filename", fileName);
			paramMap.put("filepath", prePath+separator+fileName);
			paramMap.put("opt_name", username);
			balanceAccountDao.insertFile(paramMap);
			
			osw.flush();
			osw.close();
		
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("支付宝退款匹配异常"+e);
		}
		
	}
	@Override
	public List<Map<String, String>> queryBalancAccountList(
			Map<String, Object> paramMap) {
		return balanceAccountDao.queryBalancAccountList(paramMap);
	}
	public static void main(String[] args) {
		String dateEnd=DateUtil.dateToString(new Date(), "yyyy-MM-dd");
		String dateStart=DateUtil.dateToString(DateUtil.dateAddDays(new Date(), -1), "yyyy-MM-dd");
		String str="  	 	";
		System.out.println(str.trim().length());;
	}
	
}
