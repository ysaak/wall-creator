package info.seravee.wallmanager.ui.frame;

import java.awt.BorderLayout;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.google.common.eventbus.Subscribe;

import info.seravee.wallcreator.ui.GuiConstants;
import info.seravee.wallcreator.ui.IconTestPanel;
import info.seravee.wallcreator.utils.GraphicsUtilities;
import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.business.Services;
import info.seravee.wallmanager.business.events.EventBusLine;
import info.seravee.wallmanager.ui.commons.components.LockableFrame;
import info.seravee.wallmanager.ui.commons.frame.WMFrame;
import info.seravee.wallmanager.ui.commons.icons.AppIcon;
import info.seravee.wallmanager.ui.commons.icons.navigation.GearIcon;
import info.seravee.wallmanager.ui.commons.icons.navigation.MonitorIcon;
import info.seravee.wallmanager.ui.commons.icons.navigation.WallpapersIcon;
import info.seravee.wallmanager.ui.frame.desktop.DesktopEditorPanel;
import info.seravee.wallmanager.ui.frame.events.ProfileSelectedEvent;
import info.seravee.wallmanager.ui.frame.profiles.ProfilesListPanel;
import info.seravee.wallmanager.ui.frame.wallpapers.WallpapersListListener;
import info.seravee.wallmanager.ui.frame.wallpapers.WallpapersListPanel;

public class WallManagerFrame implements LockableFrame {
    private final JFrame frame;
    
    private final ProfilesListPanel profilesListPanel;

    private final DesktopEditorPanel desktopEditorPanel;
    private final WallpapersListPanel wallpaperListPanel;
    
    private final NavigationPane mainTabbedPane;

    private final ProfileEventSubscriber profileEventSubscriber;

    public WallManagerFrame() {
        frame = new WMFrame("Wall creator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImages(getFrameIcons());
        
        profilesListPanel = new ProfilesListPanel();
        
        mainTabbedPane = new NavigationPane();
        
        desktopEditorPanel = new DesktopEditorPanel();
        
        wallpaperListPanel = new WallpapersListPanel();
        wallpaperListPanel.addWallpapersListListener(new WallpapersListListener() {
			
			@Override
			public void wallpaperSelectedForScreen(int screenId, File imageFile) {
				desktopEditorPanel.wallpaperSelectedForScreen(screenId, imageFile);
			}
		});

        buildFrame();
        frame.pack();
        frame.setLocationRelativeTo(null);
        
        profileEventSubscriber = new ProfileEventSubscriber();
        Services.getEventService().register(EventBusLine.FRAME, profileEventSubscriber);
    }

    private void buildFrame() {
        JComponent contentPane = (JComponent) frame.getContentPane();
        //contentPane.setBorder(GuiConstants.BASE_EMPTY_BORDER);
        contentPane.setLayout(new BorderLayout(0,0));
        contentPane.setBackground(GuiConstants.MAIN_BACKGROUND_COLOR);
        
        // Profile selection
        profilesListPanel.buildPanel();
        contentPane.add(profilesListPanel.getDisplay(), BorderLayout.NORTH);
        
        // --- Navigation
        // Desktop Editor
        desktopEditorPanel.buildPanel();
        mainTabbedPane.addTopTab(new MonitorIcon(), "Editor", desktopEditorPanel.getDisplay());
        
        wallpaperListPanel.buildPanel();
        mainTabbedPane.addTopTab(new WallpapersIcon(), "Wallpapers", wallpaperListPanel.getDisplay());
        
        
        mainTabbedPane.addBottomTab(new GearIcon(), "Parameters", new IconTestPanel());
        
        
        contentPane.add(mainTabbedPane.getDisplay(), BorderLayout.CENTER);
    }
    
    public void setProfiles(List<Profile> profiles) {
    	profilesListPanel.setProfiles(profiles);
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
    	public void handleSelectedProfileEvent(ProfileSelectedEvent event) {
    		wallpaperListPanel.setSelectedProfile(event.getProfile());
    	}
    }
    
    @Override
    public void lockScreen(boolean lock) {
    	// TODO Auto-generated method stub
    	
    }
    
    @Override
    public void lockScreen(boolean lock, String message) {
    	// TODO Auto-generated method stub
    	
    }
}
