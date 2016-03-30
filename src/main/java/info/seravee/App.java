package info.seravee;

import java.io.IOException;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import info.seravee.business.config.ConfigurationManager;
import info.seravee.business.exceptions.ConfigurationException;
import info.seravee.ui.CreatorFrame;
import info.seravee.wallcreator.beans.Profile;
import info.seravee.wallcreator.business.Services;
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
			return;
		}
    	
    	//ThumbnailManager.purgeThumbnails();
    	final List<Profile> profiles;
    	try {
			profiles = Services.getProfileService().list();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
			return;
		}
    	
    	//*
    	try {
    		// Set System L&F
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } 
    	catch (Exception e) { }
    	
    	LafUtils.setUIDefaultFont();
    	//*/
        frame = new CreatorFrame();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	frame.setProfiles(profiles);
                frame.show();
            }
        });
    }

    public static void main( String[] args )
    {
        new App();
    }
}
