import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l9e.transaction.dao.StationDao;
import com.l9e.transaction.vo.CtripStation;
import com.l9e.transaction.vo.Station;
import com.l9e.util.FileUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.HttpsUtil;

public class CtripStationManager {

	public static String STATION_NAME_URL = "http://webresource.ctrip.com/ResTrainOnline/R1/TrainBooking/JS/station_gb2312.js";
	public static Logger logger = Logger.getLogger("CtripStationManager.class");

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
	private StationDao stationDao = new StationDao();

	public static void main(String[] args) {

		new CtripStationManager().updateCtripStation();

	}

	/**
	 * 更新插入站名和拼音，t_ctrip_zm1
	 */
	public void updateCtripStation() {
		
		logger.info("开始更新插入站名");

		getRecentCtripStation();// 生成csv文件

		logger.info("更新车站名称");
		List<CtripStation> stationList = getStationListNew(); // 解析csv文件

		System.out.println("total size is " + stationList.size());
		for (CtripStation station : stationList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", station.getName());
			map.put("pinyin", station.getPinyin());

			List<CtripStation> selectStationList = stationDao.queryCtripStation(map);
			System.out.println(selectStationList);

			if (selectStationList.isEmpty()) {
				logger.info("车站不存在" + station.getName());
				station.setCc_status("00"); // 未查询
				stationDao.insertCtripStation(station);
			} else {
				logger.info("更新车站," + station.getName());
				CtripStation stationOld = selectStationList.get(0);
				if (!stationOld.getName().equals(station.getName()) || !stationOld.getPinyin().equals(station.getPinyin())) {
					logger.info("与库里数据不一致");
				}
				station.setCc_status("00"); // 未查询

				stationDao.updateStation(station);
			}
		}
		 logger.info("-----------车站和拼音更新完成-------");
	}

	/**
	 * 根据station_gb2312.js,生成csv文件
	 */
	public static void getRecentCtripStation() {

		String result = null;
		File folderFile = new File("file");
		if(!(folderFile.exists() && folderFile.isDirectory())) {
			folderFile.mkdirs();
		}
		if(FileUtil.existsFile("file/station_gb2312.js_" + sdf.format(new Date()))) {
			result = FileUtil.readFile("file/station_gb2312.js_" + sdf.format(new Date()));
			logger.info("从js文件中读取的车站信息：" + result);
		}else{
			logger.info("文件不存在,从网络获取!!!");
			result = httpCtripStationList();
		}
	
		logger.info(result);

		String[] stations = result.split("@");
		BufferedWriter out = null;

		if (!(new File("file/station_gb2312.js_" + sdf.format(new Date()) + ".csv").exists())
				|| FileUtil.readFile("file/station_gb2312.js_" + sdf.format(new Date()) + ".csv").length() == 0) {
			try {
				out = new BufferedWriter(
						new FileWriter("file/station_gb2312.js_" + sdf.format(new Date()) + ".csv", true));
				for (String station : stations) {
					if (station != null && station.length() > 0) {
						System.out.println(station);
						if (station.trim().indexOf(",") == 0) {
							station = station.trim().substring(1);
						}
						String[] stationDetails = station.trim().split("\\|");
						if (stationDetails.length != 6) {
							System.out.println(station);
						}

						// shenyang3|沈阳|shenyang3|沈阳|shenyang3|sy
						String name = stationDetails[3];
						String pinying = stationDetails[4];
						String janpin = stationDetails[5];
						String detailInfo = name + ", " + pinying + "," + janpin;
						out.write(detailInfo + "\n");
					}
				}

				if (out != null) {
					out.flush();
					out.close();
				}

			} catch (IOException e) {
				logger.error("异常" + e.getMessage());
			}
		} else {
			logger.info("文件已存在！");
		}

	}

