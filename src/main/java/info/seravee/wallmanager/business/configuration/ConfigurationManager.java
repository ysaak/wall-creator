package info.seravee.wallmanager.business.configuration;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.yaml.snakeyaml.Yaml;

import com.google.inject.Singleton;

import info.seravee.wallcreator.platform.Platforms;
import info.seravee.wallcreator.utils.YamlUtils;
import info.seravee.wallmanager.beans.Configuration;
import info.seravee.wallmanager.business.configuration.yaml.ConfigurationConstructor;

@Singleton
public class ConfigurationManager implements ConfigurationService {

	private Configuration currentConfig = new Configuration();
	
	@Override
	public final void load() throws ConfigurationException {
		final Path configFilePath = getConfigFile();
		
		if (Files.exists(configFilePath)) {
		
			try {
				currentConfig = YamlUtils.load(getYamlObject(), configFilePath);
			} 
			catch (IOException e) {
				throw new ConfigurationException("Error while reading config file", e);
			}
		}
	}
	
	@Override
	public final Configuration get() {
		return currentConfig;
	}
	
	@Override
	public final void store(Configuration config) throws ConfigurationException {
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
		return new Yaml(new ConfigurationConstructor(), YamlUtils.getRepresenter(), YamlUtils.getOptions());
	}
}
