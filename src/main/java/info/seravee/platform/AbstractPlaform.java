package info.seravee.platform;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ysaak on 31/01/15.
 */
class AbstractPlaform implements Platform {

    public List<Rectangle> getDesktopConfiguration() {

        List<Rectangle> config = new ArrayList<Rectangle>();

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        for(GraphicsDevice curGs : gs)  {
            GraphicsConfiguration[] gc = curGs.getConfigurations();
            for(GraphicsConfiguration curGc : gc) {
                config.add(curGc.getBounds());
            }
        }

        return config;
    }
}
