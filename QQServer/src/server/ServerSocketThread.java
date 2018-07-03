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

	/** �ȴ��ͻ��˷�����Ϣ **/
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
				/** ������Ӻ��� **/
				else if (command.equals(Protocol.P_Friends_Add)) {
					handleAddFriend(com);
					System.out.println("tianjia" + com);

				}
				/** ����ɹ� **/
				else if (command.equals(Protocol.P_Friends_Add_OK)) {
					//
					boolean addOK = addFriend(com);
					DataBean dataBean = (DataBean) com.getData();
					System.out.println("P_Friends_Add_OK" + com);
					// ���ص�ǰ�û�
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
					// ���������û�
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

	/** ������Ӻ��� **/
	private void handleAddFriend(CommonTransferBean com) {
		DataBean bean = (DataBean) com.getData();
		System.out.println("������Ӻ���" + com);
		CommonTransferBean bean1;
		boolean isAdd = isAdd(bean.getSrcId(), bean.getDestId());
		bean.setData(isAdd);
		if (isAdd) {// �Ѿ��Ǻ���
			bean1 = new CommonTransferBean(Protocol.P_F_isAdd, bean);
			try {
				oos.writeObject(bean1);
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {// δ�Ǻ��ѣ��������
			bean1 = new CommonTransferBean(Protocol.P_F_Add_Request, bean);
			transfer(bean1);
		}

	}

	/** ����Ϣת���������߳� **/
	private void transfer(CommonTransferBean bean) {
		// ��һ�����ҵ�����qq
		DataBean data = (DataBean) bean.getData();
		String UserId = data.getDestId();
		// ������map���ҵ������߳�
		ServerSocketThread friendThread = UserManager.userThreads.get(UserId);
		// ����ת��
		try {
			friendThread.oos.writeObject(bean);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/** ��Ӻ��� **/
	private boolean addFriend(CommonTransferBean bean) {
		DataBean data = (DataBean) bean.getData();
		String srcId = data.getSrcId();
		String destId = data.getDestId();
		boolean addFriend1 = userService.addFriend(srcId, destId);
		boolean addFriend2 = userService.addFriend(destId, srcId);
		return addFriend1;
	}

	/** �Ƿ��Ѿ��Ǻ��� **/
	private boolean isAdd(String srcId, String destId) {
		boolean isAdd = userService.isAdd(srcId, destId);
		return isAdd;
	}

	/** ���ݹؼ����������� **/
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
	/** ��¼ **/
	public void login(CommonTransferBean com) {
		String userIdAndPwd = (String) com.getData();
		String spilt[] = userIdAndPwd.split("##");
		userId = spilt[0];
		User userLog = userService.login(spilt[0], spilt[1]);
		System.out.println("�����û�" + userLog);

		CommonTransferBean result;
		if (userLog == null) {
			result = new CommonTransferBean(com.getCmd(), userLog, "��¼ʧ��");
		} else {
			result = new CommonTransferBean(com.getCmd(), userLog, "��¼�ɹ�");
			// ���
			UserManager.userList.add(userLog);
			// ����
			MainFame.jTable.updateUI();
			UserManager.userThreads.put(userId, this);
		}
		try {
			oos.writeObject(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** ��ȡ��ǰ�û��ĺ����б� **/
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
