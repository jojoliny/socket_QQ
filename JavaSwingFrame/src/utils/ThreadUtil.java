package utils;

public class ThreadUtil {

	public static void sleep(long millis) {
		// ͳһ�쳣����
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
