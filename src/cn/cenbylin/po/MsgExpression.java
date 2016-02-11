package cn.cenbylin.po;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
			FileInputStream in = new FileInputStream("D://log/aaaa.properties");
			Reader reader = new InputStreamReader(in,"UTF-8");
			prop.load(reader);//地址
			System.out.println(prop);
		} catch (FileNotFoundException e1) {} catch (IOException e1) {}
	}
}
