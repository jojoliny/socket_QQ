package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import bean.CommonTransferBean;
import bean.DataBean;
import bean.Protocol;
import bean.User;
import manager.UImanager;
import manager.UserManager;
import singleton.SocketSingleton;
import utils.ImageButton;
import utils.ThreadUtil;

public class LookFrame extends ParentFrame implements ActionListener {

	User friend;

	public LookFrame(User friend) {
		super(446, 311);
		this.friend = friend;
		setVisible(true);
	}

	@Override
	public void initDate() {

	}

	@Override
	public void initView() {
		setFrameMove();
		setClose();
		setMinimize();
		drawBg("img/QQLook/LookSelectBG.png");

		int a = 0;
		// 15*8
		int column = 15;
		int row = 8;
		for (int i = 0; i < row; i++) {// 以下需要优化---------------
			for (int j = 0; j < column; j++) {
				int x = 10 + j * 29;
				int y = 30 + i * 29;
				ImageButton btn = new ImageButton(x, y, 24, 24, "img/QQLook/" + a + ".gif");

				btn.addActionListener(this);
				btn.setActionCommand("" + a);

				add(btn);
				a++;
				ThreadUtil.sleep(10);
			}
		}
	}

	@Override
	public void initListen() {
	}

	public void actionPerformed(ActionEvent e) {
		ImageButton btn = (ImageButton) e.getSource();
		// Icon icon=e.getIcon();
		// 控件绑定的指令
		String actionCommand = e.getActionCommand();
		System.out.println(actionCommand);
		String path = "img/QQLook/" + actionCommand + ".gif";

		// 给自己 的聊天界面添加表情
		String chatFrameKey = UImanager.genRoomKey(friend.getId());
		ChatFrame chatFrame = UImanager.chatFrames.get(chatFrameKey);
		long time = System.currentTimeMillis();
		chatFrame.insertImg(path, time, true);

		// 封装协议
		// 发送表情地址
		DataBean dataBean = new DataBean(UserManager.currentUser.getId(), friend.getId());
		dataBean.setData(path);
		dataBean.setTime(time);
		CommonTransferBean bean = new CommonTransferBean(Protocol.P_F_SendLook, dataBean);
		try {
			SocketSingleton.getInstance().sendObj(bean);
		} catch (IOException e1) {
			System.out.println("发送表情指令有问题");
		}
	}
}
