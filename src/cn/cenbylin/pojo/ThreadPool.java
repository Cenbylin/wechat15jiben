package cn.cenbylin.pojo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 中控线程池（现主要处理图片、聊天文本异步回复）
 * @author Cenby7
 *
 */
public class ThreadPool {
	private static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
	public static void setCachedThread(Runnable command){
		cachedThreadPool.execute(command);
	}
	public static void setFixedThread(Runnable command){
		fixedThreadPool.execute(command);
	}
}
