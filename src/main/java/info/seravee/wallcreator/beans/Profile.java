package info.seravee.wallcreator.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Profile {
	private String id;
	private String name;
	private List<Screen> screens;
	
	public Profile() {
		this.id = UUID.randomUUID().toString();
		this.name = null;
		this.screens = new ArrayList<>();
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Screen> getScreens() {
		return screens;
	}
	
	public void setScreens(List<Screen> screens) {
		this.screens = screens;
	}
}
