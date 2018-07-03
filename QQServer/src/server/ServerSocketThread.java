package server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import bean.CommonTransferBean;
import bean.DataBean;
import bean.Protocol;
import bean.User;
import manager.UserManager;
import ui.MainFame;

public class ServerSocketThread implements Runnable {
	Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	public ServerSocketThread(Socket socket) {
		this.socket = socket;

		try {
			OutputStream out = socket.getOutputStream();
			InputStream in = socket.getInputStream();
			DataInputStream dis = new DataInputStream(in);
			ois = new ObjectInputStream(in);
			oos = new ObjectOutputStream(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		new Thread(this).start();
	}

	/** 等待客户端发送消息 **/
	@Override
	public void run() {
		while (true) {
			CommonTransferBean com;
			try {
				com = (CommonTransferBean) ois.readObject();
				String command = com.getCmd();
				if (command.equals("login")) {
					login(com);
				} else if (command.equals(Protocol.P_F_getFriendsList)) {
					getFriendsList(com);
				} else if (command.equals(Protocol.P_Search_Friends)) {
					searchFriendsList(com.getData());
				}
				/** 申请添加好友 **/
				else if (command.equals(Protocol.P_Friends_Add)) {
					handleAddFriend(com);
					System.out.println("tianjia" + com);

				}
				/** 申请成功 **/
				else if (command.equals(Protocol.P_Friends_Add_OK)) {
					//
					boolean addOK = addFriend(com);
					DataBean dataBean = (DataBean) com.getData();
					System.out.println("P_Friends_Add_OK" + com);
					// 返回当前用户
					List<User> list2 = getFriendsList(dataBean.getDestId());
					dataBean.setData(list2);
					CommonTransferBean bean2 = new CommonTransferBean(Protocol.P_Friends_Add_OK, dataBean);
					try {
						oos.writeObject(bean2);
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else if (command.equals(Protocol.P_Friends_Add_OK_ToSrcId)) {
					DataBean dataBean = (DataBean) com.getData();
					// 返回申请用户
					List<User> list = getFriendsList(dataBean.getDestId());
					dataBean.setData(list);
					CommonTransferBean bean = new CommonTransferBean(Protocol.P_Friends_Add_OK_ToSrcId, dataBean);
					transfer(bean);
				} else {
					transfer(com);
				}

			} catch (Exception e) {
				// e.printStackTrace();
			}

		}

	}

	/** 处理添加好友 **/
	private void handleAddFriend(CommonTransferBean com) {
		DataBean bean = (DataBean) com.getData();
		System.out.println("处理添加好友" + com);
		CommonTransferBean bean1;
		boolean isAdd = isAdd(bean.getSrcId(), bean.getDestId());
		bean.setData(isAdd);
		if (isAdd) {// 已经是好友
			bean1 = new CommonTransferBean(Protocol.P_F_isAdd, bean);
			try {
				oos.writeObject(bean1);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {// 未是好友，申请添加
			bean1 = new CommonTransferBean(Protocol.P_F_Add_Request, bean);
			transfer(bean1);
		}

	}

	/** 将消息转发到好友线程 **/
	private void transfer(CommonTransferBean bean) {
		// 第一步：找到好友qq
		DataBean data = (DataBean) bean.getData();
		String UserId = data.getDestId();
		// 二：从map中找到好友线程
		ServerSocketThread friendThread = UserManager.userThreads.get(UserId);
		// 三：转发
		try {
			friendThread.oos.writeObject(bean);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/** 添加好友 **/
	private boolean addFriend(CommonTransferBean bean) {
		DataBean data = (DataBean) bean.getData();
		String srcId = data.getSrcId();
		String destId = data.getDestId();
		boolean addFriend1 = userService.addFriend(srcId, destId);
		boolean addFriend2 = userService.addFriend(destId, srcId);
		return addFriend1;
	}

	/** 是否已经是好友 **/
	private boolean isAdd(String srcId, String destId) {
		boolean isAdd = userService.isAdd(srcId, destId);
		return isAdd;
	}

	/** 根据关键字搜索好友 **/
	private void searchFriendsList(Object nickName) {
		List<User> search = new ArrayList<User>();
		search = userService.selsecSearchFriends(nickName);
		CommonTransferBean bean = new CommonTransferBean(Protocol.P_Search_Friends, search);
		try {
			oos.writeObject(bean);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	UserService userService = new UserService();
	String userId = null;

	// String userIdAndPwd = null;
	/** 登录 **/
	public void login(CommonTransferBean com) {
		String userIdAndPwd = (String) com.getData();
		String spilt[] = userIdAndPwd.split("##");
		userId = spilt[0];
		User userLog = userService.login(spilt[0], spilt[1]);
		System.out.println("服务用户" + userLog);

		CommonTransferBean result;
		if (userLog == null) {
			result = new CommonTransferBean(com.getCmd(), userLog, "登录失败");
		} else {
			result = new CommonTransferBean(com.getCmd(), userLog, "登录成功");
			// 存放
			UserManager.userList.add(userLog);
			// 更新
			MainFame.jTable.updateUI();
			UserManager.userThreads.put(userId, this);
		}
		try {
			oos.writeObject(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 获取当前用户的好友列表 **/
	public void getFriendsList(CommonTransferBean com) {
		List<User> friends = new ArrayList<User>();
		String userId = (String) com.getData();
		friends = userService.selectFriends(userId);
		CommonTransferBean result = new CommonTransferBean(com.getCmd(), friends);
		try {
			oos.writeObject(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<User> getFriendsList(String userId) {
		List<User> friends = new ArrayList<User>();
		friends = userService.selectFriends(userId);
		return friends;
	}

}
