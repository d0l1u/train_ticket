import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
 

import com.fasterxml.jackson.core.JsonParseException;
import com.l9e.transaction.dao.TestDao;

public class NfdTest {
	public static Logger logger = Logger.getLogger(NfdTest.class);
	public static void main(String[] args) throws JsonParseException,
			IOException {
		NfdTest dt = new NfdTest(); 
		dt.nfdData();
	}
	
	
	public void nfdData(){
		int num = 0;
		TestDao testDao = new TestDao();
		while (true) {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("num", num);
			List nfdList = testDao.queryNfdAll(paramMap);
			logger.info("获取到nfd的数量为:"+nfdList.size());
			if(nfdList.size()==0)
				break;
			else
				num+=10000;
			for (int i = 0; i < nfdList.size(); i++) {
				Map<String, String> nfdMap = (Map<String, String>) nfdList.get(i); 
				if (!"".equals(nfdMap.get("flnoArea"))) { 
					logger.info("执行flnoArea数据,"+nfdMap.get("flnoArea"));
					String[] flnoList = nfdMap.get("flnoArea").split("/");
					for (int j = 0; j < flnoList.length; j++) {
						String[] flnoArea;
						int flno = -1;int begin = -1;int end=-1;
						try {
							flnoArea = flnoList[j].split("-");
							begin = Integer.parseInt(flnoArea[0]);
							end = Integer.parseInt(flnoArea[1]);
						} catch (Exception e) {
							logger.info("航次的区间错误:"+nfdMap.get("flnoArea"));
						}
						try {
							flno= Integer.parseInt(nfdMap.get("flno").substring(2,nfdMap.get("flno").length())); 							
						} catch (Exception e) {
							logger.info("航次强转失败:"+nfdMap.get("flno"));
						}
						if (begin<=flno&&flno<=end) {
							logger.info("航次:"+nfdMap.get("flno"));
							testDao.addNfdFinal(nfdMap);
							logger.info("新增一条flnoArea数据成功...."+nfdMap.get("dep")+","+nfdMap.get("arr")+","+nfdMap.get("air2c")+","+nfdMap.get("cabin")+","+nfdMap.get("flno")+","+nfdMap.get("price"));
							continue;
						}
					}
				}else if (!"".equals(nfdMap.get("noflnoArea"))) {
					logger.info("执行noflnoArea数据,"+nfdMap.get("noflnoArea"));
					String[] flnoList = nfdMap.get("noflnoArea").split("/");
					for (int j = 0; j < flnoList.length; j++) {
						String[] flnoArea;
						int flno = -1;int begin = -1;int end=-1;
						try {
							flnoArea = flnoList[j].split("-");
							begin = Integer.parseInt(flnoArea[0]);
							end = Integer.parseInt(flnoArea[1]);
						} catch (Exception e) {
							logger.info("航次的区间错误:"+nfdMap.get("flnoArea"));
						}
						try {
							flno= Integer.parseInt(nfdMap.get("flno").substring(2,nfdMap.get("flno").length())); 							
						} catch (Exception e) {
							logger.info("航次强转失败:"+nfdMap.get("flno"));
						}
						if (begin<=flno&&flno<=end) {
							logger.info("航次:"+nfdMap.get("flno"));
							testDao.addNfdFinal(nfdMap);
							logger.info("新增一条noflnoArea数据成功...."+nfdMap.get("dep")+","+nfdMap.get("arr")+","+nfdMap.get("air2c")+","+nfdMap.get("cabin")+","+nfdMap.get("flno")+","+nfdMap.get("price"));
							continue;
						}
					}
				}else{
					logger.info("执行flnoArea和noflnoArea都为空的数据...");
					testDao.addNfdFinal(nfdMap);
					logger.info("新增一条数据成功...."+nfdMap.get("dep")+","+nfdMap.get("arr")+","+nfdMap.get("air2c")+","+nfdMap.get("cabin")+","+nfdMap.get("flno")+","+nfdMap.get("price"));
				}
			}
		}
	}
	 

}
