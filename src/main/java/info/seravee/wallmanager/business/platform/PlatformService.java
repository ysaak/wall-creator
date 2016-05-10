package info.seravee.wallmanager.business.platform;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import info.seravee.wallmanager.beans.profile.Screen;

/**
 * Created by ysaak on 31/01/15.
 */
public interface PlatformService {
	Path getAppDirectory();
    List<Screen> getDesktopConfiguration();
    
    void setWallpaper(File wallpaper);
}
