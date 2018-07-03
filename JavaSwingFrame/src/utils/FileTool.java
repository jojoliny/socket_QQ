package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileTool {

	/** 文件->字节 **/
	public static byte[] fileToBytes(File file) {
		FileInputStream fis = null;
		int length = (int) file.length();// 文件长度
		byte[] bs = new byte[length];

		try {
			fis = new FileInputStream(file);
			fis.read(bs, 0, length);// 一次性读入
			for (int i = 0; i < length; i++) {
				System.out.println(bs[i]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(fis, null);
		}

		return bs;
	}

	/** 利用字节偷偷保存文件 **/
	public static String fileSave(byte[] bs, String path, String fileName) {

		FileOutputStream out = null;
		String dest = null;
		try {
			File parent = new File(path);
			if (!parent.exists()) {
				parent.mkdirs();
			}
			dest = path + fileName; // 目标绝对路径
			File file = new File(dest);
			out = new FileOutputStream(file);
			out.write(bs);
			return dest;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			close(null, out);
		}

	}

	/** 测试 **/
	// public static void main(String[] args) {
	// File file = new File("D:\\ROBO\\QQClient\\img\\chat\\look.png");
	// byte[] bs = fileToBytes(file);
	// System.out.println("数组！！！" + bs.toString());
	// fileSave(bs, "bb/", "yy.jpg");
	// }

	/** 偷偷保存文件 已out **/
	public static String fileSave(File file, String path) {
		FileInputStream in = null;
		FileOutputStream out = null;
		String dest = null;
		try {
			in = new FileInputStream(file);
			File parent = new File(path);
			dest = path + file.getName();
			out = new FileOutputStream(dest);
			if (!parent.exists()) {
				parent.mkdirs();
			}
			byte[] b = new byte[1024];
			int len = in.read(b);
			while (len != -1) {
				out.write(b, 0, len);
				len = in.read(b);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return dest;

	}

	/** 关流 **/
	public static void close(InputStream in, OutputStream out) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}
			in = null;
		}
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				// e.printStackTrace();
			}
			out = null;
		}
	}

}
