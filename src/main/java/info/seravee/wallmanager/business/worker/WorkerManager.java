package info.seravee.wallmanager.business.worker;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;

import info.seravee.wallmanager.ui.commons.components.LockableFrame;

@Singleton
public class WorkerManager implements WorkerService {
	
	private final Executor executor;
	
	private LockableFrame frame = null;

	public WorkerManager() {
		this.executor = Executors.newCachedThreadPool();
	}
	
	@Override
	public void setMainFrame(LockableFrame frame) {
		this.frame = frame;
	}

	@Override
	public <T> void schedule(AbstractWorker<T> worker) {
		Preconditions.checkNotNull(worker);
		this.executor.execute(new Worker<T>(worker));
	}
	
	private class Worker<WT> implements Runnable {
		
		private final AbstractWorker<WT> aWorker;
		
		public Worker(final AbstractWorker<WT> aWorker) {
			this.aWorker = aWorker;
		}
		
		public void run() {
			if (aWorker.shouldLockMainScreen()) {
				lockScreen(true, aWorker.getMessage());
			}
			
			try {
				final WT result = aWorker.doInBackground();
				
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						aWorker.done(result);
					}
				});
				
				if (aWorker.shouldLockMainScreen()) {
					lockScreen(false, null);
				}
			}
			catch (final Throwable t) {
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						aWorker.error(t);
					}
				});
				
				if (aWorker.shouldLockMainScreen()) {
					lockScreen(false, null);
				}
			}
		}
		
		private void lockScreen(final boolean lock, final String message) {
			if (frame != null) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						frame.lockScreen(lock, message);
					}
				});
			}
		}
	}
}
