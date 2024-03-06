package de.buddelbubi.commands.subcommand;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Level;
import de.buddelbubi.WorldManager;
import de.buddelbubi.api.World;
import de.buddelbubi.utils.Cache;

import java.util.LinkedList;

public class InfoCommand extends SubCommand {

    public InfoCommand() {
        super("info");
        this.setAliases(new String[] {
            "info"
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

        if (!sender.hasPermission("worldmanager.admin") && !sender.hasPermission("worldmanager.info")) {

            sender.sendMessage(WorldManager.prefix + "§cYou dont have the permission to do this..");
            return false;

        } else {

            if (sender instanceof ConsoleCommandSender && args.length == 1) {
                sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager info [World].");
                return false;
            }

            Level l = null;
            if (args.length == 1) {
                l = ((Player) sender).getLevel();
            } else if (args.length == 2) {
                if (Server.getInstance().getLevelByName(args[1]) != null) {
                    l = Server.getInstance().getLevelByName(args[1]);
                } else {
                    sender.sendMessage(WorldManager.prefix + "§cThis world does not exist.");
                    return false;
                }
            } else sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager info [World].");
            World world = Cache.getWorld(l);
            String str = "§7Name: §8" + l.getFolderName() + "\n" +
                    "§7Spawn: §8" + l.getSpawnLocation().x + ", " + l.getSpawnLocation().y + ", " + l.getSpawnLocation().z + "\n" +
                    "§7Generator: §8" + l.getGenerator().getName() + "\n" +
                    "§7Chunks: §8" + l.getChunks().size() + "\n" +
                    "§7Dimension: §8" + ((l.getDimension() == 0) ? "Overworld" : (l.getDimension() == 1 ? "Nether" : "End")) + "\n" +
                    "§7Seed: §8" + l.getSeed() + "\n" +
                    "§7Players: §8" + l.getPlayers().size() + "\n" +
                    "§7Entitys: §8" + l.getEntities().length + "\n" +
                    "§7Block Entitys: §8" + l.getBlockEntities().size() + "\n" +
                    "§7Note: §8" + (world.getNote().equals("") ? "§cNone" : world.getNote());
            sender.sendMessage(WorldManager.prefix + "§7Information about §8" + l.getFolderName() + "\n" + str);

        }
        return false;
    }

}