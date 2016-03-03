package info.seravee.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;

import info.seravee.data.lister.Wallpaper;
import info.seravee.ui.creator.DesktopPanel;
import info.seravee.ui.icons.navigation.MonitorIcon;
import info.seravee.ui.icons.navigation.WallpapersIcon;
import info.seravee.ui.laf.TabbedPane;
import info.seravee.ui.laf.TabbedPane.TabLocation;
import info.seravee.ui.lister.ImageListerPanel;

/**
 * Created by ysaak on 27/01/15.
 */
public class CreatorFrame {

    private final JFrame frame;

    private final DesktopPanel desktopPanel;
    private final ImageListerPanel imageListerPanel;
    
    private final TabbedPane mainTabbedPane;
    
    

    public CreatorFrame() {
        frame = new JFrame("Wall creator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        mainTabbedPane = new TabbedPane();
        
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
        contentPane.setBackground(Color.LIGHT_GRAY);
        
        desktopPanel.buildPanel();
        mainTabbedPane.addTab(TabLocation.LEFT, new MonitorIcon(), "Monitor", desktopPanel.getDisplay());
        
        imageListerPanel.buildPanel();
        mainTabbedPane.addTab(TabLocation.LEFT, new WallpapersIcon(), "Wallpapers", imageListerPanel.getDisplay());
        
        
        contentPane.add(mainTabbedPane.getDisplay(), BorderLayout.CENTER);
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
