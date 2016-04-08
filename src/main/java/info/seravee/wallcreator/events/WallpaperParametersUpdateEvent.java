package info.seravee.wallcreator.events;

import info.seravee.wallmanager.beans.profile.WallpaperParameters;

public class WallpaperParametersUpdateEvent {
	
	private final int screenId;
	
	private final WallpaperParameters parameter;
	
	public WallpaperParametersUpdateEvent(int screenId, WallpaperParameters parameter) {
		this.screenId = screenId;
		this.parameter = parameter;
	}
	
	public int getScreenId() {
		return screenId;
	}

	public WallpaperParameters getParameter() {
		return parameter;
	}
}
