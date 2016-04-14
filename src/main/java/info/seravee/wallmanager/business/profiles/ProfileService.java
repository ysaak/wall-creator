package info.seravee.wallmanager.business.profiles;

import java.io.IOException;
import java.util.List;

import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.beans.profile.ProfileVersion;
import info.seravee.wallmanager.business.exception.profile.ProfileStoreException;

public interface ProfileService {
	/**
	 * List stored profiles
	 * @return
	 * @throws ProfileStoreException 
	 */
	List<Profile> list() throws ProfileStoreException;
	
	/**
	 * Store a profile
	 * @param profile
	 * @throws ProfileStoreException 
	 */
	void store(Profile profile) throws ProfileStoreException;
	
	/**
	 * 
	 * @param profileID
	 * @return
	 * @throws IOException
	 */
	Profile get(String profileID) throws IOException;
	
	
	
	/* --- Versions --- */
	
	Profile deleteVersion(Profile profile, ProfileVersion versionToDelete) throws IOException;
	
	Profile setPreferredVersion(Profile profile, ProfileVersion preferredVersion) throws IOException, ProfileStoreException;
}
