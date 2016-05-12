package wallmanager.ui.commons.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import wallmanager.ui.commons.DropShadowBorder;
import wallmanager.ui.commons.GBCHelper;
import wallmanager.ui.commons.SolarizedColor;
import wallmanager.ui.commons.component.xbutton.XButton;
import wallmanager.ui.commons.component.xbutton.XButtonColor;
import wallmanager.ui.commons.component.xbutton.XButtonPosition;

public class WMFrame extends JFrame {
	private static final long serialVersionUID = -7189905701889228004L;
	
	private JPanel contentPane;
	private JPanel topbarPanel;
	private JPanel main;

	private JLabel appTitleLabel;
	private JButton minimizeButton;
	private JButton closeButton;

	private Point initialClick;

	public WMFrame(String title) {
		super(title);

		initialize();
		showWindow();
		installListeners();
	}

	private void initialize() {
		// don't show a frame or title bar
		setUndecorated(true);

		// Create JPanel and set it as the content pane
		contentPane = new JPanel();
		setContentPane(contentPane);

		// If main has not already been created, create it.
		// Explained later
		if (main == null) {
			main = new JPanel();
		}

		// Create panel for close button
		
		appTitleLabel = new JLabel(getTitle());
		
		topbarPanel = new JPanel();
		
		// Create point to catch initial mouse click coordinates
		initialClick = new Point();
	}

	private void showWindow() {
		// If not set, default to FlowLayout
		if (main.getLayout() == null) {
			setLayout(new FlowLayout());
		}
		
		// Frame background
		//contentPane.setBackground(SolarizedColor.BASE03);
		setBackground(new Color(0,0,0,0));
		//contentPane.setBorder(GuiConstants.BASE_EMPTY_BORDER);
		//contentPane.setBackground(new Color(0,0,0,0));
		contentPane.setOpaque(false);
		contentPane.setBorder(new DropShadowBorder(Color.BLACK, 8, .5f, 12, true, true, true, true));
		
		JPanel innerContentPane = new JPanel();
		innerContentPane.setBackground(Color.BLACK);
		innerContentPane.setBorder(BorderFactory.createLineBorder(SolarizedColor.BASE02, 1));
		
		appTitleLabel.setFont(appTitleLabel.getFont().deriveFont(Font.BOLD, 13f));
		appTitleLabel.setForeground(Color.WHITE);

		// close "button" - show this image by default
		minimizeButton = new XButton(new MinimizeFrameAction(), new XButtonColor(Color.BLACK, SolarizedColor.BASE02, SolarizedColor.BASE02.darker(), Color.WHITE), XButtonPosition.MIDDLE);
		minimizeButton.setIconTextGap(0);
		closeButton = new XButton(new CloseFrameAction(), new XButtonColor(Color.BLACK, SolarizedColor.RED, SolarizedColor.RED.darker(), Color.WHITE), XButtonPosition.MIDDLE);
		closeButton.setIconTextGap(0);
		
		// Build top bar
		topbarPanel.setOpaque(false);
		
		GBCHelper gblh = new GBCHelper(topbarPanel, new Insets(0, 0, 0, 0));
		gblh.addComponent(appTitleLabel, 0, 0, 2.0, 2.0, 1, 1, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5));
		gblh.addComponent(minimizeButton, 1, 0);
		gblh.addComponent(closeButton, 2, 0);

		
		// Add the two panels to the content pane
		innerContentPane.setLayout(new BorderLayout(5, 5));
		innerContentPane.add(topbarPanel, BorderLayout.NORTH);
		innerContentPane.add(main, BorderLayout.CENTER);
		
		contentPane.setLayout(new BorderLayout());
		contentPane.add(innerContentPane, BorderLayout.CENTER);

		// Set position somewhere near the middle of the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenSize.width / 2 - (getWidth() / 2), screenSize.height / 2 - (getHeight() / 2));

		// keep window on top of others
		//setAlwaysOnTop(true);
	}
	
	@Override
	public synchronized void setIconImages(List<? extends Image> icons) {
		super.setIconImages(icons);
		
		for (Image i : icons) {
			if (i.getWidth(null) == 16)
				appTitleLabel.setIcon(new ImageIcon(i));
		}
	}
	
	private void installListeners()
	{
	    // Get point of initial mouse click
	    addMouseListener( new MouseAdapter()
	    {
	        public void mousePressed( MouseEvent e )
	        {
	            initialClick = e.getPoint(); getComponentAt( initialClick );
	        }
	    });
	 
	    // Move window when mouse is dragged
	    addMouseMotionListener( new MouseMotionAdapter()
	    {
	        public void mouseDragged( MouseEvent e )
	        {
	            // get location of Window
	            int thisX = getLocation().x; int thisY = getLocation().y;
	 
	            // Determine how much the mouse moved since the initial click
	            int xMoved = ( thisX + e.getX() ) - ( thisX + initialClick.x );
	            int yMoved = ( thisY + e.getY() ) - ( thisY + initialClick.y );
	 
	            // Move window to this position
	            int X = thisX + xMoved; int Y = thisY + yMoved; setLocation( X, Y );
	        }
	    }); 
	 
	    // Close "button" (image) listeners
	    closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}

	@Override
	public JPanel getContentPane() {
		return main;
	}
	
	/**
	 * Close and dispose
	 */
	public void close() {
		setVisible(false);
		dispose();
	}

	@Override
	public void setLayout(LayoutManager manager) {
		if (main == null) {
			main = new JPanel();
			main.setLayout(new FlowLayout());
		} 
		else {
			main.setLayout(manager);
		}

		if (!(getLayout() instanceof BorderLayout)) {
			super.setRootPaneCheckingEnabled(false);
			super.setLayout(new BorderLayout());
			super.setRootPane(super.getRootPane());
			super.setRootPaneCheckingEnabled(true);
		}
	}
	
	private class CloseFrameAction extends AbstractAction {
		private static final long serialVersionUID = -2925320606796383163L;

		public CloseFrameAction() {
			super(null, new FrameCloseIcon());
			putValue(SHORT_DESCRIPTION, "Close");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			close();
		}
	}
	
	private class MinimizeFrameAction extends AbstractAction {
		private static final long serialVersionUID = 6154045501586688195L;

		public MinimizeFrameAction() {
			super(null, new FrameMinimizeIcon());
			putValue(SHORT_DESCRIPTION, "Minimize");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			setState(JFrame.ICONIFIED);
		}
	}
}
