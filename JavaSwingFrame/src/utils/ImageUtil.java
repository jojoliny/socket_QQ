package utils;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ImageUtil {

	public static Image getImage(String path) {
		return new ImageIcon(path).getImage();
	}

	public static Icon getIcon(String path) {
		return new ImageIcon(path);
	}
}
