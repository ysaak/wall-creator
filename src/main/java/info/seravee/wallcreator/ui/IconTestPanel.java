package info.seravee.wallcreator.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JPanel;

import info.seravee.wallcreator.ui.components.button.XButton;
import info.seravee.wallcreator.ui.components.button.XButtonPosition;
import info.seravee.wallcreator.ui.icons.MinusIcon;
import info.seravee.wallcreator.ui.icons.PlusIcon;

public class IconTestPanel extends JPanel {

	public IconTestPanel() {
		this.setLayout(new BorderLayout());
		
		XButton button = new XButton();
		button.setAction(new AbstractAction("Le texte du bouton", new PlusIcon(14, Color.RED)) {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		this.add(button, BorderLayout.NORTH);
		
		
		JPanel linePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		XButton bFirst = new XButton();
		bFirst.setText("First BUTTON");
		bFirst.setPosition(XButtonPosition.FIRST);
		
		XButton bMiddle = new XButton();
		bMiddle.setText("Middle");
		bMiddle.setPosition(XButtonPosition.MIDDLE);
		
		XButton bLast = new XButton();
		bLast.setText("Last BUTTON");
		bLast.setPosition(XButtonPosition.LAST);
		
		linePanel.add(bFirst);
		linePanel.add(bMiddle);
		linePanel.add(bLast);
		
		this.add(linePanel, BorderLayout.SOUTH);
	}	
}
