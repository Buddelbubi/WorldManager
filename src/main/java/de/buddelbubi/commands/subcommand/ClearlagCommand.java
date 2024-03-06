package de.buddelbubi.commands.subcommand;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import de.buddelbubi.WorldManager;

import java.util.LinkedList;

public class ClearlagCommand extends SubCommand {

	public ClearlagCommand() {
		super("clearlag");
		this.setAliases(new String[] {
				"clearlag",
				"killentitys"
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
		
		  if (!sender.hasPermission("worldmanager.admin") && !sender.hasPermission("worldmanager.killentitys") && !sender.hasPermission("worldmanager.clearlag")) {

              sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager.clearlag'.");
			 return false;

		  } else {
			 Level l = null;
			 if (args.length == 1) {
				if (sender instanceof Player) {
				    l = ((Player) sender).getLevel();
                } else sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager clearlag [World]*.");
			 } else
			 if (args.length == 2) {
				if (Server.getInstance().getLevelByName(args[1]) != null) {

				    l = Server.getInstance().getLevelByName(args[1]);

				} else {
                    sender.sendMessage(WorldManager.prefix + "§cThis world does not exist.");
				    return false;
				}

             } else sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager clearlag [World]*.");
			 int count = 0;
			 for (Entity entity : l.getEntities()) {
				 
				if (!(entity instanceof Player) && entity.getNetworkId() != 61) { //Entity ID 61 = Armor Stands on PowerNukkit
				    entity.close();
				    count++;
				}
				
			 }
              sender.sendMessage(WorldManager.prefix + "§7Killed " + count + " entitys in §8" + l.getName() + ".");
		  }
		return false;
	}

}
