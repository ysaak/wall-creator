package info.seravee.data;

import java.awt.Color;
import java.awt.Rectangle;
import java.io.File;

public class ScreenWallpaper {
	private final Rectangle screenBounds;
	private final File file;
	private final ScalingAlgorithm scalingAlgorithm;
	private final Color backgroundColor;
	
	public ScreenWallpaper(Rectangle screenBounds, File file, ScalingAlgorithm scalingAlgorithm, Color backgroundColor) {
		this.screenBounds = screenBounds;
		this.file = file;
		this.scalingAlgorithm = scalingAlgorithm;
		this.backgroundColor = backgroundColor;
	}

	public Rectangle getScreenBounds() {
		return screenBounds;
	}

	public File getFile() {
		return file;
	}

	public ScalingAlgorithm getScalingAlgorithm() {
		return scalingAlgorithm;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
}
