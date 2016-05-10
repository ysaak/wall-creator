package wallmanager;

public abstract class TestModule<SERVICE> {
	
	protected final SERVICE service;

	public TestModule(SERVICE service) {
		this.service = service;
	}

}
