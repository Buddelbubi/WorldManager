package de.buddelbubi.Events;

import java.util.Arrays;
import java.util.Random;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.level.Level;
import cn.nukkit.level.generator.Generator;
import de.buddelbubi.Commands.WorldManagerCommand;
import de.buddelbubi.api.World;
import de.buddelbubi.utils.Cache;

public class WorldManagerUI implements Listener{

	public static void openWorldTeleportUI(Player p) {
		
		FormWindowSimple fw = new FormWindowSimple("§3WorldManager §8- §cTeleportation UI", "§8Teleport to another level using an UI");
		for(Level l : Server.getInstance().getLevels().values()) {
		if(p.hasPermission("worldmanager.teleport") || p.hasPermission("worldmanager.teleport." + l.getName()) || p.hasPermission("worldmanager.admin")) {
			World w = Cache.getWorld(l);
			String thumbnail = "path::textures/ui/ErrorGlyph_small_hover.png";
			if(w.getThumbnail().startsWith("path::") || w.getThumbnail().startsWith("url::")) thumbnail = w.getThumbnail();
			fw.addButton(new ElementButton(l.getFolderName(), new ElementButtonImageData(thumbnail.split("::")[0], thumbnail.split("::")[1])));
			//fw.addButton(new ElementButton(l.getFolderName(), new ElementButtonImageData("path", (l.getDimension() == 0) ? "textures/blocks/grass_side_carried.png" : (l.getDimension() == 1) ? "textures/blocks/netherrack.png" : "textures/blocks/end_stone.png")));
		}
		}
		p.showFormWindow(fw);
	}
	
	
	public static void openWorldGenUI(Player p) {
		FormWindowCustom fw = new FormWindowCustom("§3WorldManager §8- §cGeneration UI");
		fw.addElement(new ElementInput("Name", "Type in a the worldname"));
		fw.addElement(new ElementInput("Seed", "Leave empty for random seed"));
		fw.addElement(new ElementDropdown("Generator", Arrays.asList(Generator.getGeneratorList()), 0));
		fw.addElement(new ElementToggle("Teleport after generation?", false));
		
		p.showFormWindow(fw);
	}
	
	@EventHandler
	public void onFormResponse(PlayerFormRespondedEvent e) {
		
		if(e.getWindow() instanceof FormWindowSimple && e.getResponse() != null) {
			
			FormWindowSimple fw = (FormWindowSimple) e.getWindow();
			if(fw.getTitle().equals("§3WorldManager §8- §cTeleportation UI")) {
				Level level = Server.getInstance().getLevelByName(fw.getResponse().getClickedButton().getText());
				e.getPlayer().teleport(level.getSafeSpawn());
				e.getPlayer().sendMessage(WorldManagerCommand.prefix + "§7You got teleported to §8" + level.getName());
			}
			
		} else if(e.getWindow() instanceof FormWindowCustom && e.getResponse() != null) {
			
			FormWindowCustom fw = (FormWindowCustom) e.getWindow();
			if(fw.getTitle().equals("§3WorldManager §8- §cGeneration UI")) {
				if(fw.getResponse().getInputResponse(0).equals("")) {
					e.getPlayer().sendMessage(WorldManagerCommand.prefix + "§cYou can't leave the name blank.");
					return;
				}
				if(!fw.getResponse().getInputResponse(1).equals("")) {
					try {
						Long.parseLong(fw.getResponse().getInputResponse(1));
					} catch (Exception e2) {
						e.getPlayer().sendMessage(WorldManagerCommand.prefix + "§cThe seed was not a number!");
						return;
					}
					
				}
				String name = fw.getResponse().getInputResponse(0);
				Class<? extends Generator> generator = Generator.getGenerator(fw.getResponse().getDropdownResponse(2).getElementContent().toUpperCase());
				long Seed = (fw.getResponse().getInputResponse(1).equals("")) ? new Random().nextLong() : Long.parseLong(fw.getResponse().getInputResponse(1));
				if(Server.getInstance().getLevelByName(name) != null) {
					e.getPlayer().sendMessage(WorldManagerCommand.prefix + "§cThis world already exist..");
					return;
				}
				
				Server.getInstance().generateLevel(name, Seed, generator);
				
				e.getPlayer().sendMessage(WorldManagerCommand.prefix + "§7The world §8" + name + "§7 got generated.");
				if(fw.getResponse().getToggleResponse(3)) e.getPlayer().teleport(Server.getInstance().getLevelByName(name).getSafeSpawn());
			}
			
		}
		
	}
	
}
