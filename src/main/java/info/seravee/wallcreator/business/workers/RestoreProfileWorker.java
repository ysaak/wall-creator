package info.seravee.wallcreator.business.workers;

import java.io.IOException;

import javax.swing.SwingWorker;

import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.business.Services;

public class RestoreProfileWorker extends SwingWorker<Profile, Void> {
	
	private final Profile profileToRestore;
	
	public RestoreProfileWorker(Profile profileToRestore) {
		super();
		if (profileToRestore == null) {
			throw new IllegalArgumentException("profileToRestore cannot be null");
		}
		
		this.profileToRestore = profileToRestore;
	}

	@Override
	protected Profile doInBackground() throws Exception {
		
		final Profile storedProfile;
		try {
			storedProfile = Services.getProfileService().get(profileToRestore.getId());
		} 
		catch (IOException e) {
			throw e; 
		}
		
		profileToRestore.setVersions(storedProfile.getVersions());
		
		return profileToRestore;
	}

}
