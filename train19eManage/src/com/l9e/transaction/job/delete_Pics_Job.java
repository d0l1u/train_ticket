package com.l9e.transaction.job;

import java.io.File;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component("delete_Pics_Job")
public class delete_Pics_Job {
	private static final Logger logger = Logger
			.getLogger(delete_Pics_Job.class);

	public void deletePics() {
		logger.info("delete_Pics_Job开始执行");
		String path = this.getClass().getClassLoader().getResource("/").getPath();
		//System.out.println(path);
		File file1 = new File(path);
		File file = new File(file1.getParentFile().getParentFile()+File.separator+"jchart");
		//System.out.println(file);
		if (!file.exists()) {
			logger.info("jchar文件夹不存在");
			return;
		}
		//jchart目录里面只有文件，没有其他的目录了
		if (file.isDirectory()) {
			File[] list = file.listFiles();
			//System.out.println(Arrays.toString(list));
			for (int i = 0; i < list.length; i++) {
				File temp = list[i];
				if (temp.isFile()) {
					temp.delete();
				}
			}
		}
		logger.info("jchar文件夹已经清空");
	}
}
