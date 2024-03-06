package de.buddelbubi.commands.subcommand;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.command.data.CommandParameter;
import de.buddelbubi.WorldManager;

import java.util.LinkedList;

public class SetspawnCommand extends SubCommand{

	public SetspawnCommand() {
		super("setspawn");
		this.setAliases(new String[] {
				"setspawn",
				"setworldspawn"
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
		
		  if (sender instanceof ConsoleCommandSender) {
			  sender.sendMessage(WorldManager.prefix + "§cYou can not do this in the console.");
			 return false;
		  }

		  if (!sender.hasPermission("worldmanager.admin") && !sender.hasPermission("worldmanager.setspawn")) {

			  sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager.setspawn'.");
			 return false;

		  } else {

			 if (args.length == 1) {
				Player p = ((Player) sender);
				p.getLevel().setSpawnLocation(p.getLocation());
				 sender.sendMessage(WorldManager.prefix + "§7You set the spawnlocation of §8" + p.getLevel().getName() + "§7 to §8" + p.getLevelBlock().x + ", " + p.getLevelBlock().y + ", " + p.getLevelBlock().z + ".");

			 } else sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager setspawn.");


		  }
		return false;
	}

}
