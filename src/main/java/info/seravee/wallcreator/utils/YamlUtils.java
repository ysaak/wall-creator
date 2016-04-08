package info.seravee.wallcreator.utils;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

public final class YamlUtils {
	
	public static final DumperOptions getOptions() {
		final DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		options.setIndent(4);
		options.setPrettyFlow(true);
		options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
		return options;
	}
	
	public static final Representer getRepresenter() {
		return new Representer();
	}
	
	@SuppressWarnings("unchecked")
	public static final <T> T load(Yaml yaml, Path file) throws IOException {
		try (Reader input = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
			return (T) yaml.load(input);
		}
	}
	
	public static final <T> void dump(Yaml yaml, Path file, T data) throws IOException {
		try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
			yaml.dump(data, writer);
		}
	}
}
