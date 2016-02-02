<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8"%>
<%@ page import="cn.cenbylin.entity.SpecificUser" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<% 
	String openid = null;
	String access_token = null;
	String code = null;
	SpecificUser SU = new SpecificUser();//来访用户信息对象
	//提取cookies
	Cookie[] cookies = request.getCookies();;
	for(Cookie c:cookies)
	{
		if(c.getName().equals("openid")){
		openid = c.getValue();
		}
		if(c.getName().equals("access_token")){
		access_token = c.getValue();
		}
	}
	if(openid!=null && access_token!=null){
		SU.init(access_token, openid);//特定用户对象产生
	}else if((code=request.getParameter("code"))!=null){
		SU.init(code);
		Cookie cookie_at = new Cookie("access_token",access_token);
		Cookie cookie_oi = new Cookie("openid",openid);
    	response.addCookie(cookie_at);
    	response.addCookie(cookie_oi);
	}else{
		response.sendRedirect("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx2e7d71280147d5a5&redirect_uri=http%3A%2F%2Fwww.cenbylin.cn%2Fwechat%2Fget.jsp&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");
		//重定向授权页
	}

%>

