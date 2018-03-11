import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l9e.util.FileUtil;
import com.l9e.util.HttpsUtil;
import com.l9e.util.MatchUtil;


public class Test {

	public static void main(String[] args) throws JsonParseException, IOException {
		String result = null;
		if (FileUtil.existsFile("file/train_list.js")){
			result = FileUtil.readFile("file/train_list.js");
		}else {
			result = HttpsUtil.sendHttps("https://kyfw.12306.cn/otn/resources/js/query/train_list.js");
		}
		int index = result.indexOf("{");
		result = result.substring(index);
		int lastIndex = result.lastIndexOf("}");
		result = result.substring(0, lastIndex + 1);
	
		ObjectMapper objMapper = new ObjectMapper();
		
		JsonNode jsonNode = objMapper.readTree(result);
		Iterator<String> dateIterator = jsonNode.fieldNames();
		while(dateIterator.hasNext()) {
			String date = dateIterator.next();
			if(FileUtil.existsFile("file/" + date + ".csv")){
				continue;
			}
			OutputStream os = FileUtil.openOutputStream("file/" + date + ".csv");
			JsonNode dateNode = jsonNode.get(date);
			Iterator<String> levelIterator = dateNode.fieldNames();
			while(levelIterator.hasNext()) {
				String level = levelIterator.next();
				JsonNode levelNode = dateNode.get(level);
				if(levelNode.isArray()) {
					for(int i = 0;i < levelNode.size(); i++) {
						JsonNode trainNode = levelNode.get(i);
						String station_train_code = trainNode.get("station_train_code").asText();
						String train_no = trainNode.get("train_no").asText();
						String trainCode = "", initialStation = "", terminalStation = "", trainNo = "";
						trainCode = MatchUtil.match(station_train_code, "[C|Z|K|D|G|T|L][0-9]+|[0-9]+");
						initialStation = station_train_code.substring(station_train_code.indexOf("(") + 1, station_train_code.indexOf("-"));
						terminalStation = station_train_code.substring(station_train_code.indexOf("-") + 1, station_train_code.indexOf(")"));
						trainNo = train_no;
						String trainInfo = trainCode + ", " + initialStation + ", " + terminalStation + ", " + trainNo + ", " + level;
						FileUtil.writeWord(os, trainInfo + "\n");
					}
				}
			}
			FileUtil.closeOutputStream(os);
		}
	}

}
