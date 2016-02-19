package cn.cenbylin.domessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import cn.cenbylin.po.MessageBean;
import cn.cenbylin.tool.XMLUtil;


public class DoMessage extends HttpServlet {
	private static final long serialVersionUID = 5952689219411916553L;
	public DoMessage() {
		super();
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 初始项
		response.setContentType("text/xml;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		// 初始化消息模型，装载信息
		MessageBean MSB = new MessageBean();
		Map<String, String> map = null;
		try {
			map = XMLUtil.xmlToMap(request.getInputStream());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		MSB.loadMap(map);

		Logger logger = Logger.getLogger(DoMessage.class);
		// 判断消息类型，分发
		if ("text".equals(MSB.getMsgType())) {
			// 分发给文本消息处理器
			logger.info("收到来自" + MSB.getFromUserName() + "的文本消息："
					+ MSB.getContent());
			out.print(cn.cenbylin.domessage.DoText.doText(MSB));
			out.close();
		} else if ("image".equals(MSB.getMsgType())) {
			logger.info("收到来自" + MSB.getFromUserName() + "的图片");
			out.print(cn.cenbylin.domessage.DoImage.doImage(MSB));
			out.close();
		} else {// 未能识别
			logger.info("收到来自" + MSB.getFromUserName() + "的未知消息类型");
			out.print("<xml><ToUserName>"
					+ MSB.getFromUserName()
					+ "</ToUserName><FromUserName>"
					+ MSB.getToUserName()
					+ "</FromUserName><CreateTime>"
					+ Long.toString(new Date().getTime())
					+ "</CreateTime><MsgType>text</MsgType><Content>未能识别的消息~</Content><MsgId>"
					+ MSB.getMsgId() + "</MsgId></xml>");
			out.close();
		}

	}

}
