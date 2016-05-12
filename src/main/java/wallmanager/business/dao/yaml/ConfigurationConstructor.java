package wallmanager.business.dao.yaml;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.Constructor;

import wallmanager.beans.Configuration;
import wallmanager.beans.DialogLastFolderType;

public class ConfigurationConstructor extends Constructor {
	public ConfigurationConstructor() {
		super(Configuration.class);
		
		TypeDescription configDescription = new TypeDescription(Configuration.class);
		configDescription.putListPropertyType("wallpapersFolders", String.class);
		configDescription.putMapPropertyType("lastFolders", DialogLastFolderType.class, String.class);
		addTypeDescription(configDescription);
	}
}
