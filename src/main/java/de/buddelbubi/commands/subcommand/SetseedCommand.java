package de.buddelbubi.commands.subcommand;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Level;
import de.buddelbubi.WorldManager;

import java.util.LinkedList;

public class SetseedCommand extends SubCommand {

    public SetseedCommand() {
        super("setseed");
        this.setAliases(new String[] {
            "setseed",
            "reseed"
        });
    }

    @Override
    public CommandParameter[] getParameters() {

        LinkedList < CommandParameter > parameters = new LinkedList < > ();
        parameters.add(CommandParameter.newEnum(this.getName(), this.getAliases()));
        parameters.add(CommandParameter.newType("world", true, CommandParamType.STRING));
        parameters.add(CommandParameter.newType("seed", false, CommandParamType.STRING));
        return parameters.toArray(new CommandParameter[parameters.size()]);

    }

    @Override
    public boolean execute(CommandSender sender, String arg1, String[] args) {

        if (!sender.hasPermission("worldmanager.admin") && !sender.hasPermission("worldmanager.setseed")) {

            sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager.setseed'.");
            return false;

        } else {

            if (args.length == 2 || args.length == 3) {

                Level l = null;
                if (args.length == 2) {
                    if (sender instanceof Player) {
                        l = ((Player) sender).getLevel();
                    } else sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager setseed [World] (Seed).");
                } else {
                    l = Server.getInstance().getLevelByName(args[1]);
                }

                if (l != null) {
                    try {
                        int seed = Integer.parseInt(args[args.length - 1]);
                        l.setSeed(seed);
                        sender.sendMessage(WorldManager.prefix + "§7Seed of §8" + l.getName() + " §7set to §8" + seed);
                    } catch (Exception e) {
                        sender.sendMessage(WorldManager.prefix + "§cThe seed has to be a numeric.");
                    }

                } else sender.sendMessage(WorldManager.prefix + "§cThis world does not exist.");

            } else sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager setseed [World] (Seed).");
        }

        return false;
    }

}