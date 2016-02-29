package info.seravee.business;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import info.seravee.business.exceptions.ConfigurationException;
import info.seravee.platform.Platforms;

public class Configuration {

	private static final Configuration INSTANCE;
	static {
		INSTANCE = new Configuration();
	}
	
	public static Configuration get() {
		return INSTANCE;
	}
	
	/* ----------------------------------------- */
	
	private Configuration() {/**/}

	public void initPaths() throws ConfigurationException {
		boolean init = false;
		
		Path appDir = Platforms.get().getAppDirectory();
		
		if (Files.notExists(appDir)) {
			try {
				Files.createDirectories(appDir);
			} catch (IOException e) {
				throw new ConfigurationException("Error while creating application directory", e);
			}

			init = true;
		}
		
		Path configFilePath = appDir.resolve("config.yml");
		
	}
}
