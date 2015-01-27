package info.seravee.ui;

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

    private ScalingAlgorithm scalingAlgorithm = ScalingAlgorithm.CENTER;
    private BufferedImage image = null;
    private Image scaledImage = null;

    public ImageDisplayer() {
        //
    }

    public void setImage(File imageFile) {
        try {
            image = ImageIO.read(imageFile);

            rebuildImage();
        }
        catch (IOException e) {
            e.printStackTrace();
            image = null;
            scaledImage = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 =(Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.clearRect(0, 0, getWidth(), getHeight());

        g2.setColor(Color.CYAN);
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (scaledImage != null) {
            int width = getWidth() - 1;
            int height = getHeight() - 1;

            int x = (width - scaledImage.getWidth(this)) / 2;
            int y = (height - scaledImage.getHeight(this)) / 2;

            g2.drawImage(scaledImage, x, y, this);
        }

        super.paintComponent(g);
    }

    public void setScalingAlgorithm(ScalingAlgorithm image1Size) {
        this.scalingAlgorithm = image1Size;
        rebuildImage();
    }

    private void rebuildImage() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Dimension scaledDimension = ImageScalerUtils.getScaledImage(scalingAlgorithm, new Dimension(image.getWidth(), image.getHeight()), getSize());
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
}
