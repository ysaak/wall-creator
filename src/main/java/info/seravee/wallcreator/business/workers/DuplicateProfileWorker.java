package info.seravee.wallcreator.business.workers;

import java.util.ArrayList;

import javax.swing.SwingWorker;

import info.seravee.wallcreator.beans.Profile;
import info.seravee.wallcreator.beans.Screen;
import info.seravee.wallcreator.business.Services;
import info.seravee.wallcreator.events.NewProfileEvent;
import info.seravee.wallcreator.events.SelectedProfileEvent;

public class DuplicateProfileWorker extends SwingWorker<Void, Void> {
	
	private final Profile profileToDuplicate;
	private final String name;
	
	public DuplicateProfileWorker(final Profile profileToDuplicate, final String name) {
		super();
		if (profileToDuplicate == null) {
			throw new IllegalArgumentException("profileToRestore cannot be null");
		}
		
		this.profileToDuplicate = profileToDuplicate;
		this.name = name;
	}

	@Override
	protected Void doInBackground() throws Exception {
		
		final Profile duplicatedProfile = new Profile();
		duplicatedProfile.setName(name);
		duplicatedProfile.setScreens(new ArrayList<Screen>());
		
		for (Screen screen : profileToDuplicate.getScreens()) {
			duplicatedProfile.getScreens().add(screen.clone());
		}
		
		Services.getProfileService().store(duplicatedProfile);
		Services.getEventService().post(new NewProfileEvent(duplicatedProfile));
		Services.getEventService().post(new SelectedProfileEvent(duplicatedProfile));
		return null;
	}

}
