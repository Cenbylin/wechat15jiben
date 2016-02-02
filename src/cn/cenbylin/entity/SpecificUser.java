package cn.cenbylin.entity;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.cenbylin.jdbc.JDBC4wechat;
import cn.cenbylin.tool.HttpRequestTool;
import com.google.gson.*;
/**
�����Ƕ�΢�ŷ����û��ķ�װ��һ����ʼ����������Ϣ�����ȡ�����������ݿ⡣
@author Cenbylin
@version 1.0
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
	public void init(String access_token,String openid){//��ʼ������
		this.access_token = access_token;
		this.openid = openid;
		if(access_token!=null && openid!=null){
			//��ȡ�û���ϢJson��������
			String json = HttpRequestTool.sendGet(inforurl.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid), "utf-8");
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(json);
			//��һ��ȡ
			nickname = object.get("nickname").getAsString();
			sex = object.get("sex").getAsString();
			province = object.get("province").getAsString();
			city = object.get("city").getAsString();
			country = object.get("country").getAsString();
			headimgurl = object.get("headimgurl").getAsString();
			DBsynchronism();//��ʼͬ��
		}
	}
	public void init(String code){//���أ�code��ʽ��ȡ
		String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx2e7d71280147d5a5&secret=bb54272fc561876a7c4f82d347f9c584&code=CODE&grant_type=authorization_code";
		String json=HttpRequestTool.sendGet(url.replace("CODE", code), "utf-8");
		JsonParser parser = new JsonParser();  
	     	//����parse������ȡ��JsonObject  
	    JsonObject object = (JsonObject) parser.parse(json);  
	    	//����һϵ��get������ȡobject��ֱ���Ӷ���  
	    String access_token = object.get("access_token").getAsString();
	    String openid = object.get("refresh_token").getAsString();
	    init(access_token,openid);
	}
	public void DBsynchronism(){//DBͬ��
		try{
			JDBC4wechat db = new JDBC4wechat();
			//��������ں���openid�ļ�¼������Ӽ�¼
			db.doSqlUpdate("insert ignore into specificperson(openid,access_token)VALUES(opid,acstk);".replace("opid", openid).replace("acstk", access_token));
		}catch(SQLException e){}
	}
	public void SUUpdate(String field,String value){//���ݸ���
		JDBC4wechat db;
		try {
			db = new JDBC4wechat();
			db.doSqlUpdate("update specificperson set field=value where openid=opid".replace("opid", openid).replace("field", field).replace("value", value));
		} catch (SQLException e) {}
		
	}
}
