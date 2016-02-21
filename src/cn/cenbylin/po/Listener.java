package cn.cenbylin.po;

import javax.servlet.ServletContextEvent;  
import javax.servlet.ServletContextListener;  
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
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }  
  
}  
