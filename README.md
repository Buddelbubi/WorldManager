
![logo](https://cloudburstmc.org/attachments/1618588553800-png.3459/)


# WorldManager Plugin

WorldManager is a powerful plugin that provides extensive world management capabilities for your Minecraft server. With a wide range of features, WorldManager makes it easy to create, manage, and customize worlds. The plugin also includes a flexible permissions system, simplifying access control for commands and worlds.

## Key Features

- **World Teleportation and UI**: Easily teleport between worlds using a user-friendly interface.
- **World Generation**: Create new worlds with any combination of generators and seeds.
- **World Management**: Load, unload, reload, rename, copy, delete, and back up worlds.
- **World Settings**: Customize world parameters such as game mode, spawn point, and game rules.
- **Biome Management**: Change biomes for all loaded chunks in the current world.
- **World Information**: Retrieve information about the seed, generator, player count, and more.
- **Game Rule Synchronization**: Quickly synchronize game rules between worlds.
- **World Protection**: Prevent building, breaking, and interacting in the world.
- **Automatic Updates**: The plugin can automatically update to the latest version.
- **Modules and Extensions**: Easily extend the plugin's functionality with additional modules.

## Commands and Permissions

The WorldManager plugin provides numerous commands. The primary command is `/worldmanager`, but you can also use shortcuts like `/wm`, `/mw`, `/levelmanager`, `/lm`, and `/mv` for Multiverse2 fans.

### Example Usage:

`/wm generate [World] (Generator) {Seed}`  
This command generates a new world. Permission `worldmanager.generate` is required to use it.

### Main Commands:

- `/worldmanager teleport [World] (Player)` — Teleports you or the specified player to a world.
- `/worldmanager generate [World] (Generator) {Seed}` — Generates a new world.
- `/worldmanager delete [World]` — Deletes a world.
- `/worldmanager list` — Shows a list of all worlds.
- `/worldmanager load [World]` — Loads a world.
- `/worldmanager unload [World]` — Unloads a world.
- `/worldmanager reload [World]` — Reloads a world.
- `/worldmanager rename [World] (New World Name)` — Renames a world.
- `/worldmanager copy [World] (Copy Name)` — Copies a world.
- `/worldmanager setspawn` — Sets the spawn point in the world.
- `/worldmanager settings [World]` — Opens the UI to configure world settings.
- `/worldmanager regenerate [World]` — Regenerates a world.
- `/worldmanager setseed [World] (Seed)` — Changes the world's seed.
- `/worldmanager info [World]` — Displays information about the world.
- `/worldmanager setbiome [Biome]` — Changes the biome of all loaded chunks in the current world.
- `/worldmanager gamerule [World]` — Opens the UI to manage game rules.
- `/worldmanager killentitys [World]` — Destroys all entities in the world.
- `/worldmanager update` — Updates WorldManager.
- `/worldmanager sync (World Template)` — Synchronizes game rules and settings with the specified world.
- `/worldmanager locatebiome (Biome) [Radius] {Teleport}` — Scans for the nearest biome in the world.
- `/worldmanager spawn` — Teleports you to the spawn point in the current world.
- `/worldmanager addons` — Opens the UI to manage modules.
- `/worldmanager default [World]` — Shows the current default world, with the option to change it.

### Available Modules:

- WorldSigns
- WorldWhitelist
- WorldPlayerLimits
- WorldChat
- WorldJoinMessage
- TheEnd
- QuasiStructurePopulator
- BetterVanillaGenerator
- ClassicVillagePopulator
- OceanMonumentPopulator
- StrongholdPopulator
- NetherFortressPopulator
- MineshaftPopulator
- ScatteredBuildingPopulator
- EmptyWorld
- MultiNether
- PerWorldInventory
- SimpleWorldEdit
- FastAsyncWorldEdit
- DbLib

If you have ideas for new modules, feel free to reach out to me on Discord: Buddelbubi#5018.




