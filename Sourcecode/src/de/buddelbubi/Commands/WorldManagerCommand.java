package de.buddelbubi.Commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.iq80.leveldb.util.FileUtils;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementToggle;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Sound;
import cn.nukkit.level.biome.Biome;
import cn.nukkit.level.biome.EnumBiome;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.utils.Config;
import de.buddelbubi.WorldManager;
import de.buddelbubi.Events.Addons;
import de.buddelbubi.Events.Events;
import de.buddelbubi.Events.WorldManagerUI;
import de.buddelbubi.api.World;
import de.buddelbubi.utils.Cache;


public class WorldManagerCommand extends Command {

	public WorldManagerCommand(String name) {
		super(name);
	}

	public static final String prefix = "§3WorldManager §8» §7";

	public static HashMap<String, Thread> threads = new HashMap<>();
	
 	@Override
	public boolean execute(CommandSender arg0, String arg1, String[] args) {
		if(args.length == 0) {
			arg0.sendMessage(prefix + "§cDo /wm help");
		} else if(args[0].equalsIgnoreCase("tp") || args[0].equalsIgnoreCase("teleport") || args[0].equalsIgnoreCase("to") || args[0].equalsIgnoreCase("move")) {
	
			
			if(args.length >= 2 && args.length <= 3) {
				
				if(Server.getInstance().getLevelByName(args[1]) != null) {
					if(arg0 instanceof ConsoleCommandSender && args.length != 3) {
						arg0.sendMessage(prefix + "§cDo /worldmanager teleport [Level] (Player)"); return false;
					}
					Level level = Server.getInstance().getLevelByName(args[1]);
					Player player = null;
					if(args.length == 2) {player = ((Player) arg0);}
					else if(args.length == 3) {
						try {
							if(Server.getInstance().getPlayer(args[2]).isOnline()) {
								player = Server.getInstance().getPlayer(args[2]); } else player = ((Player) arg0);
						} catch (Exception e) {
							arg0.sendMessage(prefix + "§cThe player called §e" + args[2] + "§c is not online"); return false;
						}
					}
					if(arg0.hasPermission("worldmanager.teleport." + level.getFolderName()) || arg0.hasPermission("worldmanager.teleport") || arg0.hasPermission("worldmanager.admin")) {
					player.teleport(level.getSpawnLocation());
					level.addSound(level.getSpawnLocation(), Sound.MOB_SHULKER_TELEPORT, 1, 1, Collections.singletonList(player));
					} else {
						arg0.sendMessage(prefix + "§cYou dont have the permission to do this.."); return false;
					}
					if(!(arg0 instanceof ConsoleCommandSender)) {
						if(player.equals(arg0)) {
							player.sendMessage(prefix + "§7You got teleported to §8" + level.getFolderName());
						}
					} else arg0.sendMessage(prefix + "§7Teleported §8" + player.getName() + " §7 to §8" + level.getFolderName());
				} else arg0.sendMessage(prefix + "§cThe world called §e" + args[1] + "§c does not exist");
				
			} else {
				
				if((arg0.hasPermission("worldmanager.teleportui") || arg0.hasPermission("worldmanager.admin")) && args.length == 1 && !(arg0 instanceof ConsoleCommandSender)) {
					WorldManagerUI.openWorldTeleportUI((Player) arg0);
					return false;
				}
				
				if(arg0 instanceof ConsoleCommandSender && args.length != 3) {
					arg0.sendMessage(prefix + "§cDo /worldmanager teleport [Level] (Player)"); return false;
				}
			}	
			
			
		} else if (args[0].equalsIgnoreCase("generate") || args[0].equalsIgnoreCase("gen") || args[0].equalsIgnoreCase("create")) {
			if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.generate")) {
				
				arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.generate'"); return false;
				
			} else 
			if(args.length >= 2 && args.length <=4) {
				
				if(Server.getInstance().getLevelByName(args[1]) == null) {
					
					String name = args[1];
					Class<? extends Generator> generator = Generator.getGenerator("DEFAULT");
					long Seed = new Random().nextLong();
					if(args.length >= 3) {
						List<String> generators = new ArrayList<>();
						for(String s : Generator.getGeneratorList()) generators.add(s);
						if(generators.contains(args[2])) {
							generator = Generator.getGenerator(args[2]);
						} else {
							arg0.sendMessage(prefix + "§cThis generator does not exist"); return false;
						}
					}
					if(args.length == 4) {
						try {
							Seed = Long.parseLong(args[3]);
						} catch (Exception e) {
							arg0.sendMessage(prefix + "§cYour seed have to be numeric"); return false;
						}
					}
					try {
						Server.getInstance().generateLevel(name, Seed, generator);
						arg0.sendMessage(prefix + "§7The world §8" + name + "§7 got generated");
					} catch (Exception e) {
						arg0.sendMessage(prefix + "§cSomething went wrong during the world generation");
					}
					
					
				} else arg0.sendMessage(prefix + "§cThis world already exist..");
				
			} else {
				if((arg0.hasPermission("worldmanager.generateui") || arg0.hasPermission("worldmanager.admin")) && args.length == 1 && !(arg0 instanceof ConsoleCommandSender)) {
					WorldManagerUI.openWorldGenUI((Player) arg0);
					return false;
				}
				
				arg0.sendMessage(prefix + "§cDo /worldmanager generate [Name] (Generator) {Seed}");
			}
		} else if(args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("del") || args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("purge")) {
if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.delete")) {
	
	arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.delete'"); return false;
				
			} else {
				
				try {
					if(args.length == 2) {
						
						if(Server.getInstance().getLevelByName(args[1]) != null) {
							
							Server.getInstance().getLevelByName(args[1]).unload();
							File regionfolder = new File(Server.getInstance().getDataPath() + "worlds/" + args[1] + "/region");
							File worldfolder = new File(Server.getInstance().getDataPath() + "worlds/" + args[1]);
							FileUtils.deleteDirectoryContents(regionfolder);
							FileUtils.deleteDirectoryContents(worldfolder);
							worldfolder.delete();
							
							arg0.sendMessage(prefix + "§7Deleted the world §8" + args[1]);
						}else arg0.sendMessage(prefix + "§cThis world is not loaded or does not exist.");
						
					} else arg0.sendMessage(prefix + "§cDo /worldmanager delete [Name]");
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
			
			
		} else if(args[0].equalsIgnoreCase("load") || args[0].equalsIgnoreCase("import")) {
			
			if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.load")) {
				
				arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.load'"); return false;
				
			} else {
				
				if(args.length == 2) {
					if(Server.getInstance().getLevelByName(args[1]) == null) {
						
						if(new File(Server.getInstance().getDataPath() + "worlds/" + args[1] + "/level.dat").exists()) {
							
							Server.getInstance().loadLevel(args[1]);
							
							arg0.sendMessage(prefix + "§7The world §8" + args[1] + "§7 loaded successfully");
							
						} else arg0.sendMessage(prefix + "§cThis world does not exist");
						
						
					} else arg0.sendMessage(prefix + "§cThis world is already loaded.");
					
					
				} else arg0.sendMessage(prefix + "§cDo /worldmanager load [Name]");
				
			}
			
		} else if(args[0].equalsIgnoreCase("unload")) {
if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.unload")) {
				
	arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.unload'"); return false;
				
			} else {
				
				if(args.length == 2) {
	
					if(Server.getInstance().getLevelByName(args[1]) != null) {
						if(Server.getInstance().getDefaultLevel().equals(Server.getInstance().getLevelByName(args[1]))) {
							arg0.sendMessage(prefix + "§cYou can not unload the default world.");
							return false;
						}
							
						Server.getInstance().unloadLevel(Server.getInstance().getLevelByName(args[1]));
						arg0.sendMessage(prefix + "§7The world §8" + args[1] + "§7 got unloaded");
						
					} else arg0.sendMessage(prefix + "§cThis world is not loaded.");
					
				} else arg0.sendMessage(prefix + "§cDo /worldmanager unload [World]");
			}
		}else if(args[0].equalsIgnoreCase("reload")) {
			
if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.reload")) {
				
	arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.reload'"); return false;
				
			} else {
				
				if(args.length == 1 || args.length == 2) {
					
					if(args.length == 1) {
						for(Level l : Server.getInstance().getLevels().values()) {
							if(!l.equals(Server.getInstance().getDefaultLevel())) {
							String name = l.getName();
							l.unload();
							Server.getInstance().loadLevel(name);
							}
						}
						arg0.sendMessage(prefix + "§7All worlds have been reloaded."); 
					} else {
						if(Server.getInstance().getLevelByName(args[1]) != null) {
							String name = Server.getInstance().getLevelByName(args[1]).getName();
							Server.getInstance().getLevelByName(args[1]).unload();
							Server.getInstance().loadLevel(name);
							arg0.sendMessage(prefix + "§7The world §8" + args[1] + " §7has been reloaded");
						}else {arg0.sendMessage(prefix + "§cThis world does not exist."); return false;}
					}
					
				} else arg0.sendMessage(prefix + "§cDo /worldmanager reload [World]");
			}
		} else if(args[0].equalsIgnoreCase("list")) {
if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.list")) {
				
	arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.list'"); return false;
				
			} else {
				
				if(args.length == 1) {
					
					
					String worldstring = "";
					File folder = new File(Server.getInstance().getDataPath() + "worlds/");
					File[] folders = folder.listFiles();
					int worlds = 0;
					int loaded = 0;
					for(File f : folders) {
						if(!f.isDirectory()) continue;
						worlds++;
						if(Server.getInstance().getLevelByName(f.getName()) != null) {
							loaded++;
							worldstring = worldstring + "§8" + f.getName() + "§8 - §aLOADED §8(" + Server.getInstance().getLevelByName(f.getName()).getGenerator().getName() + ")\n";
						} else worldstring = worldstring + "§8" + f.getName() + "§8 - §cUNLOADED\n";
					}
					arg0.sendMessage(prefix + "§7List of all worlds (" + worlds +") [§a" + loaded + "§7/§c" + (worlds-loaded) + "§7]\n" + worldstring);
					
					
				} else arg0.sendMessage(prefix + "§cDo /worldmanager list");
				
				
				}
} else if(args[0].equalsIgnoreCase("setspawn")) {
	
	if(arg0 instanceof ConsoleCommandSender) {
		arg0.sendMessage(prefix + "§cYou can not do this in the console");
		return false;
	}
	
	if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.setspawn")) {
		
		arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.setspawn'"); return false;
		
	} else {
		
		if(args.length == 1) {
			Player p = ((Player) arg0);
			p.getLevel().setSpawnLocation(p.getLocation());
			arg0.sendMessage(prefix + "§7You set the spawnlocation of §8" + p.getLevel().getName() + "§7 to §8" + p.getLevelBlock().x + ", " + p.getLevelBlock().y + ", " + p.getLevelBlock().z);
			
		} else arg0.sendMessage(prefix + "§cDo /worldmanager setspawn");
		
		
		}
	
	
} else if(args[0].equalsIgnoreCase("settings")) {
	if(arg0 instanceof ConsoleCommandSender) {
		arg0.sendMessage(prefix + "§cThis can only be done ingame.");
		return false;
	}
if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.settings")) {
		
	arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.settings'"); return false;
		
	} else {
		
		if(args.length == 1 || args.length == 2) {
			
			Level l = ((Player) arg0).getLevel();
			if(args.length == 2) {
				if(Server.getInstance().getLevelByName(args[1]) != null) {
					l = Server.getInstance().getLevelByName(args[1]);
				}else {arg0.sendMessage(prefix + "§cThis world does not exist."); return false;}
			}
			World w = Cache.getWorld(l);
			List<String> worlds = new ArrayList<>();
			for(Level level : Server.getInstance().getLevels().values()) worlds.add(level.getName());
			FormWindowCustom fw = new FormWindowCustom("§3WorldSettings - " + l.getFolderName());
			fw.addElement(new ElementToggle("Load On Start", w.doesLoadOnStart()));
			fw.addElement(new ElementToggle("Use Own Gamemode", w.isUsingOwnGamemode()));
			fw.addElement(new ElementDropdown("Gamemode", Arrays.asList("Survival", "Creative", "Adventure", "Spectator"), w.getOwnGamemode()));
			fw.addElement(new ElementToggle("Fly", w.isFlyAllowed()));
		    fw.addElement(new ElementDropdown("Respawn World", worlds, (worlds.contains(w.getRespawnWorld()) ? worlds.indexOf(w.getRespawnWorld()) : worlds.indexOf(l.getName()))));
			fw.addElement(new ElementToggle("Protected", w.isProtected()));
			fw.addElement(new ElementInput("Notepad", "Sth. to remember like coords.", w.getNote()));
			
			((Player) arg0).showFormWindow(fw);
		} else arg0.sendMessage(prefix + "§cDo /worldmanager settings [World]");
		
		}
	
}
else if(args[0].equalsIgnoreCase("info")) {
	
	if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.info")) {
		
		arg0.sendMessage(prefix + "§cYou dont have the permission to do this.."); return false;		
		
	} else {
		
		if(arg0 instanceof ConsoleCommandSender && args.length == 1) {
			arg0.sendMessage(prefix + "§cDo /worldmanager info [World]"); return false;		
		}
		
		Level l = null;
		if(args.length == 1) {
			l = ((Player) arg0).getLevel();
		} else if(args.length == 2) {
			if(Server.getInstance().getLevelByName(args[1]) != null) {
				l = Server.getInstance().getLevelByName(args[1]);
			}else { arg0.sendMessage(prefix + "§cThis world does not exist."); return false;		}
		} else arg0.sendMessage(prefix + "§cDo /worldmanager info [World]");
		World world = Cache.getWorld(l);
		String str = "§7Name: §8" + l.getFolderName() +"\n"
				+ "§7Spawn: §8" + l.getSpawnLocation().x + ", " + l.getSpawnLocation().y + ", " + l.getSpawnLocation().z + "\n"
				+ "§7Generator: §8" + l.getGenerator().getName() +"\n"
				+ "§7Chunks: §8" + l.getChunks().size() + "\n"
				+ "§7Dimension: §8" + ((l.getDimension() == 0) ? "Overworld" : (l.getDimension() == 1 ? "Nether" : "End")) + "\n"
				+ "§7Seed: §8" + l.getSeed() + "\n"
				+ "§7Players: §8" + l.getPlayers().size() +"\n"
				+ "§7Note: §8" + (world.getNote().equals("") ? "§cNone" : world.getNote());
		arg0.sendMessage(prefix + "§7Information about §8" + l.getFolderName() + "\n" + str);
		
		
		
		}
	
	
}else if(args[0].equalsIgnoreCase("setseed") || args[0].equalsIgnoreCase("reseed")) {
if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.setseed")) {
		
	arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.setseed'"); return false;
		
	} else {
		
		if(args.length == 3) {
			
			if(Server.getInstance().getLevelByName(args[1]) != null) {
				try {
					int seed = Integer.parseInt(args[2]);
					Server.getInstance().getLevelByName(args[1]).setSeed(seed);
					arg0.sendMessage(prefix + "§7Seed of §8" + Server.getInstance().getLevelByName(args[1]).getName() + " §7set to §8" + args[2]); 
				} catch (Exception e) {
					arg0.sendMessage(prefix + "§cYour seed have to be a integer."); 
				}
				
			} else arg0.sendMessage(prefix + "§cThis world does not exist."); 
			
			
		} else arg0.sendMessage(prefix + "§cDo /worldmanager reseed [World] (Seed)");
	}
	
} else if(args[0].equalsIgnoreCase("rename") || args[0].equalsIgnoreCase("changename")) {
if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.rename")) {
		
	arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.rename'"); return false;
		
	} else {
		
		if(args.length == 3) {
			
			if(Server.getInstance().getLevelByName(args[1]) != null) {
				if(new File(Server.getInstance().getDataPath() + "worlds/" + args[2]).exists()) {
					arg0.sendMessage(prefix + "§cThis name is already taken!");
					return false;
				}
				if(Server.getInstance().getDefaultLevel() == Server.getInstance().getLevelByName(args[1])) {
					arg0.sendMessage(prefix + "§cYou can't rename the default world");
					return false;
				}
				Server.getInstance().getLevelByName(args[1]).unload();
				new File(Server.getInstance().getDataPath() + "worlds/" + args[1]).renameTo(new File(Server.getInstance().getDataPath() + "worlds/" + args[2]));
				Server.getInstance().loadLevel(args[2]);
				arg0.sendMessage(prefix + "§7Renaming §8" + args[1] + " §7to §8" +args[2] + " §7was successful.");
			} else arg0.sendMessage(prefix + "§cThis world does not exist."); 
			
			
		} else arg0.sendMessage(prefix + "§cDo /worldmanager rename [Old Name] (New Name)");
	}
	
} else if(args[0].equalsIgnoreCase("copy") || args[0].equalsIgnoreCase("duplicate") || args[0].equalsIgnoreCase("dupe") || args[0].equalsIgnoreCase("replicate")) {
	
	if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.copy")) {
		
		arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.copy'"); return false;
		
	} else {
		
		if(args.length == 2 || args.length == 3) {
		
			if(Server.getInstance().getLevelByName(args[1]) != null) {
				String name = "CopyOf" + Server.getInstance().getLevelByName(args[1]).getName();
				if(args.length == 3) name = args[2];
				new File(Server.getInstance().getDataPath() + "worlds/" + args[1]).mkdirs();
				FileUtils.copyDirectoryContents(new File(Server.getInstance().getDataPath() + "worlds/" + Server.getInstance().getLevelByName(args[1]).getName()),new File( Server.getInstance().getDataPath() + "worlds/" + name));
				Server.getInstance().loadLevel(name);
				arg0.sendMessage(prefix + "§7Created a copy of §8" + Server.getInstance().getLevelByName(args[1]).getName() + " §7called §8" + args[2] );
				
			} else arg0.sendMessage(prefix + "§cThis world does not exist."); 
			
		} else arg0.sendMessage(prefix + "§cDo /worldmanager copy [World] (Name of Copy)"); 
		
	}
	
} else if(args[0].equalsIgnoreCase("regenerate") || args[0].equalsIgnoreCase("reg") || args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("regen")) {
if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.regenerate")) {
		 
	arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.regenerate'"); return false;
		
	} else {
		Level l = null;
		if(args.length == 1) {
			if(arg0 instanceof Player) {
				l = ((Player) arg0).getLevel();
			}else arg0.sendMessage(prefix + "§cDo /worldmanager regenerate [World]");
		}else
		if(args.length == 2) {
			if(Server.getInstance().getLevelByName(args[1]) != null) {
				
				l = Server.getInstance().getLevelByName(args[1]);
				
			} else {arg0.sendMessage(prefix + "§cThis world does not exist."); return false;}
			long seed = l.getSeed();
			Generator generator = l.getGenerator();
			String name = l.getFolderName();
			l.unload();
			File regionfolder = new File(Server.getInstance().getDataPath() + "worlds/" + args[1] + "/region");
			File worldfolder = new File(Server.getInstance().getDataPath() + "worlds/" + args[1]);
			FileUtils.deleteDirectoryContents(regionfolder);
			FileUtils.deleteDirectoryContents(worldfolder);
			worldfolder.delete();
			Server.getInstance().generateLevel(name, seed, generator.getClass());
			arg0.sendMessage(prefix + "§7World §8" + name + " §7regenerated"); 
			
		} else arg0.sendMessage(prefix + "§cDo /worldmanager regenerate [World]");
		}
}else if(args[0].equalsIgnoreCase("killentitys") || args[0].equalsIgnoreCase("clearlag") || args[0].equalsIgnoreCase("entkill") || args[0].equalsIgnoreCase("clag")) {
if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.killentitys") && !arg0.hasPermission("clearlag")) {
		
	arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager.clearlag'"); return false;
		
	} else {
		Level l = null;
		if(args.length == 1) {
			if(arg0 instanceof Player) {
				l = ((Player) arg0).getLevel();
			}else arg0.sendMessage(prefix + "§cDo /worldmanager killentitys [World]");
		} else
		if(args.length == 2) {
			if(Server.getInstance().getLevelByName(args[1]) != null) {
				
				l = Server.getInstance().getLevelByName(args[1]);
				
			} else {arg0.sendMessage(prefix + "§cThis world does not exist."); return false;} 
			
			
		} else arg0.sendMessage(prefix + "§cDo /worldmanager killentitys [World]");
		for(Entity entity : l.getEntities()) {
			if(!(entity instanceof Player)) {
				entity.close();
			}
		}
		arg0.sendMessage(prefix + "§7Killed all entitys in §8" + l.getFolderName()); 
		}
}
else if(args[0].equalsIgnoreCase("gamerule") || args[0].equalsIgnoreCase("gamerules")) {
	
	if(!(arg0 instanceof Player)) {
		arg0.sendMessage(prefix + "§cThis command can only be used ingame.");
		return false;
	}
	
if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.gamerule") && !arg0.hasPermission("worldmanager.gamerule." + args[1])) {arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + (args.length == 2 ? args[1] : "" ) + "'"); return false; }

	Level l = null;
	if(args.length == 1) {
			l = ((Player) arg0).getLevel();
	} else
	if(args.length == 2) {
		if(Server.getInstance().getLevelByName(args[1]) != null) {
			
			l = Server.getInstance().getLevelByName(args[1]);
			
		} else {arg0.sendMessage(prefix + "§cThis world does not exist."); return false;} 
		
		
	} else {arg0.sendMessage(prefix + "§cDo /worldmanager gamerule [World]"); return false;}
	
	FormWindowCustom c = new FormWindowCustom("§3WorldGamerules - " + l.getFolderName());
	for(GameRule r : GameRule.values()) {
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
	
} else if(args[0].equalsIgnoreCase("default") || args[0].equalsIgnoreCase("setdefault")) {
	if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.default") && !arg0.hasPermission("worldmanager.setdefault")) {arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'"); return false; }
	
	if(args.length == 1) {
			arg0.sendMessage(prefix + "§7The default world is §8" + Server.getInstance().getDefaultLevel().getName() + ".");
	} else
	if(args.length == 2) {
		if(Server.getInstance().getLevelByName(args[1]) != null) {
			if(Server.getInstance().getDefaultLevel() == Server.getInstance().getLevelByName(args[1])) {
				arg0.sendMessage(prefix + "§cThis world already is the default world!");
				return false;
			}
			Server.getInstance().setDefaultLevel(Server.getInstance().getLevelByName(args[1]));
			
			Config serverconfig = Server.getInstance().getProperties();
			serverconfig.set("level-name", args[1]);
			serverconfig.save();
			
			arg0.sendMessage(prefix + "§7The world §8" +args[1] + " §7is now the default world.");
			
		} else {arg0.sendMessage(prefix + "§cThis world does not exist."); return false;} 
		
		
	} else {arg0.sendMessage(prefix + "§cDo /worldmanager default [World]"); return false;}
	
}
else if(args[0].equalsIgnoreCase("save")) {
	
	if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.save")) {arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'"); return false; }
	Level l = null;
	if(args.length == 1) {
		if(!(arg0 instanceof Player)) {
			arg0.sendMessage(prefix + "§cDo /worldmanager save [World]");
			return false;
		} else l = ((Player) arg0).getLevel();
	} else
	if(args.length == 2) {
		if(Server.getInstance().getLevelByName(args[1]) != null) {
			
			l = Server.getInstance().getLevelByName(args[1]);
			
		} else {arg0.sendMessage(prefix + "§cThis world does not exist."); return false;} 
		
		
	} else {arg0.sendMessage(prefix + "§cDo /worldmanager save [World]"); return false;}
	
	l.save(true);
	arg0.sendMessage(prefix + "§7The world §e" + l.getFolderName() + " §7saved successfully.");
	
} else if(args[0].equalsIgnoreCase("setbiome") || args[0].equalsIgnoreCase("biome")) {
	
	if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.setbiome") && !arg0.hasPermission("worldmanager.biome")) {arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'"); return false;	}
	if(arg0 instanceof Player) {
		if(args.length == 2) {
			if(args[1].equalsIgnoreCase("list")) {
				String message = prefix + "§7A list of all biomes:§8\n";
				for(EnumBiome b : EnumBiome.values()) message = message  + b.name() + "§7, §8";
				arg0.sendMessage(message);
				return false;
			}
			Player p = (Player) arg0;
			Level l = p.level;
			Biome b = Biome.getBiome(args[1]);
			if(b == null) {
				p.sendMessage(prefix + "§cThis Biome does not exist. Do /worldmanager biome list");
				return false;
			}
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					for(FullChunk  c : l.getChunks().values()) {
						for(int x = 0; x < 16; x++) {
							for(int z = 0; z < 16; z++) {
								c.setBiome(x, z, b);
							}
						}				
					}
					
				}
			});
			thread.start();
			arg0.sendMessage(prefix + "§aSuccessfully changed the biome of this world. Please rejoin!");
		} else arg0.sendMessage(prefix + "§cDo /worldmanager setbiome [Biome]");
	} else arg0.sendMessage(prefix + "§cThis command can only be executed by a player!");
	
	
} 

		
		
