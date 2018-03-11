package com.l9e.transaction.job;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.NeedRepayService;
/**
 * 用户续费自动流程
 * @author liht
 *
 */

@Component("needRepayJob")
public class NeedRepayJob {
	private static final Logger logger = Logger.getLogger(NeedRepayJob.class);
	@Resource
	private NeedRepayService needRepayService;
	public void modifyNeedRepay() throws Exception {
		List<String> overdueList = needRepayService.queryNeedRepay() ;
		System.out.println(overdueList) ;
		for(int i=0 ;i<overdueList.size();i++){
			needRepayService.updateNeedRepay(overdueList.get(i).toString());
		}
	}
}
