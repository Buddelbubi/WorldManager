package de.buddelbubi.commands.subcommand;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import de.buddelbubi.WorldManager;
import de.buddelbubi.listener.WorldManagerUI;

import java.util.LinkedList;

public class TeleportCommand extends SubCommand {

	public TeleportCommand() {
		super("teleport");
		this.setAliases(new String[] {
				"teleport",
				"tp",
				"to",
				"move"
		});
	}

	@Override
	public CommandParameter[] getParameters() {
		
		LinkedList<CommandParameter> parameters = new LinkedList<>();
		parameters.add(CommandParameter.newEnum(this.getName(), this.getAliases()));
		parameters.add(CommandParameter.newType("world", true, CommandParamType.STRING));
		parameters.add(CommandParameter.newType("player", true, CommandParamType.TARGET));
		return parameters.toArray(new CommandParameter[parameters.size()]);
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		if(sender instanceof Player) {
			if (args.length >= 1) {

				 if((args.length == 3 && args[1].equals("-s")) || args.length == 1) {
					 
					 if ((sender.hasPermission("worldmanager.teleportui") || sender.hasPermission("worldmanager.admin"))) {
							WorldManagerUI.openWorldTeleportUI((Player) sender, args.length == 3 ? args[2] : null);
							return true;
						 } else {
						 sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager.teleportui'.");
							 return true;
						 }
					 
				 }
				
				  String world = args[1];
				  Player player = (Player) sender;
				  if (Server.getInstance().getLevelByName(world) != null) {
					if (sender instanceof ConsoleCommandSender) {
						sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager teleport [Level]");
					    return false;
					}
					Level level = Server.getInstance().getLevelByName(world);
					
					if (sender.hasPermission("worldmanager.teleport." + level.getFolderName()) || sender.hasPermission("worldmanager.teleport") || sender.hasPermission("worldmanager.admin")) {
					    player.teleport(level.getSpawnLocation());
					    level.addSound(level.getSpawnLocation(), Sound.MOB_SHULKER_TELEPORT, 1, 1, player);
					} else {
						sender.sendMessage(WorldManager.prefix + "§cYou dont have the permission to do this..");
					    return false;
					}
				
					    if (player.equals(sender)) {
							player.sendMessage(WorldManager.prefix + "§7You got teleported to §8" + level.getFolderName() + ".");
					    }

				  } else
					  sender.sendMessage(WorldManager.prefix + "§cThe world called §e" + world + "§c does not exist.");

			  }
		} else sender.sendMessage(WorldManager.prefix + "§cThis command can only be executed ingame.");
		return false;
	}

}
