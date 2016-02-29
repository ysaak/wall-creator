package info.seravee.platform;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ysaak on 31/01/15.
 */
abstract class AbstractPlaform implements Platform {
	
	public Path getAppDirectory() {
		String userHome = System.getProperty("user.home");
		return Paths.get(userHome, ".wall-creator");
	}
	
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
