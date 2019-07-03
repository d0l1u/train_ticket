package com.l9e.train.channel.request.impl;

import java.util.List;

import com.l9e.train.channel.request.IRequest;
import com.l9e.train.po.Account;
import com.l9e.train.po.CtripAcc;
import com.l9e.train.po.JdAcc;
import com.l9e.train.po.JdPrePayCard;
import com.l9e.train.po.Order;
import com.l9e.train.po.Result;
import com.l9e.train.po.Worker;

public class RequestImpl implements IRequest {
	protected Account account;
	protected Worker worker;
	protected Result result;
	protected CtripAcc ctripAcc;

	// 新增京东账户和预付卡实体
	protected JdAcc jdAcc;
	protected List<JdPrePayCard> jdPrePayCardList;

	// 12306出票
	protected RequestImpl(Account account, Worker worker) {
		result = new Result();
		result.setAccount(account);
		result.setWorker(worker);

		this.account = account;
		this.worker = worker;
	}

	// 携程出票
	protected RequestImpl(Account account, Worker worker, CtripAcc ctripAcc) {
		result = new Result();
		result.setAccount(account);
		result.setWorker(worker);
		result.setCtripAcc(ctripAcc);

		this.account = account;
		this.worker = worker;
		this.ctripAcc = ctripAcc;
	}

	// 京东出票
	protected RequestImpl(Account account, Worker worker, JdAcc jdAcc, List<JdPrePayCard> jdPrePayCardList) {
		result = new Result();
		result.setAccount(account);
		result.setWorker(worker);
		result.setJdAcc(jdAcc);
		result.setJdPrePayCardList(jdPrePayCardList);

		this.account = account;
		this.worker = worker;
		this.jdAcc = jdAcc;
		this.jdPrePayCardList = jdPrePayCardList;
	}

	@Override
	public Result request(Order order, String weight, String logid) {
		return null;
	}

	@Override
	public Worker getWorker() {
		return worker;
	}

	@Override
	public Account getAccount() {
		return account;
	}
}
