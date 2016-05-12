package wallmanager.business.profile;

import java.io.IOException;
import java.util.List;

import wallmanager.beans.profile.Profile;
import wallmanager.beans.profile.ProfileVersion;
import wallmanager.business.exception.ConfigurationAlreadyUsedException;
import wallmanager.business.exception.NameAlreadyUsedException;
import wallmanager.business.exception.NoDataFoundException;

public interface ProfileService {
	/**
	 * List stored profiles
	 * @return
	 */
	List<Profile> list();
	
	/**
	 * Store a profile
	 * @param profile
	 * @return 
	 */
	Profile store(Profile profile);
	
	/**
	 * 
	 * @param profileID
	 * @return
	 * @throws NoDataFoundException
	 */
	Profile get(String profileID) throws NoDataFoundException;
	
	
	/* --- Profiles --- */
	/**
	 * Create a new profile with the specified name based on the current configuration
	 * @param name New profile name
	 * @return new profile
	 * @throws NameAlreadyUsedException 
	 * @throws ConfigurationAlreadyUsedException 
	 */
	Profile createProfile(String name) throws NameAlreadyUsedException, ConfigurationAlreadyUsedException;
	
	
	/* --- Versions --- */
	
	Profile deleteVersion(Profile profile, ProfileVersion versionToDelete) throws IOException;
}
