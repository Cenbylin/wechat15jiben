package cn.cenbylin.domessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// String[] str = "我有啊啊啊的照片".split("\\我有(.*?)\\的照片");
		// System.out.println(str.length);
		Pattern pattern = Pattern.compile("\\我有(.*?)\\的照片");
		Matcher matcher = pattern.matcher("我有啊啊啊的照片");
		matcher.find();
		System.out.println(matcher.group(1));

	}

}
