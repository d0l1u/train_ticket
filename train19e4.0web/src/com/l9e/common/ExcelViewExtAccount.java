package com.l9e.common;

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

import com.l9e.transaction.vo.OrderInfo;
import com.l9e.util.StringUtil;

public class ExcelViewExtAccount extends AbstractExcelView {
	//对外商户--对账管理--导出excel
	private void excelDataPrepareRefund(Map<String, Object> map,
			HttpServletRequest request, HttpServletResponse response)
			throws ParseException {
		List<OrderInfo> orderlist = (List<OrderInfo>) map.get("list");
		String begin_info_time = (String) map.get("begin_info_time");
		String end_info_time = (String) map.get("end_info_time");
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("火车票对账明细");
		sheet.setDisplayGridlines(true);// 隐藏网格线
		// 设置各列宽度sheet.setDefaultColumnWidth((short)20);
		sheet.setColumnWidth(0, 2000);
		sheet.setColumnWidth(1, 6000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 3000);
		sheet.setColumnWidth(4, 3000);
		sheet.setColumnWidth(5, 6000);
		sheet.setColumnWidth(6, 6000);
		sheet.setColumnWidth(7, 3000);
		sheet.setColumnWidth(8, 3000);
		sheet.setColumnWidth(9, 3000);
		sheet.setColumnWidth(10, 3000);
		sheet.setColumnWidth(11, 3000);
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
		this.setCellNumberType(workbook, contentStyle, "String");
		// 查询需要导出的信息
		Iterator<OrderInfo> it = orderlist.iterator();
		HSSFRow row = null;
		int index = 0;
		SimpleDateFormat ss = new SimpleDateFormat("yyyy年MM月dd日");
		// 写入第一行
		row = sheet.createRow(index);
		row.setHeight((short) 600);
		StringBuffer header = new StringBuffer("火车票对账明细");

		String[] topHeaders = { header.toString(), null, null, null, null,
				null, null, null, null, null, null, null};
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
		if (begin_info_time == null || begin_info_time.length() == 0) {
			if (end_info_time == null || end_info_time.length() == 0) {
				xsDate.append(str1);
			} else {
				xsDate.append(end_info_time);
				xsDate.append("之前");
			}
		}
		else {
			if (end_info_time == null || end_info_time.length() == 0) {
				xsDate.append(begin_info_time);
				xsDate.append("-----");
				xsDate.append(str1);
			} else {
				if (begin_info_time.equals(end_info_time)) {
					xsDate.append(begin_info_time);
				} else {
					xsDate.append(begin_info_time);
					xsDate.append("-----");
					xsDate.append(end_info_time);
				}
			}
		}

		String[] secHeaders = { xsDate.toString(), null, null, null, null,
				null, null, null, null, null, null, null};
		for (int i = 0, n = secHeaders.length; i < n; i++) {
			this.createCellAndSetStrVal(row, i, secStyle, secHeaders[i]);
		}
		sheet.addMergedRegion(new Region(index, (short) 0, index, (short) 11));
		index++;
		// 写入第三行
		row = sheet.createRow(index);
		row.setHeight((short) 1200);
		String[] headers = { "序号", "订单号", "EOP订单号", "出发", "到达", "支付时间",
				"退款时间", "支付类型", "订单状态", "退款状态", "支付金额", "退款金额"};
		for (int i = 0, n = headers.length; i < n; i++) {
			this.createCellAndSetStrVal(row, i, thirStyle, headers[i]);
		}
		index++;
		while (it.hasNext()) {
			// 每条订单数据在excel中产生1行
			OrderInfo orderExcel = (OrderInfo) it.next();
			// 写入第五行
			row = sheet.createRow(index);
			row.setHeight((short) 400);
			this.createCellAndSetNumberVal(row, 0, contentStyle, String.valueOf(index - 2));
			this.createCellAndSetStrVal(row, 1, contentStyle, orderExcel.getOrder_id());
			if(orderExcel.getEop_order_id()==null || StringUtils.isEmpty(orderExcel.getEop_order_id())){
				this.createCellAndSetStrVal(row, 2, contentStyle,"");
			}else{
				this.createCellAndSetStrVal(row, 2, contentStyle,orderExcel.getEop_order_id());
			}
			if(orderExcel.getFrom_city()==null || StringUtils.isEmpty(orderExcel.getFrom_city())){
				this.createCellAndSetStrVal(row, 3, contentStyle, "");
			}else{
				this.createCellAndSetStrVal(row, 3, contentStyle, orderExcel.getFrom_city());
			}
			if(orderExcel.getTo_city()==null || StringUtils.isEmpty(orderExcel.getTo_city())){
				this.createCellAndSetStrVal(row, 4, contentStyle, "");
			}else{
				this.createCellAndSetStrVal(row, 4, contentStyle, orderExcel.getTo_city());
			}
			if(orderExcel.getPay_time()==null ||StringUtils.isEmpty(orderExcel.getPay_time())){
				this.createCellAndSetStrVal(row, 5, dateStyle,"");
			}else{
				this.createCellAndSetDateVal(row, 5, dateStyle, orderExcel.getPay_time().toString());
			}
			if(orderExcel.getRefund_time()==null ||StringUtils.isEmpty(orderExcel.getRefund_time())){
				this.createCellAndSetStrVal(row, 6, dateStyle,"");
			}else{
				this.createCellAndSetDateVal(row, 6, dateStyle, orderExcel.getRefund_time().toString());
			}
			if(orderExcel.getPay_type()==null || StringUtils.isEmpty(orderExcel.getPay_type())){
				this.createCellAndSetStrVal(row, 7, contentStyle, "");
			}else{//支付类型：0 钱包 1 支付宝
				this.createCellAndSetStrVal(row, 7, contentStyle, ExtShijiConsts.getPayType().get(orderExcel.getPay_type()));
			}
			if(orderExcel.getOrder_status()==null || StringUtils.isEmpty(orderExcel.getOrder_status())){
				this.createCellAndSetStrVal(row, 8, contentStyle, "");
			}else{//订单状态：44出票成功  45出票失败
				this.createCellAndSetStrVal(row, 8, contentStyle, ExtShijiConsts.getBookStatus().get(orderExcel.getOrder_status()));
			}
			if(orderExcel.getRefund_type()==null || StringUtils.isEmpty(orderExcel.getRefund_type())){
				this.createCellAndSetStrVal(row, 9, contentStyle, "");
			}else{//退款状态：00已差额退款(refund_type='4' refund_status='33')  11退款完成(refund_type!='4' refund_status='33')
				if(orderExcel.getRefund_type().equals("4")){
					this.createCellAndSetStrVal(row, 9, contentStyle, "已差额退款");
				}else{
					this.createCellAndSetStrVal(row, 9, contentStyle, "退款完成");
				}
				
			}
			if(orderExcel.getPay_money() == null || StringUtils.isEmpty(orderExcel.getPay_money())){
				this.createCellAndSetMoneyVal(row, 10, moneyStyle, "0");
			}else{
				this.createCellAndSetMoneyVal(row, 10, moneyStyle, orderExcel.getPay_money());
			}
			if(orderExcel.getRefund_money() == null || StringUtils.isEmpty(orderExcel.getRefund_money())){
				this.createCellAndSetMoneyVal(row, 11, moneyStyle, "0");
			}else{
				this.createCellAndSetMoneyVal(row, 11, moneyStyle, orderExcel.getRefund_money());
			}
			index++;
		}
		try {
			// 输出流导出
			OutputStream os;
			String title = "火车票对账明细.xls";
			if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0)
				title = new String(title.getBytes(), "ISO8859-1");// firefox浏览器
			else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0)
				title = URLEncoder.encode(title, "UTF-8");// IE浏览器
			response.reset();
			response.setContentType("application/msexcel");
			response.setHeader("Content-Disposition", "attachment;"
					+ " filename=" + title);
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
		} else {
			style.setDataFormat(format.getFormat("@"));//对长数字解除科学计数法
		}
	}

	protected void buildExcelDocument(Map<String, Object> map,
			HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		this.excelDataPrepareRefund(map, request, response);
	}


}
