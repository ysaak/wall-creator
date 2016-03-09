package info.seravee.business.workers;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import info.seravee.data.ScreenWallpaper;
import info.seravee.utils.ImageScalerUtils;

public class SaveImageWorker extends SwingWorker<Void, Void> {
	
	protected final File imageFile;
	protected final List<ScreenWallpaper> screenWallpapers;
	
    public SaveImageWorker(File imageFile, List<ScreenWallpaper> screenWallpapers) {
        this.imageFile = imageFile;
        this.screenWallpapers = screenWallpapers;
    }

    @Override
    protected Void doInBackground() throws Exception {
    	// TODO rewrite the whole method
    	
    	// Calculate final image dimension
    	int w, h;
    	Dimension screensDimension = new Dimension(0, 0);
    	for (ScreenWallpaper sw : screenWallpapers) {
    		w = sw.getScreenBounds().x + sw.getScreenBounds().width;
    		h = sw.getScreenBounds().y + sw.getScreenBounds().height;
    		
    		if (w > screensDimension.width) screensDimension.width = w;
    		if (h > screensDimension.height) screensDimension.height = h;
    	}
    	

        BufferedImage bi = new BufferedImage(screensDimension.width, screensDimension.height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = bi.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Set a black background (for unused space) 
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, screensDimension.width, screensDimension.height);
        
        
        for (ScreenWallpaper sw : screenWallpapers) { 
        	try {
        		paintWallpaper(g2, sw);
        	}
        	catch (Throwable e) {
        		e.printStackTrace();
        		throw e;
        	}
        }
        
        ImageIO.write(bi, "PNG", imageFile);
    	return null;
    }
    
    private void paintWallpaper(Graphics2D g2, ScreenWallpaper wallpaper) throws IOException {
    	
    	// Set the default background color
    	g2.setColor(wallpaper.getBackgroundColor());
    	g2.fillRect(wallpaper.getScreenBounds().x, wallpaper.getScreenBounds().y, wallpaper.getScreenBounds().width, wallpaper.getScreenBounds().height);

    	if (wallpaper.getFile() != null) {
	    	// 
	    	final Dimension screenDimension = new Dimension(wallpaper.getScreenBounds().width, wallpaper.getScreenBounds().height); 
	
	    	// Load original image
	    	final BufferedImage originalImage = ImageIO.read(wallpaper.getFile());
	    	final BufferedImage scaledImage = ImageScalerUtils.getScaledImage(originalImage, wallpaper.getScalingAlgorithm(), screenDimension);
	    	
	    	// Compute image position on the screen
	    	int x = wallpaper.getScreenBounds().x + ((screenDimension.width  - scaledImage.getWidth(null))  / 2);
	    	int y = wallpaper.getScreenBounds().y + ((screenDimension.height - scaledImage.getHeight(null)) / 2);
	    	
	    	// Draw image
	        g2.drawImage(scaledImage, x, y, null);
    	}
    }
}
