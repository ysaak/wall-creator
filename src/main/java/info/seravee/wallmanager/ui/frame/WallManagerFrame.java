package info.seravee.wallmanager.ui.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import com.google.inject.Inject;

import info.seravee.wallcreator.ui.GuiConstants;
import info.seravee.wallcreator.ui.IconTestPanel;
import info.seravee.wallcreator.utils.GraphicsUtilities;
import info.seravee.wallmanager.beans.profile.Profile;
import info.seravee.wallmanager.business.worker.WorkerService;
import info.seravee.wallmanager.ui.ApplicationUI;
import info.seravee.wallmanager.ui.commons.components.LockableFrame;
import info.seravee.wallmanager.ui.commons.frame.WMFrame;
import info.seravee.wallmanager.ui.commons.icons.AppIcon;
import info.seravee.wallmanager.ui.commons.icons.navigation.GearIcon;
import info.seravee.wallmanager.ui.commons.icons.navigation.MonitorIcon;
import info.seravee.wallmanager.ui.commons.icons.navigation.WallpapersIcon;
import info.seravee.wallmanager.ui.frame.desktop.DesktopEditorPanel;
import info.seravee.wallmanager.ui.frame.desktop.ProfileEditorController;
import info.seravee.wallmanager.ui.frame.profiles.ProfileListController;
import info.seravee.wallmanager.ui.frame.profiles.ProfilesListPanel;
import info.seravee.wallmanager.ui.frame.wallpapers.WallpapersController;
import info.seravee.wallmanager.ui.frame.wallpapers.WallpapersListPanel;

public class WallManagerFrame extends ApplicationUI implements LockableFrame {
    private final JFrame frame;
    
    private final ProfileListController profileListController;
    private final ProfilesListPanel profilesListPanel;

    private final ProfileEditorController profileEditorController;
    private final DesktopEditorPanel desktopEditorPanel;
    
    private final WallpapersController wallpapersController;
    private final WallpapersListPanel wallpaperListPanel;
    
    private final NavigationPane mainTabbedPane;
    
    private final JLayeredPane layeredPane;
    private final LockPanel lockLayer;
    
    @Inject
    private WorkerService workerService;
    
    @Inject
    public WallManagerFrame(ProfileListController profileListcontroller, ProfileEditorController profileEditorController, WallpapersController wallpapersController) {
    	this.profileListController = profileListcontroller;
    	this.wallpapersController = wallpapersController;
    	this.profileEditorController = profileEditorController;
    	
        frame = new WMFrame("Wall creator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImages(getFrameIcons());
        frame.addWindowListener(new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		onFrameClose();
        	}
		});
        
        profilesListPanel = new ProfilesListPanel();
        this.profileListController.setPanel(profilesListPanel);
        
        mainTabbedPane = new NavigationPane();
        
        desktopEditorPanel = new DesktopEditorPanel();
        this.profileEditorController.setPanel(desktopEditorPanel);
        
        wallpaperListPanel = new WallpapersListPanel();
        this.wallpapersController.setPanel(wallpaperListPanel);
        
        layeredPane = new JLayeredPane();
        lockLayer = new LockPanel();
    }
    
    @Override
    public void build() {
        JComponent contentPane = (JComponent) frame.getContentPane();
        //contentPane.setBorder(GuiConstants.BASE_EMPTY_BORDER);
        contentPane.setLayout(new BorderLayout(0,0));
        contentPane.setBackground(GuiConstants.MAIN_BACKGROUND_COLOR);
        
        // Profile selection
        profilesListPanel.buildPanel();
        //contentPane.add(profilesListPanel.getDisplay(), BorderLayout.NORTH);
        layeredPane.add(profilesListPanel.getDisplay(), JLayeredPane.PALETTE_LAYER + 8);
        layeredPane.add(profilesListPanel.getActionsPanel(), JLayeredPane.PALETTE_LAYER + 4);
        layeredPane.add(profilesListPanel.getOverlayPanel(), JLayeredPane.PALETTE_LAYER);
        
        // --- Navigation
        // Desktop Editor
        desktopEditorPanel.buildPanel();
        mainTabbedPane.addTopTab(new MonitorIcon(), "Editor", desktopEditorPanel.getDisplay());
        
        wallpaperListPanel.buildPanel();
        mainTabbedPane.addTopTab(new WallpapersIcon(), "Wallpapers", wallpaperListPanel.getDisplay());
        
        
        mainTabbedPane.addBottomTab(new GearIcon(), "Parameters", new IconTestPanel());
        
        
        //contentPane.add(mainTabbedPane.getDisplay(), BorderLayout.CENTER);
        layeredPane.add(mainTabbedPane.getDisplay(), JLayeredPane.DEFAULT_LAYER);
        contentPane.add(layeredPane, BorderLayout.CENTER);
        
        
        final int topbarHeight = 50;
        final int topbarShadowSize = 8;
        
        
        Dimension panelSize = new Dimension(mainTabbedPane.getDisplay().getPreferredSize());
        panelSize.height += topbarHeight - topbarShadowSize;
        
        layeredPane.setMinimumSize(panelSize);
        layeredPane.setPreferredSize(panelSize);
        
        layeredPane.add(lockLayer, JLayeredPane.MODAL_LAYER);
        lockLayer.setBounds(0, 0, panelSize.width, panelSize.height);
        lockLayer.setVisible(false);
        
        mainTabbedPane.getDisplay().setBounds(
        		0, topbarHeight - topbarShadowSize, 
        		mainTabbedPane.getDisplay().getPreferredSize().width, 
        		mainTabbedPane.getDisplay().getPreferredSize().height
        );
        
        
        profilesListPanel.getDisplay().setBounds(0, 0, 
        		panelSize.width, topbarHeight);
        
        profilesListPanel.getActionsPanel().setBounds(0, - topbarShadowSize,
        		panelSize.width, topbarHeight);
        
        profilesListPanel.getOverlayPanel().setBounds(0, topbarHeight - topbarShadowSize,
        		panelSize.width, panelSize.height);
    }
    
    public void setProfiles(List<Profile> profiles) {
    	profilesListPanel.setProfiles(profiles);
    }
    
    @Override
    public void display() {
    	workerService.setMainFrame(this);
    	
    	wallpapersController.start();
    	profileEditorController.start();
    	profileListController.start();
    	
    	frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    protected void onFrameClose() {
    	workerService.setMainFrame(null);
    	
    	profileListController.stop();
    	profileEditorController.stop();
    	wallpapersController.stop();
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
    
    @Override
    public void lockScreen(boolean lock, String message) {
    	lockLayer.setMessage(message != null ? message : "Default message");
    	lockLayer.setVisible(lock);
    }
}
