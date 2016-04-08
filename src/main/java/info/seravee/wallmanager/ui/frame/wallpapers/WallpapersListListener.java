package info.seravee.wallmanager.ui.frame.wallpapers;

import java.io.File;

public interface WallpapersListListener {
	void wallpaperSelectedForScreen(int screenId, File imageFile);
}
