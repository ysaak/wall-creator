package info.seravee.wallcreator.beans;

import java.awt.Color;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import info.seravee.DefaultConfiguration;
import info.seravee.data.ScalingAlgorithm;

public class Screen implements Cloneable {
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	// Base data for the screen
	private int id;

	// Dimension of the scrren
	private int x;
	private int y;
	private int width;
	private int height;
	
	// Wallpaper data
	private String image = null;
	private ScalingAlgorithm scalingAlgorithm = DefaultConfiguration.SCALING_ALGORITHM;
	private Color backgroundColor = DefaultConfiguration.BACKGROUND_COLOR;
	
	public Screen() {
	}
	
	public Screen(int id, Rectangle bounds) {
		this.id = id;
		this.x = bounds.x;
		this.y = bounds.y;
		this.width = bounds.width;
		this.height = bounds.height;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	/*----------------*/

	public String getImage() {
		return image;
	}

	public ScalingAlgorithm getScalingAlgorithm() {
		return scalingAlgorithm;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setImage(String newImage) {
		final String oldFile = image;
		this.image = newImage;
		
		this.pcs.firePropertyChange("image", oldFile, image);
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
	
	public void copyFrom(Screen screen) {
		setBackgroundColor(screen.getBackgroundColor());
		setScalingAlgorithm(screen.getScalingAlgorithm());
		setImage(screen.getImage());
	}
	
	public Screen clone() throws CloneNotSupportedException {
		return (Screen) super.clone();
	}
	
	/* ---- */

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
}
