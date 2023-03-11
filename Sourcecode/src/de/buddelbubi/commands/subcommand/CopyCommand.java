package de.buddelbubi.commands.subcommand;

import java.io.File;
import java.util.LinkedList;

import org.iq80.leveldb.util.FileUtils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Level;

import de.buddelbubi.WorldManager;

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

            if (args.length >= 1 && args.length <= 3) {

                Level l = null;
                String name = null;
                if (args.length == 1) {
                    if (sender instanceof Player) {
                        l = ((Player) sender).getLevel();
                        name = "CopyOf" + l.getName();
                    } else {
                        sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager copy [World] (Name of Copy)*.");
                        return false;
                    }
                } else if (args.length == 2) {

                    l = Server.getInstance().getLevelByName(args[1]);
                    if (l != null)
                        name = "CopyOf" + l.getName();

                } else {
                    l = Server.getInstance().getLevelByName(args[1]);
                    name = args[2];
                }

                if (l != null) {

                    int i = 1;
                    while (new File(Server.getInstance().getDataPath() + "worlds/" + name + (i == 1 ? "" : ("#" + i))).exists()) {
                        i++;
                    }
                    if (i != 1) name += (i == 1 ? "" : ("#" + i));
                    new File(Server.getInstance().getDataPath() + "worlds/" + name).mkdir();

                    FileUtils.copyDirectoryContents(new File(Server.getInstance().getDataPath() + "worlds/" + l.getName()), new File(Server.getInstance().getDataPath() + "worlds/" + name));
                    Server.getInstance().loadLevel(name);
                    sender.sendMessage(WorldManager.prefix + "§7Created a copy of §8" + l.getName() + " §7called §8" + name + ".");

                } else sender.sendMessage(WorldManager.prefix + "§cThis world does not exist.");

            } else sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager copy [World]* (Name of Copy).");
         
        }
        return false;
    }

}