else if(args[0].equalsIgnoreCase("version")) {
	
	arg0.sendMessage(prefix + "You are using WorldManager v" + WorldManager.plugin.getDescription().getVersion()); 
	
} else if (args[0].equalsIgnoreCase("sync")) {
	
	if(arg0.hasPermission("worldmanager.admin") || arg0.hasPermission("worldmanager.sync")) {
		
		if(arg0 instanceof Player) {

			if(args.length == 2) {
				
				if(Server.getInstance().getLevelByName(args[1]) != null) {
					
					FormWindowCustom fw = new FormWindowCustom("§3WorldSync - " + args[1]);
					List<Level> level = new ArrayList<>(Server.getInstance().getLevels().values());
					level.remove(Server.getInstance().getLevelByName(args[1]));
					Events.levels = level;
					for(Level l : level) {
						fw.addElement(new ElementToggle(l.getFolderName(), false));
					}
					arg0.sendMessage(prefix + "§7Select the worlds you want to sync with §e" + args[1] + "§7. This includes WorldSettings and Gamerules.");
					((Player) arg0).showFormWindow(fw);
					
				} else {arg0.sendMessage(prefix + "§cThis world does not exist"); return false;} 
				
			} else arg0.sendMessage(prefix + "§cDo /worldmanager sync [templateWorld]");
			
		} else arg0.sendMessage(prefix + "§cThis command can only be executed ingame.");
		
	} else arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'"); return false;	
	
	
} 

		
else if(args[0].equalsIgnoreCase("addon") || args[0].equalsIgnoreCase("installAddon") || args[0].equalsIgnoreCase("implement") || args[0].equalsIgnoreCase("addons")) {
	
	if(arg0 instanceof Player && arg0.hasPermission("worldmanager.addon")) {
		Addons.showAddonUI((Player) arg0);
		
	} else arg0.sendMessage(prefix + "§cYou are lacking the permission 'worldmanager.addon'");

}
		
