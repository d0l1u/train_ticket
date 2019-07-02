package com.l9e.train.notify.request;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.l9e.train.po.OrderBill;

public class NotifyRequestTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		NotifyRequest request = new NotifyRequest();
		
		OrderBill orderbill = new OrderBill();
		
		orderbill.setBuyMoney("5.5");
		orderbill.setNotifyUrl("http://www.din.com");
		orderbill.setOrderId("01010011");
		orderbill.setOutTicketBillno("E010102");
		orderbill.setOrderStatus(OrderBill.BILL_SUCCESS);
		orderbill.addOrderCp("test|test|郭斌");
		
		request.sending(orderbill);
	}

}
