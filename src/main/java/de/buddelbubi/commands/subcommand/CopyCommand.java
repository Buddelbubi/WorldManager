package de.buddelbubi.commands.subcommand;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Level;
import de.buddelbubi.WorldManager;
import org.iq80.leveldb.util.FileUtils;

import java.io.File;
import java.util.LinkedList;

public class CopyCommand extends SubCommand {

    public CopyCommand() {
        super("copy");
        this.setAliases(new String[] {
            "copy",
            "duplicate",
            "dupe",
            "backup",
            "replicate"
        });
    }

    @Override
    public CommandParameter[] getParameters() {

        LinkedList < CommandParameter > parameters = new LinkedList < > ();
        parameters.add(CommandParameter.newEnum(this.getName(), this.getAliases()));
        parameters.add(CommandParameter.newType("world", true, CommandParamType.STRING));
        parameters.add(CommandParameter.newType("nameofcopy", true, CommandParamType.STRING));
        return parameters.toArray(new CommandParameter[parameters.size()]);
    }

    @Override
    public boolean execute(CommandSender sender, String arg1, String[] args) {
        if (!sender.hasPermission("worldmanager.admin") && !sender.hasPermission("worldmanager.copy")) {

			sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager.copy'.");
            return false;

        } else {


                Level level = null;
                String name = null;
                
               
                
                if(args.length >= 1) {

                	for(int i = 0; i < args.length; i++) {
                		
                		String arg = args[i];
                		
                		if(!arg.equalsIgnoreCase("-t")) {
                			if(level == null) {
                				Level l = Server.getInstance().getLevelByName(arg);
                				if(l == null) {
                					name = arg;
                				}
                				level = l;
                			} else if(name == null) {
                				name = arg;
                			}
                 		}
                		
                		if(args[i].equals("-w")) {
            				
            				if(args.length >= i+1) {
            					name = args[i+1];
            				} else {
								sender.sendMessage(WorldManager.prefix + "§cUse /worldmanager copy -w [World] -n [New Name]");
            					return true;
            				}
            				
            			}
                		
                		if(args[i].equals("-n")) {
            				
            				if(args.length >= i+1) {
            					name = args[i+1];
            				} else {
								sender.sendMessage(WorldManager.prefix + "§cUse /worldmanager copy -n [New Name]");
            					return true;
            				}
            				
            			}
                		
                	}
                	
                	if(level == null && sender instanceof Player) level = ((Player) sender).getLevel();
                	if(name == null && level != null) name = "CopyOf" + level.getName();

				} else
					sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager copy (-w [World])* (-n [Name of Copy])* (-t)*.");

                if (level != null) {

                    int i = 1;
                    while (new File(Server.getInstance().getDataPath() + "worlds/" + name + (i == 1 ? "" : ("#" + i))).exists()) {
                        i++;
                    }
                    if (i != 1) name += (i == 1 ? "" : ("#" + i));
                    new File(Server.getInstance().getDataPath() + "worlds/" + name).mkdir();

                    FileUtils.copyDirectoryContents(new File(Server.getInstance().getDataPath() + "worlds/" + level.getName()), new File(Server.getInstance().getDataPath() + "worlds/" + name));
                    Server.getInstance().loadLevel(name);
                    
                    for(String arg : args) {
                    	if(arg.equalsIgnoreCase("-t")) {
           				 	if(sender instanceof Player) {
               				 	Player player = (Player) sender;
               				 	player.teleport(Server.getInstance().getLevelByName(name).getSafeSpawn());
           				 	} else  {
								sender.sendMessage(WorldManager.prefix + "§cThis parameter is for ingame use only!");
           				 	}
           				 	break;
                    	}
           		 	}

					sender.sendMessage(WorldManager.prefix + "§7Created a copy of §8" + level.getName() + " §7called §8" + name + ".");

				} else sender.sendMessage(WorldManager.prefix + "§cThis world does not exist.");

        }
        return false;
    }

}