	/**
	 * 读取station_gb2312.js_2016_05_17.csv
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<CtripStation> getStationListNew() {

		List<CtripStation> stationList = new ArrayList<CtripStation>();
		try {
			BufferedReader br = new BufferedReader(
					new FileReader("file/station_gb2312.js_" + sdf.format(new Date()) + ".csv"));
			String line = br.readLine();
			String[] infos = new String[7];
			while (line != null) {
				infos = line.trim().split(",");
				if (infos.length > 0 && infos[0].length() > 0) {
					CtripStation ctripStation = new CtripStation(infos[0].trim(), infos[1].trim());

					stationList.add(ctripStation);
				}
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			logger.info("not this file" + e.getMessage());
		} catch (IOException e) {
			logger.error("not this file" + e.getMessage());
		}

		System.out.println(stationList.size());

		return stationList;
	}

	/**
	 * @return 访问携程获取站名文件
	 */
	public static String httpCtripStationList() {
		String result = "";
		result = HttpUtil.sendByGet(CtripStationManager.STATION_NAME_URL, "gbk", "30000", "30000");
		logger.info("result:" + result);
		int index = result.indexOf("cQuery.jsonpResponse.data = \"") + "cQuery.jsonpResponse.data = \"".length();

		result = result.substring(index);
		int lastIndex = result.lastIndexOf("\"");
		if (lastIndex != -1) {
			result = result.substring(0, lastIndex);
		}
		logger.info("result:" + result);
		FileUtil.writeFile("file/station_gb2312.js_" + sdf.format(new Date()), result, "utf-8");
		return result;
	}

	// *******************************************************************************//

	/**
	 * @return
	 */
	@Deprecated
	private List<CtripStation> getStationList() {

		List<CtripStation> stationList = new ArrayList<CtripStation>();
		String fileNames[] = { "ABCD.csv", "EFGH.csv", "JKLM.csv", "NOPQRS.csv", "TUVWX.csv", "YZ.csv" };
		for (int i = 0; i < fileNames.length; i++) {
			try {

				BufferedReader br = new BufferedReader(new FileReader("file/" + fileNames[i]));
				String line = br.readLine();
				String[] infos = new String[7];
				while (line != null) {
					infos = line.trim().split(",");
					if (infos.length > 0 && infos[0].length() > 0) {
						CtripStation ctripStation = new CtripStation(infos[0].trim(), infos[1].trim());

						stationList.add(ctripStation);
					}
					line = br.readLine();
				}
			} catch (FileNotFoundException e) {
				logger.info("not this file" + e.getMessage());
			} catch (IOException e) {
				logger.error("not this file" + e.getMessage());
			}

			System.out.println(stationList.size());
		}

		return stationList;
	}

	@Deprecated
	private static void getCtripZmCSV() {

		String res2 = getCtripZmJSON();
		ObjectMapper objMapper = new ObjectMapper();
		JsonNode jsonNode = null;
		try {
			jsonNode = objMapper.readTree(res2);
		} catch (JsonProcessingException e) {
			logger.error("解析车次字符串发成异常：" + e.getMessage());
		} catch (IOException e) {
			logger.error("解析车次字符串发成异常：" + e.getMessage());
		}

		Iterator<String> typeIterator = jsonNode.fieldNames();
		while (typeIterator.hasNext()) {
			String type = typeIterator.next();
			if (FileUtil.existsFile("file/" + type + ".csv")) {
				continue;
			}
			OutputStream os = FileUtil.openOutputStream("file/" + type + ".csv");
			JsonNode typeNode = jsonNode.get(type);
			if (typeNode.isArray()) {
				for (int i = 0; i < typeNode.size(); i++) {
					JsonNode trainNode = typeNode.get(i); // { display: "敦煌",
															// data:
															// "|敦煌|dunhuang"}
					String dispaly = trainNode.get("display").asText();
					String data = trainNode.get("data").asText();
					String[] datas = data.split("\\|");
					String zmInfo = dispaly + "," + datas[2];
					FileUtil.writeWord(os, zmInfo + "\n");
				}
			}
			FileUtil.closeOutputStream(os);
		}
	}

	@Deprecated
	private static String getCtripZmJSON() {

		String result = FileUtil.readFile("file/station_gb2312.js_2016_05_17");
		int a = result.indexOf("热门");
		String res = result.substring(a - 3);
		int b = res.indexOf(";");
		String res1 = res.substring(0, b);
		String res2 = res1.replace("data", "\"data\"").replace("display", "\"display\"");

		return res2;
	}

	@Deprecated
	private static String getCtripZmStr() {

		String result = FileUtil.readFile("file/station_gb2312.js_2016_05_17");
		int a = result.indexOf("beijingbei");
		String res = result.substring(a);
		int b = res.lastIndexOf("@");
		String res1 = res.substring(0, b);
		System.out.println(res1);
		return res1;
	}

}
