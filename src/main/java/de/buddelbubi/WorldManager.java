package de.buddelbubi;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import de.buddelbubi.commands.AliasManager;
import de.buddelbubi.commands.CommandMapping;
import de.buddelbubi.listener.Events;
import de.buddelbubi.listener.WorldManagerUI;
import de.buddelbubi.utils.Cache;
import de.buddelbubi.utils.CustomMetricsManager;
import de.buddelbubi.utils.LoadWorlds;

import java.io.File;


public class WorldManager extends PluginBase {

	protected static Plugin plugin;

	public static final String prefix = "§3WorldManager §8§ §7";
	
	public void onEnable() {
		
		plugin = this;
		
		registerCommands();
		
		getServer().getPluginManager().registerEvents(new Events(), plugin);
		getServer().getPluginManager().registerEvents(new WorldManagerUI(), plugin);
		getServer().getPluginManager().registerEvents(new Cache(), plugin);
		
		LoadWorlds.loadWorlds();
		AliasManager.registerAliases();

		CustomMetricsManager.loadMetrics();
		
		//Disabling the Auto-Updater is not recommended unless your host disables file downloads or your host is blocked from cloudburstmc.org.
		
		File file = new File(Server.getInstance().getPluginPath(), "worldmanager.yml");
		if(file.exists()) {
			if(!new Config(file).getBoolean("autoupdate")) return;
		}

//		Updater.checkAndDoUpdateIfAvailable();

		get().getLogger().info("§bWorldManager v" + plugin.getDescription().getVersion() + " loaded successfully.");
		
	}
	
	private void registerCommands() {
		
		CommandMapping command = new CommandMapping();
		command.register();
		
	}
	
	public static Plugin get() {
		return plugin;
	}
	
}
