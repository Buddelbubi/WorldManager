package de.buddelbubi.commands.subcommand;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.math.Vector3f;
import de.buddelbubi.WorldManager;

import java.util.*;

public class ReloadCommand extends SubCommand {

    public ReloadCommand() {
        super("reload");
        this.setAliases(new String[] {
            "reload"
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

        if (!sender.hasPermission("worldmanager.admin") && !sender.hasPermission("worldmanager.reload")) {

            sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager.reload'.");
            return false;

        } else {

            if (args.length == 1 || args.length == 2) {

                if (args.length == 1) {
                    List < Level > levels = new ArrayList < > ();
                    for (Level l: Server.getInstance().getLevels().values()) {
                        if (!l.equals(Server.getInstance().getDefaultLevel())) {
                            levels.add(l);
                        }
                    }
                    
                    for (Level l : levels) {
                    	
                    	HashMap<UUID, Vector3f> players = new HashMap<>(); 
                    	for(Player p : l.getPlayers().values()) players.put(p.getUniqueId(), p.asVector3f());
                    	
                        String name = l.getName();
                        l.unload();
                        Server.getInstance().loadLevel(name);
                        
                        Level level = Server.getInstance().getLevelByName(name);
                        
                        for(UUID uuid : players.keySet()) {
                        	Player player = Server.getInstance().getPlayer(uuid).get();
                        	if(player.isOnline()) {
                        		Vector3f vec = players.get(uuid);
                        		Location loc = new Location(vec.x, vec.y, vec.z, level);
                        		player.teleport(loc);
                        	}
                        }
                    }
                    sender.sendMessage(WorldManager.prefix + "§7All worlds have been reloaded.");
                } else {
                    if (Server.getInstance().getLevelByName(args[1]) != null) {
                        String name = args[1];
                        if (Server.getInstance().getDefaultLevel().getName().equalsIgnoreCase(name)) {
                            sender.sendMessage(WorldManager.prefix + "§cYou cannot reload the default world.");
                            return false;
                        }
                        
                        Level l = Server.getInstance().getLevelByName(name);
                        HashMap<UUID, Vector3f> players = new HashMap<>(); 
                    	for(Player p : l.getPlayers().values()) players.put(p.getUniqueId(), p.asVector3f());
                        l.unload();
                        Server.getInstance().loadLevel(name);
                        l = Server.getInstance().getLevelByName(name);
                        for(UUID uuid : players.keySet()) {
                        	Player player = Server.getInstance().getPlayer(uuid).get();
                        	if(player.isOnline()) {
                        		Vector3f vec = players.get(uuid);
                        		Location loc = new Location(vec.x, vec.y, vec.z, l);
                        		player.teleport(loc);
                        	}
                        }

                        sender.sendMessage(WorldManager.prefix + "§7The world §8" + args[1] + " §7has been reloaded.");
                    } else {
                        sender.sendMessage(WorldManager.prefix + "§cThis world does not exist.");
                        return false;
                    }
                }

            } else sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager reload [World]");
        }
        return false;
    }

}