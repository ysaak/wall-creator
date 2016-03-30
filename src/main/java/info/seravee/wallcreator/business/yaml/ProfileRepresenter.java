package info.seravee.wallcreator.business.yaml;

import java.awt.Color;

import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

import info.seravee.wallcreator.beans.Profile;
import info.seravee.wallcreator.beans.Screen;

public class ProfileRepresenter extends Representer {
	public ProfileRepresenter() {
		
		addClassTag(Profile.class, new Tag("!Profile"));
		addClassTag(Screen.class, Tag.MAP);
		
		this.representers.put(Color.class, new RepresentColor());
	}
	

	public class RepresentColor implements Represent {
		@Override
		public Node representData(Object data) {
			Color color = (Color) data;
			/*
			String rgb = Integer.toHexString(color.getRGB());
			rgb = "#" + rgb.substring(2, rgb.length());
			return representScalar(new Tag("!color"), rgb);
			*/
			StringBuilder sb = new StringBuilder("rgb(");
			sb.append(color.getRed()).append(",");
			sb.append(color.getGreen()).append(",");
			sb.append(color.getBlue()).append(")");
			
			return representScalar(new Tag("!color"), sb.toString());
		}
	}
}
