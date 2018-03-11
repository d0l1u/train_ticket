package test;

import com.l9e.util.ParamCheckUtil;

public class ZhengzeTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String  from_station="大 庆";
		System.out.println(from_station.length());
		//大连大庆
		//String  from_station="三亚";
		//\s
		// ^[^ ]+[\s\S]*[^ ]+$
		//^[\u4e00-\u9fa5\\s]{1,10}$
		//^[\u4e00-\u9fa5\\s]{1,10}$
		//^[\u4e00-\u9fa5\\s]{1,10}$
		//^[^ ][\u4e00-\u9fa5\\s]{1,10}[^ ]$
		//^[^ ]+[\u4e00-\u9fa5\\s]?[^ ]+$
		//
		boolean flag=ParamCheckUtil.isNotCheck(from_station,"^[\u4e00-\u9fa5\\s]{1,10}$");
		System.out.println(flag);
		
		

	}

}
