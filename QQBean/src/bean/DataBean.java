package bean;

import java.io.Serializable;

public class DataBean implements Serializable {
	String srcId;

	@Override
	public String toString() {
		return "DataBean [srcId=" + srcId + ", destId=" + destId + ", data=" + data + ", time=" + time + "]";
	}

	String destId;
	Object data;
	long time;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	// public DataBean(String srcId, String destId, Object data) {
	// super();
	// this.srcId = srcId;
	// this.destId = destId;
	// this.data = data;
	// }

	public DataBean(String srcId, String destId) {
		this.srcId = srcId;
		this.destId = destId;
	}

	// public DataBean(String srcId, String destId, Object data, long time) {
	// super();
	// this.srcId = srcId;
	// this.destId = destId;
	// this.data = data;
	// this.time = time;
	// }

	public String getSrcId() {
		return srcId;
	}

	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}

	public String getDestId() {
		return destId;
	}

	public void setDestId(String destId) {
		this.destId = destId;
	}

}
