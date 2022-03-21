package de.buddelbubi;

import java.io.File;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import de.buddelbubi.Commands.AliasManager;
import de.buddelbubi.Commands.WorldManagerCommand;
import de.buddelbubi.Events.Addons;
import de.buddelbubi.Events.Events;
import de.buddelbubi.Events.WorldManagerUI;
import de.buddelbubi.utils.Cache;
import de.buddelbubi.utils.CustomMetricsManager;
import de.buddelbubi.utils.LoadWorlds;
import de.buddelbubi.utils.Updater;


public class WorldManager extends PluginBase {

	public static Plugin plugin;
	
	public void onEnable() {
		
		plugin = this;
		
		Command command = new WorldManagerCommand("WorldManager");
		command.setAliases(new String[] {"wm", "mw", "mv", "levelmanager", "lm"});
		command.setDescription("The main WorldManager Command");
		getServer().getCommandMap().register(command.getName(), command);
		getServer().getPluginManager().registerEvents(new Events(), plugin);
		getServer().getPluginManager().registerEvents(new WorldManagerUI(), plugin);
		getServer().getPluginManager().registerEvents(new Addons(), plugin);
		getServer().getPluginManager().registerEvents(new Cache(), plugin);
		LoadWorlds.loadWorlds();
		AliasManager.registerAliases();
		Addons.initJson();
		
		CustomMetricsManager.loadMetrics();
		
		//Disabling the Auto-Updater is not recommended unless your host disables file downloads.
		
		File file = new File(Server.getInstance().getPluginPath(), "worldmanager.yml");
		if(file.exists()) {
			if(!new Config(file).getBoolean("autoupdate")) return;
		}
		
		Updater.checkAndDoUpdateIfAvailable();
		
		Server.getInstance().getLogger().info("§bWorldManager v" + plugin.getDescription().getVersion() + " loaded successfully");
		
	}
	
}