else if(args[0].equalsIgnoreCase("locatebiome")) {
	
if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.locatebiome")) {
		
	arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'"); return false;	
		
	} else {
		
		if(arg0 instanceof Player) {
			
			if(args.length >= 2 && args.length <=4) {
				
				try {
					Biome biome = Biome.getBiome(args[1]);
					if(biome != null) {
						Player p = (Player) arg0;
						if(threads.containsKey(p.getName())) {
							p.sendMessage(prefix + "§cYou already search for a biome."); 
							return false;
						}
						Level l= p.getLevel();
						Thread t = new Thread(new Runnable() {
							
							@Override
							public void run() {
								int range = 1000;
								if(args.length >= 3) {
									try {
										range = Integer.parseInt(args[2]);
									} catch (Exception e) {
										p.sendMessage(prefix + "§cYour range has to be a number.");
										return;
									}
								}
								int radiusp = range/2;
								int radiusn = -range/2;
								boolean tp = false;
								if(args.length == 4 && args[3].equalsIgnoreCase("true")) tp = true;
								int step = 0;
								arg0.sendMessage(prefix + "§ePlease wait. It may take some time.");
								long millis = System.currentTimeMillis();
								boolean found = false;
								for(int x = radiusn + p.getChunkX(); x <= radiusp + p.getChunkX(); x++) {
									step++;
									for(int z = radiusn + p.getChunkZ(); z <= radiusp + p.getChunkZ(); z++) {
									try {
										boolean generated = false;
										
										if(!l.isChunkGenerated(x, z)) {
											generated = true;
											l.generateChunk(x, z);
										}
									if(biome.getId() == l.getBiomeId(x*16,z*16)) {
										found = true;	
										arg0.sendMessage(prefix + "§8" + biome.getName() + " §7found at §aX: " + x*16 +", Y: " + z*16 + " §e(Took " + ((System.currentTimeMillis() - millis) /1000f) + " Seconds)");
										
										if(tp) {
											Location loc = new Location(x * 16, l.getChunk(x, z).getHighestBlockAt(0, 0)+1, z * 16);
											p.teleport(loc);
										}
										threads.remove(p.getName());
										return;
									} else {
										if(generated) l.unloadChunk(x, z);
										if(z%250==0) p.sendActionBar((generated ? "§a" : "§e") +"Checking Chunk X: " + x + ", Z: " + z + " (" + (int)(((float)step/(float)range) * 100f) +  "%)");		
									}
									} catch (Exception e) {
										p.sendActionBar("§cCouldn't check Chunk X: " + x + ", Z: " + z);
									}}}
								if(!found) arg0.sendMessage(prefix+ "§cCouldn't find a " + biome.getName() + " Biome."); 
								//threads.get(p.getName()).stop();
								threads.remove(p.getName());
								
							}
						});
						threads.put(p.getName(), t);
						threads.get(p.getName()).start();
						
						
					} else arg0.sendMessage(prefix + "§cThis Biome does not exist. Do /worldmanager biome list");
				} catch (Exception e) {
					arg0.sendMessage(prefix + "§cThis Biome does not exist. Do /worldmanager biome list");
				}
				
				
				
			} else arg0.sendMessage(prefix + "§cDo /worldmanager locatebiome [biome]");
			
			
		} else arg0.sendMessage(prefix + "§cThis command can only be executed ingame.");
		
	}
} else if(args[0].equalsIgnoreCase("spawn")) {
	if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.spawn")) {
		
		arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'"); return false;		
		
	} else {
		
		if(arg0 instanceof Player) {
			
			((Player) arg0).teleport(((Player) arg0).getLevel().getSafeSpawn());
			arg0.sendMessage(prefix + "§7Successfully teleported to spawn.");
			
		} else arg0.sendMessage(prefix + "§cThis command can only be executed ingame.");
		
	}
	
} else if(args[0].equalsIgnoreCase("status")) {
	
	if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.status")) {
		
		arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'"); return false;		
		
	} else {
		
		String message = "§l§3WorldManager §eStatus\n§r";
		message += ("§ePlugin Version: §7" + WorldManager.plugin.getDescription().getVersion() + "\n");
		message += ("§eCached Worlds: §7" + Cache.getWorldCache().size() + "\n");
		message += ("§eCached Players: §7" + Cache.getCachedPlayerGamemodes() + "\n");
		message += "§eWorlds: §7";
		for(World w : Cache.getWorldCache()) message += w.getAsLevel().getName() + ", ";
		arg0.sendMessage(message);
		
		
	}
	
}
		
