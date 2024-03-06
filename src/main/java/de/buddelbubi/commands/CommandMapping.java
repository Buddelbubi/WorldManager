package de.buddelbubi.commands;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import de.buddelbubi.WorldManager;
import de.buddelbubi.commands.subcommand.*;

import java.util.ArrayList;
import java.util.Arrays;

public class CommandMapping extends Command {

	private ArrayList<SubCommand> subcommands = new ArrayList<>();
	
	public CommandMapping() {
		super("worldmanager");
		this.setAliases(new String[] {"wm", "mw", "mv", "levelmanager", "lm"});
		this.setDescription("The main WorldManager Command"); 
		this.commandParameters.clear();
	}
	
	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {
		
		if(args.length > 0) {
			
			String name = args[0].toLowerCase();
			
			for(SubCommand command : subcommands) {
				
				if(Arrays.asList(command.getAliases()).contains(name)) {
					command.execute(sender, arg1, args);
					return true;
				}
				
			}
			
		}
		sender.sendMessage(WorldManager.prefix + "§cUnknown Command. Use '/worldmanager help' to get a list of commands.");
		
		return true;
	}

	public void registerSubCommand(SubCommand subcommand) {
		subcommands.add(subcommand);
		addCommandParameters(subcommand.getName(), subcommand.getParameters());
	}
	
	public void register() {
		
		SubCommand[] subcommands = new SubCommand[] {
				new TeleportCommand(),
				new GenerateCommand(),
				new DeleteCommand(),
				new LoadCommand(),
				new UnloadCommand(),
				new ReloadCommand(),
				new ListCommand(),
				new SetspawnCommand(),
				new SettingsCommand(),
				new InfoCommand(),
				new SetseedCommand(),
				new RenameCommand(),
				new CopyCommand(),
				new RegenerateCommand(),
				new ClearlagCommand(),
				new GameruleCommand(),
				new SetdefaultCommand(),
				new SaveCommand(),
				new VersionCommand(),
				new SyncCommand(),
				new SpawnCommand(),
				new StatusCommand(),
				new HelpCommand()
		};
		for(SubCommand subcommand : subcommands) registerSubCommand(subcommand);
		
		Server.getInstance().getCommandMap().register(this.getName(), this);
	}
	
}
