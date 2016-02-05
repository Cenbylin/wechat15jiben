package cn.cenbylin.domessage;
import java.sql.SQLException;
import java.util.Date;

import cn.cenbylin.jdbc.JDBC4wechat;
import cn.cenbylin.po.MessageBean;
import cn.cenbylin.tool.XMLUtil;
public class DoText {
	public static String doText(MessageBean msb){//1、高拓展性 2、临时消息bean对象可读取返回两用，需要提前采取提取措施
		//设置接收方和发送方
		String temp = msb.getToUserName();
		msb.setToUserName(msb.getFromUserName());
		msb.setFromUserName(temp);
		//消息类型
		msb.setMsgType("text");
		//消息内容
		//msb.setContent("你发送的是："+msb.getContent());
		if(msb.getContent().contains("我有林彪鑫的照片")){
			
			msb.setContent("那就发来呗~~");
			try {
				JDBC4wechat jdbc = new JDBC4wechat();
				jdbc.doSqlUpdate("update person set picstatement=1,towho='林彪鑫' where name='林彪鑫';");

				jdbc.close();
			} catch (SQLException e) {e.printStackTrace();}
		}else if(msb.getContent().contains("不发了")){
			try {
				JDBC4wechat jdbc = new JDBC4wechat();
				jdbc.doSqlUpdate("update person set picstatement=0 where name='林彪鑫';");

				jdbc.close();
			} catch (SQLException e) {e.printStackTrace();}
			msb.setContent("好吧，下次记得来爆照哦~");
		}
		
		
		//消息创建时间
		msb.setCreateTime(Long.toString(new Date().getTime()));
		
		return XMLUtil.BeanToXml(msb);
	}
}
