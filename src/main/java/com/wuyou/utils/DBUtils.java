package com.wuyou.utils;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;

/**
 * @author Administrator
 * @date 2020年2月21日 上午9:08:44
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
public class DBUtils {
	private static final BasicDataSource DS;
	static {
		String driver = "org.sqlite.JDBC";
		String url = "jdbc:sqlite:coolq.db";
		DS = new BasicDataSource();
		DS.setDriverClassName(driver);
		DS.setUrl(url);
	}

	public static Connection getconn() throws Exception {
		return DS.getConnection();
	}

}
