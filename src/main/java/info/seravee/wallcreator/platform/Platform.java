package info.seravee.wallcreator.platform;

import java.awt.Rectangle;
import java.io.File;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by ysaak on 31/01/15.
 */
public interface Platform {
	Path getAppDirectory();
    List<Rectangle> getDesktopConfiguration();
    
    void setWallpaper(File wallpaper);
}
