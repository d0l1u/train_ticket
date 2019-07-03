package com.l9e.train.notify.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ICallBack {

	public void execute(Connection cn, PreparedStatement cs)throws SQLException;
}
