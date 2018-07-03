package bean;

import java.io.Serializable;

public class CommonTransferBean implements Serializable {
	String cmd;
	Object data;
	String msg;

	public CommonTransferBean(String cmd, Object data) {
		this.cmd = cmd;
		this.data = data;
	}

	public CommonTransferBean(String cmd, Object data, String msg) {
		this.cmd = cmd;
		this.data = data;
		this.msg = msg;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "CommonBean [cmd=" + cmd + ", data=" + data + ", msg=" + msg + "]";
	}

}
