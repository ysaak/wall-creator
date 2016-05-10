package info.seravee.wallmanager.ui.frame.desktop;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import info.seravee.wallcreator.utils.GraphicsUtilities;
import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.beans.profile.ProfileVersion;
import info.seravee.wallmanager.business.events.EventBusLine;
import info.seravee.wallmanager.business.events.EventService;
import info.seravee.wallmanager.business.platform.PlatformService;
import info.seravee.wallmanager.business.profiles.ProfileService;
import info.seravee.wallmanager.business.worker.AbstractWorker;
import info.seravee.wallmanager.business.worker.WorkerService;
import info.seravee.wallmanager.ui.commons.WMController;
import info.seravee.wallmanager.ui.frame.events.ProfileSelectedEvent;
import info.seravee.wallmanager.ui.frame.events.ProfileVersionSelectedEvent;
import info.seravee.wallmanager.ui.frame.events.WallpaperSelectedEvent;

public class ProfileEditorController extends WMController implements ProfileEditorListener {
	
	private final EventService eventService;
	private final ProfileService profileService;
	private final WorkerService workerService;
	private final PlatformService platformService;
	
	private DesktopEditorPanel panel;
	
	@Inject
	public ProfileEditorController(final EventService eventService, final ProfileService profileService, final WorkerService workerService, final PlatformService platformService) {
		this.eventService = eventService;
		this.profileService = profileService;
		this.workerService = workerService;
		this.platformService = platformService;
	}
	
	public void setPanel(DesktopEditorPanel panel) {
		
		if (this.panel != null)
			this.panel.setListener(null);
		
		this.panel = panel;
		this.panel.setListener(this);
	}
	
	@Override
	public void start() {
		eventService.register(EventBusLine.FRAME, this);
	}
	
	@Override
	public void stop() {
		eventService.unregister(EventBusLine.FRAME, this);
	}
	
	@Subscribe
	public void onWallpaperSelection(WallpaperSelectedEvent event) {
		panel.wallpaperSelectedForScreen(event.getScreenId(), event.getImageFile());
	}
	
	@Subscribe
	public void onProfileSelection(ProfileSelectedEvent event) {
		panel.profileSelected(event.getProfile());
	}
	
	@Subscribe 
	public void onProfileVersionSelection(ProfileVersionSelectedEvent event) {
		panel.profileVersionSelected(event.getProfile(), event.getVersion());
	}
	
	@Override
	public void resetProfileVersion(Profile profile, ProfileVersion version) {
		workerService.schedule(new ResetProfileVersionWorker(profile, version));
	}
	
	@Override
	public void storeProfile(Profile profile, ProfileVersion version, boolean setAsWallpaper) {
		workerService.schedule(new StoreAndSetProfileWorker(profile, version, setAsWallpaper));
		
	}
	
	private class ResetProfileVersionWorker extends AbstractWorker<ProfileVersion> {
		
		private final Profile profile;
		private final ProfileVersion version;
		
		public ResetProfileVersionWorker(Profile profile, ProfileVersion version) {
			super();
			this.profile = Preconditions.checkNotNull(profile);
			this.version = Preconditions.checkNotNull(version);
		}
		
		@Override
		public ProfileVersion doInBackground() throws Throwable {
			final Profile storedProfile = profileService.get(profile.getId());
			profile.setVersions(storedProfile.getVersions());
			

			ProfileVersion storedVersion = null;
			for (ProfileVersion v : profile.getVersions())
				if (storedVersion == null || v.getName().equals(version.getName()))
					storedVersion = v;
			
			return storedVersion;
		}
		
		@Override
		public void done(ProfileVersion result) {
			panel.profileVersionSelected(profile, result);
		}
	}
	
	private class StoreAndSetProfileWorker extends AbstractWorker<Void> {
		private final boolean setAsWallpaper;
		private final Profile profile;
		private final ProfileVersion version;
		
		public StoreAndSetProfileWorker(Profile profile, ProfileVersion version, boolean setAsWallpaper) {
			super();
			this.profile = Preconditions.checkNotNull(profile);
			this.version = Preconditions.checkNotNull(version);
			this.setAsWallpaper = setAsWallpaper;
		}
		
		@Override
		public Void doInBackground() throws Throwable {
			
			profileService.store(profile);
			
			if (setAsWallpaper) {
				final BufferedImage wallpaper = GraphicsUtilities.generateProfileWallpaper(profile, version);
				
				// output folder (create if necessary)
				Path outFolder = platformService.getAppDirectory().resolve("wallpapers");
				
				if (Files.notExists(outFolder)) {
					Files.createDirectories(outFolder);
				}
				
				// Write profile wallpaper
				File wallpaperFile = outFolder.resolve(profile.getId() + "__" + version.getName() + ".png").toFile();
				
				ImageIO.write(wallpaper, "PNG", wallpaperFile);
				
				// Set wallpaper on desktop
				platformService.setWallpaper(wallpaperFile);
			}
			
			return null;
		}
		
		@Override
		public void done(Void result) {
			// TODO Auto-generated method stub
			
		}
	}
}
