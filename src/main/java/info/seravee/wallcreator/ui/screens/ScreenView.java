package info.seravee.wallcreator.ui.screens;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;

import info.seravee.DefaultConfiguration;
import info.seravee.data.ScreenWallpaper;
import info.seravee.utils.ImageScalerUtils;
import info.seravee.wallcreator.beans.Screen;
import info.seravee.wallcreator.ui.components.LafUtils;
import info.seravee.wallcreator.ui.components.SolarizedColor;
import info.seravee.wallcreator.utils.GraphicsUtilities;

/**
 * Created by ysaak on 27/01/15.
 */
public class ScreenView extends JComponent {
	private static final long serialVersionUID = 3915706272759364018L;
	
	private static final int BORDER_WIDTH = 2;
	
	private static final Font ID_FONT;
	static {
		Font baseFont = (Font) UIManager.get("Label.font");
		
		ID_FONT = baseFont.deriveFont(Font.BOLD, 18f);
	}

	private BufferedImage displayedImage = null;

	private final Screen screen;

	private boolean selected = false;
	private boolean screenIdVisible = false;
		
	private final Set<ScreenListener> screenListeners;
	
	private final Timer imageBuildingTimer;

	public ScreenView(final Screen screen) {
		this.screen = screen;
		setBackground(DefaultConfiguration.BACKGROUND_COLOR);
		screenListeners = new HashSet<>();
		
		imageBuildingTimer = new Timer(20, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				rebuildImage();
			}
		});
		imageBuildingTimer.setRepeats(false);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e))
					fireScreenSelectedEvent();
			}
		});
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				if (imageBuildingTimer.isRunning()) {
					imageBuildingTimer.restart();
				}
				else {
					imageBuildingTimer.start();
				}
			}
		});
		
		screen.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				rebuildImage();
			}
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		final Color oldColor = g2.getColor();
		final RenderingHints oldHints = g2.getRenderingHints();
		
		g2.setRenderingHints(LafUtils.ANTIALIASING_HINTS);
		g2.clearRect(0, 0, getWidth(), getHeight());
		
		
		/*

		g2.setColor(getBackground());
		g2.fillRect(0, 0, getWidth(), getHeight());

		if (scaledImage != null) {
			int width = getWidth() - 1;
			int height = getHeight() - 1;

			int x = (width - scaledImage.getWidth(this)) / 2;
			int y = (height - scaledImage.getHeight(this)) / 2;

			g2.drawImage(scaledImage, x, y, this);
		}
		
		*/
		if (displayedImage != null) {
			g2.drawImage(displayedImage, 0, 0, getWidth(), getHeight(), this);
		}
		

		g2.setColor(selected ? SolarizedColor.RED : Color.BLACK);
		g2.fill(getBorderShape());
		
		g2.setColor(oldColor);
		g2.setRenderingHints(oldHints);
	}
	
	private Shape getBorderShape() {
		Shape outer;
        Shape inner;

        int size = BORDER_WIDTH + BORDER_WIDTH;
        outer = new Rectangle2D.Float(0, 0, getWidth(), getHeight());
        inner = new Rectangle2D.Float(0 + BORDER_WIDTH, 0 + BORDER_WIDTH, getWidth() - size, getHeight() - size);

        Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
        path.append(outer, false);
        path.append(inner, false);

        return path;
	}

	public ScreenWallpaper getData() {
		return new ScreenWallpaper(screen.getBounds(), screen.getImageFile(), screen.getScalingAlgorithm(), getBackground());
	}
	
	protected void rebuildImage() {
		new DisplayedImageBuilder(screen, getSize(), screenIdVisible).execute();
	}

	public Rectangle getScreenData() {
		return new Rectangle(screen.getBounds());
	}

	public Screen getScreen() {
		return screen;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
		repaint();
	}

	public void setScreenIdVisible(boolean screenIdVisible) {
		this.screenIdVisible = screenIdVisible;
		rebuildImage();
	}
	
	/* --- Listener part --- */
	
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
	
	protected void fireScreenSelectedEvent() {
		synchronized (screenListeners) {
			for (ScreenListener l : screenListeners) {
				l.screenSelected(screen);
			}
		}
	}
	
	/* --- ImageBuildingWorker ---*/
	protected void updateDisplatedImage(BufferedImage image) {
		this.displayedImage = image;
		repaint();
	}
	
	
	private class DisplayedImageBuilder extends SwingWorker<Void, BufferedImage> {
		
		private final Screen screen;
		private final Dimension containerDimension;
		private final boolean screenIdVisible;
		
		public DisplayedImageBuilder(final Screen screen, final Dimension containerDimension, final boolean screenIdVisible) {
			super();
			this.screen = screen;
			this.containerDimension = containerDimension;
			this.screenIdVisible = screenIdVisible;
		}

		@Override
		protected Void doInBackground() throws Exception {
			
			// Generating displayed image
			final BufferedImage finalImage = new BufferedImage(containerDimension.width, containerDimension.height, BufferedImage.TYPE_INT_ARGB);
			final Graphics2D g2 = finalImage.createGraphics();
			g2.setRenderingHints(LafUtils.ANTIALIASING_HINTS);
			
			
			// Paint background color
			g2.setColor(getBackground());
			g2.fillRect(0, 0, getWidth(), getHeight());
			
			// Paint wallpaper
			if (screen.getImageFile() != null) {
				paintWallpaper(g2);
			}
			
			if (screenIdVisible) {
				paintScreenId(finalImage, g2);
			}
			
			// TODO paint screen id
			publishInEdt(finalImage);
			
			return null;
		}
		
		private void paintWallpaper(Graphics2D g2) throws IOException {
			// Read original file
			final BufferedImage originalImage = ImageIO.read(screen.getImageFile());

			// Scale the image
			final BufferedImage scaledImage = ImageScalerUtils.getScaledImage(originalImage, screen.getScalingAlgorithm(), containerDimension);
			
			int x = (containerDimension.width - scaledImage.getWidth(null)) / 2;
			int y = (containerDimension.height - scaledImage.getHeight(null)) / 2;

			g2.drawImage(scaledImage, x, y, null);
		}
		
		private void paintScreenId(final BufferedImage finalImage, final Graphics2D g2) {
			final int si_x = 10, si_y = 10;
			FontMetrics m = getFontMetrics(ID_FONT);
			
			final int si_w = m.stringWidth(String.valueOf(screen.getId())),
					  si_h = m.getHeight();
			
			BufferedImage subimg = finalImage.getSubimage(si_x, si_y, si_w, si_h);
			final Color textBgColor = GraphicsUtilities.getAverageColor(subimg, si_w, si_h);
			
			// Search best foreground color according to bg color
			final Font oldFont = g2.getFont();
			g2.setFont(ID_FONT);
			g2.setColor(GraphicsUtilities.getForegroundFromBackground(textBgColor));
			
			g2.drawString(String.valueOf(screen.getId()), si_x, si_y + si_h - m.getDescent());
			
			g2.setFont(oldFont);
		}
		
		private void publishInEdt(final BufferedImage finalImage) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					updateDisplatedImage(finalImage);
				}
			});
		}
	}
}
