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
		super("������", 521, 600, new BorderLayout(), false);

	}

	@Override
	public void initDate() {

	}

	JButton btn;
	public static JTable jTable;
	String open = "������(������Թر�)";
	String close = "�ر���(������Կ���)";
	JTextField inputPort;

	@Override
	public void initView() {
		JPanel panelN = new JPanel();

		JLabel port = new JLabel("����˿ں�");

		inputPort = new JTextField();
		inputPort.setPreferredSize(new Dimension(200, 25));
		inputPort.setText("9090");
		btn = new JButton("�ر���(������Կ���)");

		panelN.add(port);
		panelN.add(inputPort);
		panelN.add(btn);

		add(panelN, BorderLayout.NORTH);

		/** �м�� **/
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
					System.out.println("����������");
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
				// User user1 = new User("1234", "ni", "����");
				// UserManager.userList.add(user1);
				// jTable.updateUI();
			}
		});
	}

	public static void main(String[] args) {
		new MainFame();
	}
}
