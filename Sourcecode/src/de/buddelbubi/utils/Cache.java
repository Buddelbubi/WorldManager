package de.buddelbubi.utils;

import java.util.HashMap;


import cn.nukkit.Server;
import cn.nukkit.level.Level;
import de.buddelbubi.api.World;

public class Cache {
	
	public static HashMap<String, Byte> gamemodes = new HashMap<>();
	public static HashMap<String, World> worlds = new HashMap<>();
	
	public static World getWorld(Level level) {
		
		if(!worlds.containsKey(level.getName())) Cache.initWorld(level);
		
		if(worlds.containsKey(level.getName())) {
			return worlds.get(level.getName());
		} else return null;
	}
	
	public static void initWorld(Level level) {
		worlds.put(level.getName(), new World(level));
	}
	
	public static void initWorld(String level) {
		worlds.put(level, new World(Server.getInstance().getLevelByName(level)));
	}
	
}
