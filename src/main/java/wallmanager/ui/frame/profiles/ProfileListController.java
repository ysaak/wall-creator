package wallmanager.ui.frame.profiles;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import wallmanager.beans.profile.Profile;
import wallmanager.beans.profile.ProfileVersion;
import wallmanager.business.event.EventBusLine;
import wallmanager.business.event.EventService;
import wallmanager.business.profile.ProfileService;
import wallmanager.business.worker.AbstractWorker;
import wallmanager.business.worker.WorkerService;
import wallmanager.ui.commons.WMController;
import wallmanager.ui.frame.events.ProfileSelectedEvent;
import wallmanager.ui.frame.events.ProfileVersionSelectedEvent;

public class ProfileListController extends WMController implements ProfileListListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileListController.class);

	protected final ProfileService profileService;
	protected final WorkerService workerService;
	private final EventService eventService;
	
	private ProfilesListPanel panel;

	@Inject
	public ProfileListController(ProfileService profileService, WorkerService workerService, EventService eventService) {
		this.profileService = profileService;
		this.workerService = workerService;
		this.eventService = eventService;
	}
	
	public void setPanel(ProfilesListPanel panel) {
		this.panel = panel;
		this.panel.setListener(this);
	}
	
	@Override
	public void start() {
		workerService.schedule(new LoadProfileWorker());
	}

	@Override
	public void profileCreate(final String name) {
		workerService.schedule(new CreateProfileWorker(name));
	}
	
	@Override
	public void profileUpdated(Profile profile) {
		workerService.schedule(new UpdateProfileWorker(profile));
	}
	
	@Override
	public void markProfileVersionAsPrefered(Profile profile, ProfileVersion version) {
		workerService.schedule(new SetVersionAsPreferredWorker(profile, version));
	}
	
	@Override
	public void profileSelected(Profile profile) {
		eventService.post(EventBusLine.FRAME, new ProfileSelectedEvent(profile));
	}
	
	@Override
	public void profileVersionSelected(Profile profile, ProfileVersion version) {
		eventService.post(EventBusLine.FRAME, new ProfileVersionSelectedEvent(profile, version));
	}
	
	private class LoadProfileWorker extends AbstractWorker<List<Profile>> {
		
		@Override
		public List<Profile> doInBackground() throws Throwable {
			return profileService.list();
		}
		
		@Override
		public void done(List<Profile> result) {
			panel.setProfiles(result);
		}
	}
	
	private class CreateProfileWorker extends AbstractWorker<Profile> {
		
		private final String name;
		
		public CreateProfileWorker(String name) {
			super();
			this.name = name;
		}

		@Override
		public Profile doInBackground() throws Throwable {
			return profileService.createProfile(name);
		}
		
		@Override
		public void done(Profile result) {
			panel.addProfile(result);
		}
	}
	
	private class UpdateProfileWorker extends AbstractWorker<Profile> {
		
		private final Profile profile;
		
		public UpdateProfileWorker(final Profile profile) {
			this.profile = profile;
			withMainScreenLocked();
		}
		
		@Override
		public Profile doInBackground() throws Throwable {
			profileService.store(profile);
			return profile;
		}
		
		@Override
		public void done(Profile result) {
			// TODO Auto-generated method stub
		}
	}
	
	private class SetVersionAsPreferredWorker extends AbstractWorker<Profile> {
		private final Profile profile;
		private final ProfileVersion preferredVersion;
		
		public SetVersionAsPreferredWorker(Profile profile, ProfileVersion preferredVersion) {
			super();
			this.profile = Preconditions.checkNotNull(profile);
			this.preferredVersion = Preconditions.checkNotNull(preferredVersion);
			
			withMainScreenLocked();
		}
		
		@Override
		public Profile doInBackground() throws Throwable {
			
			if (!profile.getVersions().contains(preferredVersion)) {
				LOGGER.warn("Version '" + preferredVersion.getName() + "' not contained in this profile '" + profile.getName() + "'");
				return profile;
			}
			
			for (ProfileVersion version : profile.getVersions()) {
				version.setPreferred(version.equals(preferredVersion));
			}
			
			
			profileService.store(profile);
			
			return profile;
		}
		
		@Override
		public void done(Profile result) {/**/}
	}
}
