package wallmanager.beans.profile;

import java.awt.Color;

import wallmanager.DefaultConfiguration;
import wallmanager.beans.ScalingAlgorithm;

public class WallpaperParameters {
	private int screenId;
	
	private String image = null;
	private ScalingAlgorithm scalingAlgorithm = DefaultConfiguration.SCALING_ALGORITHM;
	private Color backgroundColor = DefaultConfiguration.BACKGROUND_COLOR;
	
	public WallpaperParameters() {/**/}
	
	public WallpaperParameters(int screenId) {
		this.screenId = screenId;
	}
	
	public int getScreenId() {
		return screenId;
	}
	
	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public ScalingAlgorithm getScalingAlgorithm() {
		return scalingAlgorithm;
	}
	
	public void setScalingAlgorithm(ScalingAlgorithm scalingAlgorithm) {
		this.scalingAlgorithm = scalingAlgorithm;
	}
	
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}
