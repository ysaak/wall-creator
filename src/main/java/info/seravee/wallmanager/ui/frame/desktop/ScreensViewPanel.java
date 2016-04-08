package info.seravee.wallmanager.ui.frame.desktop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;

import info.seravee.data.ScalingAlgorithm;
import info.seravee.utils.ImageScalerUtils;
import info.seravee.wallmanager.beans.profile.ProfileVersion;
import info.seravee.wallmanager.beans.profile.Screen;
import info.seravee.wallmanager.ui.frame.desktop.ScreenListener.ScreenAdapter;

/**
 * Created by ysaak on 01/02/15.
 */
public class ScreensViewPanel extends JComponent {
	private static final long serialVersionUID = -8461734317635643191L;

	/**
	 * Minimal border around monitors
	 */
	private static final int BORDER_WIDTH = 20;

    private Map<Integer, ScreenView> screens = new HashMap<>();

    /**
     * Full dimension of the screen
     */
    private Dimension screensSize = new Dimension(0, 0);
    
    private double screensScaleRatio = 1.0;
    
    private final Set<ScreenListener> screenListeners;

	public ScreensViewPanel() {
		
		screenListeners = new HashSet<>();
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				updateGraphicsData();
			}
		});
		
		screenListeners.add(new ScreenAdapter() {
			
			@Override
			public void screenSelected(Screen screen) {
				setSelectedScreen(screen);
			}
		});
	}
	
	public void setScreens(Collection<Screen> screenList) {
		clearScreens();
		screensSize.setSize(0, 0);
		
		for (Screen screen : screenList) {
			ScreenView view = new ScreenView(screen);
			
			for (ScreenListener l : screenListeners)
				view.addScreenListener(l);
	    	
	        screens.put(screen.getId(), view);
	        
	        add(view);
	        
	        screensSize.width = Math.max(screensSize.width, screen.getWidth() + screen.getX());
            screensSize.height = Math.max(screensSize.height, screen.getHeight() + screen.getY());
		}
		
		updateGraphicsData();
	}
	
	private void clearScreens() {
		for (ScreenView view : screens.values()) {
			for (ScreenListener l : screenListeners)
				view.removeScreenListener(l);
			
		}
		
		removeAll();
		screens.clear();
	}

	public void setProfileVersion(ProfileVersion version) {
		for (ScreenView view : screens.values()) {
			if (version != null) {
				view.setParameters(version.getScreenParameters(view.getScreen().getId()));
			}
			else {
				view.setParameters(null);
			}
		}
	}
	
	private void updateGraphicsData() {
		final Dimension componentSize = new Dimension(getWidth() - 2 * BORDER_WIDTH, getHeight() - 2 * BORDER_WIDTH);
		
		final Dimension scaledSize = ImageScalerUtils.getScaledImageDimensions(ScalingAlgorithm.STRETCH_KEEP_PROPORTION_NO_CROP, screensSize, componentSize);

        // Calculate the scale ratio of the components
        screensScaleRatio = scaledSize.getWidth() / screensSize.getWidth();
        
        // Compute initial screens position to have it centered
        final int baseX = (getWidth() - scaledSize.width) / 2;
        final int baseY = (getHeight() - scaledSize.height) / 2;
        
        for (ScreenView sv : screens.values()) {
        	final Rectangle r = new Rectangle(sv.getScreen().getBounds());
        	
        	r.x = baseX + ((int) Math.floor(r.x * screensScaleRatio));
        	r.y = baseY + ((int) Math.floor(r.y * screensScaleRatio));
        	r.width = ((int) Math.floor(r.width * screensScaleRatio));
        	r.height = ((int) Math.floor(r.height * screensScaleRatio));
        	
        	sv.setBounds(r);
        }
        
        repaint();
	}
	
	public void rebuildScreenImage(int screenId) {
		screens.get(screenId).rebuildImage();
	}

    @Override
    protected void paintComponent(Graphics g) {
    	//super.paintComponent(g);
    	
    	Graphics2D g2d = (Graphics2D) g;
    	g2d.clearRect(0, 0, getWidth(), getHeight());
    	
    	Color oldColor = g2d.getColor();
    	
    	g2d.setColor(Color.GRAY);
    	g2d.fillRect(0, 0, getWidth(), getHeight());
    	
    	g2d.setColor(oldColor);
    	
    	// Paint each screen
        for (ScreenView view : screens.values()) {
        	view.paint(g2d.create(view.getBounds().x, view.getBounds().y, view.getBounds().width, view.getBounds().height));
        }
    }

    public Collection<ScreenView> getDisplayers() {
        return screens.values();
    }
    
    public void setSelectedScreen(Screen screen) {
    	for (ScreenView sv : screens.values()) {
    		sv.setSelected(sv.getScreen().getId() == screen.getId());
    	}
    }
    
    public Screen getSelectedScreen() {
    	for (ScreenView sv : screens.values()) {
    		if (sv.isSelected()) {
    			return sv.getScreen();
    		}
    	}
    	return ((ScreenView[]) screens.values().toArray())[0].getScreen();
    }
    
    public void addScreenListener(ScreenListener l) {
		synchronized (screenListeners) {
			screenListeners.add(l);
		}
	}
	
	public void removeScreenListener(ScreenListener l) {
		synchronized (screenListeners) {
			screenListeners.remove(l);
		}
	}
}
