package de.buddelbubi.commands.subcommand;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import de.buddelbubi.WorldManager;

import java.io.File;
import java.util.LinkedList;

public class ListCommand extends SubCommand {

    public ListCommand() {
        super("list");
        this.setAliases(new String[] {
            "list"
        });
    }

    @Override
    public CommandParameter[] getParameters() {

        LinkedList < CommandParameter > parameters = new LinkedList < > ();
        parameters.add(CommandParameter.newEnum(this.getName(), this.getAliases()));
        parameters.add(CommandParameter.newType("world", false, CommandParamType.STRING));
        return parameters.toArray(new CommandParameter[parameters.size()]);

    }

    @Override
    public boolean execute(CommandSender sender, String arg1, String[] args) {

        if (!sender.hasPermission("worldmanager.admin") && !sender.hasPermission("worldmanager.list")) {

            sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager.list'.");
            return false;

        } else {

            if (args.length == 1) {

                String worldstring = "";
                File folder = new File(Server.getInstance().getDataPath() + "worlds/");
                File[] folders = folder.listFiles();
                int worlds = 0;
                int loaded = 0;
                for (File f: folders) {
                    if (!f.isDirectory()) continue;
                    if (!new File(Server.getInstance().getDataPath() + "worlds/" + f.getName(), "level.dat").exists()) continue;
                    worlds++;
                    if (Server.getInstance().getLevelByName(f.getName()) != null) {
                        loaded++;
                        worldstring += "§8" + f.getName() + "§8 - §aLOADED §8(" + Server.getInstance().getLevelByName(f.getName()).getGenerator().getName() + ")\n";
                    } else worldstring += "§8" + f.getName() + "§8 - §cUNLOADED\n";
                }
                sender.sendMessage(WorldManager.prefix + "§7List of all worlds (" + worlds + ") [§a" + loaded + "§7/§c" + (worlds - loaded) + "§7]\n" + worldstring);

            } else sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager list.");

        }
        return false;
    }

}