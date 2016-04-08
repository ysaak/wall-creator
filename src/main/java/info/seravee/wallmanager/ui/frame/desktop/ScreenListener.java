package info.seravee.wallmanager.ui.frame.desktop;

import info.seravee.wallmanager.beans.profile.Screen;
import info.seravee.wallmanager.beans.profile.WallpaperParameters;

interface ScreenListener {
	void screenSelected(Screen screen);
	void screenParametersUpdated(WallpaperParameters params);
	
	class ScreenAdapter implements ScreenListener {

		@Override
		public void screenSelected(Screen screen) {
		}

		@Override
		public void screenParametersUpdated(WallpaperParameters params) {
		}
	}
}
