package info.seravee;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import info.seravee.business.config.ConfigurationManager;
import info.seravee.business.exceptions.ConfigurationException;
import info.seravee.ui.CreatorFrame;
import info.seravee.wallcreator.platform.Platform;
import info.seravee.wallcreator.platform.Platforms;
import info.seravee.wallcreator.ui.components.LafUtils;

/**
 * Hello world!
 *
 */
public class App 
{
    CreatorFrame frame;

    public App() {
    	// Load configuration
    	try {
			ConfigurationManager.load();
		} catch (ConfigurationException e) {
			e.printStackTrace();
			System.exit(-1);
		}
    	
    	
    	//ThumbnailManager.purgeThumbnails();
    	
    	//*
    	try {
    		// Set System L&F
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } 
    	catch (Exception e) { }
    	
    	LafUtils.setUIDefaultFont();
    	//*/
        frame = new CreatorFrame();

        final Platform p = Platforms.get();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setDesktopConfig(p.getDesktopConfiguration());
                frame.show();
            }
        });
    }

    public static void main( String[] args )
    {
        new App();
    }
}
