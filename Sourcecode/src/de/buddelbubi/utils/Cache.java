package de.buddelbubi.utils;

import java.util.Collection;
import java.util.HashMap;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.level.LevelUnloadEvent;
import cn.nukkit.level.Level;
import de.buddelbubi.WorldManager;
import de.buddelbubi.api.World;

public class Cache implements Listener {
	
	public static HashMap<String, Byte> gamemodes = new HashMap<>();
	private static HashMap<String, World> worlds = new HashMap<>();
	
	public static World getWorld(Level level) {
		
		if(!worlds.containsKey(level.getName())) Cache.initWorld(level);
		
		if(worlds.containsKey(level.getName())) {
			return worlds.get(level.getName());
		} else return null;
	}
	
	public static Collection<World> getWorldCache() {
		return worlds.values();
	}
	
	public static int getCachedPlayerGamemodes() {
		return gamemodes.size();
	}
	
	public static void initWorld(Level level) {
		worlds.put(level.getName(), new World(level));
	}
	
	public static void initWorld(String level) {
		worlds.put(level, new World(Server.getInstance().getLevelByName(level)));
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void on(LevelUnloadEvent e) {
		if(worlds.containsKey(e.getLevel().getName())) worlds.remove(e.getLevel().getName());
		Server.getInstance().getScheduler().scheduleDelayedTask(new Runnable() {
			
			@Override
			public void run() {
				WorldManager.plugin.getLogger().info("�eIf you see any errors, please keep in mind that they are not related to WorldManager.");	
			}
		}, 2);
	}
	
}
