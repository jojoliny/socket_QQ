package utils;

import javax.swing.Icon;
import javax.swing.JButton;

public class ImageButton extends JButton {

	public ImageButton(int x, int y, int width, int height, String defaultIconPath, String rollOverIconPath,
			String PressedIconPath) {
		/** 去除边框 */
		// setBounds(x, y, width, height);
		this.setBorder(null);
		Icon defaultIcon = ImageUtil.getIcon(defaultIconPath);
		Icon rollOverIcon = ImageUtil.getIcon(rollOverIconPath);
		Icon PressedIcon = ImageUtil.getIcon(PressedIconPath);
		/** 初始时 **/
		this.setIcon(defaultIcon);
		/** 滚动到上面时 **/
		this.setRolloverIcon(rollOverIcon);
		/** 按下时 **/
		this.setPressedIcon(PressedIcon);
		this.setBounds(x, y, width, height);

		setBorder(null);// 无边框
		setBorderPainted(false);// 不画边框
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
