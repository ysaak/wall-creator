package info.seravee.wallmanager.business.worker;

public abstract class AbstractWorker<T> {
	
	private boolean lockMainScreen = false;
	private String message = null;
	
	public abstract T doInBackground() throws Throwable;
	
	public abstract void done(T result);
	
	public void error(Throwable error) {
		// Override to use
	}
	
	public void withMessage(String message) {
		this.message = message;
	}
	
	public void withMainScreenLocked() {
		withMainScreenLocked(null);
	}
	
	public void withMainScreenLocked(String message) {
		this.lockMainScreen = true;
		this.message = message;
	}

	public boolean shouldLockMainScreen() {
		return lockMainScreen;
	}
	
	public String getMessage() {
		return message;
	}
}