else if(args[0].equalsIgnoreCase("help")) {
if(!arg0.hasPermission("worldmanager.admin") && !arg0.hasPermission("worldmanager.help")) {
		
	arg0.sendMessage(prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'"); return false;	
		
	} else {
		
		if(args.length == 1) {

			arg0.sendMessage("§eWorldmanager Help\n"
					+ "§3The Maincommand is /worldmanager. But you can also use §c/wm, /mw, /mv, /levelmanager, /world §3and §c/lm§3\n"
					+ "§c/worldmanager teleport [World] (Player)§4* §3teleports you or the pointed player in this world. Instead of teleport, you can use §ctp §3and §cto\n"
					+ "§c/worldmanager generate [World] (Generator)§4*§c {Seed}§4* §3generate a new world. Instead of generate you can use §cgen §3or §ccreate\n"
					+ "§c/worldmanager delete [World] §3deletes the world. Instead of delete you can use §cdel, remove §3or §cpurge\n"
					+ "§c/worldmanager load [World] §3loads the world while §c/worldmanager unload [World]§3 unloads a world\n"
					+ "§c/worldmanager list §3shows you a list of every world\n"
					+ "§c/worldmanager reload [World]§4*§3 reloads a world.\n"
					+ "§c/worldmanager rename [World] (New Worldname) §3renames a world\n"
					+ "§c/worldmanager copy [World] (Name of the Copy)§4*§3 will copy a world. Be careful. You can overwrite other worlds.\n"
					+ "§c/worldmanager setspawn §3will set the worldspawn\n"
					+ "§c/worldmanager settings [World]§4* §3opens a FormUI with world-specific settings\n"
					+ "§c/worldmanager regenerate [World]§4* §3regenerates the world. You also can use §creg, regen §3 or §creset §3instead of §cregenerate\n"
					+ "§c/worldmanager setseed [World] (Seed) §3Change the seed of your world. Also works with §creseed\n"
					+ "§c/worldmanager killentitys [World]§4*§3 This kills all entitys and lying items. You can use §cclearlag, entkill\n"
					+ "§c/worldmanager info [World]§4*§3 shows you informations about this world\n"
					+ "§c/worldmanager gamerule [World]§4*§3 opens an UI to manage the gamerules\n"
					+ "§c/worldmanager setbiome [Biome]§3 let you change the biome of the world. Every loaded chunk is affected.\n"
					+ "§c/worldmanager version §3shows you your current version of worldmanager while\n"
					+ "§c/worldmanager sync (templateworld) §3opens and UI to sync gamerules and settings with this world\n"
					+ "§c/worldmanager locatebiome (biome) [range]* {teleport}* §3scans the nearby world for the chosen biome.\n"
					+ "§c/worldmanager spawn §3teleports you to the spawn of your current world.\n"
					+ "§c/worldmanager addons §3will open the Addon UI to extend your Server\n"
					+ "§c/worldmanager default §3shows you the default level. You can change it if you write the worldname too."
					+ "§4§l* optional");
			
			
		} else arg0.sendMessage(prefix + "§cDo /worldmanager help"); return false;	
		
	}
	
}else arg0.sendMessage(prefix + "§cDo /worldmanager help");
		
		
		return false;
	}

}
