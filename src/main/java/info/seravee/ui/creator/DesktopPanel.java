package info.seravee.ui.creator;

import info.seravee.data.ScalingAlgorithm;
import info.seravee.utils.ImageScalerUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ysaak on 01/02/15.
 */
public class DesktopPanel extends JComponent {
	private static final long serialVersionUID = -8461734317635643191L;

	private static final int BORDER_WIDTH = 20;

    private Map<Integer, ImageDisplayer> screens = new HashMap<Integer, ImageDisplayer>();

    boolean initComplete = false;

    // Cal vars
    private Dimension screensDim = new Dimension(0, 0);

    public void addScreen(int number, Rectangle config) {
        screens.put(number, new ImageDisplayer(config));

        // Compute data
        for (ImageDisplayer id : screens.values()) {
            final Rectangle sc = id.getScreenData();
            screensDim.width = Math.max(screensDim.width, sc.width + sc.x);
            screensDim.height = Math.max(screensDim.height, sc.height + sc.y);
        }

        add(screens.get(number));
    }

    private void setComponentBounds() {
        final Dimension compDim = new Dimension(getWidth() - 2 * BORDER_WIDTH, getHeight() - 2 * BORDER_WIDTH);

        final Dimension scaledDim = ImageScalerUtils.getScaledImage(ScalingAlgorithm.STRETCH_KEEP_PROPORTION_NO_CROP,
                screensDim, compDim);

        final double scaleRatio = scaledDim.getWidth() / screensDim.getWidth();

        final int x = (getWidth() - scaledDim.width) / 2;
        final int y = (getHeight() - scaledDim.height) / 2;

        for (ImageDisplayer id : screens.values()) {
            Rectangle r = id.getScreenData();

            r.x = (int) (x + (r.x * scaleRatio));
            r.y = (int) (y + (r.y * scaleRatio));
            r.width = (int) (r.width * scaleRatio);
            r.height = (int) (r.height * scaleRatio);

            id.setDisplayScaleRatio(scaleRatio);
            id.setBounds(r);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (!initComplete) {
            setComponentBounds();
            initComplete = true;
        }

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

    public Dimension getScreensDimension() {
        return screensDim;
    }
    
    public Collection<ImageDisplayer> getDisplayers() {
        return screens.values();
    }
}
