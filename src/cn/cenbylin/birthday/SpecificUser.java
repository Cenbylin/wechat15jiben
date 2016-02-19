package cn.cenbylin.birthday;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import cn.cenbylin.dao.JDBC4wechat;
import cn.cenbylin.tool.HttpRequestTool;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 此类是对微信访问用户的封装，一旦初始化，各种信息将会读取，并更新数据库。
 * 
 * @author Cenbylin
 * @version 1.0
 */
public class SpecificUser {
	private final String inforurl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
	private String access_token = null;
	private String openid = null;
	private String nickname = null;
	private String sex = null;
	private String province = null;
	private String city = null;
	private String country = null;
	private String headimgurl = null;

	public String getNickname() {
		return nickname;
	}

	public String getSex() {
		return sex;
	}

	public String getProvince() {
		return province;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void init(String access_token, String openid) {// 初始化函数
		this.access_token = access_token;
		this.openid = openid;
		if (access_token != null && openid != null) {
			// 获取用户信息Json，并解析
			String json = HttpRequestTool.sendGet(
					inforurl.replace("ACCESS_TOKEN", access_token).replace(
							"OPENID", openid), "utf-8");
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(json);
			// 逐一获取
			nickname = object.get("nickname").getAsString();
			sex = object.get("sex").getAsString();
			province = object.get("province").getAsString();
			city = object.get("city").getAsString();
			country = object.get("country").getAsString();
			headimgurl = object.get("headimgurl").getAsString();
			DBsynchronism();// 开始同步
		}
	}

	public void init(String code) {// 重载，code方式获取
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx2e7d71280147d5a5&secret=bb54272fc561876a7c4f82d347f9c584&code=CODE&grant_type=authorization_code";
		String json = HttpRequestTool.sendGet(url.replace("CODE", code),
				"utf-8");
		JsonParser parser = new JsonParser();
		// 调用parse方法获取到JsonObject
		JsonObject object = (JsonObject) parser.parse(json);
		// 调用一系列get方法获取object的直接子对象
		String access_token = object.get("access_token").getAsString();
		String openid = object.get("refresh_token").getAsString();
		init(access_token, openid);
	}

	public void DBsynchronism() {// DB同步
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JDBC4wechat.getConnection();
			// 如果不存在含该openid的记录，则添加记录
			stmt = conn.createStatement();
			stmt.executeUpdate("insert ignore into specificperson(openid,access_token)VALUES(opid,acstk);"
					.replace("opid", openid).replace("acstk", access_token));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBC4wechat.release(conn, stmt, null);
		}
	}

	public void SUUpdate(String field, String value) {// 数据更新
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = JDBC4wechat.getConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate("update specificperson set field=value where openid=opid"
					.replace("opid", openid).replace("field", field)
					.replace("value", value));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBC4wechat.release(conn, stmt, null);
		}

	}
}
