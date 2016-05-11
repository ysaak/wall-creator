package wallmanager.services;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.google.inject.Inject;

import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.business.exception.NoDataFoundException;
import info.seravee.wallmanager.business.exception.profile.ConfigurationAlreadyUsedException;
import info.seravee.wallmanager.business.exception.profile.NameAlreadyUsedException;
import info.seravee.wallmanager.business.profiles.ProfileService;
import wallmanager.TestServiceModule;

@Guice(modules = TestServiceModule.class)
public class TestProfileService {
	
	private final ProfileService service;

	@Inject
	public TestProfileService(ProfileService service) {
		this.service = service;
	}

	@Test
	public void testListNeverReturnNull() {
		List<Profile> list = service.list();
		Assert.assertNotNull(list);
	}
	
	@Test(dependsOnMethods="testListNeverReturnNull")
	public void testListAlwaysReturnAProfile() {
		List<Profile> list = service.list();
		Assert.assertTrue(list.size() > 0);
		Assert.assertNotNull(list.get(0));
	}
	
	@Test(dependsOnMethods="testListNeverReturnNull", expectedExceptions = { NameAlreadyUsedException.class })
	public void testCannotAddProfileWithSameName() throws NameAlreadyUsedException, ConfigurationAlreadyUsedException {
		List<Profile> list = service.list();
		Profile listProfile = list.get(0);
		
		// Try recreate a profile with the same name
		service.createProfile(listProfile.getName());
	}
	
	@Test(dependsOnMethods="testListAlwaysReturnAProfile")
	public void testGetExistingProfile() throws NoDataFoundException {
		
		List<Profile> list = service.list();
		Profile listProfile = list.get(0);
		
		Profile requestedProfile = service.get(listProfile.getId());
		
		Assert.assertNotNull(requestedProfile);
		Assert.assertEquals(listProfile, requestedProfile);
	}
	
	@Test(dependsOnMethods="testListAlwaysReturnAProfile", expectedExceptions = { NoDataFoundException.class })
	public void testGetNonExistingProfile() throws NoDataFoundException {
		service.get("xxxx");
	}
	
	@Test(dependsOnMethods="testCannotAddProfileWithSameName", expectedExceptions = { ConfigurationAlreadyUsedException.class })
	public void testNoDuplicateConfiguration() throws NameAlreadyUsedException, ConfigurationAlreadyUsedException {
		
		service.createProfile("New one");
		
	}
}
