package singleton;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketSingleton {
	Socket socket;
	private static ObjectOutputStream oos;
	private static ObjectInputStream ois;

	private SocketSingleton() {

		try {
			socket = new Socket("192.168.1.104", 9090);// "localhost"
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			try {
				oos = new ObjectOutputStream(out);
				ois = new ObjectInputStream(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	static SocketSingleton instance;

	public static SocketSingleton getInstance() {
		if (instance == null) {
			synchronized (SocketSingleton.class) {
				if (instance == null) {
					instance = new SocketSingleton();
				}
			}
		}
		return instance;
	}

	public void sendObj(Object obj) throws IOException {
		oos.writeObject(obj);
	}

	public Object readObj() throws Exception {
		return ois.readObject();
	}
}
