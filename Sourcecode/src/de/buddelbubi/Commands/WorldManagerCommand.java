package de.buddelbubi.Commands;

import java.io.File;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.iq80.leveldb.util.FileUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.entity.Entity;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.element.ElementSlider;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.utils.Config;
import de.buddelbubi.WorldManager;
import de.buddelbubi.Events.Addons;
import de.buddelbubi.Events.WorldManagerUI;
import de.buddelbubi.api.World;
import de.buddelbubi.api.WorldManagerOption;
import de.buddelbubi.utils.Cache;
import de.buddelbubi.utils.Updater;


public class WorldManagerCommand extends Command {

    public WorldManagerCommand(String name) {
	   super(name);
		
		for(String subcommand : sub) {
			String[] args = subcommand.split("#");
			String[] subname = args[0].split(",");
			LinkedList<CommandParameter> parameters = new LinkedList<>();
			
			parameters.add(CommandParameter.newEnum(subname[0], subname));
			if(args.length > 1) {
				for(int i = 1; i < args.length; i++) {
					parameters.add(CommandParameter.newType(args[i].split("-")[0], Boolean.parseBoolean(args[i].split("-")[1]), CommandParamType.STRING));
				}
			}
			this.commandParameters.put(subcommand, parameters.toArray(new CommandParameter[parameters.size()]));
			
		}
	   
    }

    public static final String prefix = "§3WorldManager §8» §7";



