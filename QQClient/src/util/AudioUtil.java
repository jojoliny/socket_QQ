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

	// ����¼����ʽ
	AudioFormat af = null;
	// ����Ŀ�������У����Դ��ж�ȡ�����ݣ���TargetDataLine�ӿ��ṩ��Ŀ�������еĻ�������ȡ���������ݵķ���
	TargetDataLine td = null;
	// ����Դ�����У�Դ�������ǿ���д�����ݵ������С����䵱���Ƶ����Դ��Ӧ�ó�����Ƶ�ֽ�д��Դ�����У������ɴ����ֽڻ��岢�����Ǵ��ݸ���Ƶ��
	SourceDataLine sd = null;
	// �����ֽ����������
	ByteArrayInputStream bais = null;
	ByteArrayOutputStream baos = null;
	// ������Ƶ������
	AudioInputStream ais = null;
	// ����ֹͣ¼��ʱ�䣬������¼���̵߳�����
	boolean stopflag = true;
	// ������¼�����ֽ����飬��Ϊ������
	byte bts[] = new byte[10000];
	// ��¼��ʼ¼��ʱ��
	long startPlay;
	// ���ű��
	Boolean playflag;
	// �����ļ���
	File tarFile = null;
	// ÿ����Ƶ������ʾ�ֽ���
	int intBytes = 0;
	// ÿ��¼��ʱ��ȡ�ֽڻ���Ƶ��
	byte[] audioDataBuffer = null;

	// ���û������̵߳���ֹ��־
	boolean flag = true;
	// ����¼��ʱ�ļ���ֵ
	int cnt;
	// ����¼��ʱ��һ����������
	byte btsPlay[] = null;

	/** ����AudioFormat�Ĳ��� **/
	public AudioFormat getAudioFormat() {
		// AudioFormat.Encoding������������Ƶ�������ݱ�����ʽ���ض�����
		AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;// ��Ƶ���뼼��
		float rate = 8000f;// ÿ���������
		int sampleSize = 16;// ÿ�������е�λ��
		int channels = 1;// �������������� 1 ���������� 2 �����ȵȣ�
		String signedString = "signed";
		boolean bigEndian = true;// ָʾ�Ƿ��� big-endian �ֽ�˳��洢���������е����ݣ�false ��ζ��
									// little-endian����

		return new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
	}

	// ���ֽ������װ��������մ���baos��
	@Override
	public void run() {
		baos = new ByteArrayOutputStream();
		stopflag = false;
		while (!stopflag) {
			// ��δֹͣ¼��ʱ��һֱִ�и��߳�
			// �������е����뻺������ȡ��Ƶ����
			// Ҫ��ȡbts.length�����ֽڣ�cnt��ʵ�ʶ�ȡ���ֽ���
			int cnt = td.read(bts, 0, bts.length);
			if (cnt > 0) {
				baos.write(bts, 0, cnt);
			}
			try {
				// ��ʼ����Ƶ���ж�ȡ�ֽ���
				bais = new ByteArrayInputStream(bts);
				ais = new AudioInputStream(bais, af, bts.length / af.getFrameSize());
				DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, af);
				sd = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
				sd.open(af);
				sd.start();
				// ����Ƶ���ж�ȡ
				int Buffer_Size = 10000;
				// ÿ��¼��ʱ��ȡ�ֽڻ���Ƶ��
				audioDataBuffer = new byte[Buffer_Size];
				int outBytes = ais.read(audioDataBuffer, 0, audioDataBuffer.length);
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("��Ƶ�����߳�������");
			} finally {
				// ����
				td.close();
				// ˢ����ʾ���ε����
				//
			}

		}
	}

}
