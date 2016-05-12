package wallmanager.mock.dao;

import java.util.ArrayList;
import java.util.List;

import wallmanager.beans.profile.Profile;
import wallmanager.business.dao.ProfileDao;
import wallmanager.business.exception.NoDataFoundException;

public class InMemoryProfileDao implements ProfileDao {
	
	private List<Profile> storedProfiles = new ArrayList<>();

	@Override
	public List<Profile> list() {
		return storedProfiles;
	}

	@Override
	public Profile store(Profile profile) {
		storedProfiles.add(profile);
		return profile;
	}

	@Override
	public Profile get(String profileID) throws NoDataFoundException {
		for (Profile p : storedProfiles)
			if (p.getId().equals(profileID))
				return p;
		throw new NoDataFoundException("No profile found with this name");
	}
}
