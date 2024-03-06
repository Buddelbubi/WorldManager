package de.buddelbubi.commands.subcommand;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.registry.Registries;
import de.buddelbubi.WorldManager;
import de.buddelbubi.utils.LevelNBT;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LoadCommand extends SubCommand {

    public LoadCommand() {
        super("load");
        this.setAliases(new String[]{
                "load",
                "import"
        });
    }

    @Override
    public CommandParameter[] getParameters() {

        LinkedList<CommandParameter> parameters = new LinkedList<>();
        parameters.add(CommandParameter.newEnum(this.getName(), this.getAliases()));
        return parameters.toArray(new CommandParameter[parameters.size()]);

    }

    @Override
    public boolean execute(CommandSender sender, String arg1, String[] args) {

        if (!sender.hasPermission("worldmanager.admin") && !sender.hasPermission("worldmanager.load")) {

            sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager.load'.");
            return false;

        } else {

            if (args.length >= 2) {

                String levelname = args[1];

                if (Server.getInstance().getLevelByName(levelname) == null) {

                    File levelDat = new File(Server.getInstance().getDataPath() + "worlds/" + args[1] + "/level.dat");

                    if (levelDat.exists()) {

                        if (args.length >= 3) {

                            for (int i = 0; i < args.length; i++) {

                                if (args[i].equals("-g")) {

                                    if (args.length >= i + 1) {

                                        String generator = "flat";

                                        List<String> generators = new ArrayList<>(Registries.GENERATOR.getGeneratorList());
                                        if (generators.contains(args[i + 1])) {
                                            generator = args[i + 1];
                                        } else {
                                            sender.sendMessage(WorldManager.prefix + "§cThis generator does not exist.");
                                            return true;
                                        }

                                        CompoundTag tag = LevelNBT.getLevelData(levelDat);
                                        tag.putString("generatorName", args[i + 1].toLowerCase());
                                        if (!LevelNBT.saveNBT(tag, levelDat)) {
                                            sender.sendMessage(WorldManager.prefix + "§cFailed to load world §e" + levelname + " §cwith generator §e" + generator + ".");
                                            return true;
                                        }

                                    } else {
                                        sender.sendMessage(WorldManager.prefix + "§cUse /worldmanager load [Name] -g [Generator]");
                                        return true;
                                    }

                                }

                            }

                        }

                        boolean loaded = Server.getInstance().loadLevel(levelname);
                        if (loaded) {
                            sender.sendMessage(WorldManager.prefix + "§7The world §8" + levelname + "§7 loaded successfully.");

                            //Checking for the teleport attribute
                            for (String arg : args) {
                                if (arg.equalsIgnoreCase("-t")) {
                                    if (sender instanceof Player) {
                                        Player player = (Player) sender;
                                        player.teleport(Server.getInstance().getLevelByName(levelname).getSafeSpawn());
                                    } else {
                                        sender.sendMessage(WorldManager.prefix + "§cThis parameter is for ingame use only!");
                                    }
                                    break;
                                }
                            }

                        } else
                            sender.sendMessage(WorldManager.prefix + "§cThe world §8" + levelname + "§4failed §cload.");

                    } else sender.sendMessage(WorldManager.prefix + "§cThis world does not exist.");

                } else sender.sendMessage(WorldManager.prefix + "§cThis world is already loaded.");

            } else sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager load [Name] (args).");

        }
        return false;
    }

}