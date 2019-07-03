package test;

import java.util.HashMap;
import java.util.Map;

import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;

public class TestIsLock {

	public static void main(String[] args) {

		Map<String, String> maps = new HashMap<String, String>();
		maps.put("ScriptPath", "isLocked.lua");
		maps.put("SessionID", String.valueOf(System.currentTimeMillis()));
		maps.put("Timeout", "20000");
		maps.put("ParamCount", "1");

		String param = "";
		try {
			param = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String reqResult = HttpUtil.sendByPost("http://127.0.0.1:8091/RunScript", param, "30000", "30000", "UTF-8");

		System.out.println(reqResult);

	}

}
