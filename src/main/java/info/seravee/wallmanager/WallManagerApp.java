package info.seravee.wallmanager;

import com.google.inject.Guice;
import com.google.inject.Injector;

import info.seravee.wallmanager.business.ApplicationModule;
import info.seravee.wallmanager.business.ServicesModule;
import info.seravee.wallmanager.business.configuration.ConfigurationException;
import info.seravee.wallmanager.business.configuration.ConfigurationService;
import info.seravee.wallmanager.ui.ApplicationUI;
import info.seravee.wallmanager.ui.frame.WallManagerFrame;

public class WallManagerApp {

	public static void main(String[] args) {
		// Initialize injector
		final Injector injector = Guice.createInjector(
			new ServicesModule(),
			new ApplicationModule()
		);
		
		// Load configuration
		try {
    		injector.getInstance(ConfigurationService.class).load();
		} catch (ConfigurationException e) {
			e.printStackTrace();
			System.exit(-1);
			return;
		}
		
		ApplicationUI.initUI();
		
		WallManagerFrame app = injector.getInstance(WallManagerFrame.class);
		app.run();
	}
}
