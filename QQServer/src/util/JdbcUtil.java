package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtil {
	static {
		/** ��һ��:ע������ **/
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("�������سɹ�");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		/** ��һ����ע������ **/
		// ����Driver�� �Զ�����
		// try {
		// Class.forName("com.mysql.jdbc.Driver");
		//
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// }
		/** �ڶ��������� **/
		// "Э��://������://�˿ں�/��Դ"
		String ur1 = "jdbc:mysql://localhost:3306/qq_1701";
		// String ur1 = "192.168.1.102";����
		String user = "root";
		String p = "123";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(ur1, user, p);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void closeConn(ResultSet resultSet, Statement statement, Connection connnection) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			resultSet = null;
		}

		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			statement = null;
		}

		try {
			if (connnection != null) {
				connnection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			connnection = null;
		}
	}

}
