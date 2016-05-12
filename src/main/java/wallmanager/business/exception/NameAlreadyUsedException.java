package wallmanager.business.exception;

public class NameAlreadyUsedException extends Exception {
	private static final long serialVersionUID = 3488896503115658509L;

	public NameAlreadyUsedException(String message) {
		super(message);
	}
}
