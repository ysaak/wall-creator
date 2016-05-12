package wallmanager.ui.frame.profiles;

import wallmanager.beans.profile.Profile;
import wallmanager.beans.profile.ProfileVersion;

interface ProfileListListener {
	void profileCreate(String name);
	void profileUpdated(Profile profile);
	
	void markProfileVersionAsPrefered(Profile profile, ProfileVersion version);
	
	void profileSelected(Profile profile);
	void profileVersionSelected(Profile profile, ProfileVersion version);
}
