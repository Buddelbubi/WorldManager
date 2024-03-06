package de.buddelbubi.commands.subcommand;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.level.Level;
import de.buddelbubi.WorldManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SyncCommand extends SubCommand{

	public SyncCommand() {
		super("sync");
		this.setAliases(new String[] {
				"sync"
		});
	}

	@Override
	public CommandParameter[] getParameters() {
		
		LinkedList<CommandParameter> parameters = new LinkedList<>();
		parameters.add(CommandParameter.newEnum(this.getName(), this.getAliases()));
		parameters.add(CommandParameter.newType("templateworld", true, CommandParamType.STRING));
		return parameters.toArray(new CommandParameter[parameters.size()]);
		
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		
		if (sender.hasPermission("worldmanager.admin") || sender.hasPermission("worldmanager.sync")) {

			 if (sender instanceof Player) {

				if (args.length == 1 || args.length == 2) {

				    String levelname = null;

				    if (args.length == 2) {
					   levelname = args[1];
				    } else levelname = ((Player) sender).getLevel().getName();

				    if (Server.getInstance().getLevelByName(levelname) != null) {

						FormWindowCustom fw = new FormWindowCustom("§3WorldSync - " + levelname);
						fw.addElement(new ElementLabel("§7Select the worlds you want to sync with §e" + levelname + "§7. This includes WorldSettings and Gamerules."));
					   List <Level> level = new ArrayList <> (Server.getInstance().getLevels().values());
					   level.remove(Server.getInstance().getLevelByName(levelname));
					   
					   for (Level l : level) fw.addElement(new ElementToggle(l.getFolderName(), false));
					   
					   ((Player) sender).showFormWindow(fw);

				    } else {
						sender.sendMessage(WorldManager.prefix + "§cThis world does not exist.");
					   return false;
				    }

				} else sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager sync [templateWorld]*.");

			 } else sender.sendMessage(WorldManager.prefix + "§cThis command can only be executed ingame.");

		} else
			sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'.");
		
		  return false;
	}

}
