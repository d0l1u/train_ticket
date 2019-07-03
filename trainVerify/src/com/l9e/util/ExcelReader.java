package com.l9e.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * 操作Excel表格的功能类
 */
public class ExcelReader {
    private POIFSFileSystem fs;
    private HSSFWorkbook wb;
    private HSSFSheet sheet;
    private HSSFRow row;

    /**
     * 读取Excel数据内容
     * @param InputStream
     * @return Map 包含单元格数据内容的Map对象
     */
    public Map<Integer, String> readExcelContent(InputStream is) {
        Map<Integer, String> content = new HashMap<Integer, String>();
        String str = "";
        try {
            fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 0; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            while (j < colNum) {
                // 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
                // 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
                // str += getStringCellValue(row.getCell((short) j)).trim() +
                // "-";
                str += getCellFormatValue(row.getCell((short) j)).trim() + "    ";
                j++;
            }
            content.put(i, str);
            str = "";
        }
        return content;
    }

    /**
     * 获取单元格数据内容为字符串类型的数据
     * 
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private String getStringCellValue(HSSFCell cell) {
        String strCell = "";
        switch (cell.getCellType()) {
        case HSSFCell.CELL_TYPE_STRING:
            strCell = cell.getStringCellValue();
            break;
        case HSSFCell.CELL_TYPE_NUMERIC:
            strCell = String.valueOf(cell.getNumericCellValue());
            break;
        case HSSFCell.CELL_TYPE_BOOLEAN:
            strCell = String.valueOf(cell.getBooleanCellValue());
            break;
        case HSSFCell.CELL_TYPE_BLANK:
            strCell = "";
            break;
        default:
            strCell = "";
            break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        if (cell == null) {
            return "";
        }
        return strCell;
    }

    /**
     * 获取单元格数据内容为日期类型的数据
     * 
     * @param cell
     *            Excel单元格
     * @return String 单元格数据内容
     */
    private String getDateCellValue(HSSFCell cell) {
        String result = "";
        try {
            int cellType = cell.getCellType();
            if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
                Date date = cell.getDateCellValue();
                result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1)
                        + "-" + date.getDate();
            } else if (cellType == HSSFCell.CELL_TYPE_STRING) {
                String date = getStringCellValue(cell);
                result = date.replaceAll("[年月]", "-").replace("日", "").trim();
            } else if (cellType == HSSFCell.CELL_TYPE_BLANK) {
                result = "";
            }
        } catch (Exception e) {
            System.out.println("日期格式不正确!");
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据HSSFCell类型设置数据
     * @param cell
     * @return
     */
    private String getCellFormatValue(HSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
            // 如果当前Cell的Type为NUMERIC
            case HSSFCell.CELL_TYPE_NUMERIC:
            case HSSFCell.CELL_TYPE_FORMULA: {
                // 判断当前的cell是否为Date
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 如果是Date类型则，转化为Data格式
                    
                    //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                    //cellvalue = cell.getDateCellValue().toLocaleString();
                    
                    //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    cellvalue = sdf.format(date);
                    
                }
                // 如果是纯数字
                else {
                    // 取得当前Cell的数值
                    cellvalue = String.valueOf(cell.getNumericCellValue());
                }
                break;
            }
            // 如果当前Cell的Type为STRIN
            case HSSFCell.CELL_TYPE_STRING:
                // 取得当前的Cell字符串
                cellvalue = cell.getRichStringCellValue().getString();
                break;
            // 默认的Cell值
            default:
                cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }
    
    private void writeExcel(List<Map<String, String>> list) throws Exception{
    	HSSFWorkbook wb = new HSSFWorkbook();

        // 创建Excel的工作sheet,对应到一个excel文档的tab
        HSSFSheet sheet = wb.createSheet("sheet1");

        // 设置excel每列宽度
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 8000);
        sheet.setColumnWidth(2, 4000);

        // 创建字体样式
        HSSFFont font = wb.createFont();
        font.setFontName("Verdana");
        font.setBoldweight((short) 100);
        font.setFontHeight((short) 300);
//        font.setColor(HSSFColor.BLUE.index);

        // 创建单元格样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
