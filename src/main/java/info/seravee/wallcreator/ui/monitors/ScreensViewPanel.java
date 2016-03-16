package info.seravee.wallcreator.ui.monitors;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import info.seravee.data.ScalingAlgorithm;
import info.seravee.data.Screen;
import info.seravee.data.ScreenWallpaper;
import info.seravee.utils.ImageScalerUtils;

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

    private final ScreenListener screenListener = new ScreenListener() {
		
		@Override
		public void screenSelected(int id) {
			setSelectedMonitor(id);
		}
	}; 
	
	public ScreensViewPanel() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				updateGraphicsData();
			}
		});
	}
	
	public void setScreens(List<Screen> screenList) {
		clearScreens();
		screensSize.setSize(0, 0);
		
		for (Screen screen : screenList) {
			ScreenView view = new ScreenView(screen);
	    	view.addScreenListener(screenListener);
	    	
	        screens.put(screen.getId(), view);
	        add(view);
	        
	        screensSize.width = Math.max(screensSize.width, screen.getWidth() + screen.getX());
            screensSize.height = Math.max(screensSize.height, screen.getHeight() + screen.getY());
		}
		
		updateGraphicsData();
	}
	
	private void clearScreens() {
		for (ScreenView view : screens.values()) {
			view.removeScreenListener(screenListener);
		}
		
		screens.clear();
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
        	
        	sv.setDisplayScaleRatio(screensScaleRatio);
        	sv.setBounds(r);
        }
        
        repaint();
	}

    @Override
    protected void paintComponent(Graphics g) {
    	//super.paintComponent(g);
    	
    	Graphics2D g2d = (Graphics2D) g;
    	
    	Color oldColor = g2d.getColor();
    	
    	g2d.setColor(Color.GRAY);
    	g2d.fillRect(0, 0, getWidth(), getHeight());
    	
    	g2d.setColor(oldColor);
    	
    	// Paint each screen
        for (ScreenView view : screens.values()) {
        	view.paint(g2d.create(view.getBounds().x, view.getBounds().y, view.getBounds().width, view.getBounds().height));
        }
    }

    public void setImage(int no, File image) {
        if (screens.containsKey(no)) {
            screens.get(no).setImage(image);
        }
    }

    public void setScalingAlgorithm(int no, ScalingAlgorithm algorithm) {
        if (screens.containsKey(no)) {
            screens.get(no).setScalingAlgorithm(algorithm);
        }
    }
    
    public void setBackgroundColor(int no, Color backgroundColor) {
        if (screens.containsKey(no)) {
            screens.get(no).setBackground(backgroundColor);
        }
    }

    public Collection<ScreenView> getDisplayers() {
        return screens.values();
    }
    
    public List<ScreenWallpaper> getData() {
    	List<ScreenWallpaper> datas = new ArrayList<>();
    	
    	for (ScreenView sv : screens.values()) {
    		datas.add(sv.getData());
    	}
    	
    	return datas;
    }
    
    public void setSelectedMonitor(int id) {
    	for (ScreenView sv : screens.values()) {
    		sv.setSelected(sv.getId() == id);
    	}
    }
}