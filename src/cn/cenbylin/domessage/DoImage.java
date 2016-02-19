package cn.cenbylin.domessage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Logger;

import cn.cenbylin.dao.JDBC4wechat;
import cn.cenbylin.po.InnerInfo;
import cn.cenbylin.po.MessageBean;
import cn.cenbylin.tool.XMLUtil;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.util.Auth;

public class DoImage {
	public static String doImage(MessageBean msb) throws QiniuException {// 1、高拓展性
																			// 2、临时消息bean对象可读取返回两用，需要提前采取提取措施
		String openid = msb.getFromUserName();// 读取Openid
		// 状态读取
		JDBC4wechat j1;
		ResultSet rs;
		int statement = -1;// 如果处在给别人传照片状态，为1，否则为0
		String towho = null;
		try {
			j1 = new JDBC4wechat();
			rs = j1.doSqlQuery("select * FROM person WHERE `wc_openid`= \""
					+ openid + "\";");
			rs.next();
			statement = rs.getInt("picstatement");
			towho = rs.getString("towho");
			j1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Logger logger = Logger.getLogger(DoMessage.class);
		if (statement == 1) {
			logger.info(msb.getFromUserName() + "在给 " + towho + " 传照片");
			// 抓取照片
			Auth me = Auth.create(InnerInfo.getAccessKey(),
					InnerInfo.getSecretKey());
			BucketManager bucketManager = new BucketManager(me);
			bucketManager.fetch(msb.getPicUrl(), "image",
					towho + "_" + Long.toString(new Date().getTime()));

			// 消息类型
			msb.setMsgType("text");
			// 消息内容
			msb.setContent("再来几张" + towho + "的照片呗~\n\n回复‘不发了’结束");
			// 消息创建时间
			msb.setCreateTime(Long.toString(new Date().getTime()));

		}
		/*
		 * //消息类型 msb.setMsgType("image"); //消息内容
		 * msb.setContent("你发送的是："+msb.getContent()); //消息创建时间
		 * msb.setCreateTime(Long.toString(new Date().getTime()));
		 */
		// 设置接收方和发送方
		String temp = msb.getToUserName();
		msb.setToUserName(msb.getFromUserName());
		msb.setFromUserName(temp);
		return XMLUtil.BeanToXml(msb);
	}
}
