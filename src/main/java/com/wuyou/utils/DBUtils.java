package com.wuyou.utils;

import java.sql.Connection;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * @author Administrator
 * @date 2020年2月21日 上午9:08:44
 */
public class DBUtils {
	private static BasicDataSource ds;
	static {
		String driver = "org.sqlite.JDBC";
		String url = "jdbc:sqlite:coolq.db";
		ds = new BasicDataSource();
		ds.setDriverClassName(driver);
		ds.setUrl(url);
	}

	public static Connection getconn() throws Exception {
		Connection conn = ds.getConnection();
		return conn;
	}

}
