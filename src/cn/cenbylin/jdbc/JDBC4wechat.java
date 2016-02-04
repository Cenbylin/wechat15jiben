package cn.cenbylin.jdbc;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC4wechat {
	private static final String className = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:mysql://localhost:3306/wechat?useUnicode=true&characterEncoding=utf8";
	private static final String username = "root";
	private static final String password = "037037037";
	java.sql.Connection conn = null;// ÿ���������ݿ�����

	public JDBC4wechat() throws SQLException {
		// ������
		try {
			Class.forName(className);
			System.out.println("1�����سɹ�");
		} catch (ClassNotFoundException e) {
			System.out.println("�Ҳ���������� ��������ʧ�ܣ�");
			e.printStackTrace();
		}
		// ������ݿ�
		try {
			conn = DriverManager.getConnection(url, username, password);
			System.out.println("2�����ӳɹ�");
		} catch (SQLException se) {
			System.out.println("��ݿ�����ʧ�ܣ�");
			se.printStackTrace();
		}
	}

	public ResultSet doSqlQuery(String sql) throws SQLException {
		ResultSet rs = null;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			System.out.println("3��ִ��sql���");
			rs = stmt.executeQuery(sql);
			System.out.println("3.1��ִ��sql���");
			// rs.getString("id");//������
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
			System.out.println("3��ִ��sql���");
			rs = stmt.executeUpdate(sql);
			System.out.println("3.1��ִ��sql���");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			stmt.close();
			conn.close();
		}
		return rs;
	}

}
