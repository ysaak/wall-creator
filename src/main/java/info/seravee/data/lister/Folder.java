package info.seravee.data.lister;

import java.awt.Image;
import java.io.File;

public class Folder extends Wallpaper {

	public Folder(File file) {
		super(file, getFolderIcon());
	}

	private static Image getFolderIcon() {
		return null;
	}
}
