package wallmanager.beans.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Profile {
	private String id;
	private String name;
	
	private List<Screen> configuration;
	private List<ProfileVersion> versions;
	
	private boolean selected;
	
	public Profile() {
		this.id = UUID.randomUUID().toString();
		this.name = null;
		this.configuration = new ArrayList<>();
		this.versions = new ArrayList<>();
		this.selected = false;
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

	public List<Screen> getConfiguration() {
		return configuration;
	}
	
	public Screen getConfiguration(int screenId) {
		return configuration.get(screenId);
	}
	
	public void setConfiguration(List<Screen> configurations) {
		this.configuration = configurations;
	}
	
	public List<ProfileVersion> getVersions() {
		return versions;
	}

	public void setVersions(List<ProfileVersion> versions) {
		this.versions = versions;
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Profile)
			return this.id.equals(((Profile) obj).getId());
		
		return false;
	}
}
