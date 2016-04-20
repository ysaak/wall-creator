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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import com.google.common.base.Preconditions;

import info.seravee.wallcreator.platform.Platforms;
import info.seravee.wallcreator.utils.YamlUtils;
import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.beans.profile.ProfileVersion;
import info.seravee.wallmanager.beans.profile.Screen;
import info.seravee.wallmanager.beans.profile.WallpaperParameters;
import info.seravee.wallmanager.business.exception.profile.ProfileStoreException;
import info.seravee.wallmanager.business.profiles.yaml.ProfileContructor;
import info.seravee.wallmanager.business.profiles.yaml.ProfileRepresenter;

public class ProfileManager implements ProfileService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileService.class);

	@Override
	public List<Profile> list() throws ProfileStoreException {
		List<Profile> profiles = new ArrayList<>();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(getProfilesPath(), "*.yml")) {
			Iterator<Path> iterator = stream.iterator();
			while (iterator.hasNext()) {
				profiles.add((Profile) YamlUtils.load(getYamlObject(), iterator.next()));
			}
		}
		catch (IOException e) {
			LOGGER.error("Error while listing profile from directories");
			throw new RuntimeException(e);
		}
		
		if (profiles.size() == 0) {
			// No profile defined, create a default one
			Profile defaultProfile = createProfile("Default");
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
	public void store(Profile profile) throws ProfileStoreException {
		final Path file;
		try {
			file = getProfilesPath().resolve(profile.getId() + ".yml");
		}
		catch (IOException e) {
			throw new ProfileStoreException("Error while creating profile folder", e);
		}
	
		try {
			YamlUtils.dump(getYamlObject(), file, profile);
		}
		catch (IOException e) {
			throw new ProfileStoreException("Error while storing profile", e);
		}
	}
	
	/* --- Profile's actions --- */
	
	@Override
	public Profile createProfile(String name) throws ProfileStoreException {
		Profile profile = new Profile();
		profile.setName(name);
		profile.setConfiguration(Platforms.get().getDesktopConfiguration());
		
		// Create default version
		ProfileVersion version = new ProfileVersion();
		version.setName("Default");
		version.setPreferred(true);
		
		for (Screen screen : profile.getConfiguration()) {
			WallpaperParameters parameters = new WallpaperParameters();
			parameters.setScreenId(screen.getId());
			
			version.getParameters().add(parameters);
		}
		
		profile.getVersions().add(version);
		
		store(profile);
		
		return profile;
	}
	
	/* --- Versions --- */

	@Override
	public Profile deleteVersion(Profile profile, ProfileVersion versionToDelete) throws IOException {
		Preconditions.checkNotNull(profile);
		Preconditions.checkNotNull(versionToDelete);
		
		profile.getVersions().remove(versionToDelete);
		
		if (versionToDelete.isPreferred()) {
			profile.getVersions().get(0).setPreferred(true);
		}
		
		
		return profile;
	}

	@Override
	public Profile setPreferredVersion(Profile profile, ProfileVersion preferredVersion) throws IOException, ProfileStoreException {
		if (!profile.getVersions().contains(preferredVersion)) {
			LOGGER.warn("Version '" + preferredVersion.getName() + "' not contained in this profile '" + profile.getName() + "'");
			return profile;
		}
		
		for (ProfileVersion version : profile.getVersions()) {
			version.setPreferred(version.equals(preferredVersion));
		}
		
		store(profile);

		return profile;
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
