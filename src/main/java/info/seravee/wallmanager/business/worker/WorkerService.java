package info.seravee.wallmanager.business.worker;

import info.seravee.wallmanager.ui.commons.components.LockableFrame;

public interface WorkerService {
	/**
	 * 
	 * @param frame
	 */
	void setMainFrame(LockableFrame frame);
	
	<T> void schedule(AbstractWorker<T> worker);
}
