package info.seravee.wallcreator.platform;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.common.io.Files;

import info.seravee.business.files.FileUtils;

public class WindowsPlatform extends AbstractPlaform {

	@Override
	public Path getAppDirectory() {
		return super.getAppDirectory();
		//return Paths.get(System.getenv("APPDATA"), "WallCreator");
	}
	
	@Override
	public void setWallpaper(File wallpaper) {
		
		
		File tempWallpaper = getAppDirectory().resolve("wallpaper.png").toFile();
		
		// Copy wallpaper to temp location
		try {
			Files.copy(wallpaper, tempWallpaper);
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		
		
		
		// Generate ouput file
		final Path outfile = Paths.get(getAppDirectory().toString(), "wallsetter.ps1");
		
		try {
			FileUtils.exportResource("/info/seravee/wallcreator/platform/windows/wallsetter.ps1", outfile.toFile());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Exec file
		Process setterProcess = null;
		try {
			System.err.println(wallpaper.toString());
			
			setterProcess = Runtime.getRuntime().exec(new String[] {
					"powershell.exe", "-file", outfile.toString(), tempWallpaper.toString()
			});
			setterProcess.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
