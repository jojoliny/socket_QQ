package ui;

import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.ImageButton;
import utils.ImageUtil;
import utils.StringUtil;

public abstract class ParentFrame extends JFrame {

	public ParentFrame(int width, int height) {
		this(null, width, height, null);

	}

	public ParentFrame(String title, int x, int y, LayoutManager manager) {
		this(title, x, y, manager, true);// 调用自己的构造
	}

	public ParentFrame(String title, int x, int y, LayoutManager manager, boolean isUndecorated) {
		setTitle(title);
		setUndecorated(isUndecorated);
		setSize(x, y);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(3);
		setLayout(manager);
		initDate();
		initView();
		setVisible(true);
		initListen();
	}

	public abstract void initDate();

	public abstract void initView();

	// 添加组件
	public abstract void initListen();

	public void drawBg(String path) {

		/** 检测参数 **/
		StringUtil.isEmpty(path);
		/** contentPane 透明 **/
		((JPanel) (getContentPane())).setOpaque(false);// 是否不透明

		Icon img_bg = ImageUtil.getIcon(path);
		JLabel bg = new JLabel(img_bg);
		bg.setBounds(0, 0, getWidth(), getHeight());
		getLayeredPane().add(bg, new Integer(Integer.MIN_VALUE));
	}

	int x1, y1;

	public void setFrameMove() {
		this.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				super.mousePressed(e);
				x1 = e.getX();
				y1 = e.getY();
			}
		});

		addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				super.mouseDragged(e);
				int x = getLocation().x - x1 + e.getX();
				int y = getLocation().y - y1 + e.getY();
				setLocation(x, y);
			}
		});
	}

	public void setClose(ImageButton btn) {
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	public void setMinimize(ImageButton btn) {
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setExtendedState(Frame.ICONIFIED);
			}
		});
	}

	public void setClose() {
		ImageButton btnClose = new ImageButton(getWidth() - 18, 2, 16, 16, "img/r1.png", "img/r2.png", "img/r1.png");
		add(btnClose);
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	public void setMinimize() {
		ImageButton btnMinimize = new ImageButton(getWidth() - 18 * 2, 2, 16, 16, "img/y1.png", "img/y2.png",
				"img/y1.png");
		add(btnMinimize);
		btnMinimize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setExtendedState(Frame.ICONIFIED);
			}
		});
	}
}