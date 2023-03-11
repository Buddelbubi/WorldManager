package de.buddelbubi.commands.subcommand;

import java.util.Collections;
import java.util.LinkedList;

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
		  if (args.length >= 2 && args.length <= 3) {

			 if (Server.getInstance().getLevelByName(args[1]) != null) {
				if (sender instanceof ConsoleCommandSender && args.length != 3) {
				    sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager teleport [Level] (Player).");
				    return false;
				}
				Level level = Server.getInstance().getLevelByName(args[1]);
				Player player = null;
				if (args.length == 2) {
				    player = ((Player) sender);
				} else if (args.length == 3) {
				    try {
					   if (Server.getInstance().getPlayer(args[2]).isOnline()) {
						  player = Server.getInstance().getPlayer(args[2]);
					   } else player = ((Player) sender);
				    } catch (Exception e) {
					   sender.sendMessage(WorldManager.prefix + "§cThe player called §e" + args[2] + "§c is not online.");
					   return false;
				    }
				}
				if (sender.hasPermission("worldmanager.teleport." + level.getFolderName()) || sender.hasPermission("worldmanager.teleport") || sender.hasPermission("worldmanager.admin")) {
				    player.teleport(level.getSpawnLocation());
				    level.addSound(level.getSpawnLocation(), Sound.MOB_SHULKER_TELEPORT, 1, 1, Collections.singletonList(player));
				} else {
				    sender.sendMessage(WorldManager.prefix + "§cYou dont have the permission to do this..");
				    return false;
				}
				if (!(sender instanceof ConsoleCommandSender)) {
				    if (player.equals(sender)) {
					   player.sendMessage(WorldManager.prefix + "§7You got teleported to §8" + level.getFolderName() + ".");
				    }
				} else sender.sendMessage(WorldManager.prefix + "§7Teleported §8" + player.getName() + " §7 to §8" + level.getFolderName() + ".");
			 } else sender.sendMessage(WorldManager.prefix + "§cThe world called §e" + args[1] + "§c does not exist.");

		  } else {

			 if ((sender.hasPermission("worldmanager.teleportui") || sender.hasPermission("worldmanager.admin")) && args.length == 1 && !(sender instanceof ConsoleCommandSender)) {
				WorldManagerUI.openWorldTeleportUI((Player) sender);
				return false;
			 } else sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager.teleportui'.");

			 if (sender instanceof ConsoleCommandSender && args.length != 3) {
				sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager teleport [Level] (Player).");
				return false;
			 }
		  }
		return false;
	}

}
