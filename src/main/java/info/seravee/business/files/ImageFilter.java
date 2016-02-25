package info.seravee.business.files;

import java.io.File;
import java.io.FilenameFilter;

import javax.swing.filechooser.FileFilter;

public class ImageFilter extends FileFilter implements FilenameFilter {
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}

		String extension = FileUtils.getExtension(f);
		return extension != null && FileUtils.IMAGE_EXTENSIONS.contains(extension.toLowerCase());
	}

    @Override
    public String getDescription() {
        return "Image files";
    }

	@Override
	public boolean accept(File dir, String name) {
		return accept(new File(dir, name));
	}
}
