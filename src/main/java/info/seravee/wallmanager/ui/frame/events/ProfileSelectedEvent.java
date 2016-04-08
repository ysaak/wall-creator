package info.seravee.wallmanager.ui.frame.events;

import info.seravee.wallmanager.beans.profile.Profile;
import lombok.Data;
import lombok.NonNull;

@Data
public class ProfileSelectedEvent {
	@NonNull
	private final Profile profile;
}
