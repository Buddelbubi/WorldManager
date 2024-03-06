package de.buddelbubi.commands.subcommand;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import de.buddelbubi.WorldManager;

import java.util.LinkedList;

public class SpawnCommand extends SubCommand{

	public SpawnCommand() {
		super("spawn");
		this.setAliases(new String[] {
				"spawn"
		});
	}

	@Override
	public CommandParameter[] getParameters() {
		
		LinkedList<CommandParameter> parameters = new LinkedList<>();
		parameters.add(CommandParameter.newEnum(this.getName(), this.getAliases()));
		return parameters.toArray(new CommandParameter[parameters.size()]);
		
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		
		 if (!sender.hasPermission("worldmanager.admin") && !sender.hasPermission("worldmanager.spawn")) {

			 sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'.");
			 return false;

		  } else {

			 if (sender instanceof Player) {

				((Player) sender).teleport(((Player) sender).getLevel().getSafeSpawn());
				 sender.sendMessage(WorldManager.prefix + "§7Successfully teleported to spawn.");

			 } else sender.sendMessage(WorldManager.prefix + "§cThis command can only be executed ingame.");

		  }
		
		return false;
	}

}
