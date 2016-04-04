package info.seravee.wallcreator.events;

import info.seravee.wallcreator.beans.Profile;

public class SelectedProfileEvent {
	private final Profile selectedProfile;

	public SelectedProfileEvent(Profile selectedProfile) {
		this.selectedProfile = selectedProfile;
	}

	public Profile getSelectedProfile() {
		return selectedProfile;
	}
}
