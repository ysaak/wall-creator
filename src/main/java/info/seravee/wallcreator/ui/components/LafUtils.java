package info.seravee.wallcreator.ui.components;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class LafUtils {
	public static final RenderingHints ANTIALIASING_HINTS;
	static {
		ANTIALIASING_HINTS = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		ANTIALIASING_HINTS.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	}
	
	public static final void setUIDefaultFont() {
		
		Font defaultFont = null;
		
		// Search font
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    Font[] fonts = e.getAllFonts(); // Get the fonts
	    for (Font f : fonts) {
	    	
	    	if ("verdana".equals(f.getFontName().toLowerCase())) {
	    		defaultFont = f;
	    		break;
	    	}
	    }
	    
	    if (defaultFont == null) {
	    	return;
	    }
	    
	    
	    Enumeration<Object> keys = UIManager.getDefaults().keys();
	    
	    while (keys.hasMoreElements()) {
	    	Object key = keys.nextElement();
	    	Object value = UIManager.get(key);
	    	
	    	if (value != null && value instanceof FontUIResource) {
	    		FontUIResource oldFUR = (FontUIResource) value;
	    		
	    		UIManager.put(key, new FontUIResource(defaultFont.getFontName(), oldFUR.getStyle(), oldFUR.getSize()));
	    	}
	    	
	    }
	}
}
