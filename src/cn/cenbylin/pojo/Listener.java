package cn.cenbylin.pojo;

import javax.servlet.ServletContextEvent;  
import javax.servlet.ServletContextListener;  

import cn.cenbylin.tool.HttpRequestTool;
/**
 * 监听器 启动初始化和销毁回收动作
 * @author Cenby7
 *
 */
public class Listener implements ServletContextListener {  
    public void contextDestroyed(ServletContextEvent event) {
  
    }  
    
    /**
     * 应用初始化
     */
    public void contextInitialized(ServletContextEvent event) {  
    	try {
			Class.forName("cn.cenbylin.po.InnerInfo");//内置信息对象的加载
			while(InnerInfo.getAccessToken()==null){
				try{
					   Thread.sleep(500);//等待1秒
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			String sentUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+InnerInfo.getAccessToken();
			String sentJson = "{\"touser\":\"OPENID\",\"msgtype\":\"text\",\"text\":{\"content\":\"CONTENT\"}}";
			sentJson = sentJson.replace("OPENID", "oyk7nwR8nFIGv_rcrVVp441DUFJk").replace("CONTENT", "BOSS 服务器启动了");
			System.out.println(HttpRequestTool.sendPost(sentUrl, sentJson, "utf-8"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }  
  
}  
