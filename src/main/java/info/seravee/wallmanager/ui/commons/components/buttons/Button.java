package info.seravee.wallmanager.ui.commons.components.buttons;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

import info.seravee.wallcreator.utils.GraphicsUtilities;
import info.seravee.wallmanager.graphics.filters.GaussianFilter;
import info.seravee.wallmanager.ui.commons.laf.LafUtils;
import sun.swing.SwingUtilities2;

@SuppressWarnings("restriction")
public class Button extends RippleButton {
	private static final long serialVersionUID = -7618982731750611490L;
	
	private static final int BORDER_RADIUS = 4;
	private static final int SHADOW_SIZE = 3;

	private final int elevation;

	private BufferedImage shadow;
	private Shape base;

	public Button(Action action, int elevation) {
		super(action);

		this.elevation = elevation;
		
		setUI(new ButtonUI());

		setContentAreaFilled(false);
		setBorderPainted(false);
		setFocusPainted(false);

		initShape();
		setBorder(new EmptyBorder(10 + (SHADOW_SIZE * elevation), 10 + (SHADOW_SIZE * elevation),
				10 + (SHADOW_SIZE * elevation), 10 + (SHADOW_SIZE * elevation)));

		setFont(getFont());

		setBackground(ButtonsConstants.DEFAULT_BG_COLOR);
		setForeground(ButtonsConstants.DEFAULT_FG_COLOR);
	}

	@Override
	public void setFont(Font font) {
		super.setFont(font.deriveFont(Font.BOLD));
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		Rectangle oldBound = getBounds();
		super.setBounds(x, y, width, height);

		initShape();

		if (oldBound.width != width || oldBound.height != height) {

			int w = getBounds().width - (SHADOW_SIZE * elevation * 2);
			int h = getBounds().height - (SHADOW_SIZE * elevation * 2);
			int arc = BORDER_RADIUS;

			shadow = GraphicsUtilities.createCompatibleTranslucentImage(w, h);
			Graphics2D g2 = shadow.createGraphics();
			// The color does not matter, red is used for debugging
			g2.setColor(Color.RED);
			applyQualityRenderingHints(g2);
			// g2.setComposite(AlphaComposite.Clear);
			g2.fillRoundRect(0, 0, w, h, arc, arc);
			g2.dispose();

			shadow = generateShadow(shadow, SHADOW_SIZE * elevation, Color.BLACK, 0.5f);
		}
	}

	@Override
	protected void initShape() {
		if (!getBounds().equals(base)) {
			Dimension s = getSize();
			base = getBounds();
			shape = new RoundRectangle2D.Float(SHADOW_SIZE * elevation, SHADOW_SIZE * elevation,
					s.width - (SHADOW_SIZE * elevation * 2), s.height - (SHADOW_SIZE * elevation * 2), BORDER_RADIUS,
					BORDER_RADIUS);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		initShape();

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHints(LafUtils.ANTIALIASING_HINTS);

		final Color bgColor;

		if (isEnabled()) {
			bgColor = getBackground();
		} else {
			bgColor = ButtonsConstants.DISABLED_BG_COLOR;
		}

		if (shadow != null) {
			g2.drawImage(shadow, 0, 0, this);
		} 

		g2.setColor(bgColor);
		g2.fill(shape);

		super.paintComponent(g);
	}

	public static void applyQualityRenderingHints(Graphics2D g2d) {
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	}

	/***************/
	public BufferedImage generateBlur(BufferedImage imgSource, int size, Color color, float alpha) {
		GaussianFilter filter = new GaussianFilter(size);

		int imgWidth = imgSource.getWidth();
		int imgHeight = imgSource.getHeight();

		BufferedImage imgBlur = createCompatibleImage(imgWidth, imgHeight);
		Graphics2D g2 = imgBlur.createGraphics();
		applyQualityRenderingHints(g2);

		g2.drawImage(imgSource, 0, 0, null);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
		g2.setColor(color);

		g2.fillRect(0, 0, imgSource.getWidth(), imgSource.getHeight());
		g2.dispose();

		imgBlur = filter.filter(imgBlur, null);

		return imgBlur;
	}

	public BufferedImage createCompatibleImage(int width, int height) {
		return createCompatibleImage(width, height, Transparency.TRANSLUCENT);
	}

	public BufferedImage createCompatibleImage(int width, int height, int transparency) {
		BufferedImage image = getGraphicsConfiguration().createCompatibleImage(width, height, transparency);
		image.coerceData(true);
		return image;
	}

	public BufferedImage createCompatibleImage(BufferedImage image) {
		return createCompatibleImage(image, image.getWidth(), image.getHeight());
	}

	public BufferedImage createCompatibleImage(BufferedImage image, int width, int height) {
		return getGraphicsConfiguration().createCompatibleImage(width, height, image.getTransparency());
	}
	
	public BufferedImage generateShadow(BufferedImage imgSource, int size, Color color, float alpha) {
        int imgWidth = imgSource.getWidth() + (size * 2);
        int imgHeight = imgSource.getHeight() + (size * 2);

        BufferedImage imgMask = createCompatibleImage(imgWidth, imgHeight);
        Graphics2D g2 = imgMask.createGraphics();
        applyQualityRenderingHints(g2);

        int x = Math.round((imgWidth - imgSource.getWidth()) / 2f);
        int y = Math.round((imgHeight - imgSource.getHeight()) / 2f);
        g2.drawImage(imgSource, x, y, null);
        g2.dispose();

        // ---- Blur here ---

        BufferedImage imgGlow = generateBlur(imgMask, (size * 2), color, alpha);

        return imgGlow;
    }
	
	/* ----------------------------------------------------------------------------------- */
	private class ButtonUI extends BasicButtonUI {
		
		@Override
	    protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
			// FIXME: alignment problem if button not centered
			
	        AbstractButton b = (AbstractButton) c;
	        ButtonModel model = b.getModel();
	        FontMetrics fm = SwingUtilities2.getFontMetrics(c, g);
	        int mnemonicIndex = b.getDisplayedMnemonicIndex();

	        /* Draw the Text */
	        if(model.isEnabled()) {
	            /*** paint the text normally */
	            g.setColor(b.getForeground());
	            SwingUtilities2.drawStringUnderlineCharAt(c, g,text, mnemonicIndex,
	                                          textRect.x + getTextShiftOffset(),
	                                          textRect.y + fm.getAscent() + getTextShiftOffset());
	        }
	        else {
	            /*** paint the text disabled ***/
	        	g.setColor(ButtonsConstants.DISABLED_FG_COLOR);
	            SwingUtilities2.drawStringUnderlineCharAt(c, g,text, mnemonicIndex,
	                                          textRect.x, textRect.y + fm.getAscent());
	        }
	    }
	}
}
