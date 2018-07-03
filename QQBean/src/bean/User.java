package bean;

import java.io.Serializable;
import java.sql.Date;

public class User implements Serializable {

	public User(String id, String username, String sign, Date birthday) {
		super();
		this.id = id;
		this.username = username;
		this.sign = sign;
		this.birthday = birthday;
	}

	String id;
	String username;
	String pwd;
	String sign;
	Date birthday;
	String State;

	public User(String id, String username, String pwd, String sign, Date birthday) {
		super();
		this.id = id;
		this.username = username;
		this.pwd = pwd;
		this.sign = sign;
		this.birthday = birthday;
	}

	public User(String id, String username, String state) {
		super();
		this.id = id;
		this.username = username;
		State = state;
	}

	public User() {
		super();
		this.id = id;
		this.username = username;
		this.pwd = pwd;
		this.sign = sign;
		this.birthday = birthday;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", pwd=" + pwd + ", sign=" + sign + ", birthday="
				+ birthday + "]";
	}

}
