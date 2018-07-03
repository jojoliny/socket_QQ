package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;

import server.ServerSocketThread;

public class MainFame extends ParentFrame {

	public MainFame() {
		super("服务器", 521, 600, new BorderLayout(), false);

	}

	@Override
	public void initDate() {

	}

	JButton btn;
	public static JTable jTable;
	String open = "开启中(点击可以关闭)";
	String close = "关闭中(点击可以开启)";
	JTextField inputPort;

	@Override
	public void initView() {
		JPanel panelN = new JPanel();

		JLabel port = new JLabel("输入端口号");

		inputPort = new JTextField();
		inputPort.setPreferredSize(new Dimension(200, 25));
		inputPort.setText("9090");
		btn = new JButton("关闭中(点击可以开启)");

		panelN.add(port);
		panelN.add(inputPort);
		panelN.add(btn);

		add(panelN, BorderLayout.NORTH);

		/** 中间的 **/
		TableModel tm = new MyTableModel();
		jTable = new JTable(tm);
		JScrollPane jScrollPane = new JScrollPane(jTable);
		add(jScrollPane, BorderLayout.CENTER);

	}

	boolean isStart = false;
	private ServerSocket serverSocket;

	@Override
	public void initListen() {
		btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isStart) {
					btn.setText(open);
					isStart = true;
					System.out.println("服务器开启");
					String port = inputPort.getText();
					new Thread() {
						public void run() {

							try {
								serverSocket = new ServerSocket(Integer.parseInt(port));
								while (true) {
									Socket socket = serverSocket.accept();
									new ServerSocketThread(socket);
								}
							} catch (NumberFormatException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							} finally {
								try {
									serverSocket.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}

						}

					}.start();
				} else {
					btn.setText(close);
					isStart = false;
				}
				// isStart = !isStart;
				// User user1 = new User("1234", "ni", "在线");
				// UserManager.userList.add(user1);
				// jTable.updateUI();
			}
		});
	}

	public static void main(String[] args) {
		new MainFame();
	}
}
