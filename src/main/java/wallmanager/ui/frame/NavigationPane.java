package wallmanager.ui.frame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import wallmanager.ui.GuiConstants;
import wallmanager.ui.commons.GBCHelper;
import wallmanager.ui.commons.SolarizedColor;
import wallmanager.ui.icons.navigation.NavigationIcon;

class NavigationPane {

	public enum TabLocation {
		TOP, BOTTOM
	}
	
	private final JPanel mainPanel;

	private final CardLayout cardLayout;
	private final JPanel buttonsPanel;
	private final JPanel panelsPanel;

	private final Map<String, TabButton> tabButtonMap = new HashMap<String, TabButton>(50);

	private int tabCount = 0;
	private final List<TabButton> topTabButtonList = new ArrayList<>();
	private final List<TabButton> bottomTabButtonList = new ArrayList<>();
	private final ButtonGroup buttonGroup = new ButtonGroup();

	private final ItemListener itemListener = new ItemListener() {
		@SuppressWarnings("synthetic-access")
		@Override
		public void itemStateChanged(final ItemEvent e) { tabItemEvent(e); }
	};

	public NavigationPane() {
		mainPanel = new JPanel(new BorderLayout());
		//mainPanel.setOpaque(false);
		mainPanel.setBackground(SolarizedColor.BASE02);

		buttonsPanel = new JPanel(new BorderLayout(GuiConstants.BASE_SPACER, 0));
		buttonsPanel.setOpaque(false);

		// Panel containing all the panels
		cardLayout = new CardLayout();
		panelsPanel = new JPanel(cardLayout);
		panelsPanel.setBackground(Color.WHITE);
		panelsPanel.setBorder(BorderFactory.createEmptyBorder(GuiConstants.BASE_SPACER, GuiConstants.BASE_SPACER, GuiConstants.BASE_SPACER, GuiConstants.BASE_SPACER));
		panelsPanel.setOpaque(true);
		
		
		Dimension buttonBarSize = new Dimension(buttonsPanel.getPreferredSize().width, TabButton.TAB_BUTTON_HEIGHT);
		buttonsPanel.setMinimumSize(buttonBarSize);

		
		mainPanel.add(buttonsPanel, BorderLayout.WEST);
		mainPanel.add(panelsPanel, BorderLayout.CENTER);
	}

	public void addTopTab(final NavigationIcon icon, String name, final JComponent component) {
		addTab(TabLocation.TOP, icon, name, component, true);
	}
	
	public void addBottomTab(final NavigationIcon icon, String name, final JComponent component) {
		addTab(TabLocation.BOTTOM, icon, name, component, true);
	}
	
	private void addTab(final TabLocation location, final NavigationIcon icon, final String name, final JComponent component, final boolean iconOnly) {

		if (!tabButtonMap.containsKey(name)) {

			TabButton tb = new TabButton(icon, name, iconOnly);
			tabButtonMap.put(name, tb);

			if (location == TabLocation.TOP) {
				topTabButtonList.add(tb);
			} else {
				bottomTabButtonList.add(tb);
			}

			tb.addItemListener(itemListener);
			buttonGroup.add(tb);

			panelsPanel.add(component, tb.getName());
			if (++tabCount == 1) {
				tb.setSelected(true);
			}
			
			buildButtonsPanel();
		}
	}
	
	private void buildButtonsPanel() {
		buttonsPanel.removeAll();
		buttonsPanel.setBorder(new EmptyBorder(GuiConstants.BASE_SPACER, 0, GuiConstants.BASE_SPACER, 0));
		
		JPanel topButtonsPanel = new JPanel(true);
		topButtonsPanel.setOpaque(false);

		GBCHelper gblh = new GBCHelper(topButtonsPanel, new Insets(5, 0, 5, 0));
		
		int index = 0;
		for (TabButton tb : topTabButtonList) {
			gblh.addComponent(tb, 0, index);
			index++;
		}
		
		buttonsPanel.add(topButtonsPanel, BorderLayout.NORTH);
		
		
		
		JPanel bottomButtonsPanel = new JPanel(true);
		
		gblh = new GBCHelper(bottomButtonsPanel, new Insets(5, 0, 5, 0));
		bottomButtonsPanel.setOpaque(false);
		
		index = 0;
		for (TabButton tb : bottomTabButtonList) {
			gblh.addComponent(tb, 0, index);
			index++;
		}

		buttonsPanel.add(bottomButtonsPanel, BorderLayout.SOUTH);
	}
	

  private void tabItemEvent(final ItemEvent event) {
    if (event.getStateChange()==ItemEvent.SELECTED) {
      final TabButton source = ((TabButton)event.getSource());
      cardLayout.show(panelsPanel, source.getName());
    }
  }
	
