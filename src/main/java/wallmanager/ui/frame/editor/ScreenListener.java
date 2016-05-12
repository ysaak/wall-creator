package wallmanager.ui.frame.editor;

import wallmanager.beans.profile.Screen;
import wallmanager.beans.profile.WallpaperParameters;

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
