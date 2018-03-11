package com.l9e.util;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@SuppressWarnings("deprecation")
public class ExcelUtilNew {
	private static final Logger loger =Logger.getLogger(ExcelUtilNew.class);
	
	@SuppressWarnings( "unchecked" )
	public static XSSFWorkbook createExcel(String filename,
			String title, String date, String[] secondTitles,
			List<LinkedList<String>> list, HttpServletRequest request, HttpServletResponse response) {

		XSSFWorkbook book = new XSSFWorkbook();
		XSSFSheet sheet = book.createSheet(title);
		sheet.setDisplayGridlines(true);
		// 定义各种样式
		XSSFCellStyle firStyle = createMyCellStyle(book, 18, true, true,
				true, null);// 大标题 第一行样式
		XSSFCellStyle secStyle = createMyCellStyle(book, 12, true, false,
				true, null);// 中标题 第二、三行样式
		XSSFCellStyle thirStyle = createMyCellStyle(book, 10, true, true,
				true, null);// 小标题 第四行样式
		XSSFCellStyle contentStyle = createMyCellStyle(book, 10, false,
				true, true, "宋体");// 内容

		XSSFRow row = null;
		int index = 0;
		@SuppressWarnings("unused")
		SimpleDateFormat ss = new SimpleDateFormat("yyyy年MM月dd日");
		row = sheet.createRow(index);
		row.setHeight((short) 600);
		createCellAndSetStrVal(row, 0, firStyle, title);
		sheet.addMergedRegion(new CellRangeAddress(index, (short) 0, index,
				(short) (secondTitles.length - 1)) );
		loger.info("生成大标题");
		index++;
	
		// 写入第二行
		row = sheet.createRow(index);
		row.setHeight((short) 600);
		createCellAndSetStrVal(row, 0, secStyle, date);
//		sheet.addMergedRegion(new Region(index, (short) 0, index,
//				(short) (secondTitles.length - 1)));
//		sheet.addMergedRegion(new CellRangeAddress(index, (short) 0, index,
//				(short) (secondTitles.length - 1)) );
		loger.info("生成中标题");
		index++;

		// 第三行
		row = sheet.createRow(index);
		row.setHeight((short) 1200);
		for (int i = 0; i < secondTitles.length; i++) {
		
			createCellAndSetStrVal(row, i, thirStyle, secondTitles[i]);
		}
		index++;
		loger.info("list size:" + list.size());
		for (LinkedList<String> set : list) {
			row = sheet.createRow(index);
			row.setHeight((short) 400);
			createCellAndSetNumberVal(row, 0, contentStyle, String
					.valueOf(index - 2));
			Iterator it = set.iterator();
			int j = 1;
			//System.out.println(set);
			while (it.hasNext()) {
				String content = (String) it.next();
				
				if (content != null) {
					//loger.info(j + "     content:     " + content);
					createCellAndSetStrVal(row, j++, contentStyle, content);
				} else {
					
					//loger.info(j + "      content:       " + null);
					createCellAndSetStrVal(row, j++, contentStyle, "");
				}
			}
			index++;
		}
		for(int i = 0;i < secondTitles.length;i++) {
			sheet.autoSizeColumn(i);
		}
		export(filename, book, request, response);
		return book;
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
	public static XSSFCellStyle createMyCellStyle(XSSFWorkbook workbook,
			int fontHeight, boolean isBold, boolean isAlignCenter,
			boolean isVerticalCenter, String fontName) {
		// 设置字体和样式
		XSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		if (isAlignCenter) {
			style.setAlignment(XSSFCellStyle.ALIGN_CENTER);// 水平居中
		}
		if (isVerticalCenter) {
			style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		}
		XSSFFont font = workbook.createFont();
		if (fontName != null) {
			font.setFontName(fontName);
		} else {
			font.setFontName("微软雅黑");
		}
//		font.setColor(XSSFColor.BLACK.index);

		font.setFontHeightInPoints((short) fontHeight);
		if (isBold) {
			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		}
		style.setFont(font);
		return style;
	}

	/**
	 * 创建单元格并写入数字
	 * 
	 * @param row行
	 * @param num列
	 * @param style单元格式样
	 * @param value单元格内容
	 */
	public static void createCellAndSetNumberVal(XSSFRow row, int num,
			XSSFCellStyle style, String value) {
		XSSFCell cell = row.createCell(num);
		if (style != null) {
			cell.setCellStyle(style);
		}
		if (StringUtil.isNotBlank(value)) {
			cell.setCellValue(Double.parseDouble(value));
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
	public static void createCellAndSetStrVal(XSSFRow row, int num,
			XSSFCellStyle firStyle, String value) {
		// XSSFCell cell = row.createCell((short) num);
		XSSFCell cell = row.createCell(num);
		if (firStyle != null) {
			cell.setCellStyle(firStyle);
		}
		if (StringUtil.isNotEmpty(value)) {
			XSSFRichTextString richstr = new XSSFRichTextString(value);
			cell.setCellValue(richstr);
		}
	}
	public static void export(String filename, XSSFWorkbook book, HttpServletRequest request, HttpServletResponse response){
		try {
			// 输出流导出
			OutputStream os;

			if (request.getHeader("User-Agent").toLowerCase()
					.indexOf("firefox") > 0)
				filename = new String(filename.getBytes(), "ISO8859-1");// firefox浏览器
			else if (request.getHeader("User-Agent").toUpperCase().indexOf(
					"MSIE") > 0)
				filename = URLEncoder.encode(filename, "UTF-8");// IE浏览器
			response.reset();
			response.setContentType("application/msexcel");
			response.setHeader("Content-Disposition", "attachment;"
					+ " filename=" + filename);
			os = response.getOutputStream();
			
			book.write(os);
			os.flush();
			os.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

}
