import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.l9e.transaction.dao.StationDao;
import com.l9e.transaction.vo.Station;
import com.l9e.util.FileUtil;
import com.l9e.util.HttpUtil;
/**
 * 更新车站信息   
 * t_zm表的数据
 * @author guona
 *
 */
public class ZmManager {
	public static String STATION_NAME_URL = "http://118.190.94.73:8027/servlet/queryStationName";
//	public static String STATION_NAME_URL = "https://kyfw.12306.cn/otn/resources/js/framework/station_name.js";
	public static Logger logger = Logger.getLogger("ZmManager.class");

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
	private String today = sdf.format(new Date());//2016-01-07
	
	private StationDao stationDao = new StationDao();
	
	public static void main(String[] args) {
		new ZmManager().updateStation(); 
	}

	public void updateStation() {
		logger.info("开始执行获取车站信息~~~~~");
		getRecentStation();
		logger.info("已获得最新的车站信息");
		List<Station> stationList = getStationList();//最新的车站信息列表
		logger.info("updateStation已将" + stationList.size() + "条车站信息填入stationList");
		for (Station station : stationList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("stationCode", station.getStationCode());//VAP			车站名code
			map.put("stationName", station.getStationName());//北京北			车站名称
			List<Station> selectStationList = stationDao.queryStationFromZm(map);
			
			if (selectStationList == null || selectStationList.isEmpty()) {		
				Map<String, String> map1 = new HashMap<String, String>();
				map1.put("stationCode", station.getStationCode());//VAP			车站名code
				List<Station> selectStationList1 = stationDao.queryStationFromZm(map1);
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("stationName", station.getStationName());//北京北			车站名称
				List<Station> selectStationList2 = stationDao.queryStationFromZm(map2);
				
				if(!selectStationList1.isEmpty()){
					stationDao.deteStationByCode(selectStationList1);
				}
				if(!selectStationList2.isEmpty()){
					stationDao.deteStationByName(selectStationList2);
				}
							
				logger.info("向数据库t_zm添加数据新车站：" + station.getStationName());
				stationDao.insertStationIntoZm(station);
			} else {
				Station queryStation = selectStationList.get(0);
				if (!queryStation.getfirstLettersOfStation().equals(
						station.getfirstLettersOfStation())
						|| !queryStation.getStationName().equals(
								station.getStationName())
						|| !queryStation.getStationCode().equals(
								station.getStationCode())) {
					logger.info("更新t_zm数据信息车站名：" + station.getStationName());
					stationDao.updateStationOfZm(station);
				}else{
					stationDao.updateStationOfZm(station);
				}
			}
		}
		logger.info("更新车站执行结束！！");
	}

	//获得最新的12306车站信息
	public void getRecentStation() {
		String result = null;
		File folderFile = new File("file");
		if (!(folderFile.exists() && folderFile.isDirectory())) {
			folderFile.mkdirs();
		}
		//查找/file/station_name.js_2016-01-07（当天的车站名js文件）
		if (FileUtil.existsFile("file/station_name.js_" + today)) {
			result = FileUtil.readFile("file/station_name.js_" + today);
			logger.info("从js文件中读取的车站信息：" + result);
		} else {
			result = httpStationList();
		}
		//bjb|北京北|VAP|beijingbei|bjb|0@bjd|北京东|BOP|beijingdong|bjd|1@bji|北京|BJP|beijing|bj|2@bjn|北京南|VNP|beijingnan|bjn|3@bjx|北京西|BXP|beijingxi|bjx|4@
		String[] stations = result.split("@");
		BufferedWriter out = null;
//		当天的车站名cvs文件不存在，遍历js文件内容写入cvs文件
		if (!(new File("file/station_name_" + today + ".csv").exists())
				|| FileUtil.readFile("file/station_name_" + today + ".csv").length() == 0) {
			logger.info("当天的车站名cvs文件不存在");
			try {
				out = new BufferedWriter(new FileWriter("file/station_name_"
						+ today + ".csv", true));
				for (String station : stations) {
					//bjb|北京北|VAP|beijingbei|bjb|0
					if (station != null && station.length() > 0) {
						if (station.trim().indexOf(",") == 0) {
							station = station.trim().substring(1);
						}
						String[] stationDetails = station.trim().split("\\|");
						if (stationDetails.length != 6) {
							logger.info("[line102]station length is not 6------"+station);
						}

						int stationId = Integer.parseInt(stationDetails[5]);//0	车站名id
						String firstLetterOfStation = stationDetails[4];//bjb	车站名首字母简写
						String stationPinyin = stationDetails[3];//beijingbei	车站名拼音
						String stationCode = stationDetails[2];//VAP			车站名code
						String stationName = stationDetails[1];//北京北			车站名称
						String abbreviatedStation = stationDetails[0];//bjb		车站名缩写
						//cvs文件格式：id  名称    code  拼音    首字母简写	缩写
						String detailInfo = stationId + ", " + URLEncoder.encode(stationName,"utf-8")
								+ ", " + stationCode + ", " + stationPinyin
								+ ", " + firstLetterOfStation + ", "
								+ abbreviatedStation;
						logger.info("写入cvs文件的车站信息：" + detailInfo);
						out.write(detailInfo + "\n");
					}
				}
				if (out != null) {
					out.flush();
					out.close();
				}

			} catch (IOException e) {
				logger.error("更新station_name的cvs文件时发生错误：" + e.getMessage());
			}
		}
	}

	private List<Station> getStationList() {
		List<Station> stationList = new ArrayList<Station>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"file/station_name_" + today + ".csv"));
			String line = br.readLine();
			String[] infos = new String[7];
			while (line != null) {
				infos = line.trim().split(",");
				if (infos.length > 0 && infos[0].length() > 0) {
					Station stationVo = new Station(Integer.parseInt(infos[0]
							.trim()), infos[2].trim(), URLDecoder.decode(infos[1].trim(), "UTF-8"),
							infos[3].trim(), infos[5].trim(), infos[4].trim(), Station.STATIONUSING);//运营中的
					stationList.add(stationVo);
				}
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			logger.info("读取车站文件时发生错误：" + e.getMessage());
		} catch (IOException e) {
			logger.error("读取车站文件时发生错误：" + e.getMessage());
		}

		return stationList;
	}

	public String httpStationList() {
		String result = "";
		result = HttpUtil.sendByPost(STATION_NAME_URL, "", "utf-8");
		int index = result.indexOf("\'");
		result = result.substring(index + 2);
		int lastIndex = result.lastIndexOf("\'");
		if (lastIndex != -1) {
			result = result.substring(0, lastIndex);
		}
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		try { 
			FileUtil.writeFile("file/station_name.js_" + today,
					result, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
