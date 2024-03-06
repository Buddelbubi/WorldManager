package de.buddelbubi.commands;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;

public class AliasManager extends Command {

	public AliasManager(String name) {
		super(name);
	}

	@Override
	public boolean execute(CommandSender arg0, String arg1, String[] arg2) {
		
		String args = "";
		for(String s : arg2) args = args + " " + s;
		
		if(arg1.toLowerCase().matches("world|mvtp|unitp|wmtp|lmtp")) {
			Server.getInstance().executeCommand(arg0, "wm tp" + args);
		} else if(arg1.toLowerCase().matches("mvimport|mvload|uniload|mvimport")) {
			Server.getInstance().executeCommand(arg0, "wm load" + args);
		} else if(arg1.equalsIgnoreCase("mvedit")){
			Server.getInstance().executeCommand(arg0, "wm gamerule" + args);
		} else if(arg1.equalsIgnoreCase("worlds")) {
			Server.getInstance().executeCommand(arg0, "wm list");
		}
		return true;
	}

	public static void registerAliases() {
		
		Command world = new AliasManager("world");
		world.setDescription("Teleport to a different world.");
		world.addCommandParameters("world", new CommandParameter[] {CommandParameter.newType("world", true, CommandParamType.STRING)});
		Command worlds = new AliasManager("worlds");
		worlds.setDescription("Shows you a list of all worlds.");
		Command load = new AliasManager("mvimport");
		load.setDescription("Lets you load a unloaded world.");
		load.addCommandParameters("world", new CommandParameter[] {CommandParameter.newType("world", false, CommandParamType.STRING)});
		Command edit = new AliasManager("mvedit");
		edit.setDescription("Opens the WorldManager Settings Menu");
		edit.addCommandParameters("world", new CommandParameter[] {CommandParameter.newType("world", true, CommandParamType.STRING)});
		
		load.setAliases(new String[] {
				"mvload", "uniload", "mvimport"
		});
		world.setAliases(new String[] {
				"mvtp", "unitp", "wmtp", "mwtp", "lmtp"
		});
		
		for(Command c : new Command[] {world, worlds, load, edit}) {
			Server.getInstance().getCommandMap().register(c.getName(), c);
		}
		
	}
	
}
