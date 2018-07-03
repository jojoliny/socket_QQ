package ui;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import bean.User;
import manager.UserManager;

public class MyTableModel implements TableModel {

	@Override
	public int getRowCount() {
		return UserManager.userList.size();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if (columnIndex == 0) {
			return "qqºÅ";
		}
		if (columnIndex == 1) {
			return "êÇ³Æ";
		}
		if (columnIndex == 2) {
			return "×´Ì¬";
		}
		return "Ã»ÓÐ";
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
		User user = UserManager.userList.get(rowIndex);
		if (columnIndex == 0) {
			return user.getId();
		}
		if (columnIndex == 1) {
			return user.getUsername();
		}
		if (columnIndex == 2) {
			return user.getState();
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

}
