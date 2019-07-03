package com.l9e.excel;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.l9e.transaction.vo.ExcelAdminVo;
import com.l9e.util.StringUtil;

public class ExcelViewAdmin extends AbstractExcelView {

	/**
	 * 超级管理员--打码统计--导出Excel
	 */
	private void excelDataAdmin(Map<String, Object> map,
			HttpServletRequest request, HttpServletResponse response)throws ParseException {
		List<ExcelAdminVo> orderlist = (List<ExcelAdminVo>) map.get("list");
//		String begin_time = (String) map.get("begin_time");
//		String end_time = (String) map.get("end_time");
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("打码统计");
		sheet.setDisplayGridlines(true);// 隐藏网格线
		// 设置各列宽度sheet.setDefaultColumnWidth((short)20);
		sheet.setColumnWidth(0, 3000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 5000);
		sheet.setColumnWidth(4, 5000);
		sheet.setColumnWidth(5, 3500);
		sheet.setColumnWidth(6, 3500);
		sheet.setColumnWidth(7, 3500);
		sheet.setColumnWidth(8, 3500);
		sheet.setColumnWidth(9, 3500);
		sheet.setColumnWidth(10, 3500);
		sheet.setColumnWidth(11, 3500);//打码成功率
		// 定义各种样式
		HSSFCellStyle firStyle = this.createMyCellStyle(workbook, 18, true,
				true, true, null);// 大标题 第一行样式
		HSSFCellStyle secStyle = this.createMyCellStyle(workbook, 12, true,
				false, true, null);// 中标题 第二、三行样式
		HSSFCellStyle thirStyle = this.createMyCellStyle(workbook, 10, true,
				true, true, null);// 小标题 第四行样式
		HSSFCellStyle contentStyle = this.createMyCellStyle(workbook, 10,
				false, true, true, "宋体");// 内容
		HSSFCellStyle dateStyle = this.createMyCellStyle(workbook, 10, false,
				true, true, "宋体");// 日期格式
		this.setCellNumberType(workbook, dateStyle, "date");
		HSSFCellStyle moneyStyle = this.createMyCellStyle(workbook, 10, false,
				true, true, "宋体");// 货币格式
		this.setCellNumberType(workbook, moneyStyle, "money");
		// 查询需要导出的信息
		Iterator<ExcelAdminVo> it = orderlist.iterator();
		HSSFRow row = null;
		int index = 0;
		SimpleDateFormat ss = new SimpleDateFormat("yyyy年MM月dd日");
		// 写入第一行
		row = sheet.createRow(index);
		row.setHeight((short) 600);
		StringBuffer header = new StringBuffer("用户打码统计");
//        if (orderlist.size() > 0) {
//        	header.append(orderlist.get(0).getProvince_name()); 
//        } header.append("出票明细");
		 
		String[] topHeaders = { header.toString(), null, null, null, null,null, null,null,null,null,null,null};

		for (int i = 0, n = topHeaders.length; i < n; i++) {
			this.createCellAndSetStrVal(row, i, firStyle, topHeaders[i]);
		}
		sheet.addMergedRegion(new Region(index, (short) 0, index, (short) 11));
		index++;
		// 写入第二行
		row = sheet.createRow(index);
		row.setHeight((short) 600);
		StringBuffer xsDate = new StringBuffer("日期：");
		DateFormat d1 = DateFormat.getDateInstance();
		Date now = new Date();
		String str1 = d1.format(now);
		xsDate.append(str1);
//		if (begin_info_time == null || begin_info_time.length() == 0) {
//			if (end_info_time == null || end_info_time.length() == 0) {
//				xsDate.append(str1);
//			} else {
//				xsDate.append(end_info_time);
//				xsDate.append("之前");
//			}
//		}
//		else {
//			if (end_info_time == null || end_info_time.length() == 0) {
//				xsDate.append(begin_info_time);
//				xsDate.append("-----");
//				xsDate.append(str1);
//			} else {
//				if (begin_info_time.equals(end_info_time)) {
//					xsDate.append(begin_info_time);
//				} else {
//					xsDate.append(begin_info_time);
//					xsDate.append("-----");
//					xsDate.append(end_info_time);
//				}
//			}
//		}

		String[] secHeaders = { xsDate.toString(), null, null, null, null,null, null,null,null,
				null,null,null};
		for (int i = 0, n = secHeaders.length; i < n; i++) {
			this.createCellAndSetStrVal(row, i, secStyle, secHeaders[i]);
		}
		sheet.addMergedRegion(new Region(index, (short) 0, index, (short) 18));
		index++;
		// 写入第三行
		row = sheet.createRow(index);
		row.setHeight((short) 1200);
		String[] headers = {"序号","操作日期","用户名","真实姓名","所在部门","打码总数","打码成功","打码失败","打码超时","打码收入","渠道","打码成功率"};
		for (int i = 0, n = headers.length; i < n; i++) {
			this.createCellAndSetStrVal(row, i, thirStyle, headers[i]);
		}
		index++;
		
		while (it.hasNext()) {
			// 每条订单数据在excel中产生1行
			ExcelAdminVo orderExcel = (ExcelAdminVo) it.next();
			// 写入第五行
			row = sheet.createRow(index);
			row.setHeight((short) 400);
			this.createCellAndSetNumberVal(row, 0, contentStyle, String.valueOf(index - 2));//序号
			if(orderExcel.getOpt_time()==null || StringUtils.isEmpty(orderExcel.getOpt_time())){//操作日期
				this.createCellAndSetStrVal(row, 1, contentStyle,"");
			}else{
				this.createCellAndSetStrVal(row, 1, contentStyle,orderExcel.getOpt_time());
			}
			if(orderExcel.getOpt_ren()==null || StringUtils.isEmpty(orderExcel.getOpt_ren())){//用户名
				this.createCellAndSetStrVal(row, 2, contentStyle,"");
			}else{
				this.createCellAndSetStrVal(row, 2, contentStyle,orderExcel.getOpt_ren());
			}
			if(orderExcel.getOpt_name()==null || StringUtils.isEmpty(orderExcel.getOpt_name())){//真实姓名
				this.createCellAndSetStrVal(row, 3, contentStyle,"");
			}else{
				this.createCellAndSetStrVal(row, 3, contentStyle,orderExcel.getOpt_name());
			}
			
			String depart1 = orderExcel.getDepartment();//从数据库得到的部门
			//String depart = (String) map.get("department");
			String department = null;
			Map<String,String> userDepartment = (Map<String, String>) map.get("userDepartment");
			for(int i=0;i<userDepartment.size();i++){
				Iterator itor= userDepartment.keySet().iterator();
				while (itor.hasNext()){
					String key = (String) itor.next();
					if(depart1==null){
						department = "其他";
					}else if(depart1.equals(key)){
						department = userDepartment.get(key);
					}
				}
				
			}
			if(orderExcel.getDepartment()==null || StringUtils.isEmpty(orderExcel.getDepartment())){//所在部门
				this.createCellAndSetStrVal(row, 4, contentStyle,"");
			}else{
				this.createCellAndSetStrVal(row, 4, contentStyle,department);
			}
			if(orderExcel.getPic_count()==null || StringUtils.isEmpty(orderExcel.getPic_count().toString())){//打码总数
				this.createCellAndSetNumberVal(row, 5, contentStyle,"");
			}else{
				this.createCellAndSetNumberVal(row, 5, contentStyle,orderExcel.getPic_count().toString());
			}
			if(orderExcel.getPic_success()==null || StringUtils.isEmpty(orderExcel.getPic_success().toString())){//打码成功
				this.createCellAndSetNumberVal(row, 6, contentStyle,"");
			}else{
				this.createCellAndSetNumberVal(row, 6, contentStyle,orderExcel.getPic_success().toString());
			}
			if(orderExcel.getPic_fail()==null || StringUtils.isEmpty(orderExcel.getPic_fail().toString())){//打码失败
				this.createCellAndSetNumberVal(row, 7, contentStyle,"");
			}else{
				this.createCellAndSetNumberVal(row, 7, contentStyle,orderExcel.getPic_fail().toString());
			}
			if(orderExcel.getPic_unkonwn()==null || StringUtils.isEmpty(orderExcel.getPic_unkonwn().toString())){//打码超时
				this.createCellAndSetNumberVal(row, 8, contentStyle,"");
			}else{
				this.createCellAndSetNumberVal(row, 8, contentStyle,orderExcel.getPic_unkonwn().toString());
			}
			
			double money = (double)orderExcel.getPic_success()/100;
			if(orderExcel.getPic_success() == null || StringUtils.isEmpty(orderExcel.getPic_success().toString())){//打码收入
				this.createCellAndSetMoneyVal(row, 9, moneyStyle,"0");
			}else{
				this.createCellAndSetMoneyVal(row, 9, moneyStyle, Double.toString(money));
			}
			
			if(orderExcel.getChannel()==null || StringUtils.isEmpty(orderExcel.getChannel())){//渠道
				this.createCellAndSetStrVal(row, 10, contentStyle,"");
			}else{
				this.createCellAndSetStrVal(row, 10, contentStyle,orderExcel.getChannel());
			}
			
			if(orderExcel.getCode_cgl()==null || StringUtils.isEmpty(orderExcel.getCode_cgl())){//打码成功率
				this.createCellAndSetStrVal(row, 11, contentStyle,"0.00%");
			}else{
				this.createCellAndSetStrVal(row, 11, contentStyle,orderExcel.getCode_cgl());
			}
			index++;
		}
		try {
			// 输出流导出
			OutputStream os;
			String title = "用户打码统计"+str1+".xls";
			if (request.getHeader("User-Agent").toLowerCase()
					.indexOf("firefox") > 0)
				title = new String(title.getBytes(), "ISO8859-1");// firefox浏览器
			else if (request.getHeader("User-Agent").toUpperCase().indexOf(
					"MSIE") > 0)
				title = URLEncoder.encode(title, "UTF-8");// IE浏览器
			response.reset();
			response.setContentType("application/msexcel");
			response.setHeader("Content-Disposition", "attachment;" + " filename=" + title);
			os = response.getOutputStream();
			workbook.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建单元格并写入文本内容
	 * 
	 * @param row行
	 * @param num列
	 * @param style单元格式样
	 * @param value单元格内容
	 */
	private void createCellAndSetStrVal(HSSFRow row, int num,
			HSSFCellStyle style, String value) {
		HSSFCell cell = row.createCell(num);
		if (style != null) {
			cell.setCellStyle(style);
		}
		if (StringUtil.isNotEmpty(value)) {
			HSSFRichTextString richstr = new HSSFRichTextString(value);
			cell.setCellValue(richstr);
		}
	}

	/**
	 * 创建单元格并写入金额
	 * 
	 * @param row行
	 * @param num列
	 * @param style单元格式样
	 * @param value单元格内容
	 */
	private void createCellAndSetMoneyVal(HSSFRow row, int num,
			HSSFCellStyle style, String value) {
		HSSFCell cell = row.createCell(num);
		if (style != null) {
			cell.setCellStyle(style);
		}
		if (StringUtil.isNotBlank(value)) {
			cell.setCellValue(Double.parseDouble(value));
		}
	}
	
	private void createCellAndSetMoneyVal(HSSFRow row, int num,
			HSSFCellStyle style, Integer value) {
		HSSFCell cell = row.createCell(num);
		if (style != null) {
			cell.setCellStyle(style);
		}
		if (StringUtil.isNotBlank(value)) {
			cell.setCellValue(Double.parseDouble(value.toString()));
		}
	}

	/**
	 * 创建单元格并写入日期
	 * 
	 * @param row行
	 * @param num列
	 * @param style单元格式样
	 * @param value单元格内容
	 */
	private void createCellAndSetDateVal(HSSFRow row, int num,
			HSSFCellStyle style, String value) {
		HSSFCell cell = row.createCell(num);
		if (style != null) {
			cell.setCellStyle(style);
		}
		if (StringUtil.isNotBlank(value)) {
			cell.setCellValue(value);
		}
	}

	/**
	 * 创建单元格并写入数字
	 * 
	 * @param row行
	 * @param num列
	 * @param style单元格式样
	 * @param value单元格内容
	 */
	private void createCellAndSetNumberVal(HSSFRow row, int num,
			HSSFCellStyle style, String value) {
		HSSFCell cell = row.createCell(num);
		if (style != null) {
			cell.setCellStyle(style);
		}
		if (StringUtil.isNotBlank(value)) {
			cell.setCellValue(Double.parseDouble(value));
		}
	}

	/**
	 * 创建单元格式样
	 * 
	 * @param workbook
	 * @param fontHeight字号
	 * @param isBold是否粗体
	 * @param isAlignCenter是否水平居中
	 * @param isVerticalCenter是否垂直居中
	 * @param fontName字体名称
	 * @return
	 */
	private HSSFCellStyle createMyCellStyle(HSSFWorkbook workbook,
			int fontHeight, boolean isBold, boolean isAlignCenter,
			boolean isVerticalCenter, String fontName) {
		// 设置字体和样式
		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		if (isAlignCenter) {
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
		}
		if (isVerticalCenter) {
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		}
		HSSFFont font = workbook.createFont();
		if (fontName != null) {
			font.setFontName(fontName);
		} else {
			font.setFontName("微软雅黑");
		}
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) fontHeight);
		if (isBold) {
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		}
		style.setFont(font);
		return style;
	}

