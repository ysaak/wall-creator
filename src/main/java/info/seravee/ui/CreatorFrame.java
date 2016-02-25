package info.seravee.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import info.seravee.data.ScalingAlgorithm;
import info.seravee.data.lister.Wallpaper;
import info.seravee.ui.creator.DesktopPanel;
import info.seravee.ui.creator.DesktopParameterPanel;
import info.seravee.ui.creator.ImageDisplayer;
import info.seravee.ui.lister.ImageListerPanel;

/**
 * Created by ysaak on 27/01/15.
 */
public class CreatorFrame {

    private final JFrame frame;

    private final DesktopPanel desktopPanel;
    private final ImageListerPanel imageListerPanel;
    
    private final JTabbedPane mainTabbedPane;
    

    private final JTabbedPane tabbedPane;
    
    private final JButton saveImageButton;

    public CreatorFrame() {
        frame = new JFrame("Wall creator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        mainTabbedPane = new JTabbedPane();
        
        
        desktopPanel = new DesktopPanel();
        imageListerPanel = new ImageListerPanel();
        imageListerPanel.setWallpaperSelectionListener(new WallpaperSelectionListener() {
			@Override
			public void wallpaperSelected(Wallpaper wallpaper, int desktop) {
				wallpaperSelectedForDesktop(wallpaper, desktop);
			}
		});
        
        
        tabbedPane = new JTabbedPane();
        
        saveImageButton = new JButton("Save image");
        saveImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                final JFileChooser fc = new JFileChooser();
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.addChoosableFileFilter(new FileNameExtensionFilter("PNG Image (.png)", "png"));
                fc.setAcceptAllFileFilterUsed(false);

                int returnVal = fc.showSaveDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    
                    if (!file.getName().endsWith(".png")) {
                        file = new File(file.getAbsolutePath() + ".png");
                    }

                    new SaveImageWorker(file).execute();
                }
            }
        });

        buildFrame();
        //frame.setSize(new Dimension(811, 500));
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private void buildFrame() {
        JComponent contentPane = (JComponent) frame.getContentPane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(10, 10));
        
        // Selection panel
        JPanel selectionPanel = new JPanel(new BorderLayout());
        
        selectionPanel.add(desktopPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.add(tabbedPane, BorderLayout.CENTER);
        bottomPanel.add(saveImageButton, BorderLayout.SOUTH);

        selectionPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        mainTabbedPane.add("Selection", selectionPanel);
        
        
        imageListerPanel.buildPanel();
        mainTabbedPane.add("Lister", imageListerPanel.getDisplay());
        
        
        contentPane.add(mainTabbedPane, BorderLayout.CENTER);
    }

    public void setDesktopConfig(List<Rectangle> config) {
        int i = 0;
        for (Rectangle dc : config) {
            buildImageSelectPanel(dc, ++i);
        }
    }

    private void buildImageSelectPanel(Rectangle config, final int nbDesktop) {
        DesktopParameterPanel dpPanel = new DesktopParameterPanel();

        final JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        dpPanel.build();
        panel.add(dpPanel.getDisplay(), BorderLayout.CENTER);

        tabbedPane.add("Desktop " + nbDesktop, panel);

        desktopPanel.addScreen(nbDesktop, config);

        dpPanel.setListener(new DesktopParameterPanel.DesktopParameterListener() {
            @Override
            public void imageSelected(File imageFile) {
                desktopPanel.setImage(nbDesktop, imageFile);
            }

            @Override
            public void scalingAlgorithmSelected(ScalingAlgorithm algorithm) {
                desktopPanel.setScalingAlgorithm(nbDesktop, algorithm);
            }

            @Override
            public void backgroundColorSelected(Color backgroundColor) {
                desktopPanel.setBackgroundColor(nbDesktop, backgroundColor);
            }
        });
    }

    public void show() {
        frame.setVisible(true);
    }
    
    protected void wallpaperSelectedForDesktop(Wallpaper wallpaper, int desktopIndex) {
    	desktopPanel.setImage(desktopIndex, wallpaper.getFile());
    }
    
    private class SaveImageWorker extends SwingWorker<Void, Void> {
        private final File imageFile;
        public SaveImageWorker(File imageFile) {
            this.imageFile = imageFile;
        }

        @Override
        protected Void doInBackground() throws Exception {
            
            Dimension screensDim = desktopPanel.getScreensDimension();

            BufferedImage bi = new BufferedImage(screensDim.width, screensDim.height, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2 = bi.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, screensDim.width, screensDim.height);
            
            for (ImageDisplayer id : desktopPanel.getDisplayers()) {
                
                Image idI = id.getScaledImage();
                g2.drawImage(idI, id.getScreenData().x, id.getScreenData().y, null);
            }

            ImageIO.write(bi, "PNG", imageFile);
            return null;
        }
    }
}
