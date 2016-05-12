package wallmanager.ui.commons.component;

import java.awt.Font;

import javax.swing.JEditorPane;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLDocument;

public class ComponentFactory {
	private ComponentFactory() {
		/**/}

	public static JEditorPane createEditorPane() {
		return createEditorPane(UIManager.getFont("Label.font"));
	}
	
	public static JEditorPane createEditorPane(Font font) {
	    JEditorPane editor = new JEditorPane("text/html", "");

	    // add a CSS rule to force body tags to use the default label font
	    // instead of the value in javax.swing.text.html.default.csss
	    StringBuffer bodyRule = new StringBuffer("body {")
	      .append("font-family: ").append(font.getFamily()).append("; ")
	      .append("font-size: ").append(font.getSize()).append("pt; ");

	    if (font.isBold()) {
	      bodyRule.append("font-weight: bold; ");
	    }

	    if (font.isItalic()) {
	      bodyRule.append("font-style: italic; ");
	    }

	    bodyRule.append("}");
	    ((HTMLDocument) editor.getDocument()).getStyleSheet().addRule(bodyRule.toString());

	    return editor;
	  }
}
