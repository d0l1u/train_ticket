import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l9e.transaction.dao.TrainDao;
import com.l9e.transaction.vo.Train;
import com.l9e.util.DateUtil;
import com.l9e.util.FileUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.MatchUtil;
/**
 * 更新车次信息
 * t_lccc表的数据
 * @author guona
 *
 */
public class LcccManager {
	public static Logger logger = Logger.getLogger("LcccManager.class");
//	public static String TRAIN_LIST_URL = "https://kyfw.12306.cn/otn/resources/js/query/train_list.js";
	public static String TRAIN_LIST_URL = "http://118.190.94.73:8027/servlet/queryTrainList";
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
	private String today = sdf.format(new Date());//2016-01-07
	
	private TrainDao trainDao = new TrainDao();
	
	public static void main(String[] args) {
		
		new LcccManager().updateTrain();
		//System.out.println(new Date().getTime());
		//new LcccManager().getRecentTrain();
	}

	public void updateTrain() {
		getRecentTrain();
	//	File trianListFolderFile = new File("file");
	//	File[] trainListFiles = trianListFolderFile.listFiles();
		
		List<File> trainListFiles = Arrays.asList(new File("file").listFiles());
		Collections.sort(trainListFiles, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				if (o1.isDirectory() && o2.isFile())
					return -1;
				if (o1.isFile() && o2.isDirectory())
					return 1;
				return o1.getName().compareTo(o2.getName());
			}
		}); //按文件名排序
		
		List<Train> trainList = new ArrayList<Train>();
		Map<String, String> paramMap = new HashMap<String, String>();
		int addCount = 1;
		for (File file : trainListFiles) {
			String filename = file.getName(); 
			if (filename.contains("-") && filename.contains(".csv")&&!filename.contains("Y")&&!filename.contains("station_name")) {
			
				trainList = getTrainList(file, 0);
				if (trainList.size() == 0) {
					continue;
				}
				int i = 0;
				for (Train train : trainList) {
					paramMap.put("train_code", train.getTrainCode());//车次:D1
					//paramMap.put("train_no", train.getTrainNo());//train_no:12000000D804
					/*Map<String, String> stationMap = new HashMap<String, String>();
					 stationMap.put("stationName", train.getStartStation().trim());
					 System.out.println(stationMap);
					 train.setStartStationCode(stationDao.queryStationFromZm(stationMap).get(0).getStationCode());
					 stationMap.put("stationName", train.getEndStation());
					 train.setEndStationCode(stationDao.queryStationFromZm(stationMap).get(0).getStationCode());
					*/
					
					//查询某个车次是否存在 
					List<Train> queryTrainList = trainDao.queryTrainfromLccc(paramMap);
					//System.out.println(train.getStation_train_code());
					if (queryTrainList.size() == 0) {
						train.setStatus(Train.NEEDUPDATE);//02、不存在火车需要更新
						Map<String, String> pMap = new HashMap<String, String>();
						pMap.put("train_no", train.getTrainNo());//train_no:12000000D804
						//查询某个车次是否存在 
						List<Train> ccnoList = trainDao.queryTrainfromLccc(pMap);
						if(ccnoList.size()==0){
							trainDao.insertTrainIntoLccc(train);//不存在插入
							addCount++;
							logger.info("[车次插入t_lccc]：" + train.getTrainCode() + ", " + train.getTrainNo());
						}else{
							String cc = ccnoList.get(0).getTrainCode();//车次
							cc = cc + "/" + train.getTrainCode();//1818/1819
							train.setTrainCode(cc);
							train.setStatus(Train.NEEDUPDATE);//00、已存在不需更新
							trainDao.updateTrainOfLcccByccno(train);
							logger.info("[车次相同[/]，合并,车次更新t_lccc]：" + train.getTrainCode() + ", " + train.getTrainNo());
						}

					} else {
						
						Train queryTrain = queryTrainList.get(0);
						logger.info("[车次存在,更新t_lccc]：" + train.getTrainCode() + ", " + train.getTrainNo()+"开始日期："+train.getStartDate()+",库里个数："+queryTrainList.size()+","+queryTrain.getTrainCode()+","+queryTrain.getTrainNo());
					
						if (!train.getStartStation().equals(queryTrain.getStartStation())
								|| !train.getEndStation().equals(queryTrain.getEndStation())) {
							train.setStatus(Train.NEEDUPDATE);//02、不存在火车需要更新
						}
						/*String startDate = queryTrain.getStartDate();
						if (startDate != null && startDate.trim().length() != 0) {
							train.setStartDate(startDate);
						}*/
						train.setTrainCode(queryTrain.getTrainCode());
						train.setStatus(Train.NEEDUPDATE);
						trainDao.updateTrainOfLcccBycc(train);
					}
				}
				String file_name_new=file.getAbsolutePath() + "_Y_"+DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3);
				file.renameTo(new File(file_name_new));
				logger.info(file.getName()+",,,cvs数据："+trainList.size()+", 插入条数：" + addCount);
			}
			
		}
	}

	//得到12306最新的车次信息
	public void getRecentTrain() {
		String result = null;
		if (FileUtil.existsFile("file/train_list.js_" + today)) {
			result = FileUtil.readFile("file/train_list.js_" + today);
		} else {
			result = httpTrainList();
			if(result == null || result.isEmpty()) {
				logger.info("获取12306车次列表为空！");
				return;
			}
		}
		ObjectMapper objMapper = new ObjectMapper();

		JsonNode jsonNode = null;
		try {
			jsonNode = objMapper.readTree(result);
		} catch (JsonProcessingException e) {
			logger.error("解析车次字符串发成异常：" + e.getMessage());
		} catch (IOException e) {
			logger.error("解析车次字符串发成异常：" + e.getMessage());
		}

		Iterator<String> dateIterator = jsonNode.fieldNames();
		while (dateIterator.hasNext()) {
			String date = dateIterator.next();
			if (FileUtil.existsFile("file/" + date + ".csv")) {
				continue;
			}
			OutputStream os = FileUtil.openOutputStream("file/" + date + ".csv");
			JsonNode dateNode = jsonNode.get(date);

			Iterator<String> levelIterator = dateNode.fieldNames();
			while (levelIterator.hasNext()) {
				String level = levelIterator.next();
				JsonNode levelNode = dateNode.get(level);
				if (levelNode.isArray()) {
					for (int i = 0; i < levelNode.size(); i++) {
						JsonNode trainNode = levelNode.get(i);
						//{"station_train_code":"D1(北京-沈阳)","train_no":"24000000D10R"}
						String station_train_code = trainNode.get("station_train_code").asText();//D1(北京-沈阳)
						String train_no = trainNode.get("train_no").asText();//24000000D10R
						String trainCode = MatchUtil.match(station_train_code, "[C|Z|K|D|G|T|L][0-9]+|[0-9]+");//D1   车次
						String initialStation = station_train_code.substring(
								station_train_code.indexOf("(") + 1, station_train_code.indexOf("-"));//北京   始发站
						String terminalStation = station_train_code.substring(
								station_train_code.indexOf("-") + 1, station_train_code.indexOf(")"));//沈阳   终点站
						//车次D1    始发站     终点站    train_no  车次等级D 
						String trainInfo = trainCode + "," + initialStation
								+ "," + terminalStation + "," + train_no + ","
								+ level;
						FileUtil.writeWord(os, trainInfo + "\n");
					}
				}
			}
			FileUtil.closeOutputStream(os);
		}
	}

	//读取cvs文件获取车次列表
	private List<Train> getTrainList(File file, int n) {
		List<Train> trainList = new ArrayList<Train>();
		BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			String line = bufferReader.readLine();
			int j = 0;
			while (line != null) {
				logger.info("[读取车次cvs文件]第" + ++j + "行");
				String[] elements = line.split(",");
				if (elements.length > 0) {
					String trainCode = elements[0];//车次:D1
					String initialStation = elements[1];//始发站
					String terminalStation = elements[2];//终点站
					String trainNo = elements[3];//train_no
					String level = elements[4];//车次等级D
					Train train = new Train(initialStation.trim(),
							terminalStation.trim(), trainNo.trim(),
							trainCode.trim(), level.trim());
					String filename = file.getName();
					logger.info("[读取车次cvs文件]filename:" + train.getStation_train_code());
					train.setStartDate(filename.substring(0, 10));
					trainList.add(train);

				}
				line = bufferReader.readLine();
			}
			if (bufferReader != null) {
				bufferReader.close();
			}
		} catch (IOException e) {
			logger.error("[读取车次cvs文件]发生错误：" + e.getMessage());
		}
		return trainList;
	}
 
	public String httpTrainList() {
		String result = "";		
		while(result == null || result.isEmpty() || result.length() == 0) {
//			result = HttpsUtil.sendHttps(TRAIN_LIST_URL);//https://kyfw.12306.cn/otn/resources/js/query/train_list.js
			long a=System.currentTimeMillis();
			result = HttpUtil.sendByPost(TRAIN_LIST_URL, "", "utf-8");
			long b=System.currentTimeMillis();
			if((b-a) > 10*60*1000){
				return null;
			}

		}
		int index = result.indexOf("{");
		result = result.substring(index);
		int lastIndex = result.lastIndexOf("}");
		result = result.substring(0, lastIndex + 1);
		FileUtil.writeFile("file/train_list.js_" + today,
				result,"utf-8");
		return result;
	}
}
