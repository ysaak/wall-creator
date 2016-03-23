package info.seravee.business.config;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import info.seravee.business.exceptions.ConfigurationException;
import info.seravee.data.config.Configuration;
import info.seravee.data.config.DialogLastFolderType;
import info.seravee.wallcreator.platform.Platforms;

public class ConfigurationManager {

	private static Configuration currentConfig = new Configuration();
	
	public static final void load() throws ConfigurationException {
		final Path configFilePath = getConfigFile();
		
		if (Files.exists(configFilePath)) {
		
			// Init YAML loader
			Yaml yaml = getYamlObject();
	
			try {
				currentConfig = (Configuration) yaml.load(Files.newInputStream(configFilePath));
			} catch (IOException e) {
				throw new ConfigurationException("Error while reading config file", e);
			}
		}
	}
	
	public static final Configuration get() {
		return currentConfig;
	}
	
	public static final void store(Configuration config) throws ConfigurationException {
		final Path configFilePath = getConfigFile();
		final Yaml yaml = getYamlObject();
		
		currentConfig = config;
		
		try (Writer writer = Files.newBufferedWriter(configFilePath, StandardCharsets.UTF_8)) {
			yaml.dump(config, writer);
		}
		catch (IOException e) {
			throw new ConfigurationException("Error while storing configuration data", e);
		}
	}
	
	private static final Path getConfigFile() {
		return Platforms.get().getAppDirectory().resolve("config.yml");
	}
	
	private static final Yaml getYamlObject() {
		final Constructor constructor = new Constructor(Configuration.class); // Configuration.class is root
		TypeDescription configDescription = new TypeDescription(Configuration.class);
		configDescription.putListPropertyType("wallpapersFolders", String.class);
		configDescription.putMapPropertyType("lastFolders", DialogLastFolderType.class, String.class);
		constructor.addTypeDescription(configDescription);
		
		final DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		options.setIndent(4);
		options.setPrettyFlow(true);
		options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
		
		return new Yaml(constructor, new Representer(), options);
	}
}
