
![logo](https://cloudburstmc.org/attachments/1618588553800-png.3459/)


First of all I should show you all features:


    Teleport to another world
    World-Teleport-UI
    World-Generation UI
    generate new worlds (every generator and seed is possible)
    Delete a world
    list all worlds. Loaded and unloaded worlds
    load and unload and even reload worlds
    rename worlds
    copy/duplicate/backup worlds
    set the worldspawn
    regenerate the whole world
    change the seed of a existing world
    world infos like seed, generator, amount of players..
    per world gamemode (toggleable)
    UI Settings
    Change the default world
    World Load on startup
    World Restrictions via permissions
    World Clearlagg (Kill every entity in a world)
    Change the biome for every loaded chunk
    Change gamerules via an UI
    Enable / Disable the Nukkit Build-In movementcheck per world. (Addon)
    Add notes to your world.
    Protect your world (Deny build, break and interacts)
    Fast and easy gamerule syncing
    Auto-Updater
    Easy extendable! Ingame addons ui with tons of useful stuff
    Set a playerlimit per world*
    Per World Chat*
    World Whitelist*
    World Join Messages*
    World Teleportation Signs*
    Generators and Populators like The End*



       * Addon is required! Not everyone needs those features. Thats why they were seperated from the core so people do not have to download stuff they dont need and save performance for other stuff.




Commands and permissions:

WorldManager provides alot of features.. So dont get confused..

Basicly if you have the permission worldmanager.admin you can do anything.

But lets get started with the main command: Its /worldmanager. But you can also use /wm, /mw, /levelmanager, /lm and even /mv for multiverse2 fans of you ;)


The permission is always worldmanager.[MainSubcommand]. 

For example, the command /wm generate [World] (Generator) {Seed} has the permission worldmanager.generate. worldmanager.gen wont work. It have to be the main subcommand.

You can see the command and all the aliases when you execute /wm help ingame or via console.

You also can prevent players from entering a world. Just give them the permission worldmanager.deny.[World]

If you want to give a player access to /worldmanager teleport but only to a specific world, just give him the permission worldmanager.teleport.[World] 

To open the teleport ui, the player needs the permisson worldmanager.teleportui.

For the generation ui, they need the permission worldmanager.generationui.

You can change the max players per world in your world settings. If the limit is reached, you can't enter the world unless you have the permission worldmanager.enterfullworlds

[SPOILER="Commands"][/SPOILER][SPOILER="Commands"][/spoiler][SPOILER="Commands"]

/worldmanager teleport [World] (Player)* teleports you or the pointed player in this world. Instead of teleport, you can use tp and to

/worldmanager generate [World] (Generator)* {Seed}* generate a new world. Instead of "generate" you can use gen or create

/worldmanager delete [World] deletes the world. Instead of "delete" you can use del, remove or purge

/worldmanager list lists all worlds.

/worldmanager load [World] loads the world while /worldmanager unload [World] unloads a world

/worldmanager reload [World]* reloads a world.

/worldmanager rename [World] (New Worldname) renames a world

/worldmanager copy [World] (Name of the Copy)* will copy a world. Be careful. You can overwrite other worlds.

/worldmanager setspawn will set the worldspawn

/worldmanager settings [World]* opens a FormUI with world-specific settings

/worldmanager regenerate [World]* regenerates the world. You also can use reg  or reset instead of "regenerate"

/worldmanager setseed [World] (Seed) Change the seed of your world. Also works with reseed

/worldmanager info [World]* shows you informations about this world

/worldmanager setbiome [Biome] changes the biome of every loaded chunk in the current world.

/worldmanager gamerule [World]* opens an UI to manage the gamerules

/worldmanager version tells you your current WorldManager version.

/worldmanager killentitys [World] kills all entities in a world.

/worldmanager update Updates WorldManager.

/worldmanager sync (templateworld) opens and UI to sync gamerules and settings with this world.

/worldmanager locatebiome (biome) [range]* {teleport}* scans the nearby world for the chosen biome.

/worldmanager spawn teleports you to the spawn of your current world.

/worldmanager addons opens an addon ui where you can extend your server very easy.

/worldmanager default [World]* shows you the default level. You can change it if you write the worldname too.


* optional

[/SPOILER]


[SPOILER="Available Addons"]

WorldSigns by Buddelbubi

WorldWhilelist by Buddelbubi

WorldPlayerLimits by Buddelbubi

WorldChat by Buddelbubi

WorldJoinMessage by Buddelbubi

TheEnd by wode

QuasiStructurePopulator by wode

BetterVanillaGenerator by wode

ClassicVillagePopulator by wode

OceanMonumentPopulator by wode

StrongholdPopulator by wode

NetherFortressPopulator by wode

MineshaftPopulator by wode

ScatteredBuildingPopulator by wode

EmptyWorld by Creeperface

MultiNether by PetteriM1

PerWorldInventory by lukeeey

SimpleWorldEdit by raffa505

FastAsyncWorldEdit by IntellectualSites

DbLib by fromgate (in addition to Fawe)


You have addon Ideas? Message me on Discord: Buddelbubi#5018

[/SPOILER]



