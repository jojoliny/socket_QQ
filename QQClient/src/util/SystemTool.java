package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SystemTool {
	public static void processExec(String command) {
		InputStream ins = null;
		String[] cmd = new String[] { "cmd.exe", "/C", command };

		Process process;
		try {
			process = Runtime.getRuntime().exec(cmd);
			ins = process.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			int exitValue = process.waitFor();
			System.out.println("∑µªÿ÷µ£∫ " + exitValue);

			process.getOutputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
