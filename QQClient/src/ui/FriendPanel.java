package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bean.User;
import manager.UImanager;
import utils.StringUtil;

public class FriendPanel extends JPanel {

	private User user;// ����
	private JLabel lb_mood;
	private JLabel lb_nickName;
	private JButton jButton;
	private String useriId;

	public FriendPanel(User user) {
		this.user = user;
		this.setLayout(null);
		initView();
		addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					// �����
					String genRoomKey = UImanager.genRoomKey(user.getId());
					System.out.println("�����" + genRoomKey);
					// �ж��Ƿ����
					ChatFrame chatFrame = UImanager.chatFrames.get(genRoomKey);
					if (chatFrame == null) {
						chatFrame = new ChatFrame(user.getUsername(), user);
						UImanager.chatFrames.put(genRoomKey, chatFrame);
						System.out.println(UImanager.chatFrames);
					} else {
						chatFrame.setVisible(true);
					}

				}
			}
		});
	}

	public void initView() {

		/** ǩ�� **/
		lb_mood = new JLabel();
		lb_mood.setBounds(new Rectangle(51, 30, 131, 20));
		lb_mood.setFont(new Font("Dialog", Font.PLAIN, 12));
		if (StringUtil.isEmpty(user.getSign())) {
			lb_mood.setText("��������ңԶ�ľ��벻��������,������վ������ǰ��ȴ��֪���Ұ���!");
		} else {
			lb_mood.setText(user.getSign());
		}
		//
		lb_mood.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent e) {
				lb_mood.setToolTipText(lb_mood.getText());
			}
		});
		/** �ǳ� **/
		lb_nickName = new JLabel();
		lb_nickName.setBounds(new Rectangle(52, 10, 80, 20));
		lb_nickName.setFont(new Font("Dialog", Font.BOLD, 14));
		lb_nickName.setText(user.getUsername());

		/** ͷ�� **/
		this.add(getJButton(), null);
		this.add(lb_nickName, null);
		this.add(lb_mood, null);
		/** ������������� **/
		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseExited(java.awt.event.MouseEvent e) {
				exchangeExited();// ����Ƴ�ģ�������ı䱳����ɫ��
			}

			public void mouseEntered(java.awt.event.MouseEvent e) {
				exchangeEnter();// ����ƽ�ģ�������ı䱳����ɫ��
			}
		});
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				exchangeEnter();

			}
		});
	}

	private void exchangeEnter() {
		this.setBackground(new Color(192, 224, 248));
	}

	private void exchangeExited() {
		this.setBackground(null);
	}

	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(8, 10, 40, 40));
			jButton.setBackground(new Color(236, 255, 236));
			jButton.setIcon(new ImageIcon("img/" + "1.jpg"));
		}
		return jButton;
	}
}
