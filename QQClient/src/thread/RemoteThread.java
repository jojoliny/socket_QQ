package thread;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import bean.CommonTransferBean;
import bean.DataBean;
import bean.Protocol;
import manager.UserManager;
import singleton.SocketSingleton;
import utils.ThreadUtil;

/**
 * @author 林大王
 * @category 发送截图
 */
public class RemoteThread implements Runnable {

	private Rectangle rectangle;
	private Robot robot;

	@Override
	public void run() {
		while (true) {
			ThreadUtil.sleep(10);
			capture();
		}
	}

	String friendId = null;

	public RemoteThread(String friendId) {
		this.friendId = friendId;
		try {
			// 拿到屏幕截图
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Dimension screenSize = toolkit.getScreenSize();

			System.out.println(screenSize.getWidth());
			System.out.println(screenSize.getHeight());
			// 发送
			DataBean dataBean = new DataBean(UserManager.currentUser.getId(), friendId);
			dataBean.setData(screenSize);
			CommonTransferBean bean = new CommonTransferBean(Protocol.P_F_Remote_ScreenSize, dataBean);
			SocketSingleton.getInstance().sendObj(bean);

			robot = new Robot();
			rectangle = new Rectangle(screenSize);
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("RemoteThread有问题");
		}

		new Thread(this).start();
	}

	/** 截图 **/
	public void capture() {
		FileOutputStream fos = null;
		try {
			// 截图
			BufferedImage bf_img = robot.createScreenCapture(rectangle);
			// 字节流
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(bf_img, "JPG", out);
			byte[] b = out.toByteArray();// 以byte数组的形式返回此输出流的当前大小
			// fos = new FileOutputStream("img/xxx.jpg");
			// fos.write(b);
			DataBean dataBean = new DataBean(UserManager.currentUser.getId(), friendId);
			dataBean.setData(b);
			CommonTransferBean bean = new CommonTransferBean(Protocol.P_F_Remote_Screen, dataBean);
			SocketSingleton.getInstance().sendObj(bean);
		} catch (Exception e) {
			System.out.println("---------Robot-----------");
			e.printStackTrace();
		}
	}

	public void answerOperation(CommonTransferBean bean) {
		String cmd = bean.getCmd();
		DataBean dataBean = (DataBean) bean.getData();
		if (Protocol.P_F_Remote_Mouse_Moved.equals(cmd)) {
			// System.out.println("接收到RemoteThread->Mouse_Moved");
			Integer[] isMoved = (Integer[]) dataBean.getData();
			System.out.println("Mouse_Moved" + isMoved[0] + "  " + isMoved[1]);
			robot.mouseMove(isMoved[0], isMoved[1]);
			System.out.println("接收到RemoteThread->Mouse_Moved");
		} else if (Protocol.P_F_Remote_Mouse_Released.equals(cmd)) {
			int button = (int) dataBean.getData();
			if (button == 1)
				robot.mouseRelease(InputEvent.BUTTON1_MASK);
			if (button == 3)
				robot.mouseRelease(InputEvent.BUTTON3_MASK);
			System.out.println("接收到RemoteThread->Mouse_Released");
		} else if (Protocol.P_F_Remote_Mouse_Pressed.equals(cmd)) {
			int button = (int) dataBean.getData();
			if (button == 1)
				robot.mousePress(InputEvent.BUTTON1_MASK);
			if (button == 3)
				robot.mousePress(InputEvent.BUTTON3_MASK);
			System.out.println("接收到RemoteThread->Mouse_Press");
		} else if (Protocol.P_F_Remote_Mouse_Exited.equals(cmd)) {

		} else if (Protocol.P_F_Remote_Mouse_Entered.equals(cmd)) {
		} else if (Protocol.P_F_Remote_Mouse_Dragged.equals(cmd)) {
		} else if (Protocol.P_F_Remote_Mouse_Clicked.equals(cmd)) {
			Integer[] click = (Integer[]) dataBean.getData();
			Integer button = click[0];
			Integer clickCount = click[1];
			if (button == 1) {
				for (int i = 0; i < clickCount; i++)
					robot.mousePress(InputEvent.BUTTON1_MASK);
			}
			if (button == 3) {
				for (int i = 0; i < clickCount; i++)
					robot.mousePress(InputEvent.BUTTON3_MASK);

			}
			System.out.println("接收到RemoteThread->Mouse_Clicked");
		} else if (Protocol.P_F_Remote_Mouse_Wheel.equals(cmd)) {
			int wheelAmt = (int) dataBean.getData();
			robot.mouseWheel(wheelAmt);
			System.out.println("接收到RemoteThread->Mouse_Wheel");
		} else if (Protocol.P_Remote_Key_Released.equals(cmd)) {
			int keycode = (int) dataBean.getData();
			robot.keyRelease(keycode);
			System.out.println("接收到RemoteThread->Key_Released");
		} else if (Protocol.P_Remote_Key_Pressed.equals(cmd)) {
			int keycode = (int) dataBean.getData();
			robot.keyPress(keycode);
			System.out.println("接收到RemoteThread->Key_Pressed");

		}

	}
}
