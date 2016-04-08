package info.seravee.wallmanager;

import java.io.IOException;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.business.Services;
import info.seravee.wallmanager.business.configuration.ConfigurationException;
import info.seravee.wallmanager.ui.commons.laf.LafUtils;
import info.seravee.wallmanager.ui.frame.WallManagerFrame;

/**
 * Hello world!
 *
 */
public class App 
{
    WallManagerFrame frame;

    public App() {
    	// Load configuration
    	try {
			Services.getConfigurationService().load();
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
        frame = new WallManagerFrame();

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
