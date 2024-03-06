package de.buddelbubi.utils;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.utils.Config;
import de.buddelbubi.WorldManager;
import de.buddelbubi.api.WorldManagerOption;

import java.io.File;
import java.io.IOException;

public class LoadWorlds {

	public static boolean loaded = false;
	public static void loadWorlds() {
		
		File folder = new File(Server.getInstance().getDataPath() + "worlds/");
		File[] folders = folder.listFiles();
		for(File f : folders) {
			if(!f.isDirectory()) continue;
			
			if(!new File(Server.getInstance().getDataPath() + "worlds/" + f.getName(), "level.dat").exists()) {
				WorldManager.get().getLogger().alert("Unknown folder: " + f.getName() + ", Missing level.dat.");
				continue;
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
			if(c.exists("UseOwnGamemode")) if(!c.getBoolean("UseOwnGamemode")) c.set("Gamemode", 4);
			if(!c.exists("Gamemode")) 	c.set("Gamemode", 4);
			if(!c.exists("fly")) 	c.set("fly", false);
			if(!c.exists("respawnworld")) 	c.set("respawnworld", worldname);
			if(!c.exists("protected"))		c.set("protected", false);
			if(!c.exists("note")) 	c.set("note", "");
			for(WorldManagerOption option : WorldManagerOption.getCustomOptions()) {
				if(!c.exists(option.getKey())) {
					c.set(option.getKey(), option.getValue());
				}
			}
			
			c.save();
			
			if(c.getBoolean("LoadOnStart")) {
				
				try {
				Level level = Server.getInstance().getLevelByName(f.getName());
					if(level == null) Server.getInstance().loadLevel(f.getName());
				
					if(!c.exists("thumbnail") ) {
						c.set("thumbnail", "path::" + ((level.getDimension() == 0) ? "textures/blocks/grass_side_carried.png" : (level.getDimension() == 1) ? "textures/blocks/netherrack.png" : "textures/blocks/end_stone.png"));
						c.save();
					}
				} catch (Exception e) {
					c.set("thumbnail", "path::textures/blocks/grass_side_carried.png");
				}
				
			} else continue;
			
			try {
				Cache.initWorld(worldname);
			} catch (Exception e) {
				WorldManager.get().getLogger().critical("Could not initialize " + worldname + ". Please message the developer.");
				e.printStackTrace();
			}

		}
		loaded = true;
	}
	
}
