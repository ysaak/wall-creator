package info.seravee.wallcreator.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import info.seravee.wallcreator.ui.components.button.XButton;
import info.seravee.wallmanager.ui.commons.icons.PlusIcon;

public class IconTestPanel extends JPanel {

	public IconTestPanel() {
		this.setLayout(new BorderLayout());
		
		XButton button = new XButton(new AbstractAction("Le texte du bouton", new PlusIcon(14, Color.RED)) {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		this.add(button, BorderLayout.NORTH);
	}	
}
