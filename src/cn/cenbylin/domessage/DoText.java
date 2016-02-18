package cn.cenbylin.domessage;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import cn.cenbylin.jdbc.JDBC4wechat;
import cn.cenbylin.po.InnerInfo;
import cn.cenbylin.po.MessageBean;
import cn.cenbylin.po.MsgExpression;
import cn.cenbylin.tool.UpdateImage;
import cn.cenbylin.tool.XMLUtil;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;

public class DoText {
	public static String doText(MessageBean msb) {// 1、高拓展性
												  // 2、临时消息bean对象可读取返回两用，需要提前采取提取措施
		Logger logger = Logger.getLogger(DoMessage.class);
		String openid = msb.getFromUserName();// 读取Openid
		// 消息类型
		msb.setMsgType("text");
		// 消息内容
		// msb.setContent("你发送的是："+msb.getContent());
		String msg = msb.getContent();

		// 判断开始

		/**
		 * 要发照片了
		 */
		if (msg.matches("我有(.*?)的(照片|相片)")) {
			// 正则
			Pattern pattern = Pattern.compile("我有(.*?)的(照片|相片)");
			Matcher matcher = pattern.matcher(msg);
			matcher.find();
			String towho = matcher.group(1);
			logger.info(msb.getFromUserName() + "正在尝试给 " + towho + " 传照片");
			try {
				JDBC4wechat jdbc = new JDBC4wechat();
				ResultSet rs = jdbc
						.doSqlQuery("select * FROM person WHERE `name`= \"" + towho + "\" OR `alias`=\"" + towho + "\";");
				if (rs.next()) {// 找到这个人了
					msb.setContent("那就发来呗~~");
					towho = rs.getString("name");
					jdbc.doSqlUpdate("update person set picstatement=1,towho='" + towho + "' where wc_openid='" + openid + "';");
				} else {
					msb.setContent("你确定有" + towho + "这个人吗~~");
				}
				jdbc.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			/**
			 * 不发了
			 */
		} else if (msb.getContent().contains("不发了")) {
			try {
				JDBC4wechat jdbc = new JDBC4wechat();
				jdbc.doSqlUpdate("update person set picstatement=0 where wc_openid='" + openid + "';");
				jdbc.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			msb.setContent("好吧，下次记得来爆照哦~");
			/**
			 * 求照片
			 */
		} else if (msg.matches("我要(.*?)的(照片|相片)")) {
			// 正则
			Pattern pattern = Pattern.compile("我要(.*?)的(照片|相片)");
			Matcher matcher = pattern.matcher(msg);
			matcher.find();
			String towho = matcher.group(1);
			logger.info(msb.getFromUserName() + "正在尝试获取 " + towho + " 的照片");
			try {
				JDBC4wechat jdbc = new JDBC4wechat();
				ResultSet rs = jdbc.doSqlQuery("select * FROM person WHERE `name`= \"" + towho + "\" OR `alias`=\"" + towho + "\";");
				if (rs.next()) {// 找到这个人了
					towho = rs.getString("name");
					// 从库里得到一个url
					Auth auth = Auth.create(InnerInfo.getAccessKey(),
							InnerInfo.getSecretKey());
					BucketManager bucketManager = new BucketManager(auth);
					BucketManager.FileListIterator it = bucketManager.createFileListIterator("image", towho, InnerInfo.getIterator(), null);
					if (it.hasNext()) {// 有这个人的图
						FileInfo[] items = it.next();
						if (items.length > 0) {
							String url;
							Random rand = new Random();//随机种子
							try {
								url = "http://7xqqi6.com1.z0.glb.clouddn.com/" + java.net.URLEncoder.encode(items[rand.nextInt(items.length)].key, "UTF-8");
								String mediaid = UpdateImage.uploadImage(InnerInfo.getAccessToken(), url);
								// System.out.println("获取到啦"+mediaid);
								msb.setMsgType("image");
								msb.setMediaId(mediaid);
								msb.setContent(null);
								msb.setMsgId(null);
								logger.info("已发送给" + msb.getFromUserName() + "一张 " + towho + " 的照片");
							} catch (UnsupportedEncodingException e) {e.printStackTrace();}
						}
					} else {
						msb.setContent("没有" + towho + "的图耶，你有没有呐~");
					}
				} else {
					msb.setContent("你确定有" + towho + "这个人吗~~");
				}
				jdbc.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else if((msg+"666").matches("我是(.*?)666")){
			Pattern pattern = Pattern.compile("我是(.*?)666");
			Matcher matcher = pattern.matcher((msg+"666"));
			matcher.find();
			String iam = matcher.group(1);
			JDBC4wechat jdbc;
			try {
				jdbc = new JDBC4wechat();
				ResultSet rs = jdbc.doSqlQuery("select * FROM person WHERE `name`= \"" + iam + "\" OR `alias`=\"" + iam + "\";");
				if (rs.next()) {// 找到这个人了
					iam = rs.getString("name");
					if(rs.getString("wc_openid")==null||rs.getString("wc_openid").equals("")){
						jdbc.doSqlUpdate("update person set wc_openid='"+openid+"' where name='" + iam + "';");
						jdbc.close();
						msb.setContent("哟西 绑定成功");
					}else{
						
						msb.setContent(iam+"已经绑定过微信号了，如果不是本人绑定的，\n联系成哥/:wipe"+"<a href=\"http://www.baidu.com\">百度</a>");
					}
					
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		} else {//普通聊天接口
			Properties prop = MsgExpression.getProp();
			Set<String> list = prop.stringPropertyNames();//返回正则列表
			for(String e:list){
				if(msg.matches(e)){
					msb.setContent(prop.getProperty(e));
				}
			}
		}

		// 消息创建时间
		msb.setCreateTime(Long.toString(new Date().getTime()));
		// 设置接收方和发送方
		String temp = msb.getToUserName();
		msb.setToUserName(msb.getFromUserName());
		msb.setFromUserName(temp);

		// 对生成的xml作最后一步的检查
		if (msb.getMsgType().equals("image")) {// image的消息类型，xml多了个Image节点
			return XMLUtil.BeanToXml(msb).replace("<MediaId>", "<Image><MediaId>").replace("</MediaId>", "</MediaId></Image>");
		}
		return XMLUtil.BeanToXml(msb);
	}
}
