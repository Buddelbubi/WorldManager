package de.buddelbubi.api;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.utils.Config;
import de.buddelbubi.utils.Cache;

public class WorldManagerOption {

private static List<WorldManagerOption> custom = new ArrayList<>();
	
	public static List<WorldManagerOption> getCustomOptions(){
		return custom;
	}
	
	public static void insertCustom(WorldManagerOption option) {
		custom.add(option);
		for(World w : Cache.getWorldCache()) {
			Config c = w.getConfig();
			if(!c.exists(option.getKey())) {
			c.set(option.getKey(), option.getValue());
			c.save();
			}
		}
	}
	
	String key;
	String display;
	String description;
	Object value;
	
	public WorldManagerOption(String key, String display, String description, Object value) {
		super();
		this.key = key;
		this.display = display;
		this.description = description;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public String getDisplay() {
		return display;
	}

	public String getDescription() {
		return description;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
}
