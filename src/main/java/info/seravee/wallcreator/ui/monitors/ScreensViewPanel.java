package info.seravee.wallcreator.ui.monitors;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
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
    private Dimension screensDim = new Dimension(0, 0);
    
    private final ScreenListener screenListener = new ScreenListener() {
		
		@Override
		public void screenSelected(int id) {
			setSelectedMonitor(id);
		}
	}; 
    
    public void addScreen(Screen config) {
    	
    	ScreenView sView = new ScreenView(config);
    	sView.addScreenListener(screenListener);
    	
        screens.put(config.getId(), sView);

        // re-Compute data
        for (ScreenView sv : screens.values()) {
            final Rectangle sData = sv.getScreenData();
            screensDim.width = Math.max(screensDim.width, sData.width + sData.x);
            screensDim.height = Math.max(screensDim.height, sData.height + sData.y);
        }

        add(sView);
    }

    private void setComponentBounds() {
        final Dimension compDim = new Dimension(getWidth() - 2 * BORDER_WIDTH, getHeight() - 2 * BORDER_WIDTH);

        final Dimension scaledDim = ImageScalerUtils.getScaledImageDimensions(ScalingAlgorithm.STRETCH_KEEP_PROPORTION_NO_CROP,
                screensDim, compDim);

        final double scaleRatio = scaledDim.getWidth() / screensDim.getWidth();

        final int x = (getWidth() - scaledDim.width) / 2;
        final int y = (getHeight() - scaledDim.height) / 2;

        for (ScreenView sv : screens.values()) {
            Rectangle r = sv.getScreenData();

            r.x = (int) (x + (r.x * scaleRatio));
            r.y = (int) (y + (r.y * scaleRatio));
            r.width = (int) (r.width * scaleRatio);
            r.height = (int) (r.height * scaleRatio);

            sv.setDisplayScaleRatio(scaleRatio);
            sv.setBounds(r);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
    	setComponentBounds();

        Color oldColor = g.getColor();
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(oldColor);

        super.paintComponent(g);
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
