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
					/** 登录 **/
					// if (cmd.equals("login")) {
					// if (receive.getMsg().equals("登录成功")) {
					// UserManager.currentUser = (User) receive.getData();
					// qqFrame.dispose();
					// UImanager.fList = new FriendListFrame();
					// /** 第三步:获取好友列表 **/
					// CommonTransferBean ctb = new
					// CommonTransferBean(Protocol.P_F_getFriendsList,
					// UserManager.currentUser.getId());
					// SocketSingleton.getInstance().sendObj(ctb);
					// }
					// }
					/** 获取好友列表 **/
					if (cmd.equals(Protocol.P_F_getFriendsList)) {
						List<User> list = (List<User>) receive.getData();
						UImanager.fList.refreshList(list);
					}
					/** 搜索好友 **/
					else if (cmd.equals(Protocol.P_Search_Friends)) {
						UserManager.searchUserList = (List<User>) receive.getData();
					}
					/** 已添加好友 **/
					else if (cmd.equals(Protocol.P_F_isAdd)) {
						DataBean dataBean = (DataBean) receive.getData();
						ChatFrame chatFrame = getCurrentChat(dataBean);
						JOptionPane.showMessageDialog(null, "你们已经是好友");
					}
					/** 请求添加好友 **/
					else if (cmd.equals(Protocol.P_F_Add_Request)) {
						DataBean dataBean = (DataBean) receive.getData();
						int result = JOptionPane.showConfirmDialog(null, "是否愿意成为" + dataBean.getSrcId() + "的好友");
						if (result == 0)// 接受
						{
							// 发送添加好友成功-》添加
							CommonTransferBean bean = new CommonTransferBean(Protocol.P_Friends_Add_OK, dataBean);
							SocketSingleton.getInstance().sendObj(bean);

						} else {
							// 拒绝
						}

					}
					/** 刷新当前好友列表 **/
					else if (cmd.equals(Protocol.P_Friends_Add_OK)) {
						DataBean dataBean = (DataBean) receive.getData();
						List<User> list = (List<User>) dataBean.getData();
						UImanager.fList.refreshList(list);
						// 并提示对方添加成功<-发送协议
						DataBean dataBean2 = new DataBean(dataBean.getDestId(), dataBean.getSrcId());
						CommonTransferBean bean = new CommonTransferBean(Protocol.P_Friends_Add_OK_ToSrcId, dataBean2);
						SocketSingleton.getInstance().sendObj(bean);
					}
					/** 刷新申请者好友列表 **/
					else if (cmd.equals(Protocol.P_Friends_Add_OK_ToSrcId)) {
						JOptionPane.showMessageDialog(null, "添加成功！！");
						DataBean dataBean = (DataBean) receive.getData();
						List<User> list = (List<User>) dataBean.getData();
						UImanager.fList.refreshList(list);
					}
					/** 抖动 **/
					else if (cmd.equals(Protocol.P_F_Twitter)) {
						DataBean dataBean = (DataBean) receive.getData();
						// 获取聊天界面
						ChatFrame chatFrame = getCurrentChat(dataBean);
						// 调用抖动方法
						chatFrame.twitter();
					}
					/** 发送消息 **/
					else if (cmd.equals(Protocol.P_F_SendMsg)) {
						DataBean dataBean = (DataBean) receive.getData();
						ChatFrame chatFrame = getCurrentChat(dataBean);
						// 聊天内容
						String text = (String) dataBean.getData();
						System.out.println(text);
						// 发送消息
						chatFrame.insertMsg(text, dataBean.getTime(), false);
					}
					/** 发送图片=文件 **/
					else if (cmd.equals(Protocol.P_F_SendImg)) {
						DataBean dataBean = (DataBean) receive.getData();
						ChatFrame chatFrame = getCurrentChat(dataBean);
						// 文件字节
						byte[] bs = (byte[]) dataBean.getData();
						// 保存文件
						String path = FileTool.fileSave(bs, "receive/", receive.getMsg());
						// 本窗体插入图片
						chatFrame.insertImg(path, dataBean.getTime(), false);
					}
					/** 发送表情 **/
					else if (cmd.equals(Protocol.P_F_SendLook)) {
						DataBean dataBean = (DataBean) receive.getData();
						ChatFrame chatFrame = getCurrentChat(dataBean);
						String filePath = (String) dataBean.getData();// 获取表情
																		// 第i个

						chatFrame.insertImg(filePath, dataBean.getTime(), false);
					}
					// ===================远程======================
					/** 控制方 **/
					if (cmd.equals(Protocol.P_F_Remote_ScreenSize)) {
						DataBean dataBean = (DataBean) receive.getData();
						ChatFrame chatFrame = getCurrentChat(dataBean);
						chatFrame.remoteFrame.setScreenSize((Dimension) dataBean.getData());
					} else if (cmd.equals(Protocol.P_F_Remote_Screen)) {
						DataBean dataBean = (DataBean) receive.getData();
						ChatFrame chatFrame = getCurrentChat(dataBean);
						chatFrame.remoteFrame.setBg((byte[]) dataBean.getData());
					}
					/** 被控制方 **/
					if (cmd.equals(Protocol.P_F_Remote_Request)) {
						int result = JOptionPane.showConfirmDialog(null, "是否接受远程控制");
						if (result == 0)// 接受
						{
							DataBean dataBean = (DataBean) receive.getData();
							ChatFrame chatFrame = getCurrentChat(dataBean);
							// 发送屏幕截图
							chatFrame.startRemote();
							// new RemoteThread(dataBean.getDestId());
						} else {
							// 拒绝
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
