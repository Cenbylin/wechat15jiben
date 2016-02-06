package cn.cenbylin.domessage;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

import com.qiniu.storage.BucketManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;

import cn.cenbylin.jdbc.JDBC4wechat;
import cn.cenbylin.po.InnerInfo;
import cn.cenbylin.po.MessageBean;
import cn.cenbylin.tool.UpdateImage;
import cn.cenbylin.tool.XMLUtil;
public class DoText {
	public static String doText(MessageBean msb){//1、高拓展性 2、临时消息bean对象可读取返回两用，需要提前采取提取措施
		Logger logger = Logger.getLogger(DoMessage.class);
		String openid = msb.getFromUserName();//读取Openid
		//消息类型
		msb.setMsgType("text");
		//消息内容
		//msb.setContent("你发送的是："+msb.getContent());
		String msg = msb.getContent();
		
		//判断开始
		
		/**
		 * 要发照片了
		 */
		if(msg.matches("我有(.*?)的(照片|相片)")){
			//正则
			Pattern pattern = Pattern.compile("我有(.*?)的(照片|相片)");
			Matcher matcher = pattern.matcher(msg);
			matcher.find();
			String towho = matcher.group(1);
			logger.info(msb.getFromUserName()+"正在尝试给 "+towho+" 传照片");
			try {
				JDBC4wechat jdbc = new JDBC4wechat();
				ResultSet rs = jdbc.doSqlQuery("select * FROM person WHERE `name`= \""+towho+"\" OR `alias`=\""+towho+"\";");
				if(rs.next()){//找到这个人了
					msb.setContent("那就发来呗~~");
					towho = rs.getString("name");
					jdbc.doSqlUpdate("update person set picstatement=1,towho='"+towho+"' where wc_openid='"+openid+"';");
				}else{
					msb.setContent("你确定有"+towho+"这个人吗~~");
				}
				jdbc.close();
			} catch (SQLException e) {e.printStackTrace();}
		/**
		 * 不发了
		 */
		}else if(msb.getContent().contains("不发了")){
			try {
				JDBC4wechat jdbc = new JDBC4wechat();
				jdbc.doSqlUpdate("update person set picstatement=0 where wc_openid='"+openid+"';");
				jdbc.close();
			} catch (SQLException e) {e.printStackTrace();}
			msb.setContent("好吧，下次记得来爆照哦~");
		/**
		 * 求照片
		 */
		}else if(msg.matches("我要(.*?)的(照片|相片)")){
			//正则
			Pattern pattern = Pattern.compile("我要(.*?)的(照片|相片)");
			Matcher matcher = pattern.matcher(msg);
			matcher.find();
			String towho = matcher.group(1);
			logger.info(msb.getFromUserName()+"正在尝试获取 "+towho+" 的照片");
			try {
				JDBC4wechat jdbc = new JDBC4wechat();
				ResultSet rs = jdbc.doSqlQuery("select * FROM person WHERE `name`= \""+towho+"\" OR `alias`=\""+towho+"\";");
				if(rs.next()){//找到这个人了
					towho = rs.getString("name");
					//从库里得到一个url
					Auth auth = Auth.create(InnerInfo.getAccessKey(), InnerInfo.getSecretKey());
					BucketManager bucketManager = new BucketManager(auth);
					BucketManager.FileListIterator it = bucketManager.createFileListIterator("image", towho, 1, null);
					if(it.hasNext()){//有这个人的图
						FileInfo[] items = it.next();
					    if (items.length > 0) {
					        String url;
							try {
								url = "http://7xqqi6.com1.z0.glb.clouddn.com/"+java.net.URLEncoder.encode(items[0].key,"UTF-8");
								String mediaid = UpdateImage.uploadImage(InnerInfo.getAccessToken(), url);
						        //System.out.println("获取到啦"+mediaid);
						        msb.setMsgType("image");
						        msb.setMediaId(mediaid);
						        msb.setContent(null);
						        msb.setMsgId(null);
						        logger.info("已发送给"+msb.getFromUserName()+"一张 "+towho+" 的照片");
							} catch (UnsupportedEncodingException e) {e.printStackTrace();}
					        
					    }
					}else{
						msb.setContent("没有"+towho+"的图耶，你有没有呐~");
					}
				}else{
					msb.setContent("你确定有"+towho+"这个人吗~~");
				}
				jdbc.close();
			} catch (SQLException e) {e.printStackTrace();}
		}else if(true){
			
		}


		
		//消息创建时间
		msb.setCreateTime(Long.toString(new Date().getTime()));
		//设置接收方和发送方
		String temp = msb.getToUserName();
		msb.setToUserName(msb.getFromUserName());
		msb.setFromUserName(temp);
		
		//对生成的xml作最后一步的检查
		if(msb.getMsgType().equals("image")){//image的消息类型，xml多了个Image节点
			return XMLUtil.BeanToXml(msb).replace("<MediaId>", "<Image><MediaId>").replace("</MediaId>", "</MediaId></Image>");
		}
		return XMLUtil.BeanToXml(msb);
	}
}
