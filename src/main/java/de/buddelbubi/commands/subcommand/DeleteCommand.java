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

public class DeleteCommand extends SubCommand {

    public DeleteCommand() {
        super("delete");
        this.setAliases(new String[] {
            "delete",
            "del",
            "remove",
            "purge"
        });
    }

    @Override
    public CommandParameter[] getParameters() {

        LinkedList < CommandParameter > parameters = new LinkedList < > ();
        parameters.add(CommandParameter.newEnum(this.getName(), this.getAliases()));
        parameters.add(CommandParameter.newType("world", true, CommandParamType.STRING));
        return parameters.toArray(new CommandParameter[parameters.size()]);

    }

    @Override
    public boolean execute(CommandSender sender, String arg1, String[] args) {
        if (!sender.hasPermission("worldmanager.admin") && !sender.hasPermission("worldmanager.delete")) {

            sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager.delete'.");
            return false;

        } else {

            try {
                if (args.length == 2) {
                	
                	String name = args[1];
                	if(name.equals("-c") && sender instanceof Player) name = ((Player) sender).getLevel().getName(); // with argument to prevent usage on accident
                    Level l = Server.getInstance().getLevelByName(name);
                    name = l.getName();
                    String folder = l.getFolderName();
                    
                    if (Server.getInstance().getLevelByName(name) != null) {
                    	
                        l.unload();
                        File regionfolder = new File(Server.getInstance().getDataPath() + "worlds/" + folder + "/region");
                        File worldfolder = new File(Server.getInstance().getDataPath() + "worlds/" + folder);
                        FileUtils.deleteDirectoryContents(regionfolder);
                        FileUtils.deleteDirectoryContents(worldfolder);
                        worldfolder.delete();

                        sender.sendMessage(WorldManager.prefix + "§7Deleted the world §8" + name + ".");
                    } else sender.sendMessage(WorldManager.prefix + "§cThis world is not loaded or does not exist.");

                } else sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager delete [Name].");
            } catch (Exception e) {

            }
        }
        return false;
    }

}