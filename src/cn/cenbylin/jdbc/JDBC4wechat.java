package cn.cenbylin.jdbc;

import java.lang.Class;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC4wechat {
	private static final String className = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:mysql://localhost:3306/wechat?useUnicode=true&characterEncoding=utf8";
	private static final String username = "root";
	private static final String password = "037037037";
	java.sql.Connection conn = null;// 每个对象的数据库链接

	public JDBC4wechat() throws SQLException {
		// 加载驱动
		try {
			Class.forName(className);
			System.out.println("1、加载成功");
		} catch (ClassNotFoundException e) {
			System.out.println("找不到驱动程序类 ，加载驱动失败！");
			e.printStackTrace();
		}
		// 连接数据库
		try {
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("2、连接成功");
		} catch (SQLException se) {
			System.out.println("数据库连接失败！");
			se.printStackTrace();
		}
	}

	public ResultSet doSqlQuery(String sql) throws SQLException {
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			System.out.println("3、执行sql语句");
			rs = stmt.executeQuery(sql);
			System.out.println("3.1、执行sql语句");
			// rs.getString("id");//结果集操作
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			rs.close();
			stmt.close();
			conn.close();
		}
		return rs;
	}

	public int doSqlUpdate(String sql) throws SQLException {
		int rs = 0;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			System.out.println("3、执行sql语句");
			rs = stmt.executeUpdate(sql);
			System.out.println("3.1、执行sql语句");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			stmt.close();
			conn.close();
		}
		return rs;
	}

}
