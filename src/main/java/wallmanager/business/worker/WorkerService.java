package wallmanager.business.worker;

import wallmanager.ui.commons.component.LockableFrame;

public interface WorkerService {
	/**
	 * 
	 * @param frame
	 */
	void setMainFrame(LockableFrame frame);
	
	<T> void schedule(AbstractWorker<T> worker);
}
