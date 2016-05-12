package wallmanager.ui.commons.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ScrollPaneLayout;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * 
 * Based on : {@link http://ui-ideas.blogspot.fr/2012/06/mac-os-x-mountain-lion-scrollbars-in.html}
 */
public class LightScrollPane extends JComponent {
	private static final long serialVersionUID = -3224511626079501271L;
	
	private static final int SCROLL_BAR_DURATION = 1500; // 1.5 second 
	
	private static final int SCROLL_BAR_ALPHA_ROLLOVER = 150;
    private static final int SCROLL_BAR_ALPHA = 100;
    private static final int THUMB_BORDER_SIZE = 2;
    private static final int THUMB_SIZE = 8;
    private static final Color THUMB_COLOR = Color.BLACK;

    private final JScrollPane scrollPane;
    private final JScrollBar verticalScrollBar;
    private final JScrollBar horizontalScrollBar;
    
    private boolean hasVerticalScrollbar = false;
    private boolean hasHorizontalScrollbar = false;
    
    private boolean verticalScrollBarVisible = false;
    private boolean horizontalScrollBarVisible = false;
    
    private Timer hideTimer = null;
    
    public LightScrollPane(JComponent component) {
        scrollPane = new JScrollPane(component);
        scrollPane.setWheelScrollingEnabled(true);
        verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setVisible(false);
        verticalScrollBar.setOpaque(false);
        verticalScrollBar.setUI(new MyScrollBarUI());

        horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        horizontalScrollBar.setVisible(false);
        horizontalScrollBar.setOpaque(false);
        horizontalScrollBar.setUI(new MyScrollBarUI());

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayer(verticalScrollBar, JLayeredPane.PALETTE_LAYER);
        layeredPane.setLayer(horizontalScrollBar, JLayeredPane.PALETTE_LAYER);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setLayout(new ScrollPaneLayout() {
			private static final long serialVersionUID = -5144646619832131321L;

			@Override
            public void layoutContainer(Container parent) {
                viewport.setBounds(0, 0, getWidth(), getHeight());
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        displayScrollBarsIfNecessary(viewport);
                    }
                });
            }
        });
        
        layeredPane.add(horizontalScrollBar);
        layeredPane.add(verticalScrollBar);
        layeredPane.add(scrollPane);
        
        setLayout(new BorderLayout() {
			private static final long serialVersionUID = 5991098528162444341L;

			@Override
            public void layoutContainer(Container target) {
                super.layoutContainer(target);
                int width = getWidth();
                int height = getHeight();
                scrollPane.setBounds(0, 0, width, height);

                int scrollBarSize = 12;
                int cornerOffset = verticalScrollBar.isVisible() && verticalScrollBarVisible && horizontalScrollBarVisible && horizontalScrollBar.isVisible() ? scrollBarSize : 0;
                
                if (verticalScrollBar.isVisible() && verticalScrollBarVisible) {
                    verticalScrollBar.setBounds(width - scrollBarSize, 0, scrollBarSize, height - cornerOffset);
                }
                else {
                	verticalScrollBar.setBounds(width, 0, scrollBarSize, height - cornerOffset);
                }
                
                if (horizontalScrollBar.isVisible() && horizontalScrollBarVisible) {
                    horizontalScrollBar.setBounds(0, height - scrollBarSize, width - cornerOffset, scrollBarSize);
                }
            }
        });
        add(layeredPane, BorderLayout.CENTER);
        layeredPane.setBackground(Color.BLUE);
        
        
        component.addMouseListener(new ScrollMouseAdapter());
        component.addMouseMotionListener(new ScrollMouseAdapter());
        
        new ScrollbarEventListener(verticalScrollBar);
        new ScrollbarEventListener(horizontalScrollBar); 
    }
    
    public int getScrollBarSize() {
		return THUMB_SIZE + THUMB_BORDER_SIZE;
	}
    
    @Override
    public void setBackground(Color bg) {
    	super.setBackground(bg);
    	scrollPane.setBackground(bg);
    }

    private void displayScrollBarsIfNecessary(JViewport viewPort) {
        displayVerticalScrollBarIfNecessary(viewPort);
        displayHorizontalScrollBarIfNecessary(viewPort);
    }

    private void displayVerticalScrollBarIfNecessary(JViewport viewPort) {
        Rectangle viewRect = viewPort.getViewRect();
        Dimension viewSize = viewPort.getViewSize();
        hasVerticalScrollbar = viewSize.getHeight() > viewRect.getHeight();
        verticalScrollBar.setVisible(hasVerticalScrollbar);
    }

    private void displayHorizontalScrollBarIfNecessary(JViewport viewPort) {
        Rectangle viewRect = viewPort.getViewRect();
        Dimension viewSize = viewPort.getViewSize();
        hasHorizontalScrollbar = viewSize.getWidth() > viewRect.getWidth();
        horizontalScrollBar.setVisible(hasHorizontalScrollbar);
    }
    
    /**
     * Displays the scrollbars according to mouse event. The scrollbars are displayed, set a timer to hide them.
     * @param mouseEvent a mouse event
     * @param forceDisplay For the display of the scrollbars (e.i. when the wheel is used)
     */
    private void displayScrollBars(MouseEvent mouseEvent, boolean forceDisplay) {
    	if (forceDisplay || mouseHoverScrollbar(mouseEvent)) {
    		
    		verticalScrollBarVisible = hasVerticalScrollbar;
    		horizontalScrollBarVisible = hasHorizontalScrollbar;
    		
    		revalidate();
    		repaint();
    		
    		if (hideTimer != null) {
    			hideTimer.stop();
    			hideTimer = null;
    		}
    	}
    	
    	if (hideTimer != null && !horizontalScrollBarVisible && !horizontalScrollBarVisible) {
    		return;
    	}
    	
    	hideTimer = new Timer(SCROLL_BAR_DURATION, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				hideScrollbars();
			}
		});
    	hideTimer.setRepeats(false);
    	hideTimer.start();
    }
    
    /**
     * Hide the scrollbars.
     * Called by the timer
     */
    private void hideScrollbars() {
    	verticalScrollBarVisible = false;
    	horizontalScrollBarVisible = false;
    	
    	revalidate();
    	repaint();
    }
    
    /**
     * Indicate if the mouse is hover one of the scrollbars
     * @param mouseEvent a mouse event
     * @return {@code TRUE} if the mouse if hover one of the scrollbars - {@code FALSE} otherwise
     */
    private boolean mouseHoverScrollbar(MouseEvent mouseEvent) {
    	
    	final Point mousePosition = mouseEvent.getLocationOnScreen();
    	
    	if (hasVerticalScrollbar) {
    		Rectangle verticalbarRegion = new Rectangle(
    				getLocationOnScreen().x + getWidth() - 1 - (THUMB_SIZE * 2), 
    				getLocationOnScreen().y, 
    				THUMB_SIZE * 2, 
    				getHeight()
    		);
    		
    		return verticalbarRegion.contains(mousePosition);
    	}
    	
    	if (hasHorizontalScrollbar) {
    		Rectangle horizontalbarRegion = new Rectangle(
    				getLocationOnScreen().x, 
    				getLocationOnScreen().y + getHeight() - 1 - (THUMB_SIZE * 2), 
    				getWidth(), 
    				THUMB_SIZE * 2
    		);
    		
    		return horizontalbarRegion.contains(mousePosition);
    	}
    	
    	return false;
    }
    
    /**
     * A custom events listener for the scrollbars.
     * <b>Note:</b> automatically register for events
     * 
     * It ask to display the scrollbar if the mouse enter the scrollbar region or if the scrollbar value is adjusted.
     * It increments the scrollbar value when the wheel is used over it. 
     *  
     * @author ROTHDA
     */
    private class ScrollbarEventListener extends MouseAdapter implements AdjustmentListener {
    	
    	private final JScrollBar scrollbar;
    	
		public ScrollbarEventListener(JScrollBar scrollbar) {
			this.scrollbar = scrollbar;

			scrollbar.addMouseListener(this);
			scrollbar.addMouseWheelListener(this);
			scrollbar.addAdjustmentListener(this);
		}

		@Override
    	public void mouseEntered(MouseEvent e) {
			displayScrollBars(null, true);
    	}
    	
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			int increment = scrollbar.getUnitIncrement(e.getWheelRotation()) * e.getWheelRotation();
			scrollbar.setValue(scrollbar.getValue() + increment);
		}

		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			displayScrollBars(null, true);
		}
    }
    
    /**
     * A custom mouse adapter to ask to display the scrollbars when the mouse enter one of the scrollbar region.
     * 
     * @author ROTHDA
     */
    private class ScrollMouseAdapter extends MouseAdapter {
    	
		@Override
    	public void mouseEntered(MouseEvent e) {
			displayScrollBars(e, false);
    	}
    	
    	@Override
    	public void mouseMoved(MouseEvent e) {
    		displayScrollBars(e, false);
    	}
    }

    private static class MyScrollBarButton extends JButton {
		private static final long serialVersionUID = -7591003537921743246L;

		private MyScrollBarButton() {
            setOpaque(false);
            setFocusable(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setBorder(BorderFactory.createEmptyBorder());
        }
    }

    private static class MyScrollBarUI extends BasicScrollBarUI {
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return new MyScrollBarButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return new MyScrollBarButton();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            int alpha = isThumbRollover() ? SCROLL_BAR_ALPHA_ROLLOVER : SCROLL_BAR_ALPHA;
            int orientation = scrollbar.getOrientation();
            int arc = THUMB_SIZE;
            int x = thumbBounds.x + THUMB_BORDER_SIZE;
            int y = thumbBounds.y + THUMB_BORDER_SIZE;

            int width = orientation == JScrollBar.VERTICAL ?
                    THUMB_SIZE : thumbBounds.width - (THUMB_BORDER_SIZE * 2);
            width = Math.max(width, THUMB_SIZE);

            int height = orientation == JScrollBar.VERTICAL ?
                    thumbBounds.height - (THUMB_BORDER_SIZE * 2) : THUMB_SIZE;
            height = Math.max(height, THUMB_SIZE);

            Graphics2D graphics2D = (Graphics2D) g.create();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2D.setColor(new Color(THUMB_COLOR.getRed(),
                    THUMB_COLOR.getGreen(), THUMB_COLOR.getBlue(), alpha));
            graphics2D.fillRoundRect(x, y, width, height, arc, arc);
            graphics2D.dispose();
        }
    }
}