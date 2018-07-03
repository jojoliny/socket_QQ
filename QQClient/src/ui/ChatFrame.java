package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import bean.CommonTransferBean;
import bean.DataBean;
import bean.Protocol;
import bean.User;
import manager.UserManager;
import singleton.SocketSingleton;
import thread.RemoteThread;
import util.CaptureTool;
import utils.FileTool;
import utils.ImageButton;
import utils.ImageUtil;
import utils.ThreadUtil;
import utils.TimeUtil;

public class ChatFrame extends ParentFrame {

	public ChatFrame(String title, User friend) {
		super(title, 600, 650, null);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		this.friend = friend;
		btnChatName.setText("��" + friend.getUsername() + "������");
		add(btnChatName);
		lookFrame = new LookFrame(friend);
		lookFrame.setFocusable(true);
		lookFrame.setVisible(false);
		lookFrame.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				lookFrame.setVisible(false);// ʧ������
			}

			@Override
			public void focusGained(FocusEvent e) {

			}
		});
	}

	@Override
	public void initDate() {

		txt_pane = new JTextPane();// ��Ϣ��ʾ��
		txt_msg = new JTextPane();// ��Ϣ�ı��༭��
		btn_sendMsg = new JButton();// ����
		btn_close = new JButton();// �ر�

		styledDocument = txt_pane.getStyledDocument();

		// friendName = friend.getUsername();

	}

	private JTextPane txt_pane;// ��Ϣ��ʾ��
	private JTextPane txt_msg;// ��Ϣ�ı��༭��
	private JButton btn_sendMsg;// ����
	private JButton btn_close;// �ر�
	private JScrollPane jsp_h;
	private JScrollBar bar;
	private ImageButton btn_sendPic;// ����ͼƬ
	private ImageButton btn_twitter;// ����
	private ImageButton btn_look;// ����
	private long lastTime = 0;
	private StyledDocument styledDocument;
	private LookFrame lookFrame;
	private ImageButton btn_cut;// ��ͼ

	@Override
	public void initView() {
		setFrameMove();
		setClose();
		setMinimize();
		drawBg("img/bg_chat.jpg");

		// 1����������ݣ�Ⱥ���ơ�Ⱥͷ��
		setTopInfor();
		// 2��ʼ�� �����Ĺ����� �����ļ���Զ��
		setTopToolBar();
		// 3�м���Ϣ��¼���
		setMsgRecordPanel();
		// 4�м书����壺��������ͼ��������
		setCenterFunctionPanel();
		// 5���͵���Ϣ��
		setMsgPanel();

		LookFrame lookFrame = new LookFrame(friend);
		lookFrame.setVisible(false);

	}

	// 1����������ݣ�Ⱥ���ơ�Ⱥͷ��
	private void setTopInfor() {
		ImageButton btn_head = new ImageButton(20, 20, 70, 70, "img/userpic1.jpg");
		add(btn_head);

		btnChatName = new ImageButton(100, 20, 300, 30);
		// btnChatName.setText("��" + friend.getUsername() + "������");
		btnChatName.setFont(new Font("dialog", Font.LAYOUT_LEFT_TO_RIGHT | Font.BOLD, 25));
		btnChatName.setForeground(Color.WHITE);
		// add(btnChatName);
	}

	// 2��ʼ�� �����Ĺ����� �����ļ���Զ��
	private void setTopToolBar() {
		ImageButton btn_sendFile = new ImageButton(20, 100, 28, 28, "img/chat/sendfile.png");
		btn_remote = new ImageButton(50, 100, 28, 28, "img/chat/remote_assistance.png");
		btn_audio = new ImageButton(80, 100, 28, 28, "img/chat/audio.png");
		add(btn_sendFile);
		add(btn_remote);
		add(btn_audio);

	}

	// 3�м���Ϣ��¼���
	private void setMsgRecordPanel() {
		JPanel pl_txtPane = new JPanel();// �����Ϣ��ʾ��

		// txt_pane.setBounds(0, 0, pl_txtPane.getWidth(),
		// pl_txtPane.getHeight());

		pl_txtPane.setBounds(0, 125, getWidth(), 350);
		jsp_h = new JScrollPane(txt_pane);
		jsp_h.setPreferredSize(new Dimension(pl_txtPane.getWidth(), pl_txtPane.getHeight()));
		// txt_pane.setEditable(false);
		bar = jsp_h.getVerticalScrollBar();
		bar.setValue(bar.getMaximum());

		pl_txtPane.add(jsp_h);

		add(pl_txtPane);

	}

	// 4�м书����壺��������ͼ��������
	private void setCenterFunctionPanel() {
		int width = 30;
		int height = 30;
		JPanel pl_btnMidToolBar = new JPanel(new FlowLayout());// ������
		pl_btnMidToolBar.setBounds(0, 480, getWidth(), 30);
		pl_btnMidToolBar.setOpaque(false);
		btn_sendPic = new ImageButton(0, 0, width, height, "img/chat/sendpic.png");
		btn_twitter = new ImageButton(0, 0, width, height, "img/chat/twitter.png");
		btn_look = new ImageButton(0, 0, width, height, "img/chat/look.png");
		btn_cut = new ImageButton(0, 0, width, height, "img/chat/cut.png");
		btn_sendPic.setToolTipText("ͼƬ");
		btn_twitter.setToolTipText("����");
		btn_look.setToolTipText("����");
		btn_cut.setToolTipText("����");
		pl_btnMidToolBar.add(btn_sendPic);
		pl_btnMidToolBar.add(btn_twitter);
		pl_btnMidToolBar.add(btn_look);
		pl_btnMidToolBar.add(btn_cut);
		add(pl_btnMidToolBar);
	}

	// 5���͵���Ϣ��
	private void setMsgPanel() {
		txt_msg.setEditable(true);
		JPanel pl_txtMsg = new JPanel();// ��Ϣ�ı��༭��
		pl_txtMsg.setBounds(0, 510, getWidth(), 100);
		JScrollPane jsp_txtMsg = new JScrollPane(txt_msg);
		jsp_txtMsg.setPreferredSize(new Dimension(pl_txtMsg.getWidth(), pl_txtMsg.getHeight()));
		pl_txtMsg.add(jsp_txtMsg);
		pl_txtMsg.setBounds(0, 510, getWidth(), 100);
		add(pl_txtMsg);

		btn_sendMsg = new JButton("����");
		btn_close = new JButton("�ر�");
		btn_sendMsg.setBounds(getWidth() - 120, getHeight() - 35, 100, 30);
		add(btn_sendMsg);
		btn_close.setBounds(getWidth() - 230, getHeight() - 35, 100, 30);
		add(btn_close);

	}

	private String path;
	public RemoteFrame remoteFrame;

	@Override
	public void initListen() {
		btn_sendMsg.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String text = txt_msg.getText();
				long time = System.currentTimeMillis();
				insertMsg(text, time, true);
				DataBean data = new DataBean(UserManager.currentUser.getId(), friend.getId());
				data.setData(text);
				data.setTime(time);
				CommonTransferBean bean = new CommonTransferBean(Protocol.P_F_SendMsg, data);
				try {
					SocketSingleton.getInstance().sendObj(bean);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				// ��ձ༭��
				txt_msg.setText("");
			}
		});

		btn_close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		/** ����ͼƬ **/
		btn_sendPic.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser jFileChooser = new JFileChooser();
					jFileChooser.showOpenDialog(null);
					File file = jFileChooser.getSelectedFile();
					if (file == null)
						return;

					String filePath = file.getAbsolutePath();
					long time = System.currentTimeMillis();
					// ��ǰ����������ͼƬ
					insertImg(filePath, time, true);

					// file->�ֽ�
					byte[] bs = FileTool.fileToBytes(file);

					// ��װЭ��->����file�ֽ�+�ļ���
					DataBean dataBean = new DataBean(UserManager.currentUser.getId(), friend.getId());
					dataBean.setData(bs);
					dataBean.setTime(time);
					CommonTransferBean bean = new CommonTransferBean(Protocol.P_F_SendImg, dataBean, file.getName());

					SocketSingleton.getInstance().sendObj(bean);
				} catch (Exception e1) {
					// e1.printStackTrace();
					throw new RuntimeException();
				}
			}
		});

		/** ���� **/
		btn_twitter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				long time = System.currentTimeMillis();
				DataBean dataBean = new DataBean(UserManager.currentUser.getId(), friend.getId());
				dataBean.setTime(time);
				CommonTransferBean bean = new CommonTransferBean(Protocol.P_F_Twitter, dataBean);
				try {
					SocketSingleton.getInstance().sendObj(bean);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				twitter();

			}

		});
		/** ���� **/
		btn_look.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// ���㴰��λ��
				int x = getX();
				int y = getY();
				// ���ñ������λ��
				lookFrame.setLocation(x + 100, y + 164);
				// �������ɼ�
				lookFrame.setVisible(true);
			}
		});

		/** Զ�� **/
		// ���Ʒ�
		btn_remote.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// remoteFrame = new RemoteFrame(dimension);
				if (remoteFrame == null) {
					try {
						remoteFrame = new RemoteFrame(500, 600, friend.getId());
						DataBean dataBean = new DataBean(UserManager.currentUser.getId(), friend.getId());
						CommonTransferBean bean = new CommonTransferBean(Protocol.P_F_Remote_Request, dataBean);

						SocketSingleton.getInstance().sendObj(bean);
					} catch (IOException e1) {
						// e1.printStackTrace();
						System.out.println("Զ������������");
					}
				}
			}
		});
		/** ��ͼ **/
		btn_cut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CaptureTool captureTool = new CaptureTool(friend.getId());
			}
		});

		/** ¼�� **/
		btn_audio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
	}

	User friend;
	private ImageButton btn_remote;
	public RemoteThread remoteThread;
	private ImageButton btn_audio;
	private ImageButton btnChatName;

	// ������Ϣ
	public void insertMsg(String content, long time, boolean isSelf) {
		insertTime(time);
		txt_pane.select(bar.getMaximum(), bar.getMaximum());
		if (isSelf) {
			try {
				styledDocument.insertString(styledDocument.getLength(), "\r\n�ң� " + content, null);
				txt_pane.select(bar.getMaximum(), bar.getMaximum());
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
		} else {
			try {
				styledDocument.insertString(styledDocument.getLength(), "\r\n" + friend.getUsername() + ": " + content,
						null);
				txt_pane.select(bar.getMaximum(), bar.getMaximum());
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		bar.setValue(bar.getMaximum());
	}

	public void insertImg(String filePath, long time, boolean isSelf) {
		try {
			insertTime(time);
			txt_pane.select(bar.getMaximum(), bar.getMaximum());
			if (isSelf) {
				styledDocument.insertString(styledDocument.getLength(), "\r\n�ң� ", null);
				txt_pane.select(bar.getMaximum(), bar.getMaximum());
				txt_pane.insertIcon(ImageUtil.getIcon(filePath));
			} else {
				styledDocument.insertString(styledDocument.getLength(), "\r\n" + friend.getUsername() + ": ", null);
				txt_pane.select(bar.getMaximum(), bar.getMaximum());
				txt_pane.insertIcon(ImageUtil.getIcon(filePath));
			}
			bar.setValue(bar.getMaximum());
		} catch (Exception e) {
			System.out.println("insertImg������");
		}
	}

	// ����ʱ��
	public void insertTime(long time) {
		if ((time - lastTime) > 5000) {
			try {
				SimpleAttributeSet set = new SimpleAttributeSet();
				set.addAttribute(StyleConstants.Foreground, Color.BLUE);
				styledDocument.insertString(styledDocument.getLength(), "\r\n\t\t\t" + TimeUtil.format_Time(time), set);
				bar.setValue(bar.getMaximum());
				txt_pane.select(bar.getMaximum(), bar.getMaximum());
			} catch (BadLocationException e) {
				// e.printStackTrace();
			}
		}
		lastTime = time;
	}

	// ����
	public void twitter() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 5; i++) {
					setLocation((int) getX(), (int) getY() - 5);
					ThreadUtil.sleep(50);
					setLocation((int) getX() - 5, (int) getY());
					ThreadUtil.sleep(50);
					setLocation((int) getX(), (int) getY() + 5);
					ThreadUtil.sleep(50);
					setLocation((int) getX() + 5, (int) getY());
					ThreadUtil.sleep(50);
					setLocation((int) getX(), (int) getY());
				}
			}
		}).start();
	}

	public void startRemote() {
		remoteThread = new RemoteThread(friend.getId());

	}

}
