package info.seravee.data.lister;

import java.awt.Image;
import java.io.File;

public class Wallpaper {
	
	private final File file;
	private final Image image;
	
	public Wallpaper(File file, Image image) {
		super();
		this.file = file;
		this.image = image;
	}

	public File getFile() {
		return file;
	}

	public Image getImage() {
		return image;
	}
	
	@Override
	public String toString() {
		return file.getName();
	}
}
