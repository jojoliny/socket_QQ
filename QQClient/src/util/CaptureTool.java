package util;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import bean.CommonTransferBean;
import bean.DataBean;
import bean.Protocol;
import manager.UserManager;
import singleton.SocketSingleton;
import ui.ChatFrame;
import ui.ParentFrame;

public class CaptureTool extends ParentFrame {
	JLabel screen;
	Robot robot;
	String friendId;
	JButton sendCapture;// ���ͽ�ͼ
	ChatFrame chatFrame;

	public CaptureTool(String friendId) {
		super(0, 0);
		this.friendId = friendId;
		this.chatFrame = chatFrame;
		System.out.println(friendId);
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		computerScreen();
		// String roomKey =
		// UImanager.genRoomKey(UserManager.currentUser.getId());
		// chatFrame2 = UImanager.chatFrames.get(roomKey);
		// chatFrame2.insertImg(filePath, System.currentTimeMillis(), true);

	}

	public void setBg(byte[] imageData) {
		screen.setIcon(new ImageIcon(imageData));
	}

	/** ��ǰ��Ļ **/
	public void computerScreen() {
		try {
			// ��ȡĬ�Ϲ��߰�
			Toolkit toolKit = Toolkit.getDefaultToolkit();
			// ��ȡ��Ļ�ֱ���
			Dimension screenSize = toolKit.getScreenSize();
			screenRect = new Rectangle(screenSize);
			// ��Ļ��ͼ
			BufferedImage bi_screen = robot.createScreenCapture(screenRect);
			// ��
			ByteArrayOutputStream byte_out = new ByteArrayOutputStream();
			// ������ʽ ��һ��ͼ��д�� byte_out
			ImageIO.write(bi_screen, "JPG", byte_out);
			byte[] bs = byte_out.toByteArray();
			// ��ʾ��Ļ
			setSize(screenSize);
			setLocationRelativeTo(null);
			screen.setBounds(0, 0, screenSize.width, screenSize.height);
			setBg(bs);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/** ��ͼ **/
	public void capture() {
		try {
			int x = x1;
			int y = y1;
			int width = x2 - x1;
			int height = y2 - y1;
			if (width < 0 || height < 0)
				return;
			Rectangle rectangle = new Rectangle(x, y, width, height);
			Rectangle captureRect = screenRect.intersection(rectangle);// �ص�����Ϊ������

			// ��ͼ
			BufferedImage bi_capture = robot.createScreenCapture(captureRect);
			System.out.println(bi_capture.toString());
			// ��
			ByteArrayOutputStream byte_out = new ByteArrayOutputStream();
			// ������ʽ ��һ��ͼ��д�� byte_out
			ImageIO.write(bi_capture, "JPG", byte_out);
			bs = byte_out.toByteArray();
			// ��ʾ��Ļ
			// setBg(bs);
			screen.setIcon(new ImageIcon(bs));
			screen.setBounds(x, y, width, height);
			screen.setSize(width, height);
			// ((JLabel) getContentPane()).setOpaque(false);
			System.out.println("captureRect" + captureRect.toString());
			// setSize(width, height);
			screen.add(sendCapture);
			sendCapture.setBounds(screen.getWidth() - 120, screen.getHeight() - 70, 100, 50);
			//

			// filePath = FileTool.fileSave(bs, "receive/", "cut.jpg");

			// this.setSize(width, height);
			// screen.setPreferredSize(new Dimension(width, height));
			// setSize(d);
			// ����Э��
			// DataBean dataBean = new DataBean(UserManager.currentUser.getId(),
			// friendId);
			// dataBean.setData(bs);
			// dataBean.setTime(System.currentTimeMillis());
			// CommonTransferBean bean = new
			// CommonTransferBean(Protocol.P_F_SendImg, dataBean, "cut.jpg");
			// SocketSingleton.getInstance().sendObj(bean);

			sendCapture.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					try {
						DataBean dataBean = new DataBean(UserManager.currentUser.getId(), friendId);
						dataBean.setData(bs);
						long time = System.currentTimeMillis();
						dataBean.setTime(time);
						//

						//
						dispose();
						CommonTransferBean bean = new CommonTransferBean(Protocol.P_F_SendImg, dataBean, "cut.jpg");
						// System.out.println("���ͽ�ͼ" + bean);
						SocketSingleton.getInstance().sendObj(bean);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void initDate() {
		sendCapture = new JButton("����");
	}

	@Override
	public void initView() {
		screen = new JLabel();
		add(screen);

	}

	private int x1, y1;// ��ʼ��
	private int x2, y2;// �յ�
	private Rectangle screenRect;
	private byte[] bs;
	private ChatFrame chatFrame2;
	private String filePath;

	public byte[] getBs() {
		return bs;
	}

	@Override
	public void initListen() {
		screen.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				x1 = e.getX();
				y1 = e.getY();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				x2 = e.getX();
				y2 = e.getY();
				capture();
			}
		});
	}

}
