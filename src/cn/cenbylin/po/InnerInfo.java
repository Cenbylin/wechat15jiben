package cn.cenbylin.po;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

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
	private static String appID = null;
	private static String appSecret = null;
	// 七牛
	private static String accessKey = null;
	private static String secretKey = null;
	@SuppressWarnings("unused")
	private static String bucket = null;
	private static int Iterator;//列举迭代器迭代大小
	static {
		loadConfig();//加载
		//定时任务线程
		final long timeInterval = 100000;//时间(毫秒)
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
	@SuppressWarnings({ "unchecked", "unused" })
	/**
	 * 从配置文件中装载核心信息
	 */
	public static void loadConfig(){
		Element root = null;
		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		try {
				InputStream xmlStream = new FileInputStream(InnerInfo.class.getResource("").toString().replace("classes/cn/cenbylin/po/", "").replace("file:/", "")+"wechat-config.xml");
				Document doc = reader.read(xmlStream);	
				xmlStream.close();
				root = doc.getRootElement();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (DocumentException e2){
			
		}

		List<Element> list = root.elements();
		for (Element e : list) {
			if(e.getName().equals("wechat")){
				List<Element> list1 = e.elements();
				for(Element e1 : list1){
					if(e1.getName().equals("appID")){
						appID=e1.getStringValue();
					}else if(e1.getName().equals("appSecret")){
						appSecret=e1.getStringValue();
					}
				}
			}else if(e.getName().equals("qiniu")){
				List<Element> list1 = e.elements();
				for(Element e1 : list1){
					if(e1.getName().equals("accessKey")){
						accessKey=e1.getStringValue();
					}else if(e1.getName().equals("secretKey")){
						secretKey=e1.getStringValue();
					}else if(e1.getName().equals("bucket")){
						bucket=e1.getStringValue();
					}else if(e1.getName().equals("Iterator")){
						Iterator=Integer.valueOf(e1.getStringValue());
					}
				}
			}
		}
	}
}
