package utils;

import javax.swing.Icon;
import javax.swing.JButton;

public class ImageButton extends JButton {

	public ImageButton(int x, int y, int width, int height, String defaultIconPath, String rollOverIconPath,
			String PressedIconPath) {
		/** ȥ���߿� */
		// setBounds(x, y, width, height);
		this.setBorder(null);
		Icon defaultIcon = ImageUtil.getIcon(defaultIconPath);
		Icon rollOverIcon = ImageUtil.getIcon(rollOverIconPath);
		Icon PressedIcon = ImageUtil.getIcon(PressedIconPath);
		/** ��ʼʱ **/
		this.setIcon(defaultIcon);
		/** ����������ʱ **/
		this.setRolloverIcon(rollOverIcon);
		/** ����ʱ **/
		this.setPressedIcon(PressedIcon);
		this.setBounds(x, y, width, height);

		setBorder(null);// �ޱ߿�
		setBorderPainted(false);// �����߿�
		setContentAreaFilled(false);
		setFocusPainted(false);

	}

	public ImageButton(int x, int y, int width, int height) {
		this(x, y, width, height, null, null, null);
	}

	public ImageButton(int x, int y, int width, int height, String string) {
		this(x, y, width, height, string, string, string);
	}
}
