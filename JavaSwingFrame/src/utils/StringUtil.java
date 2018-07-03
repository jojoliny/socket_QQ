package utils;

public class StringUtil {
	public static boolean isEmpty(String path) {
		if (path == null || "".equals(path))
			return true;

		return false;
	}
}
