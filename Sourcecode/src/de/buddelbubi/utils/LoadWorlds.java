package de.buddelbubi.utils;

import java.io.File;

import java.io.IOException;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.utils.Config;
import de.buddelbubi.WorldManager;

public class LoadWorlds {

	public static boolean loaded = false;
	public static void loadWorlds() {
		
		File folder = new File(Server.getInstance().getDataPath() + "worlds/");
		File[] folders = folder.listFiles();
		for(File f : folders) {
			if(!f.isDirectory()) return;
			
			if(!new File(Server.getInstance().getDataPath() + "worlds/" + f.getName(), "level.dat").exists()) {
				WorldManager.plugin.getLogger().alert("Unknown folder: " + f.getName() + ", Missing level.dat.");
				return;
			}
			
			File configfile = new File(Server.getInstance().getDataPath() + "worlds/" + f.getName(), "config.yml");
			
			if(!configfile.exists()) {
				try {
					configfile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			Config c = new Config(configfile);
			
			String worldname = f.getName().split("/")[f.getName().split("/").length-1];
			
			if(!c.exists("version")) 	c.set("version", 0);
			if(!c.exists("LoadOnStart")) 	c.set("LoadOnStart", true);
			if(!c.exists("UseOwnGamemode"))		c.set("UseOwnGamemode", false);
			if(!c.exists("Gamemode")) 	c.set("Gamemode", Server.getInstance().getDefaultGamemode());
			if(!c.exists("fly")) 	c.set("fly", false);
			if(!c.exists("respawnworld")) 	c.set("respawnworld", worldname);
			if(!c.exists("protected"))		c.set("protected", false);
			if(!c.exists("note")) 	c.set("note", "");
			
			c.save();
			
			if(c.getBoolean("LoadOnStart")) {
				
				Level level = Server.getInstance().getLevelByName(f.getName());
				if(level == null) Server.getInstance().loadLevel(f.getName());
				
			} else return;
			
			try {
				Cache.initWorld(worldname);
			} catch (Exception e) {
				WorldManager.plugin.getLogger().critical("Could not initialize " + worldname + ". Please message the developer.");
				e.printStackTrace();
			}
				
		
			if(c.exists("version")) {
				if(c.getBoolean("LoadOnStart")) Server.getInstance().loadLevel(worldname);
			}
		
			loaded = true;
		}
		
	}
	
}