	public JPanel getDisplay() {
		return mainPanel;
	}
	
	private static final class TabButton extends JToggleButton {
		private static final long serialVersionUID = 945023396844547747L;

		public static final int TAB_BUTTON_HEIGHT = 50;
		private static final int ARC_SIZE = 10;
		
		// Animation parameters
		private static final int FADE_DURATION = 150; // time in MS
		private static final float FINAL_OPACITY = 0.3f;
		private static final float OPACITY_INCREMENT = FINAL_OPACITY / FADE_DURATION;
		
		private Timer transitionTimer = null;
		private boolean transition = false;
		private float bgOpacity = 0f;
		
		private final NavigationIcon icon;

		public TabButton(final NavigationIcon icon, final String name, final boolean iconOnly) {
			setName(name);
			
			this.icon = icon;
			
			setText(!iconOnly ? name: "");
			setToolTipText(name);

			setBorderPainted(false);
			setOpaque(false);
			
			
			setFont(getFont().deriveFont(Font.BOLD, 14.f));
			
			final int w;
			if (iconOnly) {
				w = TAB_BUTTON_HEIGHT; 
			}
			else {
				w = getPreferredSize().width + TAB_BUTTON_HEIGHT; // 30px for icon, 20px for spaces
			}
			
			
			Dimension d = new Dimension(w, TAB_BUTTON_HEIGHT);
			
			setSize(d);
			setMinimumSize(d);
			setPreferredSize(d);
			setMaximumSize(d);
			
			// Animation system
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					startTimer(1);
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					startTimer(-1);
				}
			});
			
			addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					icon.setColoredIcon(e.getStateChange() == ItemEvent.SELECTED);
				}
			});
		}

		@Override
		protected void paintComponent(Graphics g) {
			final Graphics2D g2 = (Graphics2D) g;

			final Color oldColor = g.getColor();
			final RenderingHints oldRenderingHints = g2.getRenderingHints();
			
			final String text = getText();
			final int w = getWidth();
			final int h = getHeight();
			
			final Color bgColor;
			final Color fgColor;
			
			if (isEnabled()) {
				if (isSelected()) {
					bgColor = Color.WHITE;
					fgColor = Color.BLACK;
				}
				else if (transition) {
					bgColor = new Color(1f, 1f, 1f, bgOpacity);
					fgColor = Color.WHITE;
				}
				else {
					bgColor = new Color(1f, 1f, 1f, 0f);
					fgColor = Color.WHITE;
				}
			}
			else {
				bgColor = new Color(1f, 1f, 1f, 0);
				fgColor = Color.LIGHT_GRAY;
			}

			RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			hints.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2.setRenderingHints(hints);
			
			// Draw the background
			g2.setColor(bgColor);
			
			g2.fillRect(0, 0, w, h);

			// Paint icon
			icon.paintIcon(this, g2, ARC_SIZE, ARC_SIZE);
			
			// Draw the text
			g2.setColor(fgColor);
			g2.setFont(getFont());
			
			drawCenteredText(g2, text, w, h);
			
			// Reset
			g2.setRenderingHints(oldRenderingHints);
			g2.setColor(oldColor);
		}
		
		private void drawCenteredText(final Graphics2D g2, final String text, final int width, final int height) {
			int x = 2 * ARC_SIZE + icon.getIconWidth(), y = 0;
			
			final FontMetrics metrics = g2.getFontMetrics(getFont());
			
			int fHeight = metrics.getHeight();
			
			y = ((height - fHeight) / 2) + fHeight - metrics.getDescent();
			
			g2.drawString(text, x, y);
		}
		
		protected void startTimer(int direction) {
			
			if (transitionTimer != null) {
				transitionTimer.stop();
				transitionTimer = null;
			}
			
			
			transition = true;
			transitionTimer = new Timer(1, new BackgroundOpacityUpdater(direction));
			transitionTimer.setRepeats(true);
			transitionTimer.start();
		}
		
		protected void updateBackgroundOpacity(int direction) {
			bgOpacity += direction * OPACITY_INCREMENT;
			repaint();
			
			// Stop condition
			if ((direction < 0 && bgOpacity <= 0) || (direction > 0 && bgOpacity >= FINAL_OPACITY)) {
				transitionTimer.stop();
				transitionTimer = null;
			}
			
			if (bgOpacity <= 0) {
				transition = false;
			}
		}
		
		private class BackgroundOpacityUpdater implements ActionListener {
			private final int direction;
			
			public BackgroundOpacityUpdater(int direction) { this.direction = direction; }
			
			@Override
			public void actionPerformed(ActionEvent e) { updateBackgroundOpacity(direction); }
		}
	}
}
