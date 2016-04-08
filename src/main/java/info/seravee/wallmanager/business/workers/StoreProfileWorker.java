package info.seravee.wallmanager.business.workers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import info.seravee.wallcreator.platform.Platforms;
import info.seravee.wallcreator.utils.GraphicsUtilities;
import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.beans.profile.ProfileVersion;
import info.seravee.wallmanager.business.Services;

public class StoreProfileWorker extends SwingWorker<Void, Void> {

	private final Profile profile;
	private final ProfileVersion version;

	public StoreProfileWorker(final Profile profile, final ProfileVersion version) {
		this.profile = profile;
		this.version = version;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		Services.getProfileService().store(profile);
		
		if (version != null) {
			final BufferedImage wallpaper = GraphicsUtilities.generateProfileWallpaper(profile, version);
			
			// output folder (create if necessary)
			Path outFolder = Platforms.get().getAppDirectory().resolve("wallpapers");
			
			if (Files.notExists(outFolder)) {
				Files.createDirectories(outFolder);
			}
			
			// Write profile wallpaper
			File wallpaperFile = outFolder.resolve(profile.getId() + "__" + version.getName() + ".png").toFile();
			
			ImageIO.write(wallpaper, "PNG", wallpaperFile);
			
			// Set wallpaper on desktop
			Platforms.get().setWallpaper(wallpaperFile);
		}

		return null;
	}
	
}