	/**
	 * 创建单元格式样1
	 * 
	 * @param workbook
	 * @param fontHeight字号
	 * @param isBold是否粗体
	 * @param isAlignCenter是否水平居中
	 * @param isVerticalCenter是否垂直居中
	 * @param fontName字体名称
	 * @return
	 */
	private HSSFCellStyle createMyCellStyleT(HSSFWorkbook workbook,
			int fontHeight, boolean isBold, boolean isAlignCenter,
			boolean isVerticalCenter, String fontName) {
		// 设置字体和样式
		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		if (!isAlignCenter) {
			style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);// 水平居右
		}
		if (isVerticalCenter) {
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		}
		HSSFFont font = workbook.createFont();
		if (fontName != null) {
			font.setFontName(fontName);
		} else {
			font.setFontName("微软雅黑");
		}
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) fontHeight);
		if (isBold) {
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		}
		style.setFont(font);
		return style;
	}

	/**
	 * 创建单元格式样2
	 * 
	 * @param workbook
	 * @param fontHeight字号
	 * @param isBold是否粗体
	 * @param isAlignCenter是否水平居中
	 * @param isVerticalCenter是否垂直居中
	 * @param fontName字体名称
	 * @return
	 */
	private HSSFCellStyle createMyCellStyleTT(HSSFWorkbook workbook,
			int fontHeight, boolean isBold, boolean isAlignCenter,
			boolean isVerticalCenter, String fontName) {
		// 设置字体和样式
		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		if (isAlignCenter) {
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
		}
		if (!isVerticalCenter) {
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		}
		HSSFFont font = workbook.createFont();
		if (fontName != null) {
			font.setFontName(fontName);
		} else {
			font.setFontName("微软雅黑");
		}
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) fontHeight);
		if (isBold) {
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		}
		style.setFont(font);
		return style;
	}

	/**
	 * 在原式样中增加格式化输出式样
	 * 
	 * @param workbook
	 * @param style原式样
	 * @param numberType
	 */
	private void setCellNumberType(HSSFWorkbook workbook, HSSFCellStyle style,
			String numberType) {
		HSSFDataFormat format = workbook.createDataFormat();
		// 货币格式
		if ("money".equals(numberType)) {
			style.setDataFormat(format.getFormat("￥#,##0.00"));
			// 日期格式
		} else if ("date".equals(numberType)) {
			style.setDataFormat(format.getFormat("m月d日"));
		}
	}

	protected void buildExcelDocument(Map<String, Object> map,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		this.excelDataAdmin(map, request, response);
	}

}
