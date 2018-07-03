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
		btnChatName.setText("和" + friend.getUsername() + "聊天中");
		add(btnChatName);
		lookFrame = new LookFrame(friend);
		lookFrame.setFocusable(true);
		lookFrame.setVisible(false);
		lookFrame.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				lookFrame.setVisible(false);// 失焦隐藏
			}

			@Override
			public void focusGained(FocusEvent e) {

			}
		});
	}

	@Override
	public void initDate() {

		txt_pane = new JTextPane();// 消息显示框
		txt_msg = new JTextPane();// 消息文本编辑框
		btn_sendMsg = new JButton();// 发送
		btn_close = new JButton();// 关闭

		styledDocument = txt_pane.getStyledDocument();

		// friendName = friend.getUsername();

	}

	private JTextPane txt_pane;// 消息显示框
	private JTextPane txt_msg;// 消息文本编辑框
	private JButton btn_sendMsg;// 发送
	private JButton btn_close;// 关闭
	private JScrollPane jsp_h;
	private JScrollBar bar;
	private ImageButton btn_sendPic;// 发送图片
	private ImageButton btn_twitter;// 抖动
	private ImageButton btn_look;// 表情
	private long lastTime = 0;
	private StyledDocument styledDocument;
	private LookFrame lookFrame;
	private ImageButton btn_cut;// 截图

	@Override
	public void initView() {
		setFrameMove();
		setClose();
		setMinimize();
		drawBg("img/bg_chat.jpg");

		// 1最上面的数据：群名称、群头像
		setTopInfor();
		// 2初始化 顶部的工具栏 发送文件、远程
		setTopToolBar();
		// 3中间消息记录面板
		setMsgRecordPanel();
		// 4中间功能面板：抖动、发图、发表情
		setCenterFunctionPanel();
		// 5发送的消息框
		setMsgPanel();

		LookFrame lookFrame = new LookFrame(friend);
		lookFrame.setVisible(false);

	}

	// 1最上面的数据：群名称、群头像
	private void setTopInfor() {
		ImageButton btn_head = new ImageButton(20, 20, 70, 70, "img/userpic1.jpg");
		add(btn_head);

		btnChatName = new ImageButton(100, 20, 300, 30);
		// btnChatName.setText("和" + friend.getUsername() + "聊天中");
		btnChatName.setFont(new Font("dialog", Font.LAYOUT_LEFT_TO_RIGHT | Font.BOLD, 25));
		btnChatName.setForeground(Color.WHITE);
		// add(btnChatName);
	}

	// 2初始化 顶部的工具栏 发送文件、远程
	private void setTopToolBar() {
		ImageButton btn_sendFile = new ImageButton(20, 100, 28, 28, "img/chat/sendfile.png");
		btn_remote = new ImageButton(50, 100, 28, 28, "img/chat/remote_assistance.png");
		btn_audio = new ImageButton(80, 100, 28, 28, "img/chat/audio.png");
		add(btn_sendFile);
		add(btn_remote);
		add(btn_audio);

	}

	// 3中间消息记录面板
	private void setMsgRecordPanel() {
		JPanel pl_txtPane = new JPanel();// 存放消息显示框

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

	// 4中间功能面板：抖动、发图、发表情
	private void setCenterFunctionPanel() {
		int width = 30;
		int height = 30;
		JPanel pl_btnMidToolBar = new JPanel(new FlowLayout());// 工具栏
		pl_btnMidToolBar.setBounds(0, 480, getWidth(), 30);
		pl_btnMidToolBar.setOpaque(false);
		btn_sendPic = new ImageButton(0, 0, width, height, "img/chat/sendpic.png");
		btn_twitter = new ImageButton(0, 0, width, height, "img/chat/twitter.png");
		btn_look = new ImageButton(0, 0, width, height, "img/chat/look.png");
		btn_cut = new ImageButton(0, 0, width, height, "img/chat/cut.png");
		btn_sendPic.setToolTipText("图片");
		btn_twitter.setToolTipText("抖动");
		btn_look.setToolTipText("表情");
		btn_cut.setToolTipText("截屏");
		pl_btnMidToolBar.add(btn_sendPic);
		pl_btnMidToolBar.add(btn_twitter);
		pl_btnMidToolBar.add(btn_look);
		pl_btnMidToolBar.add(btn_cut);
		add(pl_btnMidToolBar);
	}

	// 5发送的消息框
	private void setMsgPanel() {
		txt_msg.setEditable(true);
		JPanel pl_txtMsg = new JPanel();// 消息文本编辑框
		pl_txtMsg.setBounds(0, 510, getWidth(), 100);
		JScrollPane jsp_txtMsg = new JScrollPane(txt_msg);
		jsp_txtMsg.setPreferredSize(new Dimension(pl_txtMsg.getWidth(), pl_txtMsg.getHeight()));
		pl_txtMsg.add(jsp_txtMsg);
		pl_txtMsg.setBounds(0, 510, getWidth(), 100);
		add(pl_txtMsg);

		btn_sendMsg = new JButton("发送");
		btn_close = new JButton("关闭");
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

				// 清空编辑框
				txt_msg.setText("");
			}
		});

		btn_close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		/** 发送图片 **/
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
					// 当前聊天界面插入图片
					insertImg(filePath, time, true);

					// file->字节
					byte[] bs = FileTool.fileToBytes(file);

					// 封装协议->发送file字节+文件名
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

		/** 抖动 **/
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
		/** 表情 **/
		btn_look.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// 计算窗口位置
				int x = getX();
				int y = getY();
				// 设置表情界面位置
				lookFrame.setLocation(x + 100, y + 164);
				// 表情界面可见
				lookFrame.setVisible(true);
			}
		});

		/** 远程 **/
		// 控制方
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
						System.out.println("远程请求有问题");
					}
				}
			}
		});
		/** 截图 **/
		btn_cut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CaptureTool captureTool = new CaptureTool(friend.getId());
			}
		});

		/** 录音 **/
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

	// 插入消息
	public void insertMsg(String content, long time, boolean isSelf) {
		insertTime(time);
		txt_pane.select(bar.getMaximum(), bar.getMaximum());
		if (isSelf) {
			try {
				styledDocument.insertString(styledDocument.getLength(), "\r\n我： " + content, null);
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
				styledDocument.insertString(styledDocument.getLength(), "\r\n我： ", null);
				txt_pane.select(bar.getMaximum(), bar.getMaximum());
				txt_pane.insertIcon(ImageUtil.getIcon(filePath));
			} else {
				styledDocument.insertString(styledDocument.getLength(), "\r\n" + friend.getUsername() + ": ", null);
				txt_pane.select(bar.getMaximum(), bar.getMaximum());
				txt_pane.insertIcon(ImageUtil.getIcon(filePath));
			}
			bar.setValue(bar.getMaximum());
		} catch (Exception e) {
			System.out.println("insertImg有问题");
		}
	}

	// 插入时间
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

	// 抖动
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
