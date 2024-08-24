package de.buddelbubi.commands.subcommand;

import java.util.LinkedList;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import de.buddelbubi.WorldManager;

public class UnloadCommand extends SubCommand{

	public UnloadCommand() {
		super("unload");
		this.setAliases(new String[] {
				"unload"
		});
	}

	@Override
	public CommandParameter[] getParameters() {
		
		LinkedList<CommandParameter> parameters = new LinkedList<>();
		parameters.add(CommandParameter.newEnum(this.getName(), this.getAliases()));
		parameters.add(CommandParameter.newType("world", false, CommandParamType.STRING));
		return parameters.toArray(new CommandParameter[parameters.size()]);
		
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		
		  if (!sender.hasPermission("worldmanager.admin") && !sender.hasPermission("worldmanager.unload")) {

			 sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager.unload'.");
			 return false;

		  } else {

			 if (args.length == 2) {

				String name = args[1];
				if(name.equals("-c") && sender instanceof Player) name = ((Player) sender).getLevel().getName();
				if (Server.getInstance().getLevelByName(name) != null) {
				    if (Server.getInstance().getDefaultLevel().equals(Server.getInstance().getLevelByName(name))) {
					   sender.sendMessage(WorldManager.prefix  + "§cYou can not unload the default world.");
					   return false;
				    }

				    Server.getInstance().unloadLevel(Server.getInstance().getLevelByName(name));
				    sender.sendMessage(WorldManager.prefix  + "§7The world §8" + name + "§7 got unloaded.");

				} else sender.sendMessage(WorldManager.prefix  + "§cThis world is not loaded.");

			 } else sender.sendMessage(WorldManager.prefix  + "§cDo /worldmanager unload [World].");
		  }
		return false;
	}

}
