package info.seravee.wallmanager.ui.frame.events;

import java.io.File;

import lombok.Data;

@Data
public class WallpaperSelectedEvent {
	private final int screenId;
	private final File imageFile;
}
