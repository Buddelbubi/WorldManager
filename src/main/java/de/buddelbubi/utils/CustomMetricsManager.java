package de.buddelbubi.utils;

import cn.nukkit.Server;
import de.buddelbubi.WorldManager;
import de.buddelbubi.utils.Metrics.DrilldownPie;
import de.buddelbubi.utils.Metrics.SimplePie;
import de.buddelbubi.utils.Metrics.SingleLineChart;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

public class CustomMetricsManager {
	
	
	// WorldManager got a custom property so it does not send its data in the bukkit tab of bStats.
	// It has now the "other" tag. It does not have any have default charts. So I have to add them here.
	
	
	
	public static void loadMetrics() {
		
		Server.getInstance().getLogger().info("bStats Metrics loading...");
		
		Metrics metrics = new Metrics(WorldManager.get(), 11320);
		
		SingleLineChart servers = new SingleLineChart("servers", new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				return 1;
			}
		});
		
		SingleLineChart players = new SingleLineChart("players", new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				return Server.getInstance().getOnlinePlayers().size();
			}
		});
		
		SimplePie pluginVersion = new SimplePie("pluginVersion", new Callable<String>() {

			@Override
			public String call() throws Exception {
				
				return WorldManager.get().getDescription().getVersion();
			}
		});
		
		SimplePie minecraftVersion = new SimplePie("minecraftVersion", new Callable<String>() {

			@Override
			public String call() throws Exception {
				return Server.getInstance().getVersion();
			}
		});
		
		SimplePie nukkitVersion = new SimplePie("nukkitVersion", new Callable<String>() {

			@Override
			public String call() throws Exception {
				return Server.getInstance().getNukkitVersion();
			}
		});
		SimplePie xboxAuth = new SimplePie("onlineMode", new Callable<String>() {

			@Override
			public String call() throws Exception {
				return String.valueOf(Server.getInstance().getPropertyBoolean("xbox-auth", false));
			}
		});
		
		SingleLineChart worlds = new SingleLineChart("worldCount", new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				return Server.getInstance().getLevels().size();
			}
		});
	
		SimplePie cores = new SimplePie("coreCount", new Callable<String>() {

			@Override
			public String call() throws Exception {
				return String.valueOf(Runtime.getRuntime().availableProcessors());
			}
		});
		
		SimplePie arch = new SimplePie("osArch", new Callable<String>() {

			@Override
			public String call() throws Exception {
				return String.valueOf(System.getProperty("os.arch"));
			}
		});
		
		DrilldownPie os = new DrilldownPie("os", new Callable<Map<String,Map<String,Integer>>>() {

			@Override
			public Map<String, Map<String, Integer>> call() throws Exception {
				
				Map<String, Map<String, Integer>> map = new HashMap<>();
				Map<String, Integer> map2 = new HashMap<>();
				map2.put(System.getProperty("os.version"), 1);
				map.put(System.getProperty("os.name"), map2);
				
				return map;
			}
		});
		
		
		SimplePie serverLocation = new SimplePie("location", new Callable<String>() {

			@Override
			public String call() throws Exception {
				
			
					return Locale.getDefault().getDisplayCountry(Locale.ENGLISH);
				
			}
		});
		
		
		SimplePie javaVersion = new SimplePie("javaVersion", new Callable<String>() {

			@Override
			public String call() throws Exception {
				return System.getProperty("java.version").split("_")[0];
			}
		});
		
		
		metrics.addCustomChart(servers);
		metrics.addCustomChart(players);
		metrics.addCustomChart(pluginVersion);
		metrics.addCustomChart(minecraftVersion);
		metrics.addCustomChart(nukkitVersion);
		metrics.addCustomChart(xboxAuth);
		metrics.addCustomChart(worlds);
		metrics.addCustomChart(cores);
		metrics.addCustomChart(arch);
		metrics.addCustomChart(os);
		metrics.addCustomChart(serverLocation);
		metrics.addCustomChart(javaVersion);
		
	}
	
	
	
	
	
	

}
