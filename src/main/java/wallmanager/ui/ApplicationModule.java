package wallmanager.ui;

import com.google.inject.AbstractModule;

import wallmanager.ui.frame.WallManagerFrame;
import wallmanager.ui.frame.editor.ProfileEditorController;
import wallmanager.ui.frame.profiles.ProfileListController;
import wallmanager.ui.frame.wallpapers.WallpapersController;

public class ApplicationModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(WallManagerFrame.class);
		
		bind(ProfileListController.class);
		bind(ProfileEditorController.class);
		bind(WallpapersController.class);
	}

}
