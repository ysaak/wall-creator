package wallmanager.ui.graphics;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import wallmanager.beans.profile.Profile;
import wallmanager.beans.profile.ProfileVersion;
import wallmanager.beans.profile.Screen;
import wallmanager.beans.profile.WallpaperParameters;

public class GraphicsUtilities {
	private GraphicsUtilities() {/**/}
	
	// Returns the graphics configuration for the primary screen
    private static GraphicsConfiguration getGraphicsConfiguration() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().
                    getDefaultScreenDevice().getDefaultConfiguration();
    }

    private static boolean isHeadless() {
        return GraphicsEnvironment.isHeadless();
    }

    /**
     * Converts the specified image into a compatible buffered image.
     * 
     * @param img
     *            the image to convert
     * @return a compatible buffered image of the input
     */
    public static BufferedImage convertToBufferedImage(Image img) {
        BufferedImage buff = createCompatibleTranslucentImage(
                img.getWidth(null), img.getHeight(null));
        Graphics2D g2 = buff.createGraphics();

        try {
            g2.drawImage(img, 0, 0, null);
        } finally {
            g2.dispose();
        }

        return buff;
    }

    /**
     * <p>Returns a new <code>BufferedImage</code> using the same color model
     * as the image passed as a parameter. The returned image is only compatible
     * with the image passed as a parameter. This does not mean the returned
     * image is compatible with the hardware.</p>
     *
     * @param image the reference image from which the color model of the new
     *   image is obtained
     * @return a new <code>BufferedImage</code>, compatible with the color model
     *   of <code>image</code>
     */
    public static BufferedImage createColorModelCompatibleImage(BufferedImage image) {
        ColorModel cm = image.getColorModel();
        return new BufferedImage(cm,
            cm.createCompatibleWritableRaster(image.getWidth(),
                                              image.getHeight()),
            cm.isAlphaPremultiplied(), null);
    }

    /**
     * <p>Returns a new compatible image with the same width, height and
     * transparency as the image specified as a parameter. That is, the
     * returned BufferedImage will be compatible with the graphics hardware.
     * If this method is called in a headless environment, then
     * the returned BufferedImage will be compatible with the source
     * image.</p>
     *
     * @see java.awt.Transparency
     * @see #createCompatibleImage(int, int)
     * @see #createCompatibleImage(java.awt.image.BufferedImage, int, int)
     * @see #createCompatibleTranslucentImage(int, int)
     * @see #loadCompatibleImage(java.net.URL)
     * @see #toCompatibleImage(java.awt.image.BufferedImage)
     * @param image the reference image from which the dimension and the
     *   transparency of the new image are obtained
     * @return a new compatible <code>BufferedImage</code> with the same
     *   dimension and transparency as <code>image</code>
     */
    public static BufferedImage createCompatibleImage(BufferedImage image) {
        return createCompatibleImage(image, image.getWidth(), image.getHeight());
    }

    /**
     * <p>Returns a new compatible image of the specified width and height, and
     * the same transparency setting as the image specified as a parameter.
     * That is, the returned <code>BufferedImage</code> is compatible with
     * the graphics hardware. If the method is called in a headless
     * environment, then the returned BufferedImage will be compatible with
     * the source image.</p>
     *
     * @see java.awt.Transparency
     * @see #createCompatibleImage(java.awt.image.BufferedImage)
     * @see #createCompatibleImage(int, int)
     * @see #createCompatibleTranslucentImage(int, int)
     * @see #loadCompatibleImage(java.net.URL)
     * @see #toCompatibleImage(java.awt.image.BufferedImage)
     * @param width the width of the new image
     * @param height the height of the new image
     * @param image the reference image from which the transparency of the new
     *   image is obtained
     * @return a new compatible <code>BufferedImage</code> with the same
     *   transparency as <code>image</code> and the specified dimension
     */
    public static BufferedImage createCompatibleImage(BufferedImage image,
                                                      int width, int height) {
        return isHeadless() ?
                new BufferedImage(width, height, image.getType()) :
                getGraphicsConfiguration().createCompatibleImage(width, height,
                                                   image.getTransparency());
    }

    /**
     * <p>Returns a new opaque compatible image of the specified width and
     * height. That is, the returned <code>BufferedImage</code> is compatible with
     * the graphics hardware. If the method is called in a headless
     * environment, then the returned BufferedImage will be compatible with
     * the source image.</p>
     *
     * @see #createCompatibleImage(java.awt.image.BufferedImage)
     * @see #createCompatibleImage(java.awt.image.BufferedImage, int, int)
     * @see #createCompatibleTranslucentImage(int, int)
     * @see #loadCompatibleImage(java.net.URL)
     * @see #toCompatibleImage(java.awt.image.BufferedImage)
     * @param width the width of the new image
     * @param height the height of the new image
     * @return a new opaque compatible <code>BufferedImage</code> of the
     *   specified width and height
     */
    public static BufferedImage createCompatibleImage(int width, int height) {
        return isHeadless() ?
                new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB) :
                getGraphicsConfiguration().createCompatibleImage(width, height);
    }

    /**
     * <p>Returns a new translucent compatible image of the specified width and
     * height. That is, the returned <code>BufferedImage</code> is compatible with
     * the graphics hardware. If the method is called in a headless
     * environment, then the returned BufferedImage will be compatible with
     * the source image.</p>
     *
     * @see #createCompatibleImage(java.awt.image.BufferedImage)
     * @see #createCompatibleImage(java.awt.image.BufferedImage, int, int)
     * @see #createCompatibleImage(int, int)
     * @see #loadCompatibleImage(java.net.URL)
     * @see #toCompatibleImage(java.awt.image.BufferedImage)
     * @param width the width of the new image
     * @param height the height of the new image
     * @return a new translucent compatible <code>BufferedImage</code> of the
     *   specified width and height
     */
    public static BufferedImage createCompatibleTranslucentImage(int width,
                                                                 int height) {
        return isHeadless() ?
                new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB) :
                getGraphicsConfiguration().createCompatibleImage(width, height,
                                                   Transparency.TRANSLUCENT);
    }

    /**
     * <p>
     * Returns a new compatible image from a stream. The image is loaded from
     * the specified stream and then turned, if necessary into a compatible
     * image.
     * </p>
     * 
     * @see #createCompatibleImage(java.awt.image.BufferedImage)
     * @see #createCompatibleImage(java.awt.image.BufferedImage, int, int)
     * @see #createCompatibleImage(int, int)
     * @see #createCompatibleTranslucentImage(int, int)
     * @see #toCompatibleImage(java.awt.image.BufferedImage)
     * @param in
     *            the stream of the picture to load as a compatible image
     * @return a new translucent compatible <code>BufferedImage</code> of the
     *         specified width and height
     * @throws java.io.IOException
     *             if the image cannot be read or loaded
     */
    public static BufferedImage loadCompatibleImage(InputStream in) throws IOException {
        BufferedImage image = ImageIO.read(in);
        if(image == null) return null;
        return toCompatibleImage(image);
    }

    /**
     * <p>Returns a new compatible image from a URL. The image is loaded from the
     * specified location and then turned, if necessary into a compatible
     * image.</p>
     *
     * @see #createCompatibleImage(java.awt.image.BufferedImage)
     * @see #createCompatibleImage(java.awt.image.BufferedImage, int, int)
     * @see #createCompatibleImage(int, int)
     * @see #createCompatibleTranslucentImage(int, int)
     * @see #toCompatibleImage(java.awt.image.BufferedImage)
     * @param resource the URL of the picture to load as a compatible image
     * @return a new translucent compatible <code>BufferedImage</code> of the
     *   specified width and height
     * @throws java.io.IOException if the image cannot be read or loaded
     */
    public static BufferedImage loadCompatibleImage(URL resource)
            throws IOException {
        BufferedImage image = ImageIO.read(resource);
        return toCompatibleImage(image);
    }

    /**
     * <p>Return a new compatible image that contains a copy of the specified
     * image. This method ensures an image is compatible with the hardware,
     * and therefore optimized for fast blitting operations.</p>
     *
     * <p>If the method is called in a headless environment, then the returned
     * <code>BufferedImage</code> will be the source image.</p>
     *
     * @see #createCompatibleImage(java.awt.image.BufferedImage)
     * @see #createCompatibleImage(java.awt.image.BufferedImage, int, int)
     * @see #createCompatibleImage(int, int)
     * @see #createCompatibleTranslucentImage(int, int)
     * @see #loadCompatibleImage(java.net.URL)
     * @param image the image to copy into a new compatible image
     * @return a new compatible copy, with the
     *   same width and height and transparency and content, of <code>image</code>
     */
    public static BufferedImage toCompatibleImage(BufferedImage image) {
        if (isHeadless()) {
            return image;
        }

        if (image.getColorModel().equals(
                getGraphicsConfiguration().getColorModel())) {
            return image;
        }

        BufferedImage compatibleImage =
                getGraphicsConfiguration().createCompatibleImage(
                    image.getWidth(), image.getHeight(),
                    image.getTransparency());
        Graphics g = compatibleImage.getGraphics();
        
        try {
            g.drawImage(image, 0, 0, null);
        } finally {
            g.dispose();
        }

        return compatibleImage;
    }

    /**
     * <p>Returns a thumbnail of a source image. <code>newSize</code> defines
     * the length of the longest dimension of the thumbnail. The other
     * dimension is then computed according to the dimensions ratio of the
     * original picture.</p>
     * <p>This method favors speed over quality. When the new size is less than
     * half the longest dimension of the source image,
     * {@link #createThumbnail(BufferedImage, int)} or
     * {@link #createThumbnail(BufferedImage, int, int)} should be used instead
     * to ensure the quality of the result without sacrificing too much
     * performance.</p>
     *
     * @see #createThumbnailFast(java.awt.image.BufferedImage, int, int)
     * @see #createThumbnail(java.awt.image.BufferedImage, int)
     * @see #createThumbnail(java.awt.image.BufferedImage, int, int)
     * @param image the source image
     * @param newSize the length of the largest dimension of the thumbnail
     * @return a new compatible <code>BufferedImage</code> containing a
     *   thumbnail of <code>image</code>
     * @throws IllegalArgumentException if <code>newSize</code> is larger than
     *   the largest dimension of <code>image</code> or &lt;= 0
     */
    public static BufferedImage createThumbnailFast(BufferedImage image,
                                                    int newSize) {
        float ratio;
        int width = image.getWidth();
        int height = image.getHeight();

        if (width > height) {
            if (newSize >= width) {
                throw new IllegalArgumentException("newSize must be lower than" +
                                                   " the image width");
            } else if (newSize <= 0) {
                 throw new IllegalArgumentException("newSize must" +
                                                    " be greater than 0");
            }

            ratio = (float) width / (float) height;
            width = newSize;
            height = (int) (newSize / ratio);
        } else {
            if (newSize >= height) {
                throw new IllegalArgumentException("newSize must be lower than" +
                                                   " the image height");
            } else if (newSize <= 0) {
                 throw new IllegalArgumentException("newSize must" +
                                                    " be greater than 0");
            }

            ratio = (float) height / (float) width;
            height = newSize;
            width = (int) (newSize / ratio);
        }

        BufferedImage temp = createCompatibleImage(image, width, height);
        Graphics2D g2 = temp.createGraphics();
        
        try {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image, 0, 0, temp.getWidth(), temp.getHeight(), null);
        } finally {
            g2.dispose();
        }

        return temp;
    }

    /**
     * <p>Returns a thumbnail of a source image.</p>
     * <p>This method favors speed over quality. When the new size is less than
     * half the longest dimension of the source image,
     * {@link #createThumbnail(BufferedImage, int)} or
     * {@link #createThumbnail(BufferedImage, int, int)} should be used instead
     * to ensure the quality of the result without sacrificing too much
     * performance.</p>
     *
     * @see #createThumbnailFast(java.awt.image.BufferedImage, int)
     * @see #createThumbnail(java.awt.image.BufferedImage, int)
     * @see #createThumbnail(java.awt.image.BufferedImage, int, int)
     * @param image the source image
     * @param newWidth the width of the thumbnail
     * @param newHeight the height of the thumbnail
     * @return a new compatible <code>BufferedImage</code> containing a
     *   thumbnail of <code>image</code>
     * @throws IllegalArgumentException if <code>newWidth</code> is larger than
     *   the width of <code>image</code> or if code>newHeight</code> is larger
     *   than the height of <code>image</code> or if one of the dimensions
     *   is &lt;= 0
     */
    public static BufferedImage createThumbnailFast(BufferedImage image,
                                                    int newWidth, int newHeight) {
        if (newWidth >= image.getWidth() ||
            newHeight >= image.getHeight()) {
            throw new IllegalArgumentException("newWidth and newHeight cannot" +
                                               " be greater than the image" +
                                               " dimensions");
        } else if (newWidth <= 0 || newHeight <= 0) {
            throw new IllegalArgumentException("newWidth and newHeight must" +
                                               " be greater than 0");
        }

        BufferedImage temp = createCompatibleImage(image, newWidth, newHeight);
        Graphics2D g2 = temp.createGraphics();
        
        try {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image, 0, 0, temp.getWidth(), temp.getHeight(), null);
        } finally {
            g2.dispose();
        }

        return temp;
    }

    /**
     * <p>Returns a thumbnail of a source image. <code>newSize</code> defines
     * the length of the longest dimension of the thumbnail. The other
     * dimension is then computed according to the dimensions ratio of the
     * original picture.</p>
     * <p>This method offers a good trade-off between speed and quality.
     * The result looks better than
     * {@link #createThumbnailFast(java.awt.image.BufferedImage, int)} when
     * the new size is less than half the longest dimension of the source
     * image, yet the rendering speed is almost similar.</p>
     *
     * @see #createThumbnailFast(java.awt.image.BufferedImage, int, int)
     * @see #createThumbnailFast(java.awt.image.BufferedImage, int)
     * @see #createThumbnail(java.awt.image.BufferedImage, int, int)
     * @param image the source image
     * @param newSize the length of the largest dimension of the thumbnail
     * @return a new compatible <code>BufferedImage</code> containing a
     *   thumbnail of <code>image</code>
     * @throws IllegalArgumentException if <code>newSize</code> is larger than
     *   the largest dimension of <code>image</code> or &lt;= 0
     */
    public static BufferedImage createThumbnail(BufferedImage image,
                                                int newSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        boolean isTranslucent = image.getTransparency() != Transparency.OPAQUE;
        boolean isWidthGreater = width > height;

        if (isWidthGreater) {
            if (newSize >= width) {
                throw new IllegalArgumentException("newSize must be lower than" +
                                                   " the image width");
            }
        } else if (newSize >= height) {
            throw new IllegalArgumentException("newSize must be lower than" +
                                               " the image height");
        }

        if (newSize <= 0) {
            throw new IllegalArgumentException("newSize must" +
                                               " be greater than 0");
        }

        float ratioWH = (float) width / (float) height;
        float ratioHW = (float) height / (float) width;

        BufferedImage thumb = image;
        BufferedImage temp = null;

        Graphics2D g2 = null;

        try {
            int previousWidth = width;
            int previousHeight = height;
    
            do {
                if (isWidthGreater) {
                    width /= 2;
                    if (width < newSize) {
                        width = newSize;
                    }
                    height = (int) (width / ratioWH);
                } else {
                    height /= 2;
                    if (height < newSize) {
                        height = newSize;
                    }
                    width = (int) (height / ratioHW);
                }
    
                if (temp == null || isTranslucent) {
                    if (g2 != null) {
                        //do not need to wrap with finally
                        //outer finally block will ensure
                        //that resources are properly reclaimed
                        g2.dispose();
                    }
                    temp = createCompatibleImage(image, width, height);
                    g2 = temp.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                }
                g2.drawImage(thumb, 0, 0, width, height,
                        0, 0, previousWidth, previousHeight, null);
    
                previousWidth = width;
                previousHeight = height;
    
                thumb = temp;
            } while (newSize != (isWidthGreater ? width : height));
        } finally {
            if (g2 != null) {
                g2.dispose();
            }
        }

        if (width != thumb.getWidth() || height != thumb.getHeight()) {
            temp = createCompatibleImage(image, width, height);
            g2 = temp.createGraphics();
            
            try {
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(thumb, 0, 0, width, height, 0, 0, width, height, null);
            } finally {
                g2.dispose();
            }
            
            thumb = temp;
        }

        return thumb;
    }

    /**
     * <p>Returns a thumbnail of a source image.</p>
     * <p>This method offers a good trade-off between speed and quality.
     * The result looks better than
     * {@link #createThumbnailFast(java.awt.image.BufferedImage, int)} when
     * the new size is less than half the longest dimension of the source
     * image, yet the rendering speed is almost similar.</p>
     *
     * @see #createThumbnailFast(java.awt.image.BufferedImage, int)
     * @see #createThumbnailFast(java.awt.image.BufferedImage, int, int)
     * @see #createThumbnail(java.awt.image.BufferedImage, int)
     * @param image the source image
     * @param newWidth the width of the thumbnail
     * @param newHeight the height of the thumbnail
     * @return a new compatible <code>BufferedImage</code> containing a
     *   thumbnail of <code>image</code>
     * @throws IllegalArgumentException if <code>newWidth</code> is larger than
     *   the width of <code>image</code> or if code>newHeight</code> is larger
     *   than the height of <code>image or if one the dimensions is not &gt; 0</code>
     */
    public static BufferedImage createThumbnail(BufferedImage image,
                                                int newWidth, int newHeight) {
        int width = image.getWidth();
        int height = image.getHeight();

        boolean isTranslucent = image.getTransparency() != Transparency.OPAQUE;

        if (newWidth >= width || newHeight >= height) {
            throw new IllegalArgumentException("newWidth and newHeight cannot" +
                                               " be greater than the image" +
                                               " dimensions");
        } else if (newWidth <= 0 || newHeight <= 0) {
            throw new IllegalArgumentException("newWidth and newHeight must" +
                                               " be greater than 0");
        }

        BufferedImage thumb = image;
        BufferedImage temp = null;

        Graphics2D g2 = null;

        try {
            int previousWidth = width;
            int previousHeight = height;
    
            do {
                if (width > newWidth) {
                    width /= 2;
                    if (width < newWidth) {
                        width = newWidth;
                    }
                }
    
                if (height > newHeight) {
                    height /= 2;
                    if (height < newHeight) {
                        height = newHeight;
                    }
                }
    
                if (temp == null || isTranslucent) {
                    if (g2 != null) {
                        //do not need to wrap with finally
                        //outer finally block will ensure
                        //that resources are properly reclaimed
                        g2.dispose();
                    }
                    temp = createCompatibleImage(image, width, height);
                    g2 = temp.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                }
                g2.drawImage(thumb, 0, 0, width, height,
                             0, 0, previousWidth, previousHeight, null);
    
                previousWidth = width;
                previousHeight = height;
    
                thumb = temp;
            } while (width != newWidth || height != newHeight);
        } finally {
            if (g2 != null) {
                g2.dispose();
            }
        }

        if (width != thumb.getWidth() || height != thumb.getHeight()) {
            temp = createCompatibleImage(image, width, height);
            g2 = temp.createGraphics();
            
            try {
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(thumb, 0, 0, width, height, 0, 0, width, height, null);
            } finally {
                g2.dispose();
            }
            
            thumb = temp;
        }

        return thumb;
    }

    /**
     * <p>Returns an array of pixels, stored as integers, from a
     * <code>BufferedImage</code>. The pixels are grabbed from a rectangular
     * area defined by a location and two dimensions. Calling this method on
     * an image of type different from <code>BufferedImage.TYPE_INT_ARGB</code>
     * and <code>BufferedImage.TYPE_INT_RGB</code> will unmanage the image.</p>
     *
     * @param img the source image
     * @param x the x location at which to start grabbing pixels
     * @param y the y location at which to start grabbing pixels
     * @param w the width of the rectangle of pixels to grab
     * @param h the height of the rectangle of pixels to grab
     * @param pixels a pre-allocated array of pixels of size w*h; can be null
     * @return <code>pixels</code> if non-null, a new array of integers
     *   otherwise
     * @throws IllegalArgumentException is <code>pixels</code> is non-null and
     *   of length &lt; w*h
     */
    public static int[] getPixels(BufferedImage img,
                                  int x, int y, int w, int h, int[] pixels) {
        if (w == 0 || h == 0) {
            return new int[0];
        }

        if (pixels == null) {
            pixels = new int[w * h];
        } else if (pixels.length < w * h) {
            throw new IllegalArgumentException("pixels array must have a length" +
                                               " >= w*h");
        }

        int imageType = img.getType();
        if (imageType == BufferedImage.TYPE_INT_ARGB ||
            imageType == BufferedImage.TYPE_INT_RGB) {
            Raster raster = img.getRaster();
            return (int[]) raster.getDataElements(x, y, w, h, pixels);
        }

        // Unmanages the image
        return img.getRGB(x, y, w, h, pixels, 0, w);
    }

    /**
     * <p>Writes a rectangular area of pixels in the destination
     * <code>BufferedImage</code>. Calling this method on
     * an image of type different from <code>BufferedImage.TYPE_INT_ARGB</code>
     * and <code>BufferedImage.TYPE_INT_RGB</code> will unmanage the image.</p>
     *
     * @param img the destination image
     * @param x the x location at which to start storing pixels
     * @param y the y location at which to start storing pixels
     * @param w the width of the rectangle of pixels to store
     * @param h the height of the rectangle of pixels to store
     * @param pixels an array of pixels, stored as integers
     * @throws IllegalArgumentException is <code>pixels</code> is non-null and
     *   of length &lt; w*h
     */
    public static void setPixels(BufferedImage img,
                                 int x, int y, int w, int h, int[] pixels) {
        if (pixels == null || w == 0 || h == 0) {
            return;
        } else if (pixels.length < w * h) {
            throw new IllegalArgumentException("pixels array must have a length" +
                                               " >= w*h");
        }

        int imageType = img.getType();
        if (imageType == BufferedImage.TYPE_INT_ARGB ||
            imageType == BufferedImage.TYPE_INT_RGB) {
            WritableRaster raster = img.getRaster();
            raster.setDataElements(x, y, w, h, pixels);
        } else {
            // Unmanages the image
            img.setRGB(x, y, w, h, pixels, 0, w);
        }
    }

    /**
     * Clears the data from the image.
     * 
     * @param img
     *            the image to erase
     */
    public static void clear(Image img) {
        Graphics g = img.getGraphics();
        
        try {
            if (g instanceof Graphics2D) {
                ((Graphics2D) g).setComposite(AlphaComposite.Clear);
            } else {
                g.setColor(new Color(0, 0, 0, 0));
            }
            
            g.fillRect(0, 0, img.getWidth(null), img.getHeight(null));
        } finally {
            g.dispose();
        }
    }
    
    /* ---------------------------------------------------------------------------- */
    
    public static Image iconToImage(Icon icon) {
    	if (icon instanceof ImageIcon) {
    		return ((ImageIcon) icon).getImage();
    	}
    	else {

			BufferedImage image = createCompatibleTranslucentImage(icon.getIconWidth(), icon.getIconHeight());
			Graphics2D g = image.createGraphics();
			icon.paintIcon(null, g, 0, 0);
			g.dispose();
			return image;
		}
    }
    
    /* ---------------------------------------------------------------------------- */
    
    public static Color getAverageColor(BufferedImage img, int width, int height) {
    	float r = 0, g = 0, b = 0;
    	
    	for (int x = 0; x < width; x++) {
    		for (int y=0; y < height; y++) {
    			
    			final Color c = new Color(img.getRGB(x, y), true);
    			float a = c.getAlpha() / 255f;
    			
    			r += c.getRed() * a;
    			g += c.getGreen() * a;
    			b += c.getBlue() * a;
    		}
    	}
    	
    	
    	int nbPixels = width * height;
    	
    	return new Color(
    			Math.round(r / nbPixels),
    			Math.round(g / nbPixels),
    			Math.round(b / nbPixels)
    	);
    }
    
    public static Color getForegroundFromBackground(Color bgColor) {
    	// Counting the perceptive luminance - human eye favors green color... 
        double a = 1 - ( 0.299 * bgColor.getRed() + 0.587 * bgColor.getGreen() + 0.114 * bgColor.getBlue()) / 255;
        
        if (a < 0.5) {
        	return Color.BLACK;
        }
        else {
        	return Color.WHITE;
        }
    }
    
    /* ---------------------------------------------------------------------------- */
    
    public static BufferedImage generateProfileWallpaper(Profile profile, ProfileVersion version) throws IOException {
    	int w, h;
    	Dimension wallpaperDimension = new Dimension(0, 0);
    	
    	Map<Integer, Screen> screenMap = new HashMap<>();
    	
    	for (Screen sw : profile.getConfiguration()) {
    		w = sw.getX() + sw.getWidth();
    		h = sw.getY() + sw.getHeight();
    		
    		if (w > wallpaperDimension.width) wallpaperDimension.width = w;
    		if (h > wallpaperDimension.height) wallpaperDimension.height = h;
    		
    		screenMap.put(sw.getId(), sw);
    	}
    	

        BufferedImage bi = new BufferedImage(wallpaperDimension.width, wallpaperDimension.height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = bi.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Set a black background (for unused space) 
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, wallpaperDimension.width, wallpaperDimension.height);
        
        for (WallpaperParameters params : version.getParameters()) { 
        	try {
        		paintScreenOnWallpaper(g2, screenMap.get(params.getScreenId()), params);
        	}
        	catch (IOException e) {
        		e.printStackTrace();
        		throw e;
        	}
        }
        
        return bi;
    }
    
    private static void paintScreenOnWallpaper(Graphics2D g2, Screen screen, WallpaperParameters wallpaper) throws IOException {
    	
    	// Set the default background color
    	g2.setColor(wallpaper.getBackgroundColor());
    	g2.fillRect(screen.getX(), screen.getY(), screen.getWidth(), screen.getHeight());

    	if (wallpaper.getImage() != null) {
	    	// 
	    	final Dimension screenDimension = new Dimension(screen.getWidth(), screen.getHeight()); 
	
	    	// Load original image
	    	final BufferedImage originalImage = ImageIO.read(new File(wallpaper.getImage()));
	    	final BufferedImage scaledImage = ImageScalerUtils.getScaledImage(originalImage, wallpaper.getScalingAlgorithm(), screenDimension);
	    	
	    	// Compute image position on the screen
	    	int x = screen.getX() + ((screenDimension.width  - scaledImage.getWidth(null))  / 2);
	    	int y = screen.getY() + ((screenDimension.height - scaledImage.getHeight(null)) / 2);
	    	
	    	// Draw image
	        g2.drawImage(scaledImage, x, y, null);
    	}
    }
}
