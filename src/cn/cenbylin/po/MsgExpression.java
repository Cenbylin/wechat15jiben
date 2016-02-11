package cn.cenbylin.po;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
/**
 * 正则表达式缓存单例
 * @author Cenby7
 *
 */
public class MsgExpression {
	public static Properties prop = new Properties();
	static
	{
		reload();
	}
	public static Properties getProp(){
		return prop;
	}
	public static void reload(){
		try {
			prop.load(new FileInputStream(""));//地址
		} catch (FileNotFoundException e1) {} catch (IOException e1) {}
	}
}
