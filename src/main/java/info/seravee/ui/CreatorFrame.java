package info.seravee.ui;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import info.seravee.data.lister.Wallpaper;
import info.seravee.ui.creator.DesktopPanel;
import info.seravee.ui.lister.ImageListerPanel;

/**
 * Created by ysaak on 27/01/15.
 */
public class CreatorFrame {

    private final JFrame frame;

    private final DesktopPanel desktopPanel;
    private final ImageListerPanel imageListerPanel;
    
    private final JTabbedPane mainTabbedPane;
    
    

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

        buildFrame();
        //frame.setSize(new Dimension(811, 500));
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private void buildFrame() {
        JComponent contentPane = (JComponent) frame.getContentPane();
        contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(10, 10));
        
        desktopPanel.buildPanel();
        mainTabbedPane.add("Selection", desktopPanel.getDisplay());
        
        imageListerPanel.buildPanel();
        mainTabbedPane.add("Lister", imageListerPanel.getDisplay());
        
        
        contentPane.add(mainTabbedPane, BorderLayout.CENTER);
    }

    public void setDesktopConfig(List<Rectangle> config) {
    	desktopPanel.setDesktopConfig(config);
    	imageListerPanel.setDesktopConfig(config);
    }

    

    public void show() {
        frame.setVisible(true);
    }
    
    protected void wallpaperSelectedForDesktop(Wallpaper wallpaper, int desktopIndex) {
    	desktopPanel.setWallpaper(wallpaper.getFile(), desktopIndex);
    }
}
