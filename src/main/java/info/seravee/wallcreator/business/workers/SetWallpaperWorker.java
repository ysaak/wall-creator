package info.seravee.wallcreator.business.workers;

import java.io.File;
import java.util.List;

import info.seravee.business.workers.SaveImageWorker;
import info.seravee.data.ScreenWallpaper;
import info.seravee.wallcreator.platform.Platforms;

public class SetWallpaperWorker extends SaveImageWorker {
	
	public SetWallpaperWorker(final List<ScreenWallpaper> screenWallpapers) {
		super(getCurrentWallpaperPath(), screenWallpapers);
	}
	
	private static final File getCurrentWallpaperPath() {
		return Platforms.get().getAppDirectory().resolve("current_wallpaper.png").toFile();
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		// Generate image
		super.doInBackground();
		
		// Set wallpaper
		Platforms.get().setWallpaper(imageFile);
		return null;
	}

}
