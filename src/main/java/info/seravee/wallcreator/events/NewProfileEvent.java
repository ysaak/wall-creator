package info.seravee.wallcreator.events;

import info.seravee.wallcreator.beans.Profile;

public class NewProfileEvent {
	private final Profile newProfile;

	public NewProfileEvent(Profile newProfile) {
		this.newProfile = newProfile;
	}
	
	public Profile getNewProfile() {
		return newProfile;
	}
}
