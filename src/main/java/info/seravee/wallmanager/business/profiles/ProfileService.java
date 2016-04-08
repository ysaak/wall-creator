package info.seravee.wallmanager.business.profiles;

import java.io.IOException;
import java.util.List;

import info.seravee.wallmanager.beans.profile.Profile;

public interface ProfileService {
	/**
	 * List stored profiles
	 * @return
	 * @throws IOException 
	 */
	List<Profile> list() throws IOException;
	
	/**
	 * Store a profile
	 * @param profile
	 * @throws IOException 
	 */
	void store(Profile profile) throws IOException;
	
	/**
	 * 
	 * @param profileID
	 * @return
	 * @throws IOException
	 */
	Profile get(String profileID) throws IOException;
}
