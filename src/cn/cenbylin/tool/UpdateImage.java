package cn.cenbylin.tool;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UpdateImage {
	/**
	 * 上传图片文件
	 * 返回media_id
	 */
	public static void main(String[] args){
		//System.out.println(cn.cenbylin.tool.HttpRequestTool.sendGet("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx2e7d71280147d5a5&secret=bb54272fc561876a7c4f82d347f9c584", "utf-8"));
		String at = "K0ocSCDBXj0pBkmC-LJWuIJPrRrf8or3hmIN0TImM9EzMxzWpImericFr6zh8hXEyAgy-kY93x7anWLwOEzKkcAh2wvtkoiGD-C9MCgrFBpeWE9beVdQ6a45EG0otkbqWUBdAAACAH";
		String uri = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png";
		uploadImage(at,uri);
	}
	public static String uploadImage(String accessToken, String mediaUrl) {
		String uploadMediaUrl = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token="+accessToken+"&type=image";
		String boundary = "----------" + System.currentTimeMillis();// 设置边界  
		try {
			URL uploadUrl = new URL(uploadMediaUrl);
			HttpURLConnection uploadConn = (HttpURLConnection) uploadUrl.openConnection();
			uploadConn.setDoOutput(true);
			uploadConn.setDoInput(true);
			uploadConn.setRequestMethod("POST");
			// 设置请求头Content-Type
			uploadConn.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
			// 获取媒体文件上传的输出流（往微信服务器写数据）
			OutputStream outputStream = uploadConn.getOutputStream();

			URL mediaUrl1 = new URL(mediaUrl);
			HttpURLConnection meidaConn = (HttpURLConnection) mediaUrl1.openConnection();
			meidaConn.setDoOutput(true);
			meidaConn.setRequestMethod("GET");

			// 从请求头中获取内容类型
			String contentType = meidaConn.getHeaderField("Content-Type");
			// 根据内容类型判断文件扩展名
			String fileExt = ".jpg";
			// 请求体开始
			outputStream.write(("--" + boundary + "\r\n").getBytes());
			outputStream.write(String.format("Content-Disposition: form-data; name=\"media\"; filename=\"file1%s\"\r\n",fileExt).getBytes());
			outputStream.write(String.format("Content-Type: %s\r\n\r\n",contentType).getBytes());
			
			// 获取媒体文件的输入流（读取文件）
			BufferedInputStream bis = new BufferedInputStream(meidaConn.getInputStream());
			byte[] buf = new byte[8096];
			int size = 0;
			while ((size = bis.read(buf)) != -1) {
				outputStream.write(buf, 0, size);
			}
			// 请求体结束
			outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());
			outputStream.close();
			bis.close();
			meidaConn.disconnect();

			// 获取媒体文件上传的输入流（从微信服务器读数据）
			InputStream inputStream = uploadConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuffer buffer = new StringBuffer();
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();
			// 释放资源
			inputStream.close();
			inputStream = null;
			uploadConn.disconnect();
			// 使用JSON解析返回结果
			System.out.println();
			JsonParser parser = new JsonParser();   
	     	//调用parse方法获取到JsonObject  
			JsonObject object = (JsonObject) parser.parse(buffer.toString());  
	    	//调用一系列get方法获取object的直接子对象  
			return object.get("media_id").getAsString();
		} catch (Exception e) {
			String error = String.format("上传媒体文件失败：%s", e);
			System.out.println(error);
		}
		return null;
	}
}
