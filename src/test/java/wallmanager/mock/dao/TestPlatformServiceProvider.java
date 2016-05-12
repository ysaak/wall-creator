package wallmanager.mock.dao;

import java.awt.Rectangle;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Provider;

import wallmanager.beans.profile.Screen;
import wallmanager.business.platform.PlatformService;

public class TestPlatformServiceProvider implements Provider<PlatformService> {
	@Override
	public PlatformService get() {
		return new TestPlatform();
	}

	private static final class TestPlatform implements PlatformService {

		@Override
		public Path getAppDirectory() {
			String userHome = System.getProperty("user.home");
			return Paths.get(userHome, ".wall-creator-test");
		}

		@Override
		public List<Screen> getDesktopConfiguration() {
			List<Screen> screens = new ArrayList<>();
			screens.add(new Screen(1, new Rectangle(0, 0, 1280, 1024)));
			return screens;
		}

		@Override
		public void setWallpaper(File wallpaper) {
			// Do nothing
		}
	}
}
