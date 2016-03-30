package info.seravee.wallcreator.business.workers;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import info.seravee.wallcreator.beans.Profile;
import info.seravee.wallcreator.utils.GraphicsUtilities;

public class SaveImageWorker extends SwingWorker<Void, Void> {
	
	protected final File imageFile;
	protected final Profile profile;
	
    public SaveImageWorker(File imageFile, Profile profile) {
        this.imageFile = imageFile;
        this.profile = profile;
    }

    @Override
    protected Void doInBackground() throws Exception {
    	final BufferedImage bi = GraphicsUtilities.generateProfileWallpaper(profile);
        ImageIO.write(bi, "PNG", imageFile);
    	return null;
    }
}
