package de.buddelbubi.Events;

import java.io.File;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.level.LevelLoadEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerGameModeChangeEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.GameRules;
import cn.nukkit.level.GameRules.Type;
import cn.nukkit.level.Level;
import cn.nukkit.utils.Config;
import de.buddelbubi.Commands.WorldManagerCommand;
import de.buddelbubi.api.World;
import de.buddelbubi.api.WorldManagerOption;
import de.buddelbubi.utils.Cache;

public class Events implements Listener {

	@EventHandler
	public void onLevelLoad(LevelLoadEvent e) {

		File file = new File(Server.getInstance().getDataPath() + "worlds/" + e.getLevel().getFolderName(), "config.yml");

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		Config c = new Config(file);

		if (!c.exists("version")) c.set("version", 0);
		if (!c.exists("LoadOnStart")) c.set("LoadOnStart", true);
		if (!c.exists("UseOwnGamemode")) c.set("UseOwnGamemode", false);
		if (!c.exists("Gamemode")) c.set("Gamemode", Server.getInstance().getDefaultGamemode());
		if (!c.exists("fly")) c.set("fly", false);
		if (!c.exists("respawnworld")) c.set("respawnworld", e.getLevel().getName());
		if (!c.exists("thumbnail")) c.set("thumbnail", "path::" + ((e.getLevel().getDimension() == 0) ? "textures/blocks/grass_side_carried.png" : (e.getLevel().getDimension() == 1) ? "textures/blocks/netherrack.png" : "textures/blocks/end_stone.png"));
		if (!c.exists("protected")) c.set("protected", false);
		if (!c.exists("note")) c.set("note", "");
		for (WorldManagerOption option : WorldManagerOption.getCustomOptions()) {
			if (!c.exists(option.getKey())) {
				c.set(option.getKey(), option.getValue());
			}
		}

		c.save();

		Cache.initWorld(e.getLevel());

	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e) {

		World world = Cache.getWorld(e.getTo().getLevel());
		if (e.getPlayer().hasPermission("worldmanager.deny." + e.getTo().getLevel()) && !e.getPlayer().hasPermission("*")) {
			e.setCancelled(true);
			return;
		}

		if (!e.getTo().getLevel().equals(e.getFrom().getLevel())) {

			if (world.isUsingOwnGamemode()) {
				Cache.gamemodes.put(e.getPlayer().getName(), (byte) e.getPlayer().getGamemode());
				e.getPlayer().setGamemode(world.getOwnGamemode());

			} else if (Cache.gamemodes.containsKey(e.getPlayer().getName())) {
				e.getPlayer().setGamemode(Cache.gamemodes.get(e.getPlayer().getName()));
				Cache.gamemodes.remove(e.getPlayer().getName());
			}

			if (e.getPlayer().getGamemode() != 1) e.getPlayer().setAllowFlight(world.isFlyAllowed());

		}

	}

