package thread;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JOptionPane;

import bean.CommonTransferBean;
import bean.DataBean;
import bean.Protocol;
import bean.User;
import manager.UImanager;
import manager.UserManager;
import singleton.SocketSingleton;
import ui.ChatFrame;
import utils.FileTool;

public class ClientSocketThread implements Runnable {

	// QQFrame qqFrame;
	public List<User> saerchFriends;

	public ClientSocketThread() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (true) {
			synchronized (this) {

				try {
					CommonTransferBean receive = (CommonTransferBean) SocketSingleton.getInstance().readObj();
					String cmd = receive.getCmd();
					/** ��¼ **/
					// if (cmd.equals("login")) {
					// if (receive.getMsg().equals("��¼�ɹ�")) {
					// UserManager.currentUser = (User) receive.getData();
					// qqFrame.dispose();
					// UImanager.fList = new FriendListFrame();
					// /** ������:��ȡ�����б� **/
					// CommonTransferBean ctb = new
					// CommonTransferBean(Protocol.P_F_getFriendsList,
					// UserManager.currentUser.getId());
					// SocketSingleton.getInstance().sendObj(ctb);
					// }
					// }
					/** ��ȡ�����б� **/
					if (cmd.equals(Protocol.P_F_getFriendsList)) {
						List<User> list = (List<User>) receive.getData();
						UImanager.fList.refreshList(list);
					}
					/** �������� **/
					else if (cmd.equals(Protocol.P_Search_Friends)) {
						UserManager.searchUserList = (List<User>) receive.getData();
					}
					/** ����Ӻ��� **/
					else if (cmd.equals(Protocol.P_F_isAdd)) {
						DataBean dataBean = (DataBean) receive.getData();
						ChatFrame chatFrame = getCurrentChat(dataBean);
						JOptionPane.showMessageDialog(null, "�����Ѿ��Ǻ���");
					}
					/** ������Ӻ��� **/
					else if (cmd.equals(Protocol.P_F_Add_Request)) {
						DataBean dataBean = (DataBean) receive.getData();
						int result = JOptionPane.showConfirmDialog(null, "�Ƿ�Ը���Ϊ" + dataBean.getSrcId() + "�ĺ���");
						if (result == 0)// ����
						{
							// ������Ӻ��ѳɹ�-�����
							CommonTransferBean bean = new CommonTransferBean(Protocol.P_Friends_Add_OK, dataBean);
							SocketSingleton.getInstance().sendObj(bean);

						} else {
							// �ܾ�
						}

					}
					/** ˢ�µ�ǰ�����б� **/
					else if (cmd.equals(Protocol.P_Friends_Add_OK)) {
						DataBean dataBean = (DataBean) receive.getData();
						List<User> list = (List<User>) dataBean.getData();
						UImanager.fList.refreshList(list);
						// ����ʾ�Է���ӳɹ�<-����Э��
						DataBean dataBean2 = new DataBean(dataBean.getDestId(), dataBean.getSrcId());
						CommonTransferBean bean = new CommonTransferBean(Protocol.P_Friends_Add_OK_ToSrcId, dataBean2);
						SocketSingleton.getInstance().sendObj(bean);
					}
					/** ˢ�������ߺ����б� **/
					else if (cmd.equals(Protocol.P_Friends_Add_OK_ToSrcId)) {
						JOptionPane.showMessageDialog(null, "��ӳɹ�����");
						DataBean dataBean = (DataBean) receive.getData();
						List<User> list = (List<User>) dataBean.getData();
						UImanager.fList.refreshList(list);
					}
					/** ���� **/
					else if (cmd.equals(Protocol.P_F_Twitter)) {
						DataBean dataBean = (DataBean) receive.getData();
						// ��ȡ�������
						ChatFrame chatFrame = getCurrentChat(dataBean);
						// ���ö�������
						chatFrame.twitter();
					}
					/** ������Ϣ **/
					else if (cmd.equals(Protocol.P_F_SendMsg)) {
						DataBean dataBean = (DataBean) receive.getData();
						ChatFrame chatFrame = getCurrentChat(dataBean);
						// ��������
						String text = (String) dataBean.getData();
						System.out.println(text);
						// ������Ϣ
						chatFrame.insertMsg(text, dataBean.getTime(), false);
					}
					/** ����ͼƬ=�ļ� **/
					else if (cmd.equals(Protocol.P_F_SendImg)) {
						DataBean dataBean = (DataBean) receive.getData();
						ChatFrame chatFrame = getCurrentChat(dataBean);
						// �ļ��ֽ�
						byte[] bs = (byte[]) dataBean.getData();
						// �����ļ�
						String path = FileTool.fileSave(bs, "receive/", receive.getMsg());
						// ���������ͼƬ
						chatFrame.insertImg(path, dataBean.getTime(), false);
					}
					/** ���ͱ��� **/
					else if (cmd.equals(Protocol.P_F_SendLook)) {
						DataBean dataBean = (DataBean) receive.getData();
						ChatFrame chatFrame = getCurrentChat(dataBean);
						String filePath = (String) dataBean.getData();// ��ȡ����
																		// ��i��

						chatFrame.insertImg(filePath, dataBean.getTime(), false);
					}
					// ===================Զ��======================
					/** ���Ʒ� **/
					if (cmd.equals(Protocol.P_F_Remote_ScreenSize)) {
						DataBean dataBean = (DataBean) receive.getData();
						ChatFrame chatFrame = getCurrentChat(dataBean);
						chatFrame.remoteFrame.setScreenSize((Dimension) dataBean.getData());
					} else if (cmd.equals(Protocol.P_F_Remote_Screen)) {
						DataBean dataBean = (DataBean) receive.getData();
						ChatFrame chatFrame = getCurrentChat(dataBean);
						chatFrame.remoteFrame.setBg((byte[]) dataBean.getData());
					}
					/** �����Ʒ� **/
					if (cmd.equals(Protocol.P_F_Remote_Request)) {
						int result = JOptionPane.showConfirmDialog(null, "�Ƿ����Զ�̿���");
						if (result == 0)// ����
						{
							DataBean dataBean = (DataBean) receive.getData();
							ChatFrame chatFrame = getCurrentChat(dataBean);
							// ������Ļ��ͼ
							chatFrame.startRemote();
							// new RemoteThread(dataBean.getDestId());
						} else {
							// �ܾ�
						}
					}
					if (cmd.contains("Remote_Mouse_")) {
						robotAnswer(receive);
						System.out.println("ClintSocketThread->Remote_Mouse_");
					}

					else if (cmd.contains("Remote_Key_"))
						robotAnswer(receive);
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
	}

	private void robotAnswer(CommonTransferBean bean) {
		System.out.println("answer:" + bean);
		DataBean dataBean = (DataBean) bean.getData();
		ChatFrame chatFrame = getCurrentChat(dataBean);
		chatFrame.remoteThread.answerOperation(bean);
	}

	private ChatFrame getCurrentChat(DataBean dataBean) {
		String srcQq = dataBean.getSrcId();
		String roomKey = UImanager.genRoomKey(srcQq);
		ChatFrame chatFrame = UImanager.chatFrames.get(roomKey);
		return chatFrame;
	}

}
