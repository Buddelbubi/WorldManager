package de.buddelbubi.Commands;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

public class AliasManager extends Command {

	public AliasManager(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean execute(CommandSender arg0, String arg1, String[] arg2) {
		
		String args = "";
		for(String s : arg2) args = args + " " + s;
		
		if(arg1.toLowerCase().matches("world|mvtp|unitp|wmtp|lmtp")) {
			Server.getInstance().dispatchCommand(arg0, "wm tp" + args); 
		} else if(arg1.toLowerCase().matches("mvimport|mvload|uniload|mvimport")) {
			Server.getInstance().dispatchCommand(arg0, "wm load" + args); 
		} else if(arg1.equalsIgnoreCase("mvedit")){
			Server.getInstance().dispatchCommand(arg0, "wm gamerule" + args); 
		} else if(arg1.equalsIgnoreCase("locatebiome")) {
			Server.getInstance().dispatchCommand(arg0, "wm locatebiome" + args); 
		}
		return false;
	}

	public static void registerAliases() {
		
		Command world = new AliasManager("world");
		Command load = new AliasManager("mvimport");
		Command edit = new AliasManager("mvedit");
		Command locatebiome = new AliasManager("locatebiome");
		
		load.setAliases(new String[] {
				"mvload", "uniload", "mvimport"
		});
		world.setAliases(new String[] {
				"mvtp", "unitp", "wmtp", "mwtp", "lmtp"
		});
		
		Server.getInstance().getCommandMap().register(world.getName(), world);
		Server.getInstance().getCommandMap().register(load.getName(), load);
		Server.getInstance().getCommandMap().register(edit.getName(), edit);
		Server.getInstance().getCommandMap().register(locatebiome.getName(), locatebiome);
	}
	
}
