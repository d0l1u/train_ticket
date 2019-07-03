package com.l9e.util;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

@SuppressWarnings("deprecation")
public class ExcelUtil {
	private static final Logger loger = Logger.getLogger(ExcelUtil.class);

	@SuppressWarnings("unchecked")
	public static HSSFWorkbook createExcel(String filename, String title,
			String date, String[] secondTitles, List<LinkedList<String>> list,
			HttpServletRequest request, HttpServletResponse response) {
		loger.info("生成excel前构造的list对象内存占比，系统jvm内存占有比率："+JvmUtil.getUseRateMemoryMXBean()*100+"%");
		if (JvmUtil.getUseRateMemoryMXBean()>0.8){
			loger.info("内存占用率过大-----,回收内存");
			list=null;
			System.gc();
			return  null;
		}

		HSSFWorkbook book = new HSSFWorkbook();
		loger.info("list size:" + list.size());
		int totle = list.size();// 获取List集合的size
		int mus = 65530;// 每个工作表格最多存储2条数据（注：excel表格一个工作表可以存储65536条）
		int avg = totle / mus;
		for (int m = 0; m < avg + 1; m++) {
			HSSFSheet sheet = book.createSheet(title + m);
			sheet.setDisplayGridlines(true);
			// 定义各种样式
			HSSFCellStyle firStyle = createMyCellStyle(book, 18, true, true,
					true, null);// 大标题 第一行样式
			HSSFCellStyle secStyle = createMyCellStyle(book, 12, true, false,
					true, null);// 中标题 第二、三行样式
			HSSFCellStyle thirStyle = createMyCellStyle(book, 10, true, true,
					true, null);// 小标题 第四行样式
			HSSFCellStyle contentStyle = createMyCellStyle(book, 10, false,
					true, true, "宋体");// 内容

			HSSFRow row = null;
			int index = 0;
			@SuppressWarnings("unused")
			SimpleDateFormat ss = new SimpleDateFormat("yyyy年MM月dd日");
			row = sheet.createRow(index);
			row.setHeight((short) 600);
			createCellAndSetStrVal(row, 0, firStyle, title);
			sheet.addMergedRegion(new Region(index, (short) 0, index,
					(short) (secondTitles.length - 1)));
			loger.info("生成大标题");
			index++;

			// 写入第二行
			row = sheet.createRow(index);
			row.setHeight((short) 600);
			createCellAndSetStrVal(row, 0, secStyle, date);
			sheet.addMergedRegion(new Region(index, (short) 0, index,
					(short) (secondTitles.length - 1)));
			loger.info("生成中标题");
			index++;

			// 第三行
			row = sheet.createRow(index);
			row.setHeight((short) 1200);
			for (int i = 0; i < secondTitles.length; i++) {

				createCellAndSetStrVal(row, i, thirStyle, secondTitles[i]);
			}
			index++;

			List<LinkedList<String>> newlist = new ArrayList<LinkedList<String>>();
			// System.out.println("m*mus="+m*mus);
			int num = m * mus;
			for (int x = num; x < num + mus; x++) {
				if (x >= list.size()) {
					break;
				}
				// System.out.println("list.get("+x+")="+list.get(x));
				newlist.add(list.get(x));
			}
			// System.out.println("newlist="+newlist.size());
			for (LinkedList<String> set : newlist) {
				row = sheet.createRow(index);
				row.setHeight((short) 400);
				createCellAndSetNumberVal(row, 0, contentStyle,
						String.valueOf(index - 2));
				Iterator it = set.iterator();
				int j = 1;
				// System.out.println(set);
				while (it.hasNext()) {
					String content = (String) it.next();

					if (content != null) {
						// loger.info(j + "     content:     " + content);
						createCellAndSetStrVal(row, j++, contentStyle, content);
					} else {

						// loger.info(j + "      content:       " + null);
						createCellAndSetStrVal(row, j++, contentStyle, "");
					}
				}
				index++;
			}
			for (int i = 0; i < secondTitles.length; i++) {
				sheet.autoSizeColumn(i);
			}
		}
		loger.info("HSSFWorkbook生成后，系统jvm内存占有比率："+JvmUtil.getUseRateMemoryMXBean()*100+"%");
		export(filename, book, request, response);
		book=null;list=null;
		System.gc();
		loger.info("回收HSSFWorkbook对象，系统jvm内存占有比率："+JvmUtil.getUseRateMemoryMXBean()*100+"%");
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
	private static HSSFCellStyle createMyCellStyle(HSSFWorkbook workbook,
			int fontHeight, boolean isBold, boolean isAlignCenter,
			boolean isVerticalCenter, String fontName) {
		// 设置字体和样式
		HSSFCellStyle style = workbook.createCellStyle();
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		if (isAlignCenter) {
			style.setAlignment(CellStyle.ALIGN_CENTER);// 水平居中
		}
		if (isVerticalCenter) {
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直居中
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
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
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
	private static void createCellAndSetNumberVal(HSSFRow row, int num,
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
	 * 创建单元格并写入文本内容
	 * 
	 * @param row行
	 * @param num列
	 * @param style单元格式样
	 * @param value单元格内容
	 */
	private static void createCellAndSetStrVal(HSSFRow row, int num,
			HSSFCellStyle style, String value) {
		// HSSFCell cell = row.createCell((short) num);
		HSSFCell cell = row.createCell(num);
		if (style != null) {
			cell.setCellStyle(style);
		}
		if (StringUtil.isNotEmpty(value)) {
			HSSFRichTextString richstr = new HSSFRichTextString(value);
			cell.setCellValue(richstr);
		}
	}

	private static void export(String filename, HSSFWorkbook book,
			HttpServletRequest request, HttpServletResponse response) {
		OutputStream os = null;
		try {
			// 输出流导出
			if (request.getHeader("User-Agent").toLowerCase()
					.indexOf("firefox") > 0)
				filename = new String(filename.getBytes(), "ISO8859-1");// firefox浏览器
			else if (request.getHeader("User-Agent").toUpperCase()
					.indexOf("MSIE") > 0)
				filename = URLEncoder.encode(filename, "UTF-8");// IE浏览器
			else {
				filename = URLEncoder.encode(filename, "UTF-8");// 其它浏览器
			}

			response.reset();

			response.setContentType("application/msexcel");
			response.setHeader("Content-Disposition", "attachment;"
					+ " filename=" + filename);
			os = response.getOutputStream();

			book.write(os);
			os.flush();

		} catch (Exception e) {
			loger.info("导出excel", e);
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				loger.info("导出excel", e);
			}
		}
	}

	public static void main(String[] args) {

		String title = "携程管理预定管理明细";
		String date = "2017-2017";
		String filename = "携程管理预定管理.xls";
		String[] secondTitles = { "序号", "订单号", "商户订单号", "取票单号", "创建时间", "出票时间",
				"保险", "票价", "实际订票价", "总价", "差价", "出票账号", "支付流水号", "保险明细",
				"出发/到达", "车次", "出发时间", "手续费", "票数", "备注" };

		// LinkedList<String> 一行的数据
		List<LinkedList<String>> list = new ArrayList<LinkedList<String>>();
		HttpServletRequest request = null;
		HttpServletResponse response = null;
		ExcelUtil.createExcel(filename, title, date, secondTitles, list,request, response);
				

	}

}
