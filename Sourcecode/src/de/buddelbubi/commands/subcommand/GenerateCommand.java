package de.buddelbubi.commands.subcommand;

import java.util.ArrayList;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.generator.Generator;
import de.buddelbubi.WorldManager;
import de.buddelbubi.listener.WorldManagerUI;

public class GenerateCommand extends SubCommand {

    public GenerateCommand() {
        super("generate");
        this.setAliases(new String[] {
            "generate",
            "gen",
            "create"
        });
    }

    @Override
    public CommandParameter[] getParameters() {

        LinkedList < CommandParameter > parameters = new LinkedList < > ();
        parameters.add(CommandParameter.newEnum(this.getName(), this.getAliases()));
        parameters.add(CommandParameter.newType("name", true, CommandParamType.STRING));
        parameters.add(CommandParameter.newEnum("generator", Generator.getGeneratorList()));
        parameters.add(CommandParameter.newType("seed", true, CommandParamType.STRING));
        return parameters.toArray(new CommandParameter[parameters.size()]);
    }

    @Override
    public boolean execute(CommandSender sender, String arg1, String[] args) {

        if (!sender.hasPermission("worldmanager.admin") && !sender.hasPermission("worldmanager.generate")) {

            sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager.generate'.");
            return false;

        } else if (args.length >= 2 && args.length <= 4) {

            if (Server.getInstance().getLevelByName(args[1]) == null) {

                String name = args[1];
                Class < ? extends Generator > generator = Generator.getGenerator("DEFAULT");
                long Seed = new Random().nextLong();
                if (args.length >= 3) {
                    List < String > generators = new ArrayList < > ();
                    for (String s: Generator.getGeneratorList()) generators.add(s);
                    if (generators.contains(args[2])) {
                        generator = Generator.getGenerator(args[2]);
                    } else {
                        sender.sendMessage(WorldManager.prefix + "§cThis generator does not exist.");
                        return false;
                    }
                }
                if (args.length == 4) {
                    try {
                        Seed = Long.parseLong(args[3]);
                    } catch (Exception e) {
                        sender.sendMessage(WorldManager.prefix + "§cYour seed has to be numeric.");
                        return false;
                    }
                }
                try {
                    Server.getInstance().generateLevel(name, Seed, generator);
                    sender.sendMessage(WorldManager.prefix + "§7The world §8" + name + "§7 got generated.");
                } catch (Exception e) {
                    sender.sendMessage(WorldManager.prefix + "§cSomething went wrong during the world generation.");
                }

            } else sender.sendMessage(WorldManager.prefix + "§cThis world already exist..");

        } else {
            if ((sender.hasPermission("worldmanager.generateui") || sender.hasPermission("worldmanager.admin")) && args.length == 1 && !(sender instanceof ConsoleCommandSender)) {
                WorldManagerUI.openWorldGenUI((Player) sender);
                return false;
            }

            sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager generate [Name] (Generator) {Seed}.");
        }
        return false;
    }

}