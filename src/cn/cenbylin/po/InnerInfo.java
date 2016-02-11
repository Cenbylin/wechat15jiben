package cn.cenbylin.po;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 封装核心信息（比如各种key）的类,特别是对于需要定时刷新的数据
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
	private static int Iterator = 100;//列举迭代器迭代大小
	static {
		//定时任务线程
		final long timeInterval = 1000;//时间
		Runnable runnable = new Runnable() {
			public void run() {
				while (true) {
					String json = cn.cenbylin.tool.HttpRequestTool.sendGet(
							"https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
									+ appID + "&secret=" + appSecret, "utf-8");
					JsonParser parser = new JsonParser();
					// 调用parse方法获取到JsonObject
					JsonObject object = (JsonObject) parser.parse(json);
					// 调用一系列get方法获取object的直接子对象
					accessToken = object.get("access_token").getAsString();
					try {
						Thread.sleep(timeInterval);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
		
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

	public static int getIterator() {
		return Iterator;
	}

}
