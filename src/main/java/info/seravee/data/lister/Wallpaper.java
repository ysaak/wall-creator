package info.seravee.data.lister;

import java.awt.image.BufferedImage;
import java.io.File;

public class Wallpaper {
	
	private final File file;
	private final BufferedImage image;
	
	public Wallpaper(File file, BufferedImage image) {
		super();
		this.file = file;
		this.image = image;
	}

	public File getFile() {
		return file;
	}

	public BufferedImage getImage() {
		return image;
	}
	
	@Override
	public String toString() {
		return file.getName();
	}
}
