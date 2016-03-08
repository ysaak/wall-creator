package info.seravee.platform;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ysaak on 31/01/15.
 */
class FakePlatform extends AbstractPlaform {

    @Override
    public java.util.List<Rectangle> getDesktopConfiguration() {
        List<Rectangle> fakeConfig = new ArrayList<Rectangle>();
        fakeConfig.add(new Rectangle(0, 0, 1920, 1080));
        fakeConfig.add(new Rectangle(1920, 0, 1280, 1024));
        fakeConfig.add(new Rectangle(3200, 0, 1920, 1080));
        return fakeConfig;
    }
}
