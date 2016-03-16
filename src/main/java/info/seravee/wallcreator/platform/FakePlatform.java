package info.seravee.wallcreator.platform;

import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import info.seravee.data.Screen;

/**
 * Created by ysaak on 31/01/15.
 */
class FakePlatform extends AbstractPlaform {

    @Override
    public List<Screen> getDesktopConfiguration() {
        List<Screen> fakeConfig = new ArrayList<>();
        fakeConfig.add(new Screen(1, new Rectangle(0, 0, 1920, 1080)));
        fakeConfig.add(new Screen(2, new Rectangle(1920, 0, 1280, 1024)));
        fakeConfig.add(new Screen(3, new Rectangle(3200, 0, 1920, 1080)));
        return fakeConfig;
    }
    
    @Override
    public void setWallpaper(File wallpaper) {
    	// Nothing to do
    }
}
