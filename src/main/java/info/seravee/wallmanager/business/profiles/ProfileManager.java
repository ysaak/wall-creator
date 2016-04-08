package info.seravee.wallmanager.business.profiles;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import info.seravee.wallcreator.platform.Platforms;
import info.seravee.wallcreator.utils.YamlUtils;
import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.beans.profile.ProfileVersion;
import info.seravee.wallmanager.beans.profile.Screen;
import info.seravee.wallmanager.beans.profile.WallpaperParameters;
import info.seravee.wallmanager.business.profiles.yaml.ProfileContructor;
import info.seravee.wallmanager.business.profiles.yaml.ProfileRepresenter;

public class ProfileManager implements ProfileService {

	@Override
	public List<Profile> list() throws IOException {
		List<Profile> profiles = new ArrayList<>();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(getProfilesPath(), "*.yml")) {
			Iterator<Path> iterator = stream.iterator();
			while (iterator.hasNext()) {
				profiles.add((Profile) YamlUtils.load(getYamlObject(), iterator.next()));
			}
		}
		
		if (profiles.size() == 0) {
			// No profile defined, create a default one
			Profile defaultProfile = createDefaultProfile();
			store(defaultProfile);
			profiles.add(defaultProfile);
		}
		
		return profiles;
	}
	
	public Profile get(String profileID) throws IOException {
		Path file = getProfilesPath().resolve(profileID + ".yml");
		
		if (Files.notExists(file)) {
			throw new FileNotFoundException();
		}
		
		return YamlUtils.load(getYamlObject(), file);
	}

	@Override
	public void store(Profile profile) throws IOException {
		Path file = getProfilesPath().resolve(profile.getId() + ".yml");

		YamlUtils.dump(getYamlObject(), file, profile);
	}
	
	/**
	 * Create a default profile based on the current desktop configuration
	 * @return Default profile
	 */
	private Profile createDefaultProfile() {
		Profile defaultProfile = new Profile();
		defaultProfile.setName("Default");
		defaultProfile.setConfiguration(Platforms.get().getDesktopConfiguration());
		
		// Create default version
		ProfileVersion version = new ProfileVersion();
		version.setName("Default");
		version.setPreferred(true);
		
		for (Screen screen : defaultProfile.getConfiguration()) {
			WallpaperParameters parameters = new WallpaperParameters();
			parameters.setScreenId(screen.getId());
			
			version.getParameters().add(parameters);
		}
		
		defaultProfile.getVersions().add(version);
		
		return defaultProfile;
	}

	private static final Path getProfilesPath() throws IOException {
		Path profilesDirectory = Platforms.get().getAppDirectory().resolve("profiles");

		if (Files.notExists(profilesDirectory)) {
			Files.createDirectories(profilesDirectory);
		}

		return profilesDirectory;
	}
	
	private static final Yaml getYamlObject() {
		final Constructor constructor = new ProfileContructor();
		final Representer representer = new ProfileRepresenter();
		
		Yaml yaml = new Yaml(constructor, representer, YamlUtils.getOptions());
		yaml.addImplicitResolver(new Tag("!color"), Pattern.compile("rgb\\((\\s*(?:(\\d{1,3})\\s*,?){3})\\)"), "r");
		
		return yaml;
	}
}