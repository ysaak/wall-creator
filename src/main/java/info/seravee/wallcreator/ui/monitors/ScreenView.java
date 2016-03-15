package info.seravee.wallcreator.ui.monitors;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

import info.seravee.DefaultConfiguration;
import info.seravee.data.ScalingAlgorithm;
import info.seravee.data.ScreenWallpaper;
import info.seravee.utils.ImageScalerUtils;
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

	private File imageFile;
	private ScalingAlgorithm scalingAlgorithm = DefaultConfiguration.SCALING_ALGORITHM;
	private BufferedImage image = null;
	private BufferedImage scaledImage = null;

	private double displayScaleRatio = 1.0;

	private final int id;
	private final Rectangle screenData;

	private boolean selected = false;
	private boolean screenIdVisible = false;
		
	private final Set<ScreenListener> screenListeners;
	
	private final Timer imageBuildingTimer;

	public ScreenView(final int id, final Rectangle screenData) {
		this.id = id;
		this.screenData = screenData;
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
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		final Color oldColor = g2.getColor();
		final RenderingHints oldHints = g2.getRenderingHints();
		
		g2.setRenderingHints(LafUtils.ANTIALIASING_HINTS);
		g2.clearRect(0, 0, getWidth(), getHeight());

		g2.setColor(getBackground());
		g2.fillRect(0, 0, getWidth(), getHeight());

		if (scaledImage != null) {
			int width = getWidth() - 1;
			int height = getHeight() - 1;

			int x = (width - scaledImage.getWidth(this)) / 2;
			int y = (height - scaledImage.getHeight(this)) / 2;

			g2.drawImage(scaledImage, x, y, this);
		}

		g2.setColor(selected ? SolarizedColor.RED : Color.BLACK);
		g2.fill(getBorderShape());
		
		if (screenIdVisible) {
			
			
			final int si_x = 10, si_y = 10;
			FontMetrics m = getFontMetrics(ID_FONT);
			
			final int si_w = m.stringWidth(String.valueOf(id)),
					  si_h = m.getHeight();
			
			final Color textBgColor;
			if (scaledImage == null) {
				// No image, get background color
				textBgColor = getBackground();
			}
			else {
				// FIXME : should verify if the text is over the image or not ...
				BufferedImage subimg = scaledImage.getSubimage(si_x, si_y, si_w, si_h);
				textBgColor = GraphicsUtilities.getAverageColor(subimg, si_w, si_h);
			}
			
			// Search best foreground color according to bg color
			final Font oldFont = g2.getFont();
			g2.setFont(ID_FONT);
			g2.setColor(GraphicsUtilities.getForegroundFromBackground(textBgColor));
			
			g2.drawString(String.valueOf(id), si_x, si_y + si_h - m.getDescent());
			
			g2.setFont(oldFont);
		}

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

	public void setImage(File imageFile) {
		this.imageFile = imageFile;
		try {
			image = ImageIO.read(imageFile);

			rebuildImage();
		} catch (IOException e) {
			e.printStackTrace();
			image = null;
			scaledImage = null;
		}
	}

	public void setScalingAlgorithm(ScalingAlgorithm image1Size) {
		this.scalingAlgorithm = image1Size;
		rebuildImage();
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		repaint();
	}

	public ScreenWallpaper getData() {
		return new ScreenWallpaper(screenData, imageFile, scalingAlgorithm, getBackground());
	}
	
	protected void rebuildImage() {
		if (image == null) {
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				final Dimension initialDimensions = new Dimension((int) (image.getWidth() * displayScaleRatio),
						(int) (image.getHeight() * displayScaleRatio));

				scaledImage = ImageScalerUtils.getScaledImage(image, scalingAlgorithm, initialDimensions);
				
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						ScreenView.this.repaint();
					}
				});
			}
		}).start();
	}

	public void setDisplayScaleRatio(double displayScaleRatio) {
		this.displayScaleRatio = displayScaleRatio;
	}

	public Rectangle getScreenData() {
		return new Rectangle(screenData);
	}

	public Image getScaledImage() {
		BufferedImage bi = new BufferedImage(screenData.width, screenData.height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bi.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(getBackground());
		g2.fillRect(0, 0, screenData.width, screenData.height);

		if (image != null) {
			Dimension scaledDimension = ImageScalerUtils.getScaledImageDimensions(scalingAlgorithm,
					new Dimension(image.getWidth(), image.getHeight()), screenData.getSize());
			Image scaledImg = image.getScaledInstance(scaledDimension.width, scaledDimension.height,
					Image.SCALE_SMOOTH);

			int width = screenData.width - 1;
			int height = screenData.height - 1;

			int x = (width - scaledImg.getWidth(this)) / 2;
			int y = (height - scaledImg.getHeight(this)) / 2;

			g2.drawImage(scaledImg, x, y, this);
		}

		return bi;
	}
	
	public int getId() {
		return id;
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
		repaint();
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
				l.screenSelected(id);
			}
		}
	}
}
