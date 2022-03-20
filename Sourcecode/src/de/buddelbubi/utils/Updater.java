package de.buddelbubi.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import de.buddelbubi.WorldManager;

public class Updater {

	public static void checkAndDoUpdateIfAvailable() {
		
		if(updateAvailable()) {
			installLastestVersion();
		}
		
	}

	@SuppressWarnings("resource")
	public static boolean updateAvailable() {
		
		try {
			URL githuburl = new URL("https://raw.githubusercontent.com/Buddelbubi/WorldManager/main/version");
			if(!(new Scanner(githuburl.openStream()).next().contains(WorldManager.plugin.getDescription().getVersion()))) {
				WorldManager.plugin.getLogger().info("§eA new version of WorldManager is available. Try to Auto-Update!");
				return true; 	
			}  else return false;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	
	public static void installLastestVersion() {
		try {

			URL url = new URL("https://cloudburstmc.org/resources/worldmanager-advanced-multiworld-plugin.560/download");
			File file = new File(WorldManager.plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
			InputStream in = url.openStream();
			Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			WorldManager.plugin.getLogger().info("§aUpdated WorldManager successfully. Please reload or reboot your Server.");
			
		} catch (IOException e) {
			WorldManager.plugin.getLogger().error("§cUpdate failed... Please update manually. (" + e.getMessage() + ")");
		
		}
	}
	
}
