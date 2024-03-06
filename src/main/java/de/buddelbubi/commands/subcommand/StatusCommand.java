package de.buddelbubi.commands.subcommand;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import de.buddelbubi.WorldManager;
import de.buddelbubi.api.World;
import de.buddelbubi.utils.Cache;
import de.buddelbubi.utils.Updater;

import java.util.LinkedList;

public class StatusCommand extends SubCommand {

	public StatusCommand() {
		super("status");
		this.setAliases(new String[] {
				"status"
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
		
		  if (!sender.hasPermission("worldmanager.admin") && !sender.hasPermission("worldmanager.status")) {

			  sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'.");
				 return false;

			  } else {

			  String message = "§l§3WorldManager §eStatus\n§r";
			  message += ("§ePlugin Version: §7" + WorldManager.get().getDescription().getVersion() + "\n");
			  message += ("§eNewest Version: §7" + Updater.getNewestVersion() + "\n");
			  message += ("§eCached Worlds: §7" + Cache.getWorldCache().size() + "\n");
			  message += ("§eCached Players: §7" + Cache.getCachedPlayerGamemodes() + "\n");
			  message += "§eWorlds: §7";
				 for (World w : Cache.getWorldCache()) message += w.getAsLevel().getName() + ", ";
				 sender.sendMessage(message);

			  }
		
		return false;
	}

}
