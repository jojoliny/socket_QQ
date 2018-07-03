package server;

import java.util.List;

import bean.User;
import dao.UserDao;

public class UserService {
	UserDao userDao = new UserDao();

	public User login(String id, String pwd) {
		return userDao.selectUser(id, pwd);
	}

	public List<User> selectFriends(String userId) {
		return userDao.selectFriends(userId);
	}

	public List<User> selsecSearchFriends(Object nickName) {
		return userDao.selectSearchFriends(nickName);
	}

	public boolean addFriend(String srcId, String destId) {
		return userDao.insertFriend(srcId, destId);
	}

	public boolean isAdd(String srcId, String destId) {
		return userDao.isAdd(srcId, destId);
	}

}
