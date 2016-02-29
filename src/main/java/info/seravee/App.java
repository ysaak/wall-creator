package info.seravee;

import javax.swing.SwingUtilities;

import info.seravee.business.Configuration;
import info.seravee.business.exceptions.ConfigurationException;
import info.seravee.platform.Platform;
import info.seravee.platform.Platforms;
import info.seravee.ui.CreatorFrame;

/**
 * Hello world!
 *
 */
public class App 
{
    CreatorFrame frame;

    public App() {
    	try {
			Configuration.get().initPaths();
		} catch (ConfigurationException e) {
			e.printStackTrace();
			System.exit(0);
		}
    	
    	//ThumbnailManager.purgeThumbnails();
    	
    	/*
    	try {
    		// Set System L&F
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } 
    	catch (Exception e) { }
    	*/
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
