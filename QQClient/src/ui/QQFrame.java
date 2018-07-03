package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import bean.CommonTransferBean;
import bean.User;
import manager.ConfigParam;
import manager.UImanager;
import manager.UserManager;
import singleton.SocketSingleton;
import thread.ClientSocketThread;
import utils.ImageButton;
import utils.ImageUtil;
import utils.StringUtil;

/**
 * @author Administrator
 *
 */
public class QQFrame extends ParentFrame {

	public QQFrame() {
		super("QQ", 500, 430, null);
		setFrameMove();
		drawBg("img/3.jpg");

	}

	@Override
	public void initDate() {

	}

	ImageButton btnClose;
	ImageButton btnMinimize;
	ImageButton logIn;
	JComboBox<String> uesrList;
	JPasswordField password;

	@Override
	public void initView() {
		JLabel head = new JLabel();
		head.setIcon(ImageUtil.getIcon("img/userpic1.jpg"));
		head.setBounds(80, getWidth() - 250, 70, 70);
		add(head);

		uesrList = new JComboBox<String>();
		uesrList.setBounds(180, getWidth() - 250, 250, 25);
		uesrList.addItem("1234");
		uesrList.addItem("6666");
		uesrList.setEditable(true);
		uesrList.setBorder(null);
		add(uesrList);

		password = new JPasswordField();
		password.setBounds(180, getWidth() - 220, 250, 25);
		password.setEditable(true);
		password.setBorder(null);
		add(password);

		logIn = new ImageButton(110, 350, 240, 35, "img/login.png", "img/login1.png", "img/login.png");
		add(logIn);

		btnClose = new ImageButton(getWidth() - 18, 2, 16, 16, "img/r1.png", "img/r2.png", "img/r1.png");
		add(btnClose);
		btnMinimize = new ImageButton(getWidth() - 18 * 2, 2, 16, 16, "img/y1.png", "img/y2.png", "img/y1.png");
		add(btnMinimize);
	}

	public static List<User> friends = new ArrayList<User>();
	private String useriId;

	@Override
	public void initListen() {
		setClose(btnClose);
		setMinimize(btnMinimize);

		logIn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/** 第一步：账号密码不能为空 **/
				useriId = uesrList.getSelectedItem().toString();
				String pwd = password.getText();
				if (StringUtil.isEmpty(useriId)) {
					JOptionPane.showMessageDialog(null, "账号不能为空");
					return;
				} else if (StringUtil.isEmpty(pwd)) {
					JOptionPane.showMessageDialog(null, "密码不能为空");
					return;
				}
				String userString = useriId + "##" + pwd;
				CommonTransferBean ctb = null;
				try {
					ctb = new CommonTransferBean("login", userString);
					SocketSingleton.getInstance().sendObj(ctb);
					CommonTransferBean receive = (CommonTransferBean) SocketSingleton.getInstance().readObj();
					if (receive.getMsg().equals("登录成功")) {
						UserManager.currentUser = (User) receive.getData();
						dispose();
						ConfigParam.isDebug = false;
						UImanager.fList = new FriendListFrame();
						new ClientSocketThread();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					System.out.println("发送失败");
				}

			}
		});
	}

	public static void main(String[] args) {
		new QQFrame();
	}

}
