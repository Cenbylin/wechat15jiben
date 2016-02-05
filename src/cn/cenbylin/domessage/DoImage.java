package cn.cenbylin.domessage;
import cn.cenbylin.jdbc.JDBC4wechat;
import cn.cenbylin.po.MessageBean;
import cn.cenbylin.tool.XMLUtil;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.util.Auth;
public class DoImage {
	public static String doImage(MessageBean msb) throws QiniuException{//1、高拓展性 2、临时消息bean对象可读取返回两用，需要提前采取提取措施
		String picurl = msb.getPicUrl();
		
		//设置接收方和发送方
		String temp = msb.getToUserName();
		msb.setToUserName(msb.getFromUserName());
		msb.setFromUserName(temp);
		
		String name = "林彪鑫";//后期改为Openid
		//状态读取
		JDBC4wechat j1;
		ResultSet rs;
		int statement = -1;//如果处在给别人传照片状态，为1，否则为0
		String towho = null;
		try {
			j1 = new JDBC4wechat();
			rs = j1.doSqlQuery("select * FROM person WHERE `name` = \""+name+"\";");
			rs.next();
			statement = rs.getInt("picstatement");
			towho = rs.getString("towho");
			j1.close();
		} catch (SQLException e) {e.printStackTrace();} 
			
		
		if(statement == 1){
			//抓取照片
			Auth me = Auth.create("2hCizo1I0UNtRUMkr7Lztw18PO3cT48IjayfmDo9","a7qMw76FvqcgG8zEEHplzNxbr6uS9SnX6i4d1Ycq");
			BucketManager bucketManager = new BucketManager(me);
			bucketManager.fetch(msb.getPicUrl(), "image",towho+"_"+Long.toString(new Date().getTime()));
			
			//消息类型
			msb.setMsgType("text");
			//消息内容
			msb.setContent("再来几张呗，\n回复‘不发了’结束");
			//消息创建时间
			msb.setCreateTime(Long.toString(new Date().getTime()));
			
		}
		/*
		//消息类型
		msb.setMsgType("image");
		//消息内容
		msb.setContent("你发送的是："+msb.getContent());
		//消息创建时间
		msb.setCreateTime(Long.toString(new Date().getTime()));
		*/
		return XMLUtil.BeanToXml(msb);
	}
}
