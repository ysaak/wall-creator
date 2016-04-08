package info.seravee.wallmanager.business.configuration.yaml;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Constructor;

import info.seravee.wallmanager.beans.Configuration;
import info.seravee.wallmanager.beans.DialogLastFolderType;

public class ConfigurationConstructor extends Constructor {
	public ConfigurationConstructor() {
		super(Configuration.class);
		
		TypeDescription configDescription = new TypeDescription(Configuration.class);
		configDescription.putListPropertyType("wallpapersFolders", String.class);
		configDescription.putMapPropertyType("lastFolders", DialogLastFolderType.class, String.class);
		addTypeDescription(configDescription);
	}
}
