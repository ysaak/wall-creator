package wallmanager;

import com.google.inject.Guice;
import com.google.inject.Injector;

import wallmanager.business.ServicesModule;
import wallmanager.business.configuration.ConfigurationService;
import wallmanager.ui.ApplicationModule;
import wallmanager.ui.ApplicationUI;
import wallmanager.ui.frame.WallManagerFrame;

public class WallManagerApp {

	public static void main(String[] args) {
		// Initialize injector
		final Injector injector = Guice.createInjector(
			new ServicesModule(),
			new ApplicationModule()
		);
		
		// Load configuration
   		injector.getInstance(ConfigurationService.class).load();
		
		ApplicationUI.initUI();
		
		WallManagerFrame app = injector.getInstance(WallManagerFrame.class);
		app.run();
	}
}
