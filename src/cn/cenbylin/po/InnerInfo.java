package cn.cenbylin.po;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class InnerInfo {
	private static String accessToken = null;
	private static String appID = "wx2e7d71280147d5a5";
	private static String appSecret = "bb54272fc561876a7c4f82d347f9c584";
	static
	{
		String json = cn.cenbylin.tool.HttpRequestTool.sendGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appID+"&secret="+appSecret, "utf-8");
		JsonParser parser = new JsonParser();  
     	//调用parse方法获取到JsonObject  
		JsonObject object = (JsonObject) parser.parse(json);  
    	//调用一系列get方法获取object的直接子对象  
		accessToken = object.get("access_token").getAsString();
	}
	public static String getAccessToken() {
		return accessToken;
	}
	public static String getAppID() {
		return appID;
	}
	public static String getAppSecret() {
		return appSecret;
	}
	
}
