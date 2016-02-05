package cn.cenbylin.domessage;
import java.util.Date;

import cn.cenbylin.po.MessageBean;
import cn.cenbylin.tool.XMLUtil;
public class DoText {
	public static String dotext(MessageBean msb){//1、高拓展性 2、临时消息bean对象可读取返回两用，需要提前采取提取措施
		//设置接收方和发送方
		String temp = msb.getToUserName();
		msb.setToUserName(msb.getFromUserName());
		msb.setFromUserName(temp);
		//消息类型
		msb.setMsgType("text");
		//消息内容
		msb.setContent("你发送的是："+msb.getContent());
		//消息创建时间
		msb.setCreateTime(Long.toString(new Date().getTime()));
		
		return XMLUtil.BeanToXml(msb);
	}
}
