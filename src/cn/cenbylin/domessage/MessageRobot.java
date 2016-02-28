package cn.cenbylin.domessage;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cn.cenbylin.pojo.InnerInfo;
import cn.cenbylin.tool.HttpRequestTool;

public class MessageRobot extends Thread {
	private String msg = null;
	private String openid = null;
	public MessageRobot(String msg,String openid){
		this.msg = msg;
		this.openid = openid;
	}
	public void run() {
		String url = "http://www.tuling123.com/openapi/api";
		Map<String,String> param = new HashMap<String,String>();
		param.put("key",InnerInfo.getTulingKey());
		param.put("info", msg);
		param.put("userid", openid);
		String json = HttpRequestTool.sendPost(url,param,"UTF-8");
		System.out.println(json);
		//解析json
		JsonParser parser = new JsonParser();
		JsonObject object = (JsonObject) parser.parse(json);
		String backText = object.get("text").getAsString();
		
		//回复
		String sentUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+InnerInfo.getAccessToken();
		String sentJson = "{\"touser\":\"OPENID\",\"msgtype\":\"text\",\"text\":{\"content\":\"CONTENT\"}}";
		sentJson = sentJson.replace("OPENID", openid).replace("CONTENT", backText);
		HttpRequestTool.sendPost(sentUrl, sentJson, "utf-8");
	}
	
}
