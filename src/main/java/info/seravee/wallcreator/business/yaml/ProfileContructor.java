package info.seravee.wallcreator.business.yaml;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

import info.seravee.wallcreator.beans.Profile;
import info.seravee.wallcreator.beans.Screen;

public class ProfileContructor extends Constructor {
	public ProfileContructor() {
		super(Profile.class);

		TypeDescription profileDescription = new TypeDescription(Profile.class);
		profileDescription.putListPropertyType("screens", Screen.class);
		addTypeDescription(profileDescription);
		
		this.yamlConstructors.put(new Tag("!color"), new ConstructColor());
	}

	public class ConstructColor extends AbstractConstruct {
		@Override
		public Object construct(Node node) {
			
			final Pattern p = Pattern.compile("rgb\\((\\s*(?:(\\d{1,3})\\s*,?){3})\\)");
			
			String val = (String) constructScalar((ScalarNode) node);
			
			Matcher m = p.matcher(val);
			if (m.matches()) {
				
				final String[] decValues = m.group(1).split(",");
				
				return new Color(
						getSafeColor(decValues[0]),
						getSafeColor(decValues[1]),
						getSafeColor(decValues[2])
				);
			}
			
			return Color.WHITE;
		}
		
		private int getSafeColor(String strColor) {
			int color = Integer.parseInt(strColor);
			if (color > 255)	color = 255;
			if (color < 0)		color = 0;
			return color;
		}
	}
}
