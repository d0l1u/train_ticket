package com.l9e.transaction.job;

import org.springframework.stereotype.Component;

import com.l9e.common.WorkerBase;
import com.l9e.transaction.vo.Worker;

@Component("setRegistWorker")
public class SetRegistWorker extends WorkerBase {

	@Override
	public Integer type() {
		return Worker.TYPE_REGIST;
	}

	@Override
	public Integer maxSize() {
		return 50;
	}

	@Override
	public Integer executeMilliseconds() {
		return 1000;
	}

	@Override
	public String createLogid() {
		String millis = String.valueOf(System.currentTimeMillis());
		String logid = "[JOB-Regist-" + millis.substring(millis.length() - 4) + "] ";
		return logid;
	}
}
