package wallmanager.ui.frame.editor;

import wallmanager.beans.profile.Profile;
import wallmanager.beans.profile.ProfileVersion;

interface ProfileEditorListener {
	void resetProfileVersion(Profile profile, ProfileVersion version);
	void storeProfile(Profile profile, ProfileVersion version, boolean setAsWallpaper);
}
