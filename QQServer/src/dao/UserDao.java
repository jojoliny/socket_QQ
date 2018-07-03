package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import bean.User;
import util.JdbcUtil;
import utils.StringUtil;

public class UserDao {
	private PreparedStatement stmt;// 预编译的 SQL语句的对象
	private ResultSet resultSet;
	private Connection conn;

	public User selectUser(String userId, String pwd) {
		conn = JdbcUtil.getConnection();
		/** 第三步：准备sql **/
		String sql = "select * from qq_user where id = ? and pwd=?";

		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, userId);
			stmt.setString(2, pwd);
			resultSet = stmt.executeQuery();// 执行sql的一定是statement
			/** 第四步：返回处理结果 **/
			User user = null;
			while (resultSet.next()) {// 光标下一行
				String id = resultSet.getString("id");
				String name = resultSet.getString("username");
				String pass = resultSet.getString("pwd");
				String sign = resultSet.getString("sign");
				Date birthday = resultSet.getDate("birthday");
				user = new User(id, name, pass, sign, birthday);
			}
			return user;

		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			JdbcUtil.closeConn(resultSet, stmt, conn);
		}
		return null;

	}

	public List<User> selectFriends(String userId) {
		System.out.println("userId" + userId);
		List<User> friends = new ArrayList<User>();
		conn = JdbcUtil.getConnection();
		/** 第三步：准备sql **/
		String sql = "select id,username,sign,birthday from qq_user where id in (select destQq from qq_friends where srcQq = ?)";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, userId);
			resultSet = stmt.executeQuery();// 执行sql的一定是statement
			/** 第四步：返回处理结果 **/
			User user = null;
			while (resultSet.next()) {// 光标下一行
				String id = resultSet.getString("id");
				String name = resultSet.getString("username");
				String sign = resultSet.getString("sign");
				Date birthday = resultSet.getDate("birthday");
				user = new User(id, name, sign, birthday);
				friends.add(user);
			}
			System.out.println("friend" + friends.size());
			return friends;

		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			JdbcUtil.closeConn(resultSet, stmt, conn);
		}
		return null;

	}

	public List<User> selectSearchFriends(Object nickName) {
		List<User> friends = new ArrayList<User>();
		conn = JdbcUtil.getConnection();
		/** 第三步：准备sql **/
		String sql = "select id,username,sign,birthday from qq_user where username like ?";
		boolean isEmpty = StringUtil.isEmpty(nickName.toString());
		if (isEmpty) {
			JOptionPane.showMessageDialog(null, "不能为空");
			return null;
		} else {
			nickName = "%" + nickName + "%";
		}
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, (String) nickName);
			resultSet = stmt.executeQuery();// 执行sql的一定是statement
			/** 第四步：返回处理结果 **/
			User user = null;
			while (resultSet.next()) {// 光标下一行
				String id = resultSet.getString("id");
				String name = resultSet.getString("username");
				String sign = resultSet.getString("sign");
				Date birthday = resultSet.getDate("birthday");
				user = new User(id, name, sign, birthday);
				friends.add(user);
			}
			return friends;

		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			JdbcUtil.closeConn(resultSet, stmt, conn);
		}
		return null;
	}

	public boolean insertFriend(String srcId, String destId) {
		conn = JdbcUtil.getConnection();
		String sql = "insert into qq_friends(srcQq,destQq) values (?,?)";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, srcId);
			stmt.setString(2, destId);
			int result = stmt.executeUpdate();
			System.out.println(result);
			if (result == -1) {
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.closeConn(resultSet, stmt, conn);
		}
		return false;
	}

	// 是否已经是好友
	public boolean isAdd(String srcId, String destId) {
		conn = JdbcUtil.getConnection();
		String sql = "select id from qq_user where id in (select destQq  from qq_friends where srcQq = ? and destQq=?)";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, srcId);
			stmt.setString(2, destId);
			ResultSet result = stmt.executeQuery();
			System.out.println("result  " + result);
			if (result.next())
				return true;
			return false;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JdbcUtil.closeConn(resultSet, stmt, conn);
		}
		return false;
	}

}
