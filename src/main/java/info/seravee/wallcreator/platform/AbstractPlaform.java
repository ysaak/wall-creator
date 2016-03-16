package info.seravee.wallcreator.platform;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import info.seravee.data.Screen;

/**
 * Created by ysaak on 31/01/15.
 */
abstract class AbstractPlaform implements Platform {
	
	public Path getAppDirectory() {
		String userHome = System.getProperty("user.home");
		return Paths.get(userHome, ".wall-creator");
	}
	
    public List<Screen> getDesktopConfiguration() {
        final List<Screen> config = new ArrayList<>();
        
        int id = 1;

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();

        for(GraphicsDevice curGs : gs)  {
            GraphicsConfiguration[] gc = curGs.getConfigurations();
            for(GraphicsConfiguration curGc : gc) {
                config.add(new Screen(id++, curGc.getBounds()));
            }
        }

        return config;
    }
}
