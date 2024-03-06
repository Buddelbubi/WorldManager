package de.buddelbubi.api;

import cn.nukkit.utils.Config;
import de.buddelbubi.WorldManager;
import de.buddelbubi.utils.Cache;

import java.util.ArrayList;
import java.util.List;

public class WorldManagerOption {

private static List<WorldManagerOption> custom = new ArrayList<>();
	
	public static List<WorldManagerOption> getCustomOptions(){
		return custom;
	}
	
	public static void insertCustom(WorldManagerOption option) {

		for(WorldManagerOption o : custom) if(o.key.equals(option.key))	{
			WorldManager.get().getLogger().info(option.display + " failed to inject into WorldManager. (Duplicate)");
			return;
		}
		
		custom.add(option);
		for(World w : Cache.getWorldCache()) {
			Config c = w.getConfig();
			if(!c.exists(option.getKey())) {
			c.set(option.getKey(), option.getValue());
			c.save();
			}
		}
		WorldManager.get().getLogger().info(option.display + " injected into WorldManager.");
	}
	
	String key;
	String display;
	String description;
	Object value;
	public int maxvalue;
	
	public WorldManagerOption(String key, String display, String description, Object value) {
		super();
		this.key = key;
		this.display = display;
		this.description = description;
		this.value = value;
		this.maxvalue = 0;
	}
	
	public WorldManagerOption(String key, String display, String description, Object value, int maxvalue) {
		super();
		this.key = key;
		this.display = display;
		this.description = description;
		this.value = value;
		this.maxvalue = maxvalue;
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
