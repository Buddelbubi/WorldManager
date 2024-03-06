package de.buddelbubi.commands.subcommand;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Level;
import de.buddelbubi.WorldManager;

import java.io.File;
import java.util.LinkedList;

public class RenameCommand extends SubCommand {

    public RenameCommand() {
        super("rename");
        this.setAliases(new String[] {
            "rename",
            "changename"
        });
    }

    @Override
    public CommandParameter[] getParameters() {

        LinkedList < CommandParameter > parameters = new LinkedList < > ();
        parameters.add(CommandParameter.newEnum(this.getName(), this.getAliases()));
        parameters.add(CommandParameter.newType("world", true, CommandParamType.STRING));
        parameters.add(CommandParameter.newType("newname", false, CommandParamType.STRING));
        return parameters.toArray(new CommandParameter[parameters.size()]);

    }

    @Override
    public boolean execute(CommandSender sender, String arg1, String[] args) {

        if (!sender.hasPermission("worldmanager.admin") && !sender.hasPermission("worldmanager.rename")) {

            sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager.rename'.");
            return false;

        } else {

            if (args.length == 2 || args.length == 3) {

                Level l = null;
                String newname;
                if (args.length == 2) {
                    if (sender instanceof Player) {
                        l = ((Player) sender).getLevel();
                        newname = args[1];
                    } else {
                        sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager rename [Old Name] (New Name).");
                        return false;
                    }
                } else {
                    l = Server.getInstance().getLevelByName(args[1]);
                    newname = args[2];
                }

                if (l != null) {
                    if (new File(Server.getInstance().getDataPath() + "worlds/" + newname).exists()) {
                        sender.sendMessage(WorldManager.prefix + "§cThere already is a world called " + newname + ".");
                        return false;
                    }
                    if (Server.getInstance().getDefaultLevel() == l) {
                        sender.sendMessage(WorldManager.prefix + "§cYou can't rename the default world.");
                        return false;
                    }
                    String name = l.getName();
                    l.unload();
                    new File(Server.getInstance().getDataPath() + "worlds/" + name).renameTo(new File(Server.getInstance().getDataPath() + "worlds/" + newname));
                    Server.getInstance().loadLevel(newname);
                    sender.sendMessage(WorldManager.prefix + "§7Surccessfully renamed §8" + name + " §7to §8" + newname + ".");
                } else sender.sendMessage(WorldManager.prefix + "§cThis world does not exist.");

            } else sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager rename [Old Name]* (New Name).");
        }

        return false;
    }

}