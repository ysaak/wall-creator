package info.seravee.ui;

import info.seravee.DefaultConfiguration;
import info.seravee.data.ScalingAlgorithm;
import info.seravee.utils.ImageScalerUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by ysaak on 27/01/15.
 */
class ImageDisplayer extends JComponent {

    private ScalingAlgorithm scalingAlgorithm = DefaultConfiguration.SCALING_ALGORITHM;
    private BufferedImage image = null;
    private Image scaledImage = null;
    
    private double displayScaleRatio = 1.0;

    private final Rectangle screenData;

    public ImageDisplayer(Rectangle screenData) {
        this.screenData = screenData;
        setBackground(DefaultConfiguration.BACKGROUND_COLOR);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color oldColor = g.getColor();

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


        g.setColor(Color.BLACK);
        g.drawRect(0, 0, getWidth()-1, getHeight()-1);

        g.setColor(oldColor);

        //super.paintComponent(g);
    }

    public void setImage(File imageFile) {
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
    
    private void rebuildImage() {
        if (image == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Dimension initialDimensions = new Dimension((int) (image.getWidth() * displayScaleRatio), (int) (image.getHeight() * displayScaleRatio));
                
                scaledImage = image.getScaledInstance(initialDimensions.width, initialDimensions.height, Image.SCALE_SMOOTH);
                
                Dimension scaledDimension = ImageScalerUtils.getScaledImage(scalingAlgorithm, initialDimensions, getSize());
                scaledImage = image.getScaledInstance(scaledDimension.width, scaledDimension.height, Image.SCALE_SMOOTH);

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        ImageDisplayer.this.repaint();
                    }
                });
            }
        }).start();
    }

    public void setDisplayScaleRatio(double displayScaleRatio) {
        this.displayScaleRatio = displayScaleRatio;
    }

    public Rectangle getScreenData() {
        return screenData;
    }
}
