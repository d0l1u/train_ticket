package test;

import org.apache.commons.lang.StringUtils;

public class StringBlankTest {

	public static void main(String[] args) {
		
		
		String train_code=" D3332";
		
		train_code=StringUtils.isNotEmpty(train_code)?train_code.trim():"";
		
		System.out.println(train_code);
	}

}
