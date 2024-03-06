package de.buddelbubi.commands.subcommand;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.Config;
import de.buddelbubi.WorldManager;

import java.util.LinkedList;

public class SetdefaultCommand extends SubCommand{

	public SetdefaultCommand() {
		super("setdefault");
		this.setAliases(new String[] {
				"setdefault",
				"default"
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
		
		  if (!sender.hasPermission("worldmanager.admin") && !sender.hasPermission("worldmanager.default") && !sender.hasPermission("worldmanager.setdefault")) {
              sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'");
				 return false;
		  }

			  if (args.length == 1) {
                  sender.sendMessage(WorldManager.prefix + "§7The default world is §8" + Server.getInstance().getDefaultLevel().getName() + ".");
			  } else
			  if (args.length == 2) {
				 if (Server.getInstance().getLevelByName(args[1]) != null) {
					if (Server.getInstance().getDefaultLevel() == Server.getInstance().getLevelByName(args[1])) {
                        sender.sendMessage(WorldManager.prefix + "§cThis world already is the default world!");
					    return false;
					}
					Server.getInstance().setDefaultLevel(Server.getInstance().getLevelByName(args[1]));

					Config serverconfig = Server.getInstance().getProperties();
					serverconfig.set("level-name", args[1]);
					serverconfig.save();

                     sender.sendMessage(WorldManager.prefix + "§7The world §8" + args[1] + " §7is now the default world.");

				 } else {
                     sender.sendMessage(WorldManager.prefix + "§cThis world does not exist.");
					return false;
				 }
				 
			  } else {
                  sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager default [World]*.");
				 return false;
			  }
		return false;
	}

}
