package info.seravee.wallcreator.platform;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import info.seravee.data.Screen;

/**
 * Created by ysaak on 31/01/15.
 */
public interface Platform {
	Path getAppDirectory();
    List<Screen> getDesktopConfiguration();
    
    void setWallpaper(File wallpaper);
}
