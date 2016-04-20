package info.seravee.wallmanager.ui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import info.seravee.wallmanager.ui.commons.laf.LafUtils;

public abstract class ApplicationUI {
	
	public static final void initUI() {
		try {
    		// Set System L&F
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } 
    	catch (Exception e) { }
    	
    	LafUtils.setUIDefaultFont();
	}
	
	
	
	protected abstract void build();
	protected abstract void display();
	
	public void run() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				build();
				display();
			}
		});
	}
}
