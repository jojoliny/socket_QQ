package manager;

import java.util.HashMap;
import java.util.Map;

import ui.ChatFrame;
import ui.FriendListFrame;
import ui.SearchFriendsFrame;

public class UImanager {
	// list
	public static FriendListFrame fList;

	// 搜索列表
	public static SearchFriendsFrame searchFrame;

	// 管理聊天窗口
	public static Map<String, ChatFrame> chatFrames = new HashMap<String, ChatFrame>();

	// 生成用户房间号
	public static String genRoomKey(String destId) {
		String roomKey = UserManager.currentUser.getId() + "##" + destId;
		return roomKey;
	}
}
