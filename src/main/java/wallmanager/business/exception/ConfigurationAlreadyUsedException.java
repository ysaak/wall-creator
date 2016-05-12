package wallmanager.business.exception;

public class ConfigurationAlreadyUsedException extends Exception {
	private static final long serialVersionUID = 3215909656458148798L;

	public ConfigurationAlreadyUsedException(String message) {
		super(message);
	}
}
