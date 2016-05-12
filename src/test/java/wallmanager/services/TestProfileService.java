package wallmanager.services;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import com.google.inject.Inject;

import wallmanager.TestServiceModule;
import wallmanager.beans.profile.Profile;
import wallmanager.business.exception.ConfigurationAlreadyUsedException;
import wallmanager.business.exception.NameAlreadyUsedException;
import wallmanager.business.exception.NoDataFoundException;
import wallmanager.business.profile.ProfileService;

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
