
# Current Supported Minecraft Version: 1.20.1

# GeoPlugin
This is a Minecraft Spigot plugin that adds features we use on our server at s.mcnsa.com.

## Modules
All the content in this plugin is separated into different distinct modules. These modules can be disabled by finding the module you want to disable in the config file and setting it to false.

### Notes Module
Notes are stored in a SQLite database found at [serverDir]/plugins/GeoPlugin/GeoDB.db

|Command|Permission Node|Description|
|----|----|----|
| /gnote [playerName] | GeoPlugin.commands.gnote | Views all notes for the given player |
| /gnote [playerName] [note] | GeoPlugin.commands.gnote | Creates a new note for the given player |
| /gnote recent | GeoPlugin.commands.gnote | Views the 5 most recent notes |
| /gnote remove [noteID] | GeoPlugin.notes.removeNote | Remove the note with given ID from the database |
|  | GeoPlugin.notes.loginNote | Displays the most recent note on players when they login |

### XP Storage Module
WIP Coming Soon

### Right Click Harvest/Bonemeal Lilypads
WIP Coming Soon
This is already created, just need to move it from my old plugin.

### Wither Rose/Netherstar "Magnet"
WIP Coming Soon
This is already created, just need to move it from my old plugin.

### Vampirism
WIP Coming Soon
This is already created, just need to move it from my old plugin.

### Old Username Lookup
WIP Coming Soon

### Step Assist
WIP Coming Soon

### Grave Chests
WIP Coming Soon




