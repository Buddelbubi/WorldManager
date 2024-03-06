package de.buddelbubi.commands.subcommand;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;
import de.buddelbubi.WorldManager;

import java.util.LinkedList;

public class HelpCommand extends SubCommand{

	public HelpCommand() {
		super("help");
		this.setAliases(new String[] {
				"help"
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
		
		  if (!sender.hasPermission("worldmanager.admin") && !sender.hasPermission("worldmanager.help")) {

			  sender.sendMessage(WorldManager.prefix + "§cYou are lacking the permission §e'worldmanager." + args[0] + "'.");
			 return false;

		  } else {

			 if (args.length == 1) {

				 sender.sendMessage("§eWorldmanager Help\n" +
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

			 } else sender.sendMessage(WorldManager.prefix + "§cDo /worldmanager help");
			 return false;

		  }
		
	}

}
