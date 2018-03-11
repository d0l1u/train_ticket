import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
 

import com.fasterxml.jackson.core.JsonParseException;
import com.l9e.transaction.dao.TestDao;

public class DemoTest {
	public static Logger logger = Logger.getLogger(DemoTest.class);
	public static void main(String[] args) throws JsonParseException,
			IOException {
		DemoTest dt = new DemoTest();
		dt.menuData();
	}
	
	
	
	public void fdData(){
		TestDao testDao = new TestDao();
		List air2cList = testDao.queryAir2c();
//		logger.info("获取到航空公司编码的数量为:"+air2cList.size());
//		for (int i = 0; i < air2cList.size(); i++) {
//			Map<String, String> air2Map = (Map<String, String>) air2cList.get(i); 
			List queryList = new ArrayList(); 
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("air2c", "CZ");
			queryList = testDao.queryList(paramMap); 
			logger.info("获取到"+"CZ"+"航空公司的航班数量为:"+queryList.size());
			for (int j = 0; j < queryList.size(); j++) {
				Map<String, String> newMap = (Map<String, String>) queryList.get(j);
				testDao.addNfdFinal(newMap);
				logger.info("新增一条数据成功...."+newMap.get("dep")+","+newMap.get("arr")+","+newMap.get("air2c")+","+newMap.get("cabin")+","+newMap.get("flno")+","+newMap.get("price"));
			}
//		}
	}
	
	public void menuData(){
		TestDao testDao = new TestDao();
		List depList = testDao.queryFinalDep();
		logger.info("获取到出发机场数量:"+depList.size());
		List arrList = testDao.queryFinalArr();
		logger.info("获取到到达机场数量:"+arrList.size());
		for (int i = 0; i < depList.size(); i++) {
			Map<String, String> depMap = (Map<String, String>)depList.get(i);
			String dep = depMap.get("dep");
			for (int j = 0; j < arrList.size(); j++) {
				Map<String, String> arrMap = (Map<String, String>)arrList.get(j);
				String arr =arrMap.get("arr");
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("dep",dep);
				paramMap.put("arr",arr);
				List finalBydeparrList = testDao.queryFinalByDepArr(paramMap);
				logger.info("查询出发机场:"+dep+",到达机场:"+arr+",的航班数量为:"+finalBydeparrList.size());
				if (finalBydeparrList.size()>0) {	
					Map<String, String> deparrMap =(Map<String, String>) finalBydeparrList.get(0);
					Map<String, Object> menuOneMap = new HashMap<String, Object>();
					menuOneMap.put("level",1);
					menuOneMap.put("menuName", deparrMap.get("depName")+"-"+ deparrMap.get("arrName"));
					menuOneMap.put("identifiers",deparrMap.get("dep") + deparrMap.get("arr"));
					menuOneMap.put("parentIdent", "jipiao-airline");
					menuOneMap.put("chinesejp", deparrMap.get("depCityName")+"-"+ deparrMap.get("arrCityName"));
					testDao.addMenu(menuOneMap);
					logger.info("添加一级菜单,"+menuOneMap.get("menuName")+","+menuOneMap.get("identifiers")+","+menuOneMap.get("chinesejp"));
					for (int k = 0; k < finalBydeparrList.size(); k++) {
						Map<String, String> finalMap =(Map<String, String>) finalBydeparrList.get(k);
						Map<String, Object> menuTwoMap = new HashMap<String, Object>();
						menuTwoMap.put("level", 2);
						menuTwoMap.put("menuName", finalMap.get("aircmpName") + finalMap.get("flno"));
						menuTwoMap.put("identifiers",finalMap.get("flno"));
						menuTwoMap.put("parentIdent", finalMap.get("dep") + finalMap.get("arr"));
						menuTwoMap.put("chinesejp", finalMap.get("flno"));
						testDao.addMenu(menuTwoMap);
						logger.info("添加二级菜单,"+menuTwoMap.get("menuName")+","+menuTwoMap.get("identifiers")+","+menuTwoMap.get("chinesejp"));
					}
				}else{
					continue;
				}
			}
		}
	}

}
