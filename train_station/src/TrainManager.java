import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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
import com.l9e.transaction.dao.TrainDao;
import com.l9e.transaction.vo.Train;
import com.l9e.util.FileUtil;
import com.l9e.util.HttpsUtil;
import com.l9e.util.MatchUtil;

public class TrainManager {
	public static Logger logger = Logger.getLogger("TrainInfoManager.class");
	public static String TRAIN_LIST_URL = "https://kyfw.12306.cn/otn/resources/js/query/train_list.js";

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
	private TrainDao trainDao = new TrainDao();
	private StationDao stationDao = new StationDao();

	public void updateTrain() {
		getRecentTrain();
		File trianListFolderFile = new File("file");
		File[] trainListFiles = trianListFolderFile.listFiles();
		List<Train> trainList = new ArrayList<Train>();
		Map<String, String> paramMap = new HashMap<String, String>();

		for (File file : trainListFiles) {
			List<Train> insertTrainList = new ArrayList<Train>();
			if (file.getName().contains("train_list")
					&& file.getName().contains("csv")
					&& !file.getName().contains("_Y")) {
				trainList = getTrainList(file);
				for (Train train : trainList) {
					paramMap.put("train_code", train.getTrainCode());
					paramMap.put("train_no", train.getTrainNo());
					Map<String, String> stationMap = new HashMap<String, String>();
					stationMap.put("station_name", train.getStartStation());
					train.setStartStationCode(stationDao
							.queryStation(stationMap).get(0).getStationCode());
					stationMap.put("station_name", train.getEndStation());
					train.setEndStationCode(stationDao.queryStation(stationMap)
							.get(0).getStationCode());
					List<Train> queryTrainList = trainDao.queryTrain(paramMap);
					if (queryTrainList.size() == 0) {
						logger.info("[һ���г���Ҫ������ݿ�]��" + train.getTrainCode()
								+ ", " + train.getTrainNo());
						train.setStatus(Train.NEEDUPDATE);
						insertTrainList.add(train);

					} else {
						logger.info("[һ���г���Ҫ����]��" + train.getTrainCode() + ", "
								+ train.getTrainNo());
						Train queryTrain = queryTrainList.get(0);
						if (!train.getStartStation().equals(
								queryTrain.getStartStation())
								|| !train.getEndStation().equals(
										queryTrain.getEndStation())) {
							train.setStatus(Train.NEEDUPDATE);
						}
						String startDate = queryTrain.getStartDate();

						if (startDate != null || startDate.trim().length() != 0) {
							train.setStartDate(null);
						}
						trainDao.updateTrain(train);
					}
				}
				trainDao.insertTrain(insertTrainList);
				String filename = file.getName();
				file.renameTo(new File(file.getAbsolutePath() + "//" + filename
						+ "_Y"));
			}
		}
	}

	public void getRecentTrain() {
		String result = null;
		if (FileUtil.existsFile("file/train_list.js" + sdf.format(new Date()))) {
			result = FileUtil.readFile("file/train_list.js"
					+ sdf.format(new Date()));
		} else {
			result = httpTrainList();
		}

		ObjectMapper objMapper = new ObjectMapper();

		JsonNode jsonNode = null;
		try {
			jsonNode = objMapper.readTree(result);
		} catch (JsonProcessingException e) {
			logger.error("���������ַ����쳣��" + e.getMessage());
		} catch (IOException e) {
			logger.error("���������ַ����쳣��" + e.getMessage());
		}
		Iterator<String> dateIterator = jsonNode.fieldNames();
		while (dateIterator.hasNext()) {
			String date = dateIterator.next();
			if (FileUtil.existsFile("file/train_list_" + date + ".csv")) {
				continue;
			}
			OutputStream os = FileUtil
					.openOutputStream("file/" + date + ".csv");
			JsonNode dateNode = jsonNode.get(date);
			System.out.println(dateNode.asText());
			Iterator<String> levelIterator = dateNode.fieldNames();
			while (levelIterator.hasNext()) {
				String level = levelIterator.next();
				JsonNode levelNode = dateNode.get(level);
				if (levelNode.isArray()) {
					for (int i = 0; i < levelNode.size(); i++) {
						JsonNode trainNode = levelNode.get(i);
						String station_train_code = trainNode.get(
								"station_train_code").asText();
						String train_no = trainNode.get("train_no").asText();
						String trainCode = "", initialStation = "", terminalStation = "", trainNo = "";
						trainCode = MatchUtil.match(station_train_code,
								"[C|Z|K|D|G|T|L][0-9]+|[0-9]+");
						initialStation = station_train_code.substring(
								station_train_code.indexOf("(") + 1,
								station_train_code.indexOf("-"));
						terminalStation = station_train_code.substring(
								station_train_code.indexOf("-") + 1,
								station_train_code.indexOf(")"));
						trainNo = train_no;
						String trainInfo = trainCode + ", " + initialStation
								+ ", " + terminalStation + ", " + trainNo
								+ ", " + level;
						FileUtil.writeWord(os, trainInfo + "\n");
					}
				}
			}
			FileUtil.closeOutputStream(os);
		}
	}

	private List<Train> getTrainList(File file) {
		List<Train> trainList = new ArrayList<Train>();
		BufferedReader bufferReader = null;
		try {
			bufferReader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			String line = bufferReader.readLine();
			while (line != null) {
				String[] elements = line.split(",");
				if (elements.length > 0) {
					String trainCode = elements[0];
					String initialStation = elements[1];
					String terminalStation = elements[2];
					String trainNo = elements[3];
					String level = elements[4];
					Train train = new Train(initialStation, terminalStation,
							trainNo, trainCode, level);
					train.setStartDate(file.getName().substring(11, 21));
					trainList.add(train);
				}
			}
			if (bufferReader != null) {
				bufferReader.close();
			}
		} catch (FileNotFoundException e) {
			logger.error("��ȡ�����ļ�ʱ�������" + e.getMessage());
		} catch (IOException e) {
			logger.error("��ȡ�����ļ�ʱ�������" + e.getMessage());
		}
		return trainList;
	}

	public String httpTrainList() {
		String result = "";
		result = HttpsUtil.sendHttps(TrainManager.TRAIN_LIST_URL);
		int index = result.indexOf("{");
		result = result.substring(index);
		int lastIndex = result.lastIndexOf("}");
		result = result.substring(0, lastIndex + 1);
		FileUtil.writeFile("file/train_list.js" + sdf.format(new Date()),
				result,"utf-8");
		return result;
	}
}
