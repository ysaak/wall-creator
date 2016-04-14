package info.seravee.wallmanager.business.exception.profile;

public class ProfileStoreException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public ProfileStoreException(String message) {
		super(message);
	}

	public ProfileStoreException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
