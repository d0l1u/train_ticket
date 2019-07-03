package com.l9e.common;

import java.util.HashMap;
import java.util.Map;

public class TrainConsts {
	public static String ERROR = "ERROR";

	public static String NO_DATAS = "NO_DATAS";

	public static String STATION_ERROR = "STATION_ERROR";

	public static String NO_ROBOT = "NO_ROBOT";

	public static String ROBOT_TIMEOUT = "ROBOT_TIMEOUT";

	public static final String RETURN_CODE_000 = "000";
	public static final String RETURN_CODE_001 = "001";
	public static final String RETURN_CODE_002 = "002";
	public static final String RETURN_CODE_003 = "003";
	public static final String RETURN_CODE_004 = "004";

	private static Map<String, String> RETURNCODE_TYPE = new HashMap<String, String>();// 对外接口状态码说明

	// 初始化
	static {
		RETURNCODE_TYPE.put(RETURN_CODE_000, "");
		RETURNCODE_TYPE.put(RETURN_CODE_001, "系统错误，未知服务异常。");
		RETURNCODE_TYPE.put(RETURN_CODE_002, "安全验证错误，不符合安全校验规则。");
		RETURNCODE_TYPE.put(RETURN_CODE_003, "输入参数为空或错误。");
		RETURNCODE_TYPE.put(RETURN_CODE_004, "非法的type接口参数。");
	}

	public static Map<String, String> getReturnCode() {
		return RETURNCODE_TYPE;
	}
}
