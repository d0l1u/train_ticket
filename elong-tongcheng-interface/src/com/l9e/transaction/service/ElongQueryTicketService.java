package com.l9e.transaction.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ElongQueryTicketService {
	void queryTicket(HttpServletRequest request, 
			HttpServletResponse response);
}
