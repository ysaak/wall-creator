package info.seravee.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

import info.seravee.data.Screen;
import info.seravee.data.lister.Wallpaper;
import info.seravee.ui.creator.DesktopPanel;
import info.seravee.ui.lister.ImageListerPanel;
import info.seravee.wallcreator.ui.GuiConstants;
import info.seravee.wallcreator.ui.IconTestPanel;
import info.seravee.wallcreator.ui.icons.navigation.GearIcon;
import info.seravee.wallcreator.ui.icons.navigation.MonitorIcon;
import info.seravee.wallcreator.ui.icons.navigation.WallpapersIcon;
import info.seravee.wallcreator.ui.navigation.NavigationPane;

/**
 * Created by ysaak on 27/01/15.
 */
public class CreatorFrame {

    private final JFrame frame;

    private final DesktopPanel desktopPanel;
    private final ImageListerPanel imageListerPanel;
    
    private final NavigationPane mainTabbedPane;
    
    

    public CreatorFrame() {
        frame = new JFrame("Wall creator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        mainTabbedPane = new NavigationPane();
        
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
        contentPane.setBorder(GuiConstants.BASE_EMPTY_BORDER);
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.setBackground(GuiConstants.MAIN_BACKGROUND_COLOR);
        
        desktopPanel.buildPanel();
        mainTabbedPane.addLeftTab(new MonitorIcon(), "Monitor", desktopPanel.getDisplay());
        
        imageListerPanel.buildPanel();
        mainTabbedPane.addLeftTab(new WallpapersIcon(), "Wallpapers", imageListerPanel.getDisplay());
        
        
        mainTabbedPane.addRightTab(new GearIcon(), "Parameters", new IconTestPanel(), true);
        
        
        contentPane.add(mainTabbedPane.getDisplay(), BorderLayout.CENTER);
    }

    public void setDesktopConfig(List<Screen> config) {
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
