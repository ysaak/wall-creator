package info.seravee.wallmanager.business.profiles;

public class ProfileException extends Exception {
	private static final long serialVersionUID = -5121541174235021107L;

	public ProfileException(String message) {
		super(message);
	}
	
	public ProfileException(String message, Throwable cause) {
		super(message, cause);
	}

}
