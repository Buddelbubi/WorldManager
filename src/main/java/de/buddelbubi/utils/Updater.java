package de.buddelbubi.utils;

import de.buddelbubi.WorldManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class Updater {

	public static void checkAndDoUpdateIfAvailable() {
		
		new Thread(() -> {
			
			if(updateAvailable()) {
				installLastestVersion();
			}
			
		}).start();
		
	}

	@SuppressWarnings("resource")
	public static boolean updateAvailable() {
		
		try {
			String ver = getNewestVersion();
			if(!(ver.contains(WorldManager.get().getDescription().getVersion()))) {
				WorldManager.get().getLogger().info("§eA new version of WorldManager is available. (" + ver + ") Try to Auto-Update!");
				return true; 	
			}  else return false;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	
	public static void installLastestVersion() {
		try {

			URL url = new URL("https://cloudburstmc.org/resources/worldmanager-advanced-multiworld-plugin.560/download");
			File file = new File(WorldManager.get().getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
			InputStream in = url.openStream();
			Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			WorldManager.get().getLogger().info("§aUpdated WorldManager successfully. Please reload or reboot your Server.");
			in.close();
			
		} catch (IOException e) {
			WorldManager.get().getLogger().error("§cUpdate failed... Please update manually. (" + e.getMessage() + ")");
		
		}
	}
	
	public static String getNewestVersion() {
		
		String result = WorldManager.get().getDescription().getVersion(); //in case something goes wrong.
		
		try {
			URL githuburl = new URL("https://raw.githubusercontent.com/Buddelbubi/WorldManager/main/version");
			InputStream inputStream = githuburl.openStream();
			Scanner scanner = new Scanner(inputStream);
			result = scanner.next();
			scanner.close();
			inputStream.close();
			
		} catch (Exception e) {
			WorldManager.get().getLogger().error("§cCould not check if a WorldManager Update is available!");
			e.printStackTrace();
		}
		return result;
		
		
	}
	
}
