package manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bean.User;
import server.ServerSocketThread;

public class UserManager {
	public static ArrayList<User> userList = new ArrayList<User>();
	// public static Map<K, V>
	// 管理用户线程
	public static Map<String, ServerSocketThread> userThreads = new HashMap<String, ServerSocketThread>();
	static {

	}
}
