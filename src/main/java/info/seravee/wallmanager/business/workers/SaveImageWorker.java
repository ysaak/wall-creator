package info.seravee.wallmanager.business.workers;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import info.seravee.wallcreator.utils.GraphicsUtilities;
import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.beans.profile.ProfileVersion;

public class SaveImageWorker extends SwingWorker<Void, Void> {
	
	protected final File imageFile;
	protected final Profile profile;
	protected final ProfileVersion version;
	
    public SaveImageWorker(final File imageFile, final Profile profile, final ProfileVersion version) {
        this.imageFile = imageFile;
        this.profile = profile;
        this.version = version;
    }

    @Override
    protected Void doInBackground() throws Exception {
    	final BufferedImage bi = GraphicsUtilities.generateProfileWallpaper(profile, version);
        ImageIO.write(bi, "PNG", imageFile);
    	return null;
    }
}
