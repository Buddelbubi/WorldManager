package de.buddelbubi.commands.subcommand;

import java.util.LinkedList;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import de.buddelbubi.WorldManager;

public class VersionCommand extends SubCommand{

	public VersionCommand() {
		super("version");
		this.setAliases(new String[] {
				"version"
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
		
		sender.sendMessage(WorldManager.prefix + "You are using WorldManager v" + WorldManager.get().getDescription().getVersion());
		
		return false;
	}

}
