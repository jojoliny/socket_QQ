package singleton;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFileChooser;

public class SocketSingleton_back {
	Socket socket;
	private static InputStream in;
	private static OutputStream out;
	private static DataOutputStream dos;
	private static DataInputStream dis;
	private static FileOutputStream fos;
	private static FileInputStream fis;

	private SocketSingleton_back() {

		try {
			socket = new Socket("localhost", 9090);
			in = socket.getInputStream();
			out = socket.getOutputStream();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	static SocketSingleton_back instance;

	public static SocketSingleton_back getInstance() {
		if (instance == null) {
			synchronized (SocketSingleton_back.class) {
				if (instance == null) {
					instance = new SocketSingleton_back();
					dos = new DataOutputStream(out);

				}
			}
		}
		return instance;
	}

	public void send(String text) {
		try {
			out.write(text.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendBytes(byte[] b) {
		try {
			dos = getDataOutputStream();
			dos.write(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String receive(int length) {
		byte[] b = new byte[length];
		try {
			int len = in.read(b);
			return new String(b, 0, len);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public DataOutputStream getDataOutputStream() {
		return dos;
	}

	public DataInputStream getDataInputStream() {
		return dis;
	}

	@Deprecated
	public void sendFile() {
		dos = SocketSingleton_back.getInstance().getDataOutputStream();// socket
		/** 获取文件 **/
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.showOpenDialog(null);
		File file = jFileChooser.getSelectedFile();
		// 长度 名字
		int length = (int) file.length();
		String fileName = file.getName();
		int fileNameLen = fileName.length();

		/** 发送文件长度，名字 **/
		try {
			dos.writeInt(length);
			dos.writeInt(fileNameLen);
			dos.writeBytes(fileName);
			//
			// byte[] b = new byte[length];
			// dos.write(b, 0, length);//
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			/** 客户端:本地流 **/
			fis = new FileInputStream(file);
			byte[] bs = new byte[length];
			fis.read(bs);
			/** 客户端:socket流 **/
			SocketSingleton_back.getInstance().sendBytes(bs);
			fis.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		System.out.println("发送成功");
	}

	@Deprecated
	public void receiveFile() {
		byte[] b = new byte[1024];
		try {
			/** 获取命令 **/
			int len = in.read(b);
			String command = new String(b, 0, len);
			if (command.equals("login")) {
				String useAndpwd = new String(b, 0, len);
				System.out.println("server: " + useAndpwd);

			}

			/** 获取文件长度，名字 **/
			int length = dis.readInt();
			int fileNameLength = dis.readInt();
			byte[] b1 = new byte[fileNameLength];
			byte[] b2 = new byte[length];
			dis.readFully(b1);// 文件名
			dis.readFully(b2);// 文件
			// System.out.println("-------------------------" + new String(b1));
			fos = new FileOutputStream("receive/" + new String(b1));
			dos = new DataOutputStream(fos);
			dos.write(b2);
			// fos.write(b2);
			fos.close();
			System.out.println("发送成功");
		} catch (IOException e) {
			System.out.println("客户端断开");
			e.printStackTrace();
		}
	}
}
