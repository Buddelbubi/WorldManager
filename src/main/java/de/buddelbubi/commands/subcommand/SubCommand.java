package de.buddelbubi.commands.subcommand;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;

public abstract class SubCommand extends Command {

	public SubCommand(String name) {
		super(name);
	}
	
	public abstract CommandParameter[] getParameters();
	
	@Override
	public abstract boolean execute(CommandSender sender, String arg1, String[] args);
	
}
