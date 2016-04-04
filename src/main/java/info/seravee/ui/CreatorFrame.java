package info.seravee.ui;

import java.awt.BorderLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.google.common.eventbus.Subscribe;

import info.seravee.ui.lister.ImageListerPanel;
import info.seravee.wallcreator.beans.Profile;
import info.seravee.wallcreator.business.Services;
import info.seravee.wallcreator.events.NewProfileEvent;
import info.seravee.wallcreator.events.SelectedProfileEvent;
import info.seravee.wallcreator.ui.GuiConstants;
import info.seravee.wallcreator.ui.IconTestPanel;
import info.seravee.wallcreator.ui.icons.AppIcon;
import info.seravee.wallcreator.ui.icons.navigation.GearIcon;
import info.seravee.wallcreator.ui.icons.navigation.MonitorIcon;
import info.seravee.wallcreator.ui.icons.navigation.WallpapersIcon;
import info.seravee.wallcreator.ui.navigation.NavigationPane;
import info.seravee.wallcreator.ui.screens.DesktopPanel;
import info.seravee.wallcreator.utils.GraphicsUtilities;

/**
 * Created by ysaak on 27/01/15.
 */
public class CreatorFrame {
    private final JFrame frame;

    private final DesktopPanel desktopPanel;
    private final ImageListerPanel imageListerPanel;
    
    private final NavigationPane mainTabbedPane;


    private final ProfileEventSubscriber profileEventSubscriber;

    public CreatorFrame() {
        frame = new JFrame("Wall creator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImages(getFrameIcons());
        
        mainTabbedPane = new NavigationPane();
        
        desktopPanel = new DesktopPanel();
        imageListerPanel = new ImageListerPanel();

        buildFrame();
        frame.pack();
        frame.setLocationRelativeTo(null);
        
        profileEventSubscriber = new ProfileEventSubscriber();
        Services.getEventService().register(profileEventSubscriber);
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
    
    public void setProfiles(List<Profile> profiles) {
    	desktopPanel.setProfiles(profiles);
    }

    public void show() {
        frame.setVisible(true);
    }
    
    private List<Image> getFrameIcons() {
    	List<Image> icons = new ArrayList<Image>();
    	
    	icons.add(GraphicsUtilities.iconToImage(AppIcon.get16()));
    	icons.add(GraphicsUtilities.iconToImage(AppIcon.get32()));
    	icons.add(GraphicsUtilities.iconToImage(AppIcon.get48()));
    	icons.add(GraphicsUtilities.iconToImage(AppIcon.get64()));
    	icons.add(GraphicsUtilities.iconToImage(AppIcon.get128()));
    	
    	return icons;
    }
    
    private class ProfileEventSubscriber {
    	@Subscribe
    	public void handleNewProfileEvent(NewProfileEvent event) {
    		desktopPanel.profileAdded(event.getNewProfile());
    	}
    	
    	@Subscribe
    	public void handleSelectedProfileEvent(SelectedProfileEvent event) {
    		desktopPanel.profileSelected(event.getSelectedProfile());
    		imageListerPanel.setSelectedProfile(event.getSelectedProfile());
    	}
    }
}
