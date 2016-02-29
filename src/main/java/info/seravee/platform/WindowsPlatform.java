package info.seravee.platform;

import java.nio.file.Path;

public class WindowsPlatform extends AbstractPlaform {

	@Override
	public Path getAppDirectory() {
		return super.getAppDirectory();
		//return Paths.get(System.getenv("APPDATA"), "WallCreator");
	}
	
		// Set wallpaper : https://gallery.technet.microsoft.com/scriptcenter/Set-Random-Wallpaper-Using-e79e4235
}
