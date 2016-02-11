package cn.cenbylin.po;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 封装核心信息（比如各种key）的类
 * 
 * @author Cenby7
 * 
 */
public class InnerInfo {
	private static String accessToken = null;
	private static String appID = "wx2e7d71280147d5a5";
	private static String appSecret = "bb54272fc561876a7c4f82d347f9c584";
	// 七牛
	private static String accessKey = "2hCizo1I0UNtRUMkr7Lztw18PO3cT48IjayfmDo9";
	private static String secretKey = "a7qMw76FvqcgG8zEEHplzNxbr6uS9SnX6i4d1Ycq";
	static {
		String json = cn.cenbylin.tool.HttpRequestTool.sendGet(
				"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
						+ appID + "&secret=" + appSecret, "utf-8");
		JsonParser parser = new JsonParser();
		// 调用parse方法获取到JsonObject
		JsonObject object = (JsonObject) parser.parse(json);
		// 调用一系列get方法获取object的直接子对象
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

	public static String getAccessKey() {
		return accessKey;
	}

	public static String getSecretKey() {
		return secretKey;
	}

}
