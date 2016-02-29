package info.seravee.ui.lister;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import info.seravee.DefaultConfiguration;
import info.seravee.business.files.ImageFilter;
import info.seravee.business.files.ThumbnailManager;
import info.seravee.data.lister.Wallpaper;
import info.seravee.utils.ImageScalerUtils;

class ImageLoadingWorker extends SwingWorker<Void, Wallpaper> {
	
	private LoadingWorkerListener listener = null;
	private final File imgFolder;

	public ImageLoadingWorker(File imgFolder, LoadingWorkerListener listener) {
		this.imgFolder = imgFolder;
		this.listener = listener;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		
		// Réinitialisation de la liste des sous-dossiers
		List<File> files = new ArrayList<>(Arrays.asList(imgFolder.listFiles(new ImageFilter())));
		
		// Tri des fichiers pour mettre en avant les dossiers
		Collections.sort(files, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				if (o1.isDirectory() && !o2.isDirectory()) {
					return -1;
				}
				else if (!o1.isDirectory() && o2.isDirectory()) {
					return 1;
				}
				
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		

		for (File file : files) {
			System.out.println("Working on >> " + file.getAbsolutePath());
			if (isCancelled()) return null;
			
			if (!file.isDirectory()) {
				BufferedImage image;
				Wallpaper wp = null;
				
				// Try to load thumbnail
				image = ThumbnailManager.getThumbnail(file);
				
				if (image != null) {
					// Thumbnail exists, let's use it
					wp = new Wallpaper(file, image);
				}
				else {
					try {
						image = ImageIO.read(file);
						
						wp = new Wallpaper(file, 
								ImageScalerUtils.getScaledImage(
										image, 
										DefaultConfiguration.SCALING_ALGORITHM, 
										new Dimension(DefaultConfiguration.THUMBNAIL_WIDTH, DefaultConfiguration.THUMBNAIL_HEIGHT)
										)
								);
						
						
					}
					catch (IOException e) {
						wp = null;
						e.printStackTrace();
					}
					
					if (wp != null) {
						System.out.println("Writing thumbnail");
						ThumbnailManager.writeThumbnailFile(wp.getFile(), wp.getImage());
					}
				}

				if (wp != null) {
					publish(wp);
				}
			}
		}
		
		return null;
	}
	
	public void removeListener() {
		synchronized (listener) {
			listener = null;
		}
	}
	
	@Override
	protected void process(List<Wallpaper> chunks) {
		for (Wallpaper wallpaper : chunks) {
 			if (listener != null) {
				synchronized (listener) {
						listener.wallpaperDetected(wallpaper);
				}
 			}
		}
	}

	public static interface LoadingWorkerListener {
		void wallpaperDetected(Wallpaper wallpaper);
	}
}
