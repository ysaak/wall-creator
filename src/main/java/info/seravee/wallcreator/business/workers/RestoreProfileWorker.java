package info.seravee.wallcreator.business.workers;

import java.io.IOException;

import javax.swing.SwingWorker;

import info.seravee.wallcreator.beans.Profile;
import info.seravee.wallcreator.beans.Screen;
import info.seravee.wallcreator.business.Services;

public class RestoreProfileWorker extends SwingWorker<Void, Void> {
	
	private final Profile profileToRestore;
	
	public RestoreProfileWorker(Profile profileToRestore) {
		super();
		if (profileToRestore == null) {
			throw new IllegalArgumentException("profileToRestore cannot be null");
		}
		
		this.profileToRestore = profileToRestore;
	}

	@Override
	protected Void doInBackground() throws Exception {
		
		final Profile storedProfile;
		try {
			storedProfile = Services.getProfileService().get(profileToRestore.getId());
		} 
		catch (IOException e) {
			throw e; 
		}
		
		
		for (Screen opScreen : profileToRestore.getScreens()) {
			for (Screen spScreen : storedProfile.getScreens()) {
				if (opScreen.getId() == spScreen.getId()) {
					opScreen.copyFrom(spScreen);
					break;
				}
			}
		}

		return null;
	}

}
