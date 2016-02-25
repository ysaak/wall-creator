package info.seravee.utils;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import info.seravee.data.ScalingAlgorithm;

/**
 * Created by ysaak on 27/01/15.
 */
public class ImageScalerUtils {

	/**
	 * * Convenience method that returns a scaled instance of the * provided
	 * {@code BufferedImage}.
	 *
	 * @param img
	 *            the original image to be scaled
	 * @param targetWidth
	 *            the desired width of the scaled instance, in pixels
	 * @param targetHeight
	 *            the desired height of the scaled instance, in pixels
	 * @param hint
	 *            one of the rendering hints that corresponds to
	 *            {@code RenderingHints.KEY_INTERPOLATION} (e.g.
	 *            {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
	 *            {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
	 *            {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
	 * @param higherQuality
	 *            if true, this method will use a multi-step scaling technique
	 *            that provides higher quality than the usual one-step technique
	 *            (only useful in downscaling cases, where {@code targetWidth}
	 *            or {@code targetHeight} is smaller than the original
	 *            dimensions, and generally only when the {@code BILINEAR} hint
	 *            is specified)
	 * @return a scaled version of the original {@code BufferedImage}
	 */
	public static final BufferedImage getScaledInstance2(BufferedImage img, int targetWidth, int targetHeight, Object hint, boolean higherQuality) {
		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;
		BufferedImage ret = (BufferedImage) img;
		int w, h;
		if (higherQuality) {
			// Use multi-step technique: start with original size, then
			// scale down in multiple passes with drawImage()
			// until the target size is reached
			w = img.getWidth();
			h = img.getHeight();
		} else {
			// Use one-step technique: scale directly from original
			// size to target size with a single drawImage() call
			w = targetWidth;
			h = targetHeight;
		}

		do {
			if (higherQuality && w > targetWidth) {
				w /= 2;
				if (w < targetWidth) {
					w = targetWidth;
				}
			}
			if (higherQuality && h > targetHeight) {
				h /= 2;
				if (h < targetHeight) {
					h = targetHeight;
				}
			}

			BufferedImage tmp = new BufferedImage(w, h, type);
			Graphics2D g2 = tmp.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
			g2.drawImage(ret, 0, 0, w, h, null);
			g2.dispose();
			ret = tmp;
		} while (w != targetWidth || h != targetHeight);

		return ret;
	}

	public static final Image getScaledThumbnail(BufferedImage original, ScalingAlgorithm algorithm, Dimension thumbnailSize) {
		// Get original image dimension
		final Dimension initialDimensions = new Dimension((int) (original.getWidth(null)), (int) (original.getHeight(null)));
		
		// Get scaled dimension according to algorithm
		final Dimension scaledDimension = getScaledImage(algorithm, initialDimensions, thumbnailSize);
		
		// Resize image
		BufferedImage scaledImage = getScaledInstance2(original, scaledDimension.width, scaledDimension.height, RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);
		
		// Crop image
		if (scaledDimension.width != thumbnailSize.width || scaledDimension.height != thumbnailSize.height) {
			// One of the dimension differs, let's crop the image
			final int x = (scaledDimension.width - thumbnailSize.width) / 2;
            final int y = (scaledDimension.height - thumbnailSize.height) / 2;
            
            scaledImage = scaledImage.getSubimage(x, y, thumbnailSize.width, thumbnailSize.height);
		}
		
		if (scaledImage.getWidth() != thumbnailSize.width || scaledImage.getHeight() != thumbnailSize.height) {
			System.err.println("width > " + scaledImage.getWidth());
			System.err.println("height > " + scaledImage.getHeight());
		}
		
		return scaledImage;
		
	}

	public static Dimension getScaledImage(ScalingAlgorithm algorithm, Dimension original, Dimension target) {

		if (algorithm == ScalingAlgorithm.CENTER) {
			return original;
		}
		// else if (algorithm == ScalingAlgorithm.STRETCH) {
		// return target;
		// }
		else if (algorithm == ScalingAlgorithm.STRETCH_KEEP_PROPORTION_NO_CROP) {
			return getScaledDimension(original, getScaleFactorToFit(original, target));
		} else if (algorithm == ScalingAlgorithm.STRETCH_KEEP_PROPORTION_WITH_CROP) {
			return getScaledDimension(original, getScaleFactorToFill(original, target));
		} else {
			// Default value is stretch
			return target;
		}
	}

	private static Dimension getScaledDimension(Dimension original, double scaleFactor) {
		int scaleWidth = (int) Math.round(original.width * scaleFactor);
		int scaleHeight = (int) Math.round(original.height * scaleFactor);
		return new Dimension(scaleWidth, scaleHeight);
	}

	/**
	 * Returns the scale factor for the image to fit the available area
	 * 
	 * @param original
	 *            Original dimension
	 * @param target
	 *            Available dimension
	 * @return Scale factor
	 */
	private static double getScaleFactorToFit(Dimension original, Dimension target) {
		double dScale = 1d;

		if (original != null && target != null) {
			double dScaleWidth = getScaleFactor(original.width, target.width);
			double dScaleHeight = getScaleFactor(original.height, target.height);
			dScale = Math.min(dScaleHeight, dScaleWidth);
		}
		return dScale;
	}

	/**
	 * Returns the scale factor for the image to fill the available area
	 * 
	 * @param original
	 *            Original dimension
	 * @param target
	 *            Available dimension
	 * @return Scale factor
	 */
	private static double getScaleFactorToFill(Dimension original, Dimension target) {
		double dScale = 1d;

		if (original != null && target != null) {
			double dScaleWidth = getScaleFactor(original.width, target.width);
			double dScaleHeight = getScaleFactor(original.height, target.height);
			dScale = Math.max(dScaleHeight, dScaleWidth);
		}

		return dScale;
	}

	/**
	 * Returns the scaling factor for a particular size
	 * 
	 * @param iMasterSize
	 *            Master size
	 * @param iTargetSize
	 *            Target size
	 * @return scaling factor
	 */
	private static double getScaleFactor(int iMasterSize, int iTargetSize) {
		double dScale;
		if (iMasterSize > iTargetSize) {
			dScale = (double) iTargetSize / (double) iMasterSize;
		} else {
			dScale = (double) iTargetSize / (double) iMasterSize;
		}
		return dScale;
	}
}
