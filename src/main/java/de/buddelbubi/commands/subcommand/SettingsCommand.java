package de.buddelbubi.commands.subcommand;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.level.Level;
import cn.nukkit.utils.Config;
import de.buddelbubi.WorldManager;
import de.buddelbubi.api.World;
import de.buddelbubi.api.WorldManagerOption;
import de.buddelbubi.utils.Cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SettingsCommand extends SubCommand{

	public SettingsCommand() {
		super("settings");
		this.setAliases(new String[] {
				"settings",
				"edit"
		});
	}

	@Override
	public CommandParameter[] getParameters() {
		
		LinkedList<CommandParameter> parameters = new LinkedList<>();
		parameters.add(CommandParameter.newEnum(this.getName(), this.getAliases()));
		parameters.add(CommandParameter.newType("world", true, CommandParamType.STRING));
		return parameters.toArray(new CommandParameter[parameters.size()]);
		
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		
		if (sender instanceof ConsoleCommandSender) {
			sender.sendMessage(WorldManager.prefix + "§cThis can only be done ingame.");
			 return false;
		  }
		  if (!sender.hasPermission("worldmanager.admin") && !sender.hasPermission("worldmanager.settings")) {

			  sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager.settings'.");
			 return false;

		  } else {

			 if (args.length == 1 || args.length == 2) {

				Level l = ((Player) sender).getLevel();
				if (args.length == 2) {
					if (Server.getInstance().getLevelByName(args[1]) != null) {
					   l = Server.getInstance().getLevelByName(args[1]);
					} else {
						sender.sendMessage(WorldManager.prefix + "§cThis world does not exist.");
					   return false;
				    }
				}
				World w = Cache.getWorld(l);
				List < String > worlds = new ArrayList < > ();
				for (Level level : Server.getInstance().getLevels().values()) worlds.add(level.getName());
				 FormWindowCustom fw = new FormWindowCustom("§3WorldSettings - " + l.getFolderName());
				fw.addElement(new ElementToggle("Load On Start", w.doesLoadOnStart()));
				fw.addElement(new ElementDropdown("Gamemode", Arrays.asList("Survival", "Creative", "Adventure", "Spectator", "None"), w.getOwnGamemode()));
				fw.addElement(new ElementToggle("Fly", w.isFlyAllowed()));
				fw.addElement(new ElementDropdown("Respawn World", worlds, (worlds.contains(w.getRespawnWorld()) ? worlds.indexOf(w.getRespawnWorld()) : worlds.indexOf(l.getName()))));
				fw.addElement(new ElementToggle("Protected", w.isProtected()));
				fw.addElement(new ElementInput("Notepad", "Sth. to remember like coords.", w.getNote()));
				Config c = w.getConfig();
				for (WorldManagerOption o : WorldManagerOption.getCustomOptions()) {
				    if (o.getValue() instanceof Boolean) {
					   fw.addElement(new ElementToggle(o.getDisplay(), c.getBoolean(o.getKey())));
				    } else if (o.getValue() instanceof String) {
					   fw.addElement(new ElementInput(o.getDisplay(), o.getDescription(), c.getString(o.getKey())));
				    } else if (o.getValue() instanceof Integer) {
					   fw.addElement(new ElementSlider(o.getDisplay(), 0, o.maxvalue, 1, c.getInt(o.getKey())));
				    }
				}

				((Player) sender).showFormWindow(fw);
			 } else sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager settings [World].");

		  }

		return false;
	}

}
