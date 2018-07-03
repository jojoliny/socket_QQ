package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import bean.CommonTransferBean;
import bean.DataBean;
import bean.Protocol;
import bean.User;
import manager.UserManager;
import singleton.SocketSingleton;

public class SearchFriendsFrame extends ParentFrame {

	public static List<User> saerchFriends;
	private JButton btn_search;
	private JTextField txt_search;
	private TableModel tm;
	private JTable jTable;
	private JButton btn_add;

	public SearchFriendsFrame() {
		super("查找好友", 600, 400, null);
	}

	@Override
	public void initDate() {
		saerchFriends = new ArrayList<User>();
	}

	@Override
	public void initView() {
		setFrameMove();
		setClose();
		setMinimize();
		drawBg("img/bg_search.jpg");
		txt_search = new JTextField();
		txt_search.setBounds(90, 40, 300, 30);
		add(txt_search);
		btn_search = new JButton("搜索");
		btn_search.setBounds(400, 40, 80, 30);
		add(btn_search);
		tm = new MyTableModle();
		jTable = new JTable(tm);
		JScrollPane scrollPane = new JScrollPane(jTable);
		scrollPane.setBounds(0, 90, getWidth(), 280);
		add(scrollPane);
		btn_add = new JButton("添加好友");
		btn_add.setBounds(getWidth() - 200, getHeight() - 30, 150, 30);
		add(btn_add);

	}

	@Override
	public void initListen() {
		/** 搜索好友 **/
		btn_search.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CommonTransferBean bean = new CommonTransferBean(Protocol.P_Search_Friends, txt_search.getText());
				try {
					SocketSingleton.getInstance().sendObj(bean);
					saerchFriends = UserManager.searchUserList;
					jTable.updateUI();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		/** 发送添加好友请求 **/
		btn_add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String destId = (String) tm.getValueAt(jTable.getSelectedRow(), 0);
				DataBean dataBean = new DataBean(UserManager.currentUser.getId(), destId);
				System.out.println("好友ID" + destId);
				try {
					CommonTransferBean bean = new CommonTransferBean(Protocol.P_Friends_Add, dataBean);
					SocketSingleton.getInstance().sendObj(bean);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

	}

	// public static void main(String[] args) {
	// new SearchFriendsFrame();
	// }

	class MyTableModle implements TableModel {

		@Override
		public int getRowCount() {
			return saerchFriends.size();
		}

		@Override
		public int getColumnCount() {
			return 4;
		}

		@Override
		public String getColumnName(int columnIndex) {
			if (columnIndex == 0) {
				return "账户";
			} else if (columnIndex == 1) {
				return "昵称";
			} else if (columnIndex == 2) {
				return "生日";
			} else if (columnIndex == 3) {
				return "签名";
			}
			return null;

		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return String.class;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			return false;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			User user = saerchFriends.get(rowIndex);
			if (columnIndex == 0) {
				return user.getId();
			} else if (columnIndex == 1) {
				return user.getUsername();
			} else if (columnIndex == 2) {
				return user.getBirthday();
			} else if (columnIndex == 3) {
				return user.getSign();
			}
			return null;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

		}

		@Override
		public void addTableModelListener(TableModelListener l) {

		}

		@Override
		public void removeTableModelListener(TableModelListener l) {

		}

		private void mian() {
			new SearchFriendsFrame();
		}
	}
}
