package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import bean.CommonTransferBean;
import bean.Protocol;
import bean.User;
import manager.ConfigParam;
import manager.UserManager;
import singleton.SocketSingleton;
import util.SystemTool;
import utils.ImageButton;
import utils.ImageUtil;

public class FriendListFrame extends ParentFrame {

	private ImageButton btn_search;
	private JScrollPane scroll;
	private JPanel plFriends;
	private JPanel plContent;
	private ImageButton btn_calc;

	public FriendListFrame() {
		super(300, 600);
	}

	@Override
	public void initDate() {
		try {
			if (!ConfigParam.isDebug) { // 项目正常运行
				// 告诉服务器我要查询数据
				CommonTransferBean bean = new CommonTransferBean(Protocol.P_F_getFriendsList,
						UserManager.currentUser.getId());

				// 往服务器请求好友列表
				SocketSingleton.getInstance().sendObj(bean);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initView() {
		/** 基本 **/
		setFrameMove();
		setClose();
		setMinimize();
		drawBg("img/reg1.jpg");

		/** 自己的头像，签名 **/
		JLabel head = new JLabel();
		head.setIcon(ImageUtil.getIcon("img/userpic1.jpg"));
		head.setBounds(30, 30, 70, 70);
		add(head);
		JLabel sign = new JLabel(UserManager.currentUser.getSign());
		// sign.setText(CurrentUser.currentUser.getSign());
		sign.setBounds(120, 70, 150, 30);
		add(sign);
		JLabel nickName = new JLabel(UserManager.currentUser.getUsername());
		nickName.setBounds(120, 40, 150, 30);
		add(nickName);

		plFriends = new JPanel(new BorderLayout());
		plFriends.setBounds(0, 150, getWidth(), 400);
		add(plFriends);

		plContent = new JPanel();

		scroll = new JScrollPane(plContent);

		BoxLayout boxLayout = new BoxLayout(plContent, BoxLayout.Y_AXIS);

		plContent.setBounds(0, 0, getWidth(), getHeight());
		plContent.setLayout(boxLayout);

		if (ConfigParam.isDebug) {
			for (int i = 0; i < 20; i++) {
				User user = new User("" + i, "" + i, null, null);
				FriendPanel plFriend = new FriendPanel(user);
				plFriend.setPreferredSize(new Dimension(getWidth(), 50));
				plContent.add(plFriend);
			}
		} else
			for (int i = 0; i < QQFrame.friends.size(); i++) {
				User user = QQFrame.friends.get(i);
				FriendPanel plFriend = new FriendPanel(user);
				plFriend.setPreferredSize(new Dimension(getWidth(), 50));
				plContent.add(plFriend);
			}

		plFriends.add(scroll);
		btn_search = new ImageButton(50, getHeight() - 50, 32, 32, "img/search.png", "img/search.png",
				"img/search.png");
		add(btn_search);
		btn_calc = new ImageButton(95, getHeight() - 50, 20, 20, "img/facemanage.png", "img/facemanage.png",
				"img/facemanage.png");
		add(btn_calc);
	}

	@Override
	public void initListen() {
		btn_search.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new SearchFriendsFrame();
			}
		});

		btn_calc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SystemTool.processExec("calc");
			}
		});
	}

	public void refreshList(List<User> list) {
		System.out.println("refreshList");
		/** 第一步 移除以前的 **/
		plFriends.remove(scroll);
		/** 第二步 重新构建一个好友面板 **/
		plContent = new JPanel();
		BoxLayout boxLayout = new BoxLayout(plContent, BoxLayout.Y_AXIS);
		plContent.setLayout(boxLayout);
		plContent.setBounds(0, 0, getWidth(), getHeight());
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.size());
			User user = list.get(i);
			FriendPanel plFriend = new FriendPanel(user);
			plFriend.setPreferredSize(new Dimension(getWidth(), 50));
			plContent.add(plFriend);
			System.out.println(user);
		}
		/** 第三步 添加上去 **/
		scroll = new JScrollPane(plContent);
		plFriends.add(scroll);
		/** 第四步 刷新界面 **/
		validate();
	}

	public static void main(String[] args) {
		new FriendListFrame();
	}
}
