package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtil {
	static {
		/** 第一步:注册驱动 **/
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("驱动加载成功");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		/** 第一步：注册驱动 **/
		// 加载Driver类 自动调用
		// try {
		// Class.forName("com.mysql.jdbc.Driver");
		//
		// } catch (ClassNotFoundException e) {
		// e.printStackTrace();
		// }
		/** 第二步：连接 **/
		// "协议://主机名://端口号/资源"
		String ur1 = "jdbc:mysql://localhost:3306/qq_1701";
		// String ur1 = "192.168.1.102";错误
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
