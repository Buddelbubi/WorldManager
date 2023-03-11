package de.buddelbubi.commands.subcommand;

import java.util.LinkedList;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import de.buddelbubi.WorldManager;
import de.buddelbubi.listener.Addons;

public class AddonCommand extends SubCommand {

    public AddonCommand() {
        super("addon");
        this.setAliases(new String[] {
            "addon",
            "addons"
        });
    }

    @Override
    public CommandParameter[] getParameters() {

        LinkedList < CommandParameter > parameters = new LinkedList < > ();
        parameters.add(CommandParameter.newEnum(this.getName(), this.getAliases()));
        return parameters.toArray(new CommandParameter[parameters.size()]);

    }

    @Override
    public boolean execute(CommandSender sender, String arg1, String[] args) {

        if (sender instanceof Player && sender.hasPermission("worldmanager.addon")) {

            Addons.showAddonUI((Player) sender);

        } else sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission 'worldmanager.addon'.");

        return false;
    }

}