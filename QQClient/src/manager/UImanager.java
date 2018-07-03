package manager;

import java.util.HashMap;
import java.util.Map;

import ui.ChatFrame;
import ui.FriendListFrame;
import ui.SearchFriendsFrame;

public class UImanager {
	// list
	public static FriendListFrame fList;

	// �����б�
	public static SearchFriendsFrame searchFrame;

	// �������촰��
	public static Map<String, ChatFrame> chatFrames = new HashMap<String, ChatFrame>();

	// �����û������
	public static String genRoomKey(String destId) {
		String roomKey = UserManager.currentUser.getId() + "##" + destId;
		return roomKey;
	}
}
