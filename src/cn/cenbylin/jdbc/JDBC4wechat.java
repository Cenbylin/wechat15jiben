package cn.cenbylin.jdbc;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * 实例化本类，需要记住的是使用完之后执行close方法关闭数据库连接。
 * 
 * @author Cenby7
 *
 */
public class JDBC4wechat {
	private static final String className = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:mysql://localhost:3306/wechat?useUnicode=true&characterEncoding=utf8";
	private static final String username = "root";
	private static final String password = "037037037";
	
	
	java.sql.Connection conn = null;// 初始化链接

	public JDBC4wechat() throws SQLException {
		try {
			Class.forName(className);
			System.out.println("1、加载驱动");
		} catch (ClassNotFoundException e) {
			System.out.println("加载驱动失败");
			e.printStackTrace();
		}
		//建立连接
		try {
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("2、连接成功");
		} catch (SQLException se) {
			System.out.println("连接数据库失败");
			se.printStackTrace();
		}
	}

	public ResultSet doSqlQuery(String sql) throws SQLException {
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			System.out.println("3、处理sql语句");
			rs = stmt.executeQuery(sql);
			System.out.println("3.1、处理sql完成");
			//rs.next();
			//System.out.println(rs.getString("picstatement"));//结果集操作
			
		} catch (SQLException e) {
			e.printStackTrace();
			stmt.close();
		}
		return rs;
	}

	public int doSqlUpdate(String sql) throws SQLException {
		int rs = 0;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			System.out.println("3、处理sql");
			rs = stmt.executeUpdate(sql);
			System.out.println("3.1处理sql完成");
		} catch (SQLException e) {
			e.printStackTrace();
			stmt.close();
		} 
		return rs;
	}
	public void close(){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
