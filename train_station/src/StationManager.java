import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
import com.l9e.util.HttpsUtil;

public class StationManager {

	public static String STATION_NAME_URL = "https://kyfw.12306.cn/otn/resources/js/framework/station_name.js";
	public static Logger logger = Logger.getLogger("StationManager.class");

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
	private StationDao stationDao = new StationDao();

	public static void main(String[] args) {

		new StationManager().updateStation();
	}

	public void updateStation() {
		getRecentStation();
		System.out.println("�ѻ�����µĳ�վ��Ϣ");
		List<Station> stationList = getStationList();
		System.out.println("�ѽ���վ��Ϣ����list��" + stationList.size());
		for (Station station : stationList) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("stationCode", station.getStationCode());
			map.put("stationName", station.getStationName());
			List<Station> selectStationList = stationDao.queryStation(map);
			if (selectStationList.isEmpty()) {
				logger.info("����ݿ������ݣ�" + station.getStationName());
				stationDao.insertStation(station);
			} else {
				logger.info("������ݣ�" + station.getStationName());
				stationDao.updateStation(station);
			}
		}
	}

	public void getRecentStation() {
		String result = null;
		File folderFile = new File("file");
		if (!(folderFile.exists() && folderFile.isDirectory())) {
			folderFile.mkdirs();
		}
		if (FileUtil.existsFile("file/station_name.js_"
				+ sdf.format(new Date()))) {
			result = FileUtil.readFile("file/train_list.js"
					+ sdf.format(new Date()));
			logger.info(result);
		} else {
			result = httpStationList();
		}
		String[] stations = result.split("@");
		BufferedWriter out = null;
		int length = FileUtil.readFile(
				"file/station_name_" + sdf.format(new Date()) + ".csv")
				.length();
		if (!(new File("file/station_name_" + sdf.format(new Date()) + ".csv")
				.exists())
				|| FileUtil.readFile(
						"file/station_name_" + sdf.format(new Date()) + ".csv")
						.length() == 0) {
			try {

				out = new BufferedWriter(new FileWriter("file/station_name_"
						+ sdf.format(new Date()) + ".csv", true));
				for (String station : stations) {
					if (station != null && station.length() > 0) {
						// System.out.println(station);
						if (station.trim().indexOf(",") == 0) {
							station = station.trim().substring(1);
						}
						String[] stationDetails = station.trim().split("\\|");
						if (stationDetails.length != 6) {
							System.out.println(station);
						}

						int stationId = Integer.parseInt(stationDetails[5]);
						String firstLetterOfStation = stationDetails[4];
						String stationPinyin = stationDetails[3];

						String stationCode = stationDetails[2];
						String stationName = stationDetails[1];
						String abbreviatedStation = stationDetails[0];
						Station stationVo = new Station(stationId, stationCode,
								stationName, stationPinyin, abbreviatedStation,
								firstLetterOfStation, Station.STATIONUSING);
						String detailInfo = stationId + ", " + stationName
								+ ", " + stationCode + ", " + stationPinyin
								+ ", " + firstLetterOfStation + ", "
								+ abbreviatedStation;
						out.write(detailInfo + "\n");
					}
				}
				if (out != null) {
					out.flush();
					out.close();
				}

			} catch (IOException e) {
				logger.error("����station_name�ļ�ʱ�������" + e.getMessage());
			}
		}
	}

	private List<Station> getStationList() {
		List<Station> stationList = new ArrayList<Station>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"file/station_name_" + sdf.format(new Date()) + ".csv"));
			// System.out.println("file/station_name_" + sdf.format(new Date())
			// + ".csv");
			// System.out.println(new File("file/station_name_" + sdf.format(new
			// Date()) + ".csv").getTotalSpace());
			String line = br.readLine();
			String[] infos = new String[7];
			// System.out.println(line);
			while (line != null) {
				// System.out.println(line);
				infos = line.trim().split(",");
				if (infos.length > 0 && infos[0].length() > 0) {
					Station stationVo = new Station(Integer.parseInt(infos[0]
							.trim()), infos[2].trim(), infos[1].trim(),
							infos[3].trim(), infos[5].trim(), infos[4].trim(), Station.STATIONUSING);
					stationList.add(stationVo);
				}
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			logger.info("��ȡ��վ�ļ�ʱ�������" + e.getMessage());
		} catch (IOException e) {
			logger.error("��ȡ��վ�ļ�ʱ�������" + e.getMessage());
		}

		return stationList;
	}

	public String httpStationList() {
		String result = "";
		result = HttpsUtil.sendHttps(StationManager.STATION_NAME_URL);
		System.out.println(result);
		int index = result.indexOf("\'");
		result = result.substring(index + 2);
		int lastIndex = result.lastIndexOf("\'");
		if (lastIndex != -1) {
			result = result.substring(0, lastIndex);
		}
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		FileUtil.writeFile("file/station_name.js_" + sdf.format(new Date()),
				result,"utf-8");
		return result;
	}
}
