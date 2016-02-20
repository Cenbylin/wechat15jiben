package cn.cenbylin.tool;

import cn.cenbylin.po.InnerInfo;

public class SentImgToWechat extends Thread {
	private String url = null;
	private String openid = null;
	public SentImgToWechat(String url, String openid){
		this.url = url;
		this.openid = openid;
	}
	public void run() {
		String accessToken = InnerInfo.getAccessToken();
		String mediaid = UpdateImage.uploadImage(accessToken, url);
		//https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN
		String posturl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN".replace("ACCESS_TOKEN", accessToken);
		String param = "{\"touser\":\"OPENID\",\"msgtype\":\"image\",\"image\":{\"media_id\":\"MEDIA_ID\"}}".replace("OPENID", openid).replace("MEDIA_ID", mediaid);
		HttpRequestTool.sendPost(posturl, param, "utf-8");
	}
	
}
