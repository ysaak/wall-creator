package info.seravee.wallmanager.business;

import com.google.inject.AbstractModule;

import info.seravee.wallmanager.ui.frame.WallManagerFrame;
import info.seravee.wallmanager.ui.frame.desktop.ProfileEditorController;
import info.seravee.wallmanager.ui.frame.profiles.ProfileListController;
import info.seravee.wallmanager.ui.frame.wallpapers.WallpapersController;

public class ApplicationModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(WallManagerFrame.class);
		
		bind(ProfileListController.class);
		bind(ProfileEditorController.class);
		bind(WallpapersController.class);
	}

}
