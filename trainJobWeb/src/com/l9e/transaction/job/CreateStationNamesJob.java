package com.l9e.transaction.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.CommonService;
import com.l9e.util.FileUtil;
import com.l9e.util.XmlUtil;
import com.l9e.util.hcpjar.util.DateUtil;


/**
 * 生成车站列表文件
 */
@Component("createStationJob")
public class CreateStationNamesJob {
private static final Logger logger = Logger.getLogger(CreateStationNamesJob.class);
	@Resource
	private CommonService commonService;
	/**
	 * 9:00开始
	 * */
	public void createStation() throws Exception {
		//生成json格式的城市信息
		int index = 0;
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("before", index);
		paramMap.put("after", 5000);
		List<Map<String,String>> list = commonService.queryChinaCitysInfo(paramMap);
		for(Map<String,String> map:list){
			if("鲘门".equals(map.get("name"))){
				map.put("quanpin", "houmen");
			}
			if("栟茶".equals(map.get("name"))){
				map.put("quanpin", "bingcha");
			}
		}
		JSONArray jsonArr = JSONArray.fromObject(list);
		ObjectMapper mapper = new ObjectMapper();
		String fileDir = "//data//station_files";
		String now_date = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
		String fileName = "citys_json"+now_date+".txt";
		String filePath = fileDir + "//" + fileName;
		logger.info("filePath="+filePath);
		//创建文件保存接口返回数据
		FileUtil.removeFile(filePath);
		try{
			FileUtil.createFile(fileDir, fileName, mapper.writeValueAsString(jsonArr), "utf-8");
		}catch(Exception e){
			logger.error("保存json格式城市信息失败！",e);
		}
		
		index = 0;
		//建立document对象 
        Document document = DocumentHelper.createDocument(); 
        //建立resources根节点 
        Element configElement = document.addElement("resources"); 
        //建立string-array节点  
        configElement.addComment("全国城市信息"); 
        Element ftpElement = configElement.addElement("string-array"); 
        ftpElement.addAttribute("name","date"); 
         
		try{
			for(Map<String,String> map:list){
				if("鲘门".equals(map.get("name"))){
					map.put("quanpin", "houmen");
				}
				if("栟茶".equals(map.get("name"))){
					map.put("quanpin", "bingcha");
				}
				
				Element hostElement = ftpElement.addElement("item"); 
				hostElement.setText(map.get("pinyin"));
				Element cityName = hostElement.addElement("name"); 
				cityName.setText(map.get("name"));
				Element cityPy = hostElement.addElement("pinyin"); 
				cityPy.setText(map.get("pinyin"));
				Element cityQpy = hostElement.addElement("quanpin");
				cityQpy.setText(map.get("quanpin"));
			}
		}catch(Exception e){
			logger.error("", e);
		}
		// 保存Document
        XmlUtil.doc2XmlFile(document,"//data//station_files//citys_pinyin"+now_date+".xml");
	}
	
	/**
	 //新增城市拼音功能
			public void appendChinaPin(HttpServletRequest request, HttpServletResponse response){
				int index = 0;
				while(true){
					Map<String, Integer> paramMap = new HashMap<String, Integer>();
					paramMap.put("before", index);
					paramMap.put("after", 500);
					System.out.println("index:"+index);
					List<Map<String,String>> list = queryTicketService.queryChinaCitysInfo(paramMap);
					if(list.size()==0 || list==null){
						logger.info("城市拼接全拼音结束！");
						break;
					}
					List<Map<String,String>> new_list = new ArrayList<Map<String,String>>();
					for(Map<String,String> map:list){
						String zmhz = map.get("name");
						Map<String, String> param = new HashMap<String, String>();
						param.put("zmhz", zmhz);
						param.put("pin", WordFormatUtil.getInstance().getSelling(zmhz));
						new_list.add(param);
						queryTicketService.appendChinaPin(param);
					}
//					queryTicketService.appendChinaPinBatch(new_list);
					index += 500;
				}
			}
	 */
}