	HashMap < String, String > respawnworld = new HashMap < > ();

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {

		respawnworld.put(e.getEntity().getName(), Cache.getWorld(e.getEntity().getLevel()).getRespawnWorld());

	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerRespawnEvent e) {
		World world = Cache.getWorld(e.getRespawnPosition().getLevel());
		if (e.isFirstSpawn() && e.getPlayer().getGamemode() != 1) e.getPlayer().setAllowFlight(world.isFlyAllowed());

		if (respawnworld.containsKey(e.getPlayer().getName())) {
			Level l = Server.getInstance().getLevelByName(respawnworld.get(e.getPlayer().getName()));

			if (l != null) {
				if (e.getPlayer().getSpawn().getLevel() == l) {
					e.setRespawnPosition(e.getPlayer().getSpawn());
				} else e.setRespawnPosition(l.getSpawnLocation());
			}
			respawnworld.remove(e.getPlayer().getName());
		}

		if (world.isUsingOwnGamemode()) {
			Cache.gamemodes.put(e.getPlayer().getName(), (byte) e.getPlayer().getGamemode());
			e.getPlayer().setGamemode(world.getOwnGamemode());

		} else if (Cache.gamemodes.containsKey(e.getPlayer().getName())) {
			e.getPlayer().setGamemode(Cache.gamemodes.get(e.getPlayer().getName()));
			Cache.gamemodes.remove(e.getPlayer().getName());
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onForm(PlayerFormRespondedEvent e) {

		if (e.getWindow() instanceof FormWindowCustom) {
			FormWindowCustom fw = (FormWindowCustom) e.getWindow();

			if (fw.getTitle().startsWith("§3WorldSettings")) {
				String level = fw.getTitle().replace("§3WorldSettings - ", "");
				if (fw.getResponse() == null) {
					e.getPlayer().sendMessage(WorldManagerCommand.prefix + "§7Didn't save settings for §8" + level);
					return;
				}

				Config c = new Config(new File(Server.getInstance().getDataPath() + "/worlds/" + level, "config.yml"));
				c.set("LoadOnStart", fw.getResponse().getResponse(0));
				c.set("UseOwnGamemode", fw.getResponse().getResponse(1));
				c.set("Gamemode", fw.getResponse().getDropdownResponse(2).getElementID());
				c.set("fly", fw.getResponse().getResponse(3));
				c.set("respawnworld", fw.getResponse().getResponse(4));
				c.set("protected", fw.getResponse().getResponse(5));
				c.set("note", fw.getResponse().getResponse(6));
				int index = 7;
				for (WorldManagerOption o : WorldManagerOption.getCustomOptions()) {
					c.set(o.getKey(), fw.getResponse().getResponse(index));
					index++;
				}
				c.save();

				for (Player p: Server.getInstance().getLevelByName(level).getPlayers().values()) {
					if (p.getGamemode() != 1) {
						p.setAllowFlight(c.getBoolean("fly"));
					}
				}

				if (c.getBoolean("UseOwnGamemode"))
					for (Player p: Server.getInstance().getLevelByName(level).getPlayers().values())
						if (p.getGamemode() != c.getInt("Gamemode")) {
							Cache.gamemodes.put(p.getName(), (byte) p.getGamemode());
							p.setGamemode(c.getInt("Gamemode"));
						}

				e.getPlayer().sendMessage(WorldManagerCommand.prefix + "§7Saved settings for §8" + level);
				Cache.initWorld(level);

			} else if (fw.getTitle().startsWith("§3WorldGamerules")) {
				Level level = Server.getInstance().getLevelByName(fw.getTitle().replace("§3WorldGamerules - ", ""));
				GameRules grs = level.gameRules;
				int i = 0;
				if (fw.getResponse() == null) {
					e.getPlayer().sendMessage(WorldManagerCommand.prefix + "§7Didn't save gamerules for §8" + level.getName());
					return;
				}
				for (GameRule r : GameRule.values()) {
					if (fw.getResponse().getResponses().get(i) instanceof Boolean) {

						grs.setGameRule(r, fw.getResponse().getToggleResponse(i));
					} else if (level.getGameRules().getGameRuleType(r) == Type.INTEGER) {
						try {
							grs.setGameRule(r, Integer.valueOf(fw.getResponse().getInputResponse(i)));
						} catch (Exception e2) {
							e.getPlayer().sendMessage(WorldManagerCommand.prefix + "§cError with the gamerule §8" + r.getName() + "§c. It will remain on §8" + level.getGameRules().getInteger(r));
						}
					}
					i++;
				}
				level.gameRules = grs;
				e.getPlayer().sendMessage(WorldManagerCommand.prefix + "§7Saved gamerules for §8" + level.getName());
			} else if (fw.getTitle().startsWith("§3WorldSync")) {

				if (fw.getResponse() == null) {
					e.getPlayer().sendMessage(WorldManagerCommand.prefix + "§7Didn't synced settings and gamerules for your selection.");
					return;
				}

				try {

					Level level = Server.getInstance().getLevelByName(fw.getTitle().split(" - ")[1]);
					Config c = Cache.getWorld(level).getConfig();
					int i = 1;
					for (Level l : Server.getInstance().getLevels().values()) {
						if(l== level) continue;
						if (fw.getResponse().getToggleResponse(i)) {

							Config c2 = Cache.getWorld(l).getConfig();
							c2.setAll((LinkedHashMap < String, Object > ) c.getAll());
							c2.save();
							l.gameRules = level.gameRules;
							Cache.initWorld(l);
						}
						i++;
					}
					e.getPlayer().sendMessage(WorldManagerCommand.prefix + "§7Synced all selected worlds with §8" + level.getName() + ".");

				} catch (Exception e2) {
					e.getPlayer().sendMessage(WorldManagerCommand.prefix + "§cSomething went wrong while syncing your worlds.");
					return;
				}

			}
		}

	}
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		World world = Cache.getWorld(e.getEntity().getLevel());
		try {
			if (!(e.getEntity() instanceof Player) && e.getDamager() instanceof Player && world.isProtected()) {
				e.setCancelled(true);
			}
		} catch (Exception e2) {}

	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if (e == null) return;
		try {
			if (Cache.getWorld(e.getBlock().getLevel()).isProtected()) e.setCancelled(true);
		} catch (Exception e2) {

		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if (e == null) return;
		try {
			if (Cache.getWorld(e.getBlock().getLevel()).isProtected()) e.setCancelled(true);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {

		if (Cache.getWorld(e.getBlock().getLevel()).isProtected()) {
			if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
				e.setCancelled(true);
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void on(PlayerGameModeChangeEvent e) {
		if (e.getNewGamemode() == 0 || e.getNewGamemode() == 2) {
			e.getPlayer().setAllowFlight(Cache.getWorld(e.getPlayer().getLevel()).isFlyAllowed());
		}
	}

}