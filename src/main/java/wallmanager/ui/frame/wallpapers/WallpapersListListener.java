package wallmanager.ui.frame.wallpapers;

import java.io.File;

public interface WallpapersListListener {
	
	void addFolderEvent(File folder);
	
	void removeFolderEvent(File folder);
	
	void wallpaperSelectedForScreen(int screenId, File imageFile);
}
