package cn.cenbylin.domessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import cn.cenbylin.po.MessageBean;
import cn.cenbylin.tool.XMLUtil;

public class DoMessage extends HttpServlet {

	public DoMessage() {
		super();
	}	
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request,response);
	}


	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		out.close();
		MessageBean MSB = new MessageBean();
		Map<String,String> map = null;
		try {
			map = XMLUtil.xmlToMap(request.getInputStream());
		} catch (DocumentException e) {e.printStackTrace();}
		MSB.loadMap(map);
		//判断消息类型，分发
		if(){
			
		}else if(){
			
		}else if(){
			
		}else{
			
		}
		
		
		
		
		
		}
	}

	public void init() throws ServletException {
		// Put your code here
	}

}
