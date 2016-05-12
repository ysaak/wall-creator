package wallmanager.business.dao.yaml;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import wallmanager.business.exception.NoDataFoundException;
import wallmanager.business.exception.TechnicalException;

abstract class AbstractYamlStore<T> {
	private final Yaml yaml;
	
	public AbstractYamlStore(Constructor constructor, Representer representer) {
		yaml = new Yaml(constructor, representer, getOptions());
		yaml.addImplicitResolver(new Tag("!color"), Pattern.compile("rgb\\((\\s*(?:(\\d{1,3})\\s*,?){3})\\)"), "r");
	}

	protected abstract Path getStoreLocation();
	
	private final Path getCheckStoreLocation() throws TechnicalException {
		Path location = getStoreLocation();
		
		if (Files.notExists(location)) {
			try {
				Files.createDirectories(location);
			} catch (IOException e) {
				new TechnicalException("Error while creating directory", e);
			}
		}
		
		return location;
	}
	
	
	public T get(String name) throws NoDataFoundException {
		Path file = getCheckStoreLocation().resolve(name + ".yml");
		
		if (Files.notExists(file)) {
			throw new NoDataFoundException("No data found for code " + name);
		}
		
		return get(file);
	}
	
	@SuppressWarnings("unchecked")
	private T get(Path file) {
		try (Reader input = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
			return (T) yaml.load(input);
		}
		catch (IOException e) {
			throw new TechnicalException("Error while reading file " + file.toString(), e);
		}
	}
	
	protected void delete(String name) {
		Path file = getCheckStoreLocation().resolve(name + ".yml");
		try {
			Files.deleteIfExists(file);
		} catch (IOException e) {
			throw new TechnicalException("Error while removing file " + file.toString(), e);
		}
	}
	
	protected void store(T data, String name) {
		final Path file = getCheckStoreLocation().resolve(name + ".yml");
	
		try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
			yaml.dump(data, writer);
		}
		catch (IOException e) {
			throw new TechnicalException("Error while storing profile", e);
		}
	}
	
	public List<T> list() {
		List<T> dataList = new ArrayList<>();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(getCheckStoreLocation(), "*.yml")) {
			Iterator<Path> iterator = stream.iterator();
			while (iterator.hasNext()) {
				dataList.add(get(iterator.next()));
			}
		}
		catch (IOException e) {
			throw new TechnicalException("Error while listing directory content", e);
		}
		
		return dataList;
	}
	
	private final DumperOptions getOptions() {
		final DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		options.setIndent(4);
		options.setPrettyFlow(true);
		options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
		return options;
	}
}
