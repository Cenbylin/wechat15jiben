<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import="com.qiniu.util.Auth" %>
<%@ page import="com.qiniu.storage.BucketManager" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<%
	Auth me = Auth.create("2hCizo1I0UNtRUMkr7Lztw18PO3cT48IjayfmDo9","a7qMw76FvqcgG8zEEHplzNxbr6uS9SnX6i4d1Ycq");
	BucketManager bucketManager = new BucketManager(me);
	String url = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png";
	bucketManager.fetch(url, "image");
%>
