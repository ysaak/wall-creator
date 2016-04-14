import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import info.seravee.wallcreator.utils.GraphicsUtilities;
import info.seravee.wallmanager.ui.commons.frame.WMFrame;
import info.seravee.wallmanager.ui.commons.icons.AppIcon;

public class FrameTest {

	public static void main(String[] args) {
		
		
		WMFrame frame = new WMFrame("Test");
		
		frame.setIconImages(getFrameIcons());
		
		JPanel content = frame.getContentPane();
		content.add(new JLabel("I am the test string"));
		
		frame.setSize(500, 400);
		
		
		frame.setVisible(true);
	}

	private static List<Image> getFrameIcons() {
    	List<Image> icons = new ArrayList<Image>();
    	
    	icons.add(GraphicsUtilities.iconToImage(AppIcon.get16()));
    	icons.add(GraphicsUtilities.iconToImage(AppIcon.get32()));
    	icons.add(GraphicsUtilities.iconToImage(AppIcon.get48()));
    	icons.add(GraphicsUtilities.iconToImage(AppIcon.get64()));
    	icons.add(GraphicsUtilities.iconToImage(AppIcon.get128()));
    	
    	return icons;
    }
}