    @Override
    public boolean execute(CommandSender arg0, String arg1, String[] args) {
	   if (args.length == 0) {
		  arg0.sendMessage(prefix + "§cDo /wm help");
	   } else if (args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("to") || args[0].equalsIgnoreCase("move")) {

		  if (args.length >= 2 && args.length <= 3) {

			 if (Server.getInstance().getLevelByName(args[1]) != null) {
				if (arg0 instanceof ConsoleCommandSender && args.length != 3) {
				    arg0.sendMessage(prefix + "§cDo /worldmanager teleport [Level] (Player).");
				    return false;
				}
				Level level = Server.getInstance().getLevelByName(args[1]);
				Player player = null;
				if (args.length == 2) {
				    player = ((Player) arg0);
				} else if (args.length == 3) {
				    try {
					   if (Server.getInstance().getPlayer(args[2]).isOnline()) {
						  player = Server.getInstance().getPlayer(args[2]);
					   } else player = ((Player) arg0);
				    } catch (Exception e) {
					   arg0.sendMessage(prefix + "§cThe player called §e" + args[2] + "§c is not online.");
					   return false;
				    }
				}
				if (arg0.hasPermission("worldmanager.teleport." + level.getFolderName()) || arg0.hasPermission("worldmanager.teleport") || arg0.hasPermission("worldmanager.admin")) {
				    player.teleport(level.getSpawnLocation());
				    level.addSound(level.getSpawnLocation(), Sound.MOB_SHULKER_TELEPORT, 1, 1, Collections.singletonList(player));
				} else {
				    arg0.sendMessage(prefix + "§cYou dont have the permission to do this..");
				    return false;
				}
				if (!(arg0 instanceof ConsoleCommandSender)) {
				    if (player.equals(arg0)) {
					   player.sendMessage(prefix + "§7You got teleported to §8" + level.getFolderName() + ".");
				    }
				} else arg0.sendMessage(prefix + "§7Teleported §8" + player.getName() + " §7 to §8" + level.getFolderName() + ".");
			 } else arg0.sendMessage(prefix + "§cThe world called §e" + args[1] + "§c does not exist.");

		  } else {

			 if ((arg0.hasPermission("worldmanager.teleportui") || arg0.hasPermission("worldmanager.admin")) && args.length == 1 && !(arg0 instanceof ConsoleCommandSender)) {
				WorldManagerUI.openWorldTeleportUI((Player) arg0);
				return false;
			 } else arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.teleportui'.");

			 if (arg0 instanceof ConsoleCommandSender && args.length != 3) {
				arg0.sendMessage(prefix + "§cDo /worldmanager teleport [Level] (Player).");
				return false;
			 }
		  }
		 
	   } else if (args[0].equalsIgnoreCase("generate") || args[0].equalsIgnoreCase("gen") || args[0].equalsIgnoreCase("create")) {

		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.generate")) {

			 arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.generate'.");
			 return false;

		  } else if (args.length >= 2 && args.length <= 4) {

			 if (Server.getInstance().getLevelByName(args[1]) == null) {

				String name = args[1];
				Class <? extends Generator > generator = Generator.getGenerator("DEFAULT");
				long Seed = new Random().nextLong();
				if (args.length >= 3) {
				    List < String > generators = new ArrayList < > ();
				    for (String s : Generator.getGeneratorList()) generators.add(s);
				    if (generators.contains(args[2])) {
					   generator = Generator.getGenerator(args[2]);
				    } else {
					   arg0.sendMessage(prefix + "§cThis generator does not exist.");
					   return false;
				    }
				}
				if (args.length == 4) {
				    try {
					   Seed = Long.parseLong(args[3]);
				    } catch (Exception e) {
					   arg0.sendMessage(prefix + "§cYour seed has to be numeric.");
					   return false;
				    }
				}
				try {
				    Server.getInstance().generateLevel(name, Seed, generator);
				    arg0.sendMessage(prefix + "§7The world §8" + name + "§7 got generated.");
				} catch (Exception e) {
				    arg0.sendMessage(prefix + "§cSomething went wrong during the world generation.");
				}


			 } else arg0.sendMessage(prefix + "§cThis world already exist..");

		  } else {
			 if ((arg0.hasPermission("worldmanager.generateui") || arg0.hasPermission("worldmanager.admin")) && args.length == 1 && !(arg0 instanceof ConsoleCommandSender)) {
				WorldManagerUI.openWorldGenUI((Player) arg0);
				return false;
			 }

			 arg0.sendMessage(prefix + "§cDo /worldmanager generate [Name] (Generator) {Seed}.");
		  }
	   } else if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("purge")) {
		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.delete")) {

			 arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.delete'.");
			 return false;

		  } else {

			 try {
				if (args.length == 2) {

				    if (Server.getInstance().getLevelByName(args[1]) != null) {

					   Level l = Server.getInstance().getLevelByName(args[1]);
					   String name = l.getName();
					   l.unload();
					   File regionfolder = new File(Server.getInstance().getDataPath() + "worlds/" + name + "/region");
					   File worldfolder = new File(Server.getInstance().getDataPath() + "worlds/" + name);
					   FileUtils.deleteDirectoryContents(regionfolder);
					   FileUtils.deleteDirectoryContents(worldfolder);
					   worldfolder.delete();

					   arg0.sendMessage(prefix + "§7Deleted the world §8" + name + ".");
				    } else arg0.sendMessage(prefix + "§cThis world is not loaded or does not exist.");

				} else arg0.sendMessage(prefix + "§cDo /worldmanager delete [Name].");
			 } catch (Exception e) {

			 }
		  }
		  
	   } else if (args[0].equalsIgnoreCase("load") || args[0].equalsIgnoreCase("import")) {

		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.load")) {

			 arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.load'.");
			 return false;

		  } else {

			 if (args.length == 2) {
				if (Server.getInstance().getLevelByName(args[1]) == null) {

				    if (new File(Server.getInstance().getDataPath() + "worlds/" + args[1] + "/level.dat").exists()) {

					   Server.getInstance().loadLevel(args[1]);

					   arg0.sendMessage(prefix + "§7The world §8" + args[1] + "§7 loaded successfully.");

				    } else arg0.sendMessage(prefix + "§cThis world does not exist.");

				} else arg0.sendMessage(prefix + "§cThis world is already loaded.");

			 } else arg0.sendMessage(prefix + "§cDo /worldmanager load [Name].");

		  }

	   } else if (args[0].equalsIgnoreCase("unload")) {
		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.unload")) {

			 arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.unload'.");
			 return false;

		  } else {

			 if (args.length == 2) {

				if (Server.getInstance().getLevelByName(args[1]) != null) {
				    if (Server.getInstance().getDefaultLevel().equals(Server.getInstance().getLevelByName(args[1]))) {
					   arg0.sendMessage(prefix + "§cYou can not unload the default world.");
					   return false;
				    }

				    Server.getInstance().unloadLevel(Server.getInstance().getLevelByName(args[1]));
				    arg0.sendMessage(prefix + "§7The world §8" + args[1] + "§7 got unloaded.");

				} else arg0.sendMessage(prefix + "§cThis world is not loaded.");

			 } else arg0.sendMessage(prefix + "§cDo /worldmanager unload [World].");
		  }
	   } else if (args[0].equalsIgnoreCase("reload")) {

		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.reload")) {

			 arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.reload'.");
			 return false;

		  } else {

			 if (args.length == 1 || args.length == 2) {

				if (args.length == 1) {
				    List < Level > levels = new ArrayList < > ();
				    for (Level l : Server.getInstance().getLevels().values()) {
					   if (!l.equals(Server.getInstance().getDefaultLevel())) {
						  levels.add(l);
					   }
				    }
				    for (Level l : levels) {
					   String name = l.getName();
					   l.unload();
					   Server.getInstance().loadLevel(name);
				    }
				    arg0.sendMessage(prefix + "§7All worlds have been reloaded.");
				} else {
				    if (Server.getInstance().getLevelByName(args[1]) != null) {
					   String name = args[1];
					   if(Server.getInstance().getDefaultLevel().getName().equalsIgnoreCase(name)) {
						   arg0.sendMessage(prefix + "§cYou cannot reload the default world.");
						   return false;
					   }
					   Server.getInstance().getLevelByName(name).unload();
					   Server.getInstance().loadLevel(name);
					   arg0.sendMessage(prefix + "§7The world §8" + args[1] + " §7has been reloaded.");
				    } else {
					   arg0.sendMessage(prefix + "§cThis world does not exist.");
					   return false;
				    }
				}

			 } else arg0.sendMessage(prefix + "§cDo /worldmanager reload [World]");
		  }
	   } else if (args[0].equalsIgnoreCase("list")) {
		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.list")) {

			 arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.list'.");
			 return false;

		  } else {

			 if (args.length == 1) {

				String worldstring = "";
				File folder = new File(Server.getInstance().getDataPath() + "worlds/");
				File[] folders = folder.listFiles();
				int worlds = 0;
				int loaded = 0;
				for (File f : folders) {
				    if (!f.isDirectory()) continue;
				    if(!new File(Server.getInstance().getDataPath() + "worlds/" + f.getName(), "level.dat").exists()) continue;
				    worlds++;
				    if (Server.getInstance().getLevelByName(f.getName()) != null) {
					   loaded++;
					   worldstring += "§8" + f.getName() + "§8 - §aLOADED §8(" + Server.getInstance().getLevelByName(f.getName()).getGenerator().getName() + ")\n";
				    } else worldstring += "§8" + f.getName() + "§8 - §cUNLOADED\n";
				}
				arg0.sendMessage(prefix + "§7List of all worlds (" + worlds + ") [§a" + loaded + "§7/§c" + (worlds - loaded) + "§7]\n" + worldstring);


			 } else arg0.sendMessage(prefix + "§cDo /worldmanager list.");


		  }
	   } else if (args[0].equalsIgnoreCase("setspawn")) {

		  if (arg0 instanceof ConsoleCommandSender) {
			 arg0.sendMessage(prefix + "§cYou can not do this in the console.");
			 return false;
		  }

		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.setspawn")) {

			 arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.setspawn'.");
			 return false;

		  } else {

			 if (args.length == 1) {
				Player p = ((Player) arg0);
				p.getLevel().setSpawnLocation(p.getLocation());
				arg0.sendMessage(prefix + "§7You set the spawnlocation of §8" + p.getLevel().getName() + "§7 to §8" + p.getLevelBlock().x + ", " + p.getLevelBlock().y + ", " + p.getLevelBlock().z + ".");

			 } else arg0.sendMessage(prefix + "§cDo /worldmanager setspawn.");


		  }


	   } else if (args[0].equalsIgnoreCase("settings") || args[0].equalsIgnoreCase("edit")) {
		  if (arg0 instanceof ConsoleCommandSender) {
			 arg0.sendMessage(prefix + "§cThis can only be done ingame.");
			 return false;
		  }
		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.settings")) {

			 arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.settings'.");
			 return false;

		  } else {

			 if (args.length == 1 || args.length == 2) {

				Level l = ((Player) arg0).getLevel();
				if (args.length == 2) {
					if (Server.getInstance().getLevelByName(args[1]) != null) {
					   l = Server.getInstance().getLevelByName(args[1]);
					} else {
					   arg0.sendMessage(prefix + "§cThis world does not exist.");
					   return false;
				    }
				}
				World w = Cache.getWorld(l);
				List < String > worlds = new ArrayList < > ();
				for (Level level : Server.getInstance().getLevels().values()) worlds.add(level.getName());
				FormWindowCustom fw = new FormWindowCustom("§3WorldSettings - " + l.getFolderName());
				fw.addElement(new ElementToggle("Load On Start", w.doesLoadOnStart()));
				fw.addElement(new ElementToggle("Use Own Gamemode", w.isUsingOwnGamemode()));
				fw.addElement(new ElementDropdown("Gamemode", Arrays.asList("Survival", "Creative", "Adventure", "Spectator"), w.getOwnGamemode()));
				fw.addElement(new ElementToggle("Fly", w.isFlyAllowed()));
				fw.addElement(new ElementDropdown("Respawn World", worlds, (worlds.contains(w.getRespawnWorld()) ? worlds.indexOf(w.getRespawnWorld()) : worlds.indexOf(l.getName()))));
				fw.addElement(new ElementToggle("Protected", w.isProtected()));
				fw.addElement(new ElementInput("Notepad", "Sth. to remember like coords.", w.getNote()));
				Config c = w.getConfig();
				for (WorldManagerOption o : WorldManagerOption.getCustomOptions()) {
				    if (o.getValue() instanceof Boolean) {
					   fw.addElement(new ElementToggle(o.getDisplay(), c.getBoolean(o.getKey())));
				    } else if (o.getValue() instanceof String) {
					   fw.addElement(new ElementInput(o.getDisplay(), o.getDescription(), c.getString(o.getKey())));
				    } else if (o.getValue() instanceof Integer) {
					   fw.addElement(new ElementSlider(o.getDisplay(), 0, o.maxvalue, 1, c.getInt(o.getKey())));
				    }
				}

				((Player) arg0).showFormWindow(fw);
			 } else arg0.sendMessage(prefix + "§cDo /worldmanager settings [World].");

		  }

	   } else if (args[0].equalsIgnoreCase("info")) {

		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.info")) {

			 arg0.sendMessage(prefix + "§cYou dont have the permission to do this..");
			 return false;

		  } else {

			 if (arg0 instanceof ConsoleCommandSender && args.length == 1) {
				arg0.sendMessage(prefix + "§cDo /worldmanager info [World].");
				return false;
			 }

			 Level l = null;
			 if (args.length == 1) {
				l = ((Player) arg0).getLevel();
			 } else if (args.length == 2) {
				if (Server.getInstance().getLevelByName(args[1]) != null) {
				    l = Server.getInstance().getLevelByName(args[1]);
				} else {
				    arg0.sendMessage(prefix + "§cThis world does not exist.");
				    return false;
				}
			 } else arg0.sendMessage(prefix + "§cDo /worldmanager info [World].");
			 World world = Cache.getWorld(l);
			 String str = "§7Name: §8" + l.getFolderName() + "\n" +
				"§7Spawn: §8" + l.getSpawnLocation().x + ", " + l.getSpawnLocation().y + ", " + l.getSpawnLocation().z + "\n" +
				"§7Generator: §8" + l.getGenerator().getName() + "\n" +
				"§7Chunks: §8" + l.getChunks().size() + "\n" +
				"§7Dimension: §8" + ((l.getDimension() == 0) ? "Overworld" : (l.getDimension() == 1 ? "Nether" : "End")) + "\n" +
				"§7Seed: §8" + l.getSeed() + "\n" +
				"§7Players: §8" + l.getPlayers().size() + "\n" +
				"§7Note: §8" + (world.getNote().equals("") ? "§cNone" : world.getNote());
			 arg0.sendMessage(prefix + "§7Information about §8" + l.getFolderName() + "\n" + str);

		  }


	   } else if (args[0].equalsIgnoreCase("setseed") || args[0].equalsIgnoreCase("reseed")) {
		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.setseed")) {

			 arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.setseed'.");
			 return false;

		  } else {

			 if (args.length == 2 || args.length == 3) {

				 Level l = null;
				 if(args.length == 2) {
					 if(arg0 instanceof Player) {
						 l = ((Player) arg0).getLevel();
					 } else arg0.sendMessage(prefix + "§cDo /worldmanager setseed [World] (Seed).");
				 } else {
					l = Server.getInstance().getLevelByName(args[1]);
				 }
				 
				if (l != null) {
				    try {
					   int seed = Integer.parseInt(args[args.length-1]);
					   l.setSeed(seed);
					   arg0.sendMessage(prefix + "§7Seed of §8" + l.getName() + " §7set to §8" + seed);
				    } catch (Exception e) {
					   arg0.sendMessage(prefix + "§cThe seed has to be a numeric.");
				    }

				} else arg0.sendMessage(prefix + "§cThis world does not exist.");
				
			 } else arg0.sendMessage(prefix + "§cDo /worldmanager setseed [World] (Seed).");
		  }

	   } else if (args[0].equalsIgnoreCase("rename") || args[0].equalsIgnoreCase("changename")) {
		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.rename")) {

			 arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.rename'.");
			 return false;

		  } else {

				 if (args.length == 2 || args.length == 3) {

					 Level l = null;
					 String newname;
					 if(args.length == 2) {
						 if(arg0 instanceof Player) {
							 l = ((Player) arg0).getLevel();
							 newname = args[1];
						 } else {
							 arg0.sendMessage(prefix + "§cDo /worldmanager rename [Old Name] (New Name).");
							 return false;
						 }
					 } else {
						l = Server.getInstance().getLevelByName(args[1]);
						newname = args[2];
					 }

				if (l != null) {
				    if (new File(Server.getInstance().getDataPath() + "worlds/" + newname).exists()) {
					   arg0.sendMessage(prefix + "§cThere already is a world called " + newname + ".");
					   return false;
				    }
				    if (Server.getInstance().getDefaultLevel() == l) {
					   arg0.sendMessage(prefix + "§cYou can't rename the default world.");
					   return false;
				    }
				    String name = l.getName();
				    l.unload();
				    new File(Server.getInstance().getDataPath() + "worlds/" + name).renameTo(new File(Server.getInstance().getDataPath() + "worlds/" + newname));
				    Server.getInstance().loadLevel(newname);
				    arg0.sendMessage(prefix + "§7Surccessfully renamed §8" + name + " §7to §8" + newname + ".");
				} else arg0.sendMessage(prefix + "§cThis world does not exist.");


			 } else arg0.sendMessage(prefix + "§cDo /worldmanager rename [Old Name]* (New Name).");
		  }

	   } else if (args[0].equalsIgnoreCase("copy") || args[0].equalsIgnoreCase("duplicate") || args[0].equalsIgnoreCase("dupe") || args[0].equalsIgnoreCase("backup") || args[0].equalsIgnoreCase("replicate")) {

		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.copy")) {

			 arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.copy'.");
			 return false;

		  } else {

			 if (args.length >= 1 && args.length <= 3) {

				 Level l = null;
				 String name = null;
				 if(args.length == 1) {
					 if(arg0 instanceof Player) {
						 l = ((Player) arg0).getLevel();
						 name = "CopyOf" + l.getName();
					 } else {
						 arg0.sendMessage(prefix + "§cDo /worldmanager copy [World] (Name of Copy)*.");
						 return false;
					 }
				 } else if(args.length == 2){
					 
						 l = Server.getInstance().getLevelByName(args[1]);
						 if(l != null)
						 name = "CopyOf" + l.getName();
					
				 } else {
					l = Server.getInstance().getLevelByName(args[1]);
					name = args[2];
				 }
				 
				 
				if (l != null) {

					int i = 1;
					while(new File(Server.getInstance().getDataPath() + "worlds/" + name + (i == 1 ? "" : ("#"+i))).exists()) {
						i++;
					}
					if(i != 1) name += (i == 1 ? "" : ("#"+i));
				    new File(Server.getInstance().getDataPath() + "worlds/" + name).mkdir();
				   
				    FileUtils.copyDirectoryContents(new File(Server.getInstance().getDataPath() + "worlds/" + l.getName()), new File(Server.getInstance().getDataPath() + "worlds/" + name));
				    Server.getInstance().loadLevel(name);
				    arg0.sendMessage(prefix + "§7Created a copy of §8" + l.getName() + " §7called §8" + name + ".");

				} else arg0.sendMessage(prefix + "§cThis world does not exist.");

			 } else arg0.sendMessage(prefix + "§cDo /worldmanager copy [World]* (Name of Copy).");

		  }

	   } else if (args[0].equalsIgnoreCase("regenerate") || args[0].equalsIgnoreCase("reg") || args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("regen")) {
		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.regenerate")) {

			 arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.regenerate'.");
			 return false;

		  } else {
			  if(args.length == 1 || args.length == 2) {
			 Level l = null;
			 if (args.length == 1) {
				if (arg0 instanceof Player) {
				    l = ((Player) arg0).getLevel();
				} else arg0.sendMessage(prefix + "§cDo /worldmanager regenerate [World]*.");
			 } else
			 if (args.length == 2) {
				if (Server.getInstance().getLevelByName(args[1]) != null) {

				    l = Server.getInstance().getLevelByName(args[1]);

				} else {
				    arg0.sendMessage(prefix + "§cThis world does not exist.");
				    return false;
				}
			 }
				long seed = l.getSeed();
				Generator generator = l.getGenerator();
				String name = l.getFolderName();
				l.unload();
				File regionfolder = new File(Server.getInstance().getDataPath() + "worlds/" + name + "/region");
				File worldfolder = new File(Server.getInstance().getDataPath() + "worlds/" + name);
				FileUtils.deleteDirectoryContents(regionfolder);
				FileUtils.deleteDirectoryContents(worldfolder);
				worldfolder.delete();
				Server.getInstance().generateLevel(name, seed, generator.getClass());
				arg0.sendMessage(prefix + "§7World §8" + name + " §7regenerated.");

			 } else arg0.sendMessage(prefix + "§cDo /worldmanager regenerate [World].");
		  }
	   } else if (args[0].equalsIgnoreCase("killentitys") || args[0].equalsIgnoreCase("clearlag")) {
		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.killentitys") && !arg0.hasPermission("clearlag")) {

			 arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.clearlag'.");
			 return false;

		  } else {
			 Level l = null;
			 if (args.length == 1) {
				if (arg0 instanceof Player) {
				    l = ((Player) arg0).getLevel();
				} else arg0.sendMessage(prefix + "§cDo /worldmanager killentitys [World]*.");
			 } else
			 if (args.length == 2) {
				if (Server.getInstance().getLevelByName(args[1]) != null) {

				    l = Server.getInstance().getLevelByName(args[1]);

				} else {
				    arg0.sendMessage(prefix + "§cThis world does not exist.");
				    return false;
				}


			 } else arg0.sendMessage(prefix + "§cDo /worldmanager killentitys [World]*.");
			 for (Entity entity : l.getEntities()) {
				if (!(entity instanceof Player)) {
				    entity.close();
				}
			 }
			 arg0.sendMessage(prefix + "§7Killed all entitys in §8" + l.getName() + ".");
		  }
	   } else if (args[0].equalsIgnoreCase("gamerule") || args[0].equalsIgnoreCase("gamerules")) {

		  if (!(arg0 instanceof Player)) {
			 arg0.sendMessage(prefix + "§cThis command can only be used ingame.");
			 return false;
		  }

		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.gamerule") && !arg0.hasPermission("worldmanager.gamerule." + args[1])) {
			 arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + (args.length == 2 ? args[1] : "") + "'");
			 return false;
		  }

		  Level l = null;
		  if (args.length == 1) {
			 l = ((Player) arg0).getLevel();
		  } else
		  if (args.length == 2) {
			 if (Server.getInstance().getLevelByName(args[1]) != null) {

				l = Server.getInstance().getLevelByName(args[1]);

			 } else {
				arg0.sendMessage(prefix + "§cThis world does not exist.");
				return false;
			 }


		  } else {
			 arg0.sendMessage(prefix + "§cDo /worldmanager gamerule [World]*.");
			 return false;
		  }

		  FormWindowCustom c = new FormWindowCustom("§3WorldGamerules - " + l.getFolderName());
		  for (GameRule r : GameRule.values()) {
			 switch (l.getGameRules().getGameRuleType(r)) {

				case BOOLEAN:
				    c.addElement(new ElementToggle(r.getName(), l.getGameRules().getBoolean(r)));
				    break;
				case INTEGER:
				    c.addElement(new ElementInput(r.getName(), r.getName(), String.valueOf(l.getGameRules().getInteger(r))));
				    break;

				default:
				    break;
			 }
		  }
		  ((Player) arg0).showFormWindow(c);

	   } else if (args[0].equalsIgnoreCase("default") || args[0].equalsIgnoreCase("setdefault")) {
		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.default") && !arg0.hasPermission("worldmanager.setdefault")) {
			 arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'");
			 return false;
		  }

		  if (args.length == 1) {
			 arg0.sendMessage(prefix + "§7The default world is §8" + Server.getInstance().getDefaultLevel().getName() + ".");
		  } else
		  if (args.length == 2) {
			 if (Server.getInstance().getLevelByName(args[1]) != null) {
				if (Server.getInstance().getDefaultLevel() == Server.getInstance().getLevelByName(args[1])) {
				    arg0.sendMessage(prefix + "§cThis world already is the default world!");
				    return false;
				}
				Server.getInstance().setDefaultLevel(Server.getInstance().getLevelByName(args[1]));

				Config serverconfig = Server.getInstance().getProperties();
				serverconfig.set("level-name", args[1]);
				serverconfig.save();

				arg0.sendMessage(prefix + "§7The world §8" + args[1] + " §7is now the default world.");

			 } else {
				arg0.sendMessage(prefix + "§cThis world does not exist.");
				return false;
			 }


		  } else {
			 arg0.sendMessage(prefix + "§cDo /worldmanager default [World]*.");
			 return false;
		  }

	   } else if (args[0].equalsIgnoreCase("save")) {

		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.save")) {
			 arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'");
			 return false;
		  }
		  Level l = null;
		  if (args.length == 1) {
			 if (!(arg0 instanceof Player)) {
				arg0.sendMessage(prefix + "§cDo /worldmanager save [World]*.");
				return false;
			 } else l = ((Player) arg0).getLevel();
		  } else
		  if (args.length == 2) {
			 if (Server.getInstance().getLevelByName(args[1]) != null) {

				l = Server.getInstance().getLevelByName(args[1]);

			 } else {
				arg0.sendMessage(prefix + "§cThis world does not exist.");
				return false;
			 }

		  } else {
			 arg0.sendMessage(prefix + "§cDo /worldmanager save [World]*.");
			 return false;
		  }

		  l.save(true);
		  arg0.sendMessage(prefix + "§7The world §8" + l.getFolderName() + " §7got saved successfully.");

	   } else if (args[0].equalsIgnoreCase("version")) {

		  arg0.sendMessage(prefix + "You are using WorldManager v" + WorldManager.get().getDescription().getVersion());

	   } else if (args[0].equalsIgnoreCase("sync")) {

		  if (arg0.hasPermission("worldmanager.admin") || arg0.hasPermission("worldmanager.sync")) {

			 if (arg0 instanceof Player) {

				if (args.length == 1 || args.length == 2) {

				    String levelname = null;

				    if (args.length == 2) {
					   levelname = args[1];
				    } else levelname = ((Player) arg0).getLevel().getName();

				    if (Server.getInstance().getLevelByName(levelname) != null) {

					   FormWindowCustom fw = new FormWindowCustom("§3WorldSync - " + levelname);
					   fw.addElement(new ElementLabel("§7Select the worlds you want to sync with §e" + levelname + "§7. This includes WorldSettings and Gamerules."));
					   List <Level> level = new ArrayList <> (Server.getInstance().getLevels().values());
					   level.remove(Server.getInstance().getLevelByName(levelname));
					   
					   for (Level l : level) {
						  fw.addElement(new ElementToggle(l.getFolderName(), false));
					   }
					   
					   ((Player) arg0).showFormWindow(fw);

				    } else {
					   arg0.sendMessage(prefix + "§cThis world does not exist.");
					   return false;
				    }

				} else arg0.sendMessage(prefix + "§cDo /worldmanager sync [templateWorld]*.");

			 } else arg0.sendMessage(prefix + "§cThis command can only be executed ingame.");

		  } else arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'.");
		  return false;


	   } else if (args[0].equalsIgnoreCase("addon") || args[0].equalsIgnoreCase("addons")) {

		  if (arg0 instanceof Player && arg0.hasPermission("worldmanager.addon")) {
			 Addons.showAddonUI((Player) arg0);

		  } else arg0.sendMessage(prefix + "§cYou are lacking the permission 'worldmanager.addon'.");

	   } else if (args[0].equalsIgnoreCase("spawn")) {
		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.spawn")) {

			 arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'.");
			 return false;

		  } else {

			 if (arg0 instanceof Player) {

				((Player) arg0).teleport(((Player) arg0).getLevel().getSafeSpawn());
				arg0.sendMessage(prefix + "§7Successfully teleported to spawn.");

			 } else arg0.sendMessage(prefix + "§cThis command can only be executed ingame.");

		  }

	   } else if (args[0].equalsIgnoreCase("status")) {

		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.status")) {

			 arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'.");
			 return false;

		  } else {

			 String message = "§l§3WorldManager §eStatus\n§r";
			 message += ("§ePlugin Version: §7" + WorldManager.get().getDescription().getVersion() + "\n");
			 message += ("§eNewest Version: §7" + Updater.getNewestVersion() + "\n");
			 message += ("§eCached Worlds: §7" + Cache.getWorldCache().size() + "\n");
			 message += ("§eCached Players: §7" + Cache.getCachedPlayerGamemodes() + "\n");
			 message += "§eWorlds: §7";
			 for (World w : Cache.getWorldCache()) message += w.getAsLevel().getName() + ", ";
			 arg0.sendMessage(message);


		  }

	   } else if (args[0].equalsIgnoreCase("help")) {
		  if (!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.help")) {

			 arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'.");
			 return false;

		  } else {

			 if (args.length == 1) {

				arg0.sendMessage("§eWorldmanager Help\n" +
				    "§3The Maincommand is /worldmanager. But you can also use §c/wm, /mw, /mv, /levelmanager, /world §3and §c/lm§3\n" +
				    "§c/worldmanager teleport [World]§4*§3 (Player)§4* §3teleports you or the pointed player in this world. Instead of teleport, you can use §ctp §3and §cto\n" +
				    "§c/worldmanager generate [World] (Generator)§4*§c {Seed}§4* §3generate a new world. Instead of generate you can use §cgen §3or §ccreate\n" +
				    "§c/worldmanager delete [World] §3deletes the world. Instead of delete you can use §cdel, remove §3or §cpurge\n" +
				    "§c/worldmanager load [World] §3loads the world while §c/worldmanager unload [World]§3 unloads a world\n" +
				    "§c/worldmanager list §3shows you a list of every world\n" +
				    "§c/worldmanager reload [World]§4*§3 reloads a world.\n" +
				    "§c/worldmanager rename [World]§4*§3 (New Worldname) §3renames a world\n" +
				    "§c/worldmanager copy [World]§4*§3 (Name of the Copy)§4*§3 will copy a world.\n" +
				    "§c/worldmanager setspawn §3will set the worldspawn\n" +
				    "§c/worldmanager settings [World]§4* §3opens a FormUI with world-specific settings\n" +
				    "§c/worldmanager regenerate [World]§4* §3regenerates the world. You also can use §creg, regen §3 or §creset §3instead of §cregenerate\n" +
				    "§c/worldmanager setseed [World]§4* §3(Seed) §3Change the seed of your world. Also works with §creseed\n" +
				    "§c/worldmanager killentitys [World]§4*§3 This kills all entitys and lying items. You can use §cclearlag\n" +
				    "§c/worldmanager info [World]§4*§3 shows you informations about this world\n" +
				    "§c/worldmanager gamerule [World]§4*§3 opens an UI to manage the gamerules\n" +
				    "§c/worldmanager version §3shows you your current version of worldmanager \n" +
				    "§c/worldmanager sync (templateworld)§4*§3 §3opens and UI to sync gamerules and settings with this world\n" +
				    "§c/worldmanager spawn §3teleports you to the spawn of your current world.\n" +
				    "§c/worldmanager save [World]§4*§3 saves the selected world..\n" +
				    "§c/worldmanager addons §3will open the Addon UI to extend your Server\n" +
				    "§c/worldmanager default §3shows you the default level. You can change it if you write the worldname too." +
				    "§4§l* optional");


			 } else arg0.sendMessage(prefix + "§cDo /worldmanager help");
			 return false;

		  }

	   } else arg0.sendMessage(prefix + "§cDo /worldmanager help");


	   return false;
    }
    
    
    private final String[] sub = new String[] {
    		"teleport,tp,to,move#World-true", 
    		"generate,gen,create#World Name-true", 
    		"delete,del,remove,purge#World-false", 
    		"load,import#World-false", 
    		"unload#World-false", 
    		"reload#World-true", 
    		"list", 
    		"setspawn", 
    		"settings,edit#World-true", 
    		"info#World-true",
    		"setseed,reseed#World-false#Seed-true",
    		"rename,changename#Old Name-true#New Name-false",
    		"copy,duplicate,dupe,backup,replicate#World-true#Name of Copy-true",
    		"regenerate,reg,reset,regen#World-true",
    		"killentitys,clearlag#World-true",
    		"gamerule,gamerules#World-true",
    		"default,setdefault#World-true",
    		"save#World-true",
    		"version",
    		"sync#World-true",
    		"addon,addons",
    		"spawn",
    		"status",
    		"help"};
}