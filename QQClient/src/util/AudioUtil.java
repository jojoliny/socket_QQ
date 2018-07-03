package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioUtil implements Runnable {

	// 定义录音格式
	AudioFormat af = null;
	// 定义目标数据行，可以从中读取该数据，该TargetDataLine接口提供从目标数据行的缓冲区读取所捕获数据的方法
	TargetDataLine td = null;
	// 定义源数据行，源数据行是可以写入数据的数据行。它充当其混频器的源。应用程序将音频字节写入源数据行，这样可处理字节缓冲并将他们传递给混频器
	SourceDataLine sd = null;
	// 定义字节输入输出流
	ByteArrayInputStream bais = null;
	ByteArrayOutputStream baos = null;
	// 定义音频输入流
	AudioInputStream ais = null;
	// 定义停止录音时间，来控制录音线程的运行
	boolean stopflag = true;
	// 定义存放录音的字节数组，作为缓冲区
	byte bts[] = new byte[10000];
	// 记录开始录音时间
	long startPlay;
	// 播放标记
	Boolean playflag;
	// 最终文件名
	File tarFile = null;
	// 每个音频波形显示字节数
	int intBytes = 0;
	// 每次录音时提取字节画音频波
	byte[] audioDataBuffer = null;

	// 设置画波形线程的终止标志
	boolean flag = true;
	// 播放录音时的计数值
	int cnt;
	// 播放录音时的一个缓冲数组
	byte btsPlay[] = null;

	/** 设置AudioFormat的参数 **/
	public AudioFormat getAudioFormat() {
		// AudioFormat.Encoding类命名用于音频流的数据表现形式的特定类型
		AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;// 音频编码技术
		float rate = 8000f;// 每秒的样本数
		int sampleSize = 16;// 每个样本中的位数
		int channels = 1;// 声道数（单声道 1 个，立体声 2 个，等等）
		String signedString = "signed";
		boolean bigEndian = true;// 指示是否以 big-endian 字节顺序存储单个样本中的数据（false 意味着
									// little-endian）。

		return new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
	}

	// 将字节数组包装到流里，最终存入baos中
	@Override
	public void run() {
		baos = new ByteArrayOutputStream();
		stopflag = false;
		while (!stopflag) {
			// 当未停止录音时，一直执行该线程
			// 从数据行的输入缓冲区读取音频数据
			// 要读取bts.length长度字节，cnt是实际读取的字节数
			int cnt = td.read(bts, 0, bts.length);
			if (cnt > 0) {
				baos.write(bts, 0, cnt);
			}
			try {
				// 开始从音频流中读取字节数
				bais = new ByteArrayInputStream(bts);
				ais = new AudioInputStream(bais, af, bts.length / af.getFrameSize());
				DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, af);
				sd = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
				sd.open(af);
				sd.start();
				// 从音频流中读取
				int Buffer_Size = 10000;
				// 每次录音时提取字节画音频波
				audioDataBuffer = new byte[Buffer_Size];
				int outBytes = ais.read(audioDataBuffer, 0, audioDataBuffer.length);
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("音频工具线程有问题");
			} finally {
				// 关流
				td.close();
				// 刷新显示波形的面板
				//
			}

		}
	}

}
