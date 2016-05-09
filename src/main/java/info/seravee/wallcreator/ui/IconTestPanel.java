package info.seravee.wallcreator.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;

import info.seravee.wallcreator.ui.components.button.XButton;
import info.seravee.wallmanager.ui.commons.components.buttons.Button;
import info.seravee.wallmanager.ui.commons.icons.PlusIcon;

public class IconTestPanel extends JPanel {

	public IconTestPanel() {
		this.setLayout(new BorderLayout());
		setOpaque(false);
		
		XButton button = new XButton(new AbstractAction("Le texte du bouton", new PlusIcon(14, Color.RED)) {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBackground(Color.YELLOW);
		panel.setOpaque(false);
		
		final Action a = new AbstractAction("Bouton actif") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setEnabled(false);
			}
		};
		
		Button mButton = new Button(a, 1);
		
		Button colButton = new Button(new AbstractAction("Bouton couleur") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				a.setEnabled(true);
				
			}
		}, 2);
		colButton.setBackground(Color.RED);
		
		Button disButton = new Button(new AbstractAction("Bouton inactif") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		}, 1);
		disButton.setEnabled(false);
		disButton.setBackground(Color.RED);
		

		panel.add(mButton);
		panel.add(colButton);
		panel.add(disButton);
		
		this.add(button, BorderLayout.NORTH);
		this.add(panel, BorderLayout.SOUTH);
	}	
}
