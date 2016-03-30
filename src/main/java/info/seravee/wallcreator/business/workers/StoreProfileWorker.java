package info.seravee.wallcreator.business.workers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import info.seravee.wallcreator.beans.Profile;
import info.seravee.wallcreator.business.Services;
import info.seravee.wallcreator.platform.Platforms;
import info.seravee.wallcreator.utils.GraphicsUtilities;

public class StoreProfileWorker extends SwingWorker<Void, Void> {

	private final Profile profileToStore;
	
	public StoreProfileWorker(final Profile profileToStore) {
		this.profileToStore = profileToStore;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		
		final BufferedImage wallpaper = GraphicsUtilities.generateProfileWallpaper(profileToStore);
		
		// output folder (create if necessary)
		Path outFolder = Platforms.get().getAppDirectory().resolve("wallpapers");
		
		if (Files.notExists(outFolder)) {
			Files.createDirectories(outFolder);
		}
		
		// Write profile wallpaper
		File wallpaperFile = outFolder.resolve(profileToStore.getId() + ".png").toFile();
		
		ImageIO.write(wallpaper, "PNG", wallpaperFile);
		
		// Set wallpaper on desktop
		Platforms.get().setWallpaper(wallpaperFile);
		
		
		// Store updated profile
		Services.getProfileService().store(profileToStore);
		
		return null;
	}
	
}
