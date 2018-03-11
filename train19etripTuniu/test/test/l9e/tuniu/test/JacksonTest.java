package test.l9e.tuniu.test;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.l9e.transaction.vo.SynchronousOutput;
import com.l9e.transaction.vo.TuniuPassenger;

public class JacksonTest {
	
	private static final ObjectMapper mapper = new ObjectMapper();
	
	static {
		mapper.setSerializationInclusion(Include.NON_NULL);
	}

	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
//		synOutTest();
//		baseArrayTest()
		String json = "{\"passengers\":[{\"passengerId\":13985573,\"reason\":0,\"passportTypeName\":\"二代身份证\",\"piaoTypeName\":\"成人票\",\"zwName\":\"硬座\",\"passengerName\":\"张三\",\"zwCode\":\"1\",\"piaoType\":\"1\",\"passportTypeId\":\"1\",\"passportNo\":\"610103197598242518\",\"price\":156.0}]}";
		JsonNode root = mapper.readTree(json);
		JsonNode node = root.path("passengers");
		if(!node.isMissingNode() && node.isArray()) {
			CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, TuniuPassenger.class);
			String nodeStr = node.toString();
			System.out.println(nodeStr);
			List<TuniuPassenger> passengers = mapper.readValue(nodeStr, type);
			for(TuniuPassenger passenger : passengers) {
				System.out.println(passenger.getPassengerId());
			}
		}
	}
	
	public static void baseArrayTest() throws JsonProcessingException, IOException {
		String json = "{\"name\":\"巴拉拉小魔仙\",\"array\":[123,456,789]}";
		JsonNode root = mapper.readTree(json);
		JsonNode nameNode = root.path("name");
		if(!nameNode.isMissingNode()) {
			System.out.println(nameNode.asText());
		}
		JsonNode arrayNode = root.path("array");
		
		if(!arrayNode.isMissingNode() && arrayNode.isArray()) {
			CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, Integer.class);
			List<Integer> list = mapper.readValue(arrayNode.toString(), type);
			for(int o : list) {
				System.out.println(o);
			}
		}
	}
	
	public static void synOutTest() throws JsonProcessingException {
		SynchronousOutput out = new SynchronousOutput();
		out.setSuccess(false);
		out.setReturnCode("23309");
		out.setErrorMsg("巴拉巴拉小魔仙");
		out.setData("alksjdlakj98898_098374kkls?");
		
		String result = mapper.writeValueAsString(out);
		System.out.println(result);
	}
}
