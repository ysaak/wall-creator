package info.seravee.wallcreator.beans;

import java.awt.Color;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

import info.seravee.DefaultConfiguration;
import info.seravee.data.ScalingAlgorithm;

public class Screen {
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	// Base data for the screen
	private final int id;
	private final Rectangle bounds;
	
	// Wallpaper data
	private File imageFile = null;
	private ScalingAlgorithm scalingAlgorithm = DefaultConfiguration.SCALING_ALGORITHM;
	private Color backgroundColor = DefaultConfiguration.BACKGROUND_COLOR;
	
	public Screen(int id, Rectangle bounds) {
		this.id = id;
		this.bounds = bounds;
	}

	public int getId() {
		return id;
	}

	public Rectangle getBounds() {
		return bounds;
	}
	
	public int getX() {
		return bounds.x;
	}
	
	public int getY() {
		return bounds.y;
	}
	
	public int getWidth() {
		return bounds.width;
	}
	
	public int getHeight() {
		return bounds.height;
	}
	
	/*----------------*/

	public File getImageFile() {
		return imageFile;
	}

	public ScalingAlgorithm getScalingAlgorithm() {
		return scalingAlgorithm;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}


	public void setImageFile(File file) {
		final File oldFile = this.imageFile;
		this.imageFile = file;
		
		this.pcs.firePropertyChange("imageFile", oldFile,this.imageFile);
	}

	public void setScalingAlgorithm(ScalingAlgorithm scalingAlgorithm) {
		final ScalingAlgorithm oldSA = this.scalingAlgorithm;
		this.scalingAlgorithm = scalingAlgorithm;
		
		this.pcs.firePropertyChange("scalingAlgorithm", oldSA, this.scalingAlgorithm);
	}
	
	public void setBackgroundColor(Color backgroundColor) {
		final Color oldColor = this.backgroundColor;
		this.backgroundColor = backgroundColor;
		this.pcs.firePropertyChange("backgroundColor", oldColor, this.backgroundColor);
	}
	
	/* ---- */

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
}
