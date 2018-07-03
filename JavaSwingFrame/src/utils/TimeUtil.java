package utils;

import java.text.SimpleDateFormat;

public class TimeUtil {

	private static SimpleDateFormat format;

	public static String format_Time(long time) {
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(time);
	}
}
