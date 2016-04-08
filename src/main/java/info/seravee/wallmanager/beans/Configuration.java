package info.seravee.wallmanager.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {
	private List<String> wallpapersFolders;

	private Map<DialogLastFolderType, String> lastFolders;
	
	private String selectedProfile;

	public Configuration() {
		wallpapersFolders = new ArrayList<>();
		lastFolders = new HashMap<>();
		selectedProfile = null;
	}
	
	public List<String> getWallpapersFolders() {
		return wallpapersFolders;
	}

	public void setWallpapersFolders(List<String> wallpapersFolders) {
		this.wallpapersFolders = wallpapersFolders;
	}

	public Map<DialogLastFolderType, String> getLastFolders() {
		return lastFolders;
	}

	public void setLastFolders(Map<DialogLastFolderType, String> lastFolders) {
		this.lastFolders = lastFolders;
	}

	public String getSelectedProfile() {
		return selectedProfile;
	}

	public void setSelectedProfile(String selectedProfile) {
		this.selectedProfile = selectedProfile;
	}
}
