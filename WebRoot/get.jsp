<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" %>
<%@ page import="cn.cenbylin.tool.*" %>
<%@ page import="com.google.gson.*" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%
	//获得授权后回调code
	String code=request.getParameter("code");
	out.print("code:"+code);
	//由code获取openid和access_token
	String str="https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx2e7d71280147d5a5&secret=bb54272fc561876a7c4f82d347f9c584&code=CODE&grant_type=authorization_code";
	String json=HttpRequestTool.sendGet(str.replace("CODE", code), "utf-8");
	out.print("<br>json:"+json);
	JsonParser parser = new JsonParser();  
     	//调用parse方法获取到JsonObject  
    JsonObject object = (JsonObject) parser.parse(json);  
    	//调用一系列get方法获取object的直接子对象  
    String access_token = object.get("access_token").getAsString();
    String openid = object.get("refresh_token").getAsString();
    out.print("<br><br>access_token:"+access_token);  
    out.print("<br><br>expires_in:"+object.get("expires_in").getAsString());  
    out.print("<br><br>refresh_token:"+openid); 
    out.print("<br><br>openid:"+object.get("openid").getAsString()); 
    out.print("<br><br>scope:"+object.get("scope").getAsString()); 
    
    //由openid和access_token拉取用户信息
    str = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    json = HttpRequestTool.sendGet(str.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid), "utf-8");
    object = (JsonObject) parser.parse(json);
    String nickname = object.get("nickname").getAsString();
    String sex = object.get("sex").getAsString();
	String province = object.get("province").getAsString();
	String city = object.get("city").getAsString();
	String country = object.get("country").getAsString();
	String headimgurl = object.get("headimgurl").getAsString();
	out.print("<br><h3 color=\"red\">重点来了</h3>");
	out.print("<br><br>nickname:"+nickname);
	out.print("<br><br>sex:"+sex);
	out.print("<br><br>province:"+province);
	out.print("<br><br>city:"+city);
	out.print("<br><br>country:"+country);
	out.print("<br><br>头像:<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src=\""+headimgurl+"\" height=\"128\" width=\"128\"/>");
%>