//        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // 设置边框
        style.setBottomBorderColor(HSSFColor.RED.index);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);

        style.setFont(font);// 设置字体
        Map<String, String> map = null;
        for(int i=0;i<list.size();i++){
        	map = list.get(i);
	        // 创建Excel的sheet的一行
	        HSSFRow row = sheet.createRow(i);
	        row.setHeight((short) 500);// 设定行的高度
	        // 创建一个Excel的单元格
	        HSSFCell cell = row.createCell(0);
	
	        // 合并单元格(startRow，endRow，startColumn，endColumn)
	       // sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
	
	        // 给Excel的单元格设置样式和赋值
	        cell.setCellStyle(style);
	        cell.setCellValue(map.get("user_name"));
	      
	        cell = row.createCell(1);
	        cell.setCellStyle(style);
	        cell.setCellValue(map.get("cert_no"));
	        
	        cell = row.createCell(2);
	        cell.setCellStyle(style);
	        cell.setCellValue(map.get("verification_status_name"));
        }

        FileOutputStream os = new FileOutputStream("d:\\workbook.xls");
        wb.write(os);
        os.close();
    }

    public static void main(String[] args) throws Exception {
    	List<Map<String, String>> userList = new ArrayList<Map<String, String>>();
    	Map<String, String> userMap = null;
    	String[] str = null; 
    	ExcelReader excelReader = new ExcelReader();
        try {
            // 对读取Excel表格内容测试
            InputStream is = new FileInputStream("d:\\id.xls");
            Map<Integer, String> map = excelReader.readExcelContent(is);
            for (int i = 1; i <= map.size(); i++) {
                //System.out.println(map.get(i));
                if(StringUtils.isNotEmpty(map.get(i))){
	                str = map.get(i).split(",");
	                userMap = new HashMap<String, String>();
	                userMap.put("user_name", str[0]);
	                userMap.put("cert_no", str[1]);
	                userList.add(userMap);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("未找到指定路径的文件!");
            e.printStackTrace();
        }
        
        Map<String, String> map1 = null;
        Map<String, String> map = null;
        int cout = 1;
        for(int i=0;i<80;i++){
        	List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        	for(int j=0;j<5;j++){
        		if(5*i+j<userList.size()){
        			map1 = new HashMap<String, String>();
		        	map = userList.get(5*i+j);
		        	//System.out.println(map.get("user_name")+":"+map.get("cert_no"));
		        	map1.put("passenger_id_no", map.get("cert_no").trim());
		        	map1.put("passenger_name", map.get("user_name").trim());
		        	if(map.get("cert_no").trim().length()==18){
		        		map1.put("passenger_id_type_code", "1");
		        	}else{
		        		map1.put("passenger_id_type_code", "B");
		        	}
		        	list.add(map1);
        		}
	        }
        	JSONObject json = new JSONObject();
        	json.put("passengers", JSONArray.fromObject(list));
        	json.put("total", list.size());
        	
        	System.out.println("=============================="+(cout++)+"==================================");
        	String result = excelReader.requestInterface(json.toString());
        	System.out.println("result="+result);
        	
        	JSONObject jsonobj = JSONObject.fromObject(result);
        	if(jsonobj.getBoolean("status")){
        		JSONArray ja = jsonobj.getJSONArray("passengers");
        		JSONObject jsont = null;
        		for(int k=5*i;k<5*i+5;k++){
        			if(k<userList.size()){
	        			Map<String, String> mk = userList.get(k);
	        			System.out.println("mk="+mk);
		        		for(int z=0;z<ja.size();z++){
		        			jsont = ja.getJSONObject(z);
		        			if(jsont.getString("passenger_name").equals(mk.get("user_name").trim())
		        					&& jsont.getString("passenger_id_no").equals(mk.get("cert_no").trim())){
		        				mk.put("verification_status_name", jsont.getString("verification_status_name"));
		        			}
		        		}
        			}
        		}
        	}
        	
        	
        	
        }
        excelReader.writeExcel(userList);
    }
    
    
    private String requestInterface(String json) throws Exception{
    	Map<String, String> maps = new HashMap<String, String>(); 
    	maps.put("datas", json);
    	
    	System.out.println("req="+json);
    	long begin = System.currentTimeMillis();
        String url = "http://192.168.12.203:8088/passenger/verify.jhtml";
        
		String reqParams = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");
		String result = HttpUtil.sendByPost(url, reqParams, "UTF-8");
		System.out.println("+++++++++++++++++++++++++take "+(System.currentTimeMillis() - begin)+" ms+++++++++++++++++++++++++++++++++");
		return result;
    }
}