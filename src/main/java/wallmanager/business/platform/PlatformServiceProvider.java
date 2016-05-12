package wallmanager.business.platform;

import com.google.inject.Provider;

public class PlatformServiceProvider implements Provider<PlatformService> {
	@Override
	public PlatformService get() {
		return new WindowsPlatform();
	}
}
