package de.buddelbubi.commands.subcommand;

import java.util.LinkedList;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Level;
import de.buddelbubi.WorldManager;

public class SaveCommand extends SubCommand {

    public SaveCommand() {
        super("save");
        this.setAliases(new String[] {
            "save"
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

        if (!sender.hasPermission("worldmanager.admin") && !sender.hasPermission("worldmanager.save")) {
            sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'");
            return false;
        }
        Level l = null;
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager save [World]*.");
                return false;
            } else l = ((Player) sender).getLevel();
        } else
        if (args.length == 2) {
            if (Server.getInstance().getLevelByName(args[1]) != null) {

                l = Server.getInstance().getLevelByName(args[1]);

            } else {
                sender.sendMessage(WorldManager.prefix + "§cThis world does not exist.");
                return false;
            }

        } else {
            sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager save [World]*.");
            return false;
        }

        l.save();
        sender.sendMessage(WorldManager.prefix + "§7The world §8" + l.getFolderName() + " §7got saved successfully.");
        return false;
    }

}