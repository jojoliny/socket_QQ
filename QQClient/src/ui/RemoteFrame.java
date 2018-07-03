package ui;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import bean.CommonTransferBean;
import bean.DataBean;
import bean.Protocol;
import manager.UserManager;
import singleton.SocketSingleton;
import utils.ImageUtil;

public class RemoteFrame extends ParentFrame {

	String userId;
	private static final long serialVersionUID = 1L;

	public RemoteFrame(int width, int height, String userId) {
		super(width, height);
		this.userId = userId;
	}

	@Override
	public void initDate() {

	}

	@Override
	public void initView() {
		lbBg = new JLabel();
		lbBg.setIcon(ImageUtil.getIcon("img/粉红玫瑰.png"));
		lbBg.setBounds(0, 0, 600, 600);
		this.add(lbBg);
	}

	@Override
	public void initListen() {
		lbBg.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				Integer button = e.getButton();
				DataBean dataBean = new DataBean(UserManager.currentUser.getId(), userId);
				dataBean.setData(button);
				CommonTransferBean bean = new CommonTransferBean(Protocol.P_F_Remote_Mouse_Released, dataBean);
				try {
					System.out.println("发送RemoteFrame->Mouse_Released");
					SocketSingleton.getInstance().sendObj(bean);

				} catch (IOException e1) {

				}

			}

			@Override
			public void mousePressed(MouseEvent e) {
				Integer button = e.getButton();
				// System.out.println(button);
				DataBean dataBean = new DataBean(UserManager.currentUser.getId(), userId);
				dataBean.setData(button);
				CommonTransferBean bean = new CommonTransferBean(Protocol.P_F_Remote_Mouse_Pressed, dataBean);
				try {
					System.out.println("发送RemoteFrame->Mouse_Pressed");
					SocketSingleton.getInstance().sendObj(bean);
				} catch (IOException e1) {
					// e1.printStackTrace();
					// System.out.println("mousePressed");
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				Integer button = e.getButton();
				DataBean dataBean = new DataBean(UserManager.currentUser.getId(), userId);
				dataBean.setData(button);
				CommonTransferBean bean = new CommonTransferBean(Protocol.P_F_Remote_Mouse_Exited, dataBean);
				try {
					SocketSingleton.getInstance().sendObj(bean);
				} catch (IOException e1) {
					// e1.printStackTrace();
					// System.out.println("mouseExited");
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				Integer button = e.getButton();
				DataBean dataBean = new DataBean(UserManager.currentUser.getId(), userId);
				dataBean.setData(button);
				CommonTransferBean bean = new CommonTransferBean(Protocol.P_F_Remote_Mouse_Entered, dataBean);
				try {
					SocketSingleton.getInstance().sendObj(bean);
				} catch (IOException e1) {
					// e1.printStackTrace();
					// System.out.println("mouseEntered");
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				Integer button = e.getButton();
				Integer clickCount = e.getClickCount();
				DataBean dataBean = new DataBean(UserManager.currentUser.getId(), userId);
				dataBean.setData(new Integer[] { button, clickCount });
				CommonTransferBean bean = new CommonTransferBean(Protocol.P_F_Remote_Mouse_Clicked, dataBean);
				try {
					SocketSingleton.getInstance().sendObj(bean);
				} catch (IOException e1) {
					// e1.printStackTrace();
					// System.out.println("RemoteFrame->mouseClicked有问题");
				}
			}
		});

		lbBg.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				synchronized (this) {
					try {
						Integer x = e.getX();
						Integer y = e.getY();
						DataBean dataBean = new DataBean(UserManager.currentUser.getId(), userId);
						dataBean.setData(new Integer[] { x, y });
						CommonTransferBean bean = new CommonTransferBean(Protocol.P_F_Remote_Mouse_Moved, dataBean);

						SocketSingleton.getInstance().sendObj(bean);
					} catch (IOException e1) {
						// e1.printStackTrace();
						System.out.println("MouseMoved问题");
					}
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				synchronized (this) {
					DataBean dataBean = new DataBean(UserManager.currentUser.getId(), userId);
					CommonTransferBean bean = new CommonTransferBean(Protocol.P_F_Remote_Mouse_Dragged, dataBean);
					try {
						SocketSingleton.getInstance().sendObj(bean);
					} catch (IOException e1) {
						// e1.printStackTrace();
						System.out.println("mouseDragged问题");
					}
				}
			}
		});

		lbBg.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				synchronized (this) {
					Integer wheelRotation = e.getWheelRotation();
					DataBean dataBean = new DataBean(UserManager.currentUser.getId(), userId);
					dataBean.setData(wheelRotation);
					CommonTransferBean bean = new CommonTransferBean(Protocol.P_F_Remote_Mouse_Wheel, dataBean);
					try {
						SocketSingleton.getInstance().sendObj(bean);
					} catch (IOException e1) {
						// e1.printStackTrace();
						System.out.println("mouseWheelMoved问题");
					}
				}
			}
		});

		this.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				synchronized (this) {

					try {
						Integer keyCode = e.getKeyCode();
						// 告诉2移动位置
						// 1. 封装协议
						DataBean dataBean = new DataBean(UserManager.currentUser.getId(), userId);
						dataBean.setData(keyCode);

						CommonTransferBean bean = new CommonTransferBean(Protocol.P_Remote_Key_Released, dataBean);

						SocketSingleton.getInstance().sendObj(bean);
					} catch (Exception e1) {
						// e1.printStackTrace();
						System.out.println("发送RemoteFrame->keyReleased出错");
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				synchronized (this) {

					try {
						Integer keyCode = e.getKeyCode();
						// 告诉2移动位置
						// 1. 封装协议
						DataBean dataBean = new DataBean(UserManager.currentUser.getId(), userId);
						dataBean.setData(keyCode);

						CommonTransferBean bean = new CommonTransferBean(Protocol.P_Remote_Key_Pressed, dataBean);

						SocketSingleton.getInstance().sendObj(bean);
					} catch (Exception e1) {
						// e1.printStackTrace();
						System.out.println("RemoteFrame->keyPressed发送出错");
					}
				}
			}

		});

	}

	@Override
	public String toString() {
		return "RemoteFrame [userId=" + userId + ", lbBg=" + lbBg + "]";
	}

	private JLabel lbBg;

	/** 同步屏幕大小 **/
	public void setScreenSize(Dimension dimension) {
		setSize(dimension);
		setLocationRelativeTo(null);
		// 修改lbBg大小
		lbBg.setBounds(0, 0, (int) dimension.getWidth(), (int) dimension.getHeight());
		this.repaint();
	}

	/** 设置大小 **/
	public void setScreenSize(int width, int height) {
		setSize(width, height);
		setLocationRelativeTo(null);
		lbBg.setBounds(0, 0, width, height);
		this.repaint();
	}

	/** 同步画面 **/
	public void setBg(Icon icon) {
		lbBg.setIcon(icon);
		this.repaint();
	}

	public void setBg(byte[] imageData) {
		lbBg.setIcon(new ImageIcon(imageData));
		this.repaint();
	}

}
