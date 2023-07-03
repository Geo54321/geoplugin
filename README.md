
# Current Supported Minecraft Version: 1.20.1

# GeoPlugin
This is a Minecraft Spigot plugin that adds features we use on the MCNSA community server.

## Modules
All the content in this plugin is separated into different distinct modules. These modules can be disabled by finding the module you want to disable in the config file and setting it to false.

### Notes Module
Notes are stored in a SQLite database found at [serverDir]/plugins/GeoPlugin/GeoDB.db

Aliases: /note

| Command | Permission Node | Description |
|----|----|----|
| /gnote [playerName] | GeoPlugin.commands.gnote | Views all notes for the given player |
| /gnote [playerName] [note] | GeoPlugin.commands.gnote | Creates a new note for the given player |
| /gnote recent | GeoPlugin.commands.gnote | Views the 5 most recent notes |
| /gnote remove [noteID] | GeoPlugin.commands.gnote.remove | Remove the note with given ID from the database |
|  | GeoPlugin.notes.loginNote | Displays the most recent note on players when they login |

### XP Storage Module
Player XP is stored in a SQLite database found at [serverDir]/plugins/GeoPlugin/GeoDB.db

Aliases: /geoxp /gxp /gkeeper

#### Player Commands
| Command | Permission Node | Description |
|----|----|----|
| /geokeeper | GeoPlugin.commands.geokeeper | Displays the amount of XP you have stored |
| /geokeeper store all | GeoPlugin.commands.geokeeper | Stores all of your current XP |
| /geokeeper store [# of levels] | GeoPlugin.commands.geokeeper | Stores given number of levels |
| /geokeeper retrieve all | GeoPlugin.commands.geokeeper | Retrieves all currently stored XP |
| /geokeeper retrieve [# of levels] | GeoPlugin.commands.geokeeper | Retrieves given number of levels from XP storage |
|  | GeoPlugin.geokeeper.death.high | Stores a high amount of XP into storage on death (default: 100%) |
|  | GeoPlugin.geokeeper.death.medium | Stores a modest amount of XP into storage on death (default: 50%) |
|  | GeoPlugin.geokeeper.death.low | Stores a small amount of XP into storage on death (default: 25%) |

#### Staff Commands
| Command | Permission Node | Description |
|----|----|----|
| /geokeeper [playerName] | GeoPlugin.commands.geokeeper.others | Displays current stored XP of a given player |
| /geokeeper store all [playerName] | GeoPlugin.commands.geokeeper.others | Stores all of your XP into given players XP |
| /geokeeper store [# of levels] [playerName] | GeoPlugin.commands.geokeeper.others | Stores a given amount of your levels into given players XP |
| /geokeeper retrieve all [playerName] | GeoPlugin.commands.geokeeper.others | Retrieve all stored XP of a given player to you |
| /geokeeper retrieve [# of levels] [playerName] | GeoPlugin.commands.geokeeper.others | Retrieves a given number of levels of a given player to you |


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

### All Permission As Tree

GeoPlugin.*
    GeoPlugin.notes.*
        GeoPlugin.notes.moderator
            GeoPlugin.commands.gnote -- Allows use of /gnote
            GeoPlugin.notes.loginNote -- Allows view of play login note
        GeoPlugin.commands.gnote.remove -- Allows removal of notes with /gnote
    GeoPlugin.geokeeper.*
        GeoPlugin.commands.geokeeper -- Allows use of /geokeeper
        GeoPlugin.commands.geokeeper.other -- Allows use of /geokeeper on other players
        GeoPlugin.geokeeper.death.high -- Stored high amount of XP on death
        GeoPlugin.geokeeper.death.medium -- Stored medium amount of XP on death
        GeoPlugin.geokeeper.death.low -- Stored low amount of XP on death
    GeoPlugin.geoplugin.*
        GeoPlugin.commands.geoplugin - Allows use of /geoplugin
        GeoPlugin.commands.geoplugin.reload - Allows reload of config file
