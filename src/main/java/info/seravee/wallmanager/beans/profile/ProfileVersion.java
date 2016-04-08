package info.seravee.wallmanager.beans.profile;

import java.util.ArrayList;
import java.util.List;

public class ProfileVersion {
	private String name;
	
	private List<WallpaperParameters> parameters;
	
	private boolean preferred = false;
	
	public ProfileVersion() {
		this.name = null;
		this.parameters = new ArrayList<>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<WallpaperParameters> getParameters() {
		return parameters;
	}
	
	public WallpaperParameters getScreenParameters(int screenId) {
		for (WallpaperParameters params : parameters) {
			if (params.getScreenId() == screenId)
				return params;
		}
		
		// No params found, create defaults ones
		return new WallpaperParameters(screenId);
	}
	
	public void setParameters(List<WallpaperParameters> parameters) {
		this.parameters = parameters;
	}

	public boolean isPreferred() {
		return preferred;
	}

	public void setPreferred(boolean preferred) {
		this.preferred = preferred;
	}
}
