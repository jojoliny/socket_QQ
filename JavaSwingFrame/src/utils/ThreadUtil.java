package utils;

public class ThreadUtil {

	public static void sleep(long millis) {
		// 统一异常处理
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
