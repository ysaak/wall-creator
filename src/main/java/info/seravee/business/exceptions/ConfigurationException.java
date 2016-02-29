package info.seravee.business.exceptions;

public class ConfigurationException extends Exception {
	private static final long serialVersionUID = 3292347248002242782L;
	
	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

}
