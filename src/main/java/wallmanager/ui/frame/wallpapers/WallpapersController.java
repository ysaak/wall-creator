package wallmanager.ui.frame.wallpapers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JFileChooser;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import wallmanager.beans.Configuration;
import wallmanager.beans.DialogLastFolderType;
import wallmanager.business.configuration.ConfigurationException;
import wallmanager.business.configuration.ConfigurationService;
import wallmanager.business.event.EventBusLine;
import wallmanager.business.event.EventService;
import wallmanager.business.worker.AbstractWorker;
import wallmanager.business.worker.WorkerService;
import wallmanager.ui.commons.WMController;
import wallmanager.ui.frame.events.ProfileSelectedEvent;
import wallmanager.ui.frame.events.WallpaperSelectedEvent;

public class WallpapersController extends WMController {

	private WallpapersListPanel panel = null;
	
	private final EventService eventService;
	private final WorkerService workerService;
	private final ConfigurationService configurationService;
	
	private final WallpapersListListener panelListener;
	
	@Inject
	public WallpapersController(final EventService eventService, final WorkerService workerService, final ConfigurationService configurationService) {
		this.eventService = eventService;
		this.workerService = workerService;
		this.configurationService = configurationService;
		
		panelListener = new WallpapersListListener() {
			
			@Override
			public void wallpaperSelectedForScreen(int screenId, File imageFile) {
				eventService.post(EventBusLine.FRAME, new WallpaperSelectedEvent(screenId, imageFile));
			}
			
			@Override
			public void removeFolderEvent(File folder) {
				workerService.schedule(new RemoveFolderWorker(folder));
			}
			
			@Override
			public void addFolderEvent(File folder) {
				workerService.schedule(new AddFolderWorker());
			}
		};
	}
	
	@Override
	public void start() {
		eventService.register(EventBusLine.FRAME, this);
		
		workerService.schedule(new LoadFoldersWorker());
	}
	
	@Override
	public void stop() {
		eventService.unregister(EventBusLine.FRAME, this);
	}
	
	
	public void setPanel(WallpapersListPanel panel) {
		if (this.panel != null) {
			this.panel.removeWallpapersListListener(panelListener);
		}
		
		this.panel = panel;
		this.panel.addWallpapersListListener(panelListener);
	}
	
	@Subscribe
	public void onProfileSelection(ProfileSelectedEvent event) {
		if (panel == null) return;
		panel.setSelectedProfile(event.getProfile());
	}
	
	private class LoadFoldersWorker extends AbstractWorker<List<String>> {
		
		@Override
		public List<String> doInBackground() throws Throwable {
			return configurationService.get().getWallpapersFolders();
		}
		
		@Override
		public void done(List<String> result) {
			panel.setFolders(result);
		}
	}
	
	private class AddFolderWorker extends AbstractWorker<File> {
		
		@Override
		public File doInBackground() throws Throwable {
			final Configuration config = configurationService.get();
			String lastFolder = config.getLastFolders().get(DialogLastFolderType.WALLPAPER_FOLDER);

			JFileChooser chooser = new JFileChooser();
			if (lastFolder != null && Files.exists(Paths.get(lastFolder))) {
				chooser.setCurrentDirectory(new File(lastFolder));
			}
			chooser.setDialogTitle("Select a folder");
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setAcceptAllFileFilterUsed(false);

			if (chooser.showOpenDialog(panel.getDisplay()) == JFileChooser.APPROVE_OPTION) {
				
				// -- Update configuration
				// Get parent directory
				config.getLastFolders().put(DialogLastFolderType.WALLPAPER_FOLDER, chooser.getCurrentDirectory().getAbsolutePath());
				
				// Add selected folder
				config.getWallpapersFolders().add(chooser.getSelectedFile().getAbsolutePath());
				
				try {
					configurationService.store(config);
				} catch (ConfigurationException e1) {
					e1.printStackTrace();
				}
				
				return chooser.getSelectedFile();
				
			} 
			return null;
		}
		
		@Override
		public void done(File result) {
			if (result != null)
				panel.addFolder(result);
		}
	}
	
	private class RemoveFolderWorker extends AbstractWorker<Void> {
		
		private final File folderToRemove;
		
		public RemoveFolderWorker(final File folderToRemove) {
			this.folderToRemove = folderToRemove;
		}
		
		@Override
		public Void doInBackground() throws Throwable {
			final Configuration config = configurationService.get();
			config.getWallpapersFolders().remove(folderToRemove.getAbsolutePath());
			
			try {
				configurationService.store(config);
			} catch (ConfigurationException e1) {
				e1.printStackTrace();
			}
			return null;
		}

		@Override
		public void done(Void result) {
			/**/
		}
	}
	
}
