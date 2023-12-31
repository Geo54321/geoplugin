
# Current Supported Minecraft Version: 1.20.1

## GeoPlugin
This is a Minecraft Spigot plugin that adds features we use on the MCNSA community server.

# Modules
All the content in this plugin is separated into different distinct modules. These modules can be disabled by finding the module you want to disable in the config file and setting it to false.

## Notes Module
Notes are stored in a SQLite database found at [serverDir]/plugins/GeoPlugin/GeoDB.db

**Aliases: /gnotes /note /notes**

| Command | Permission Node | Description |
|----|----|----|
| /gnote [playerName] | GeoPlugin.commands.gnote | Views all notes for the given player |
| /gnote [playerName] [note] | GeoPlugin.commands.gnote | Creates a new note for the given player |
| /gnote recent | GeoPlugin.commands.gnote | Views the 5 most recent notes |
| /gnote remove [noteID] | GeoPlugin.commands.gnote.remove | Remove the note with given ID from the database |
|  | GeoPlugin.notes.loginNote | Displays the most recent note on players when they login |

## XP Storage Module
Player XP is stored in a SQLite database found at [serverDir]/plugins/GeoPlugin/GeoDB.db

**Aliases: /geoxp /gxp /gkeeper**

### Player Commands:
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

### Staff Commands:
| Command | Permission Node | Description |
|----|----|----|
| /geokeeper [playerName] | GeoPlugin.commands.geokeeper.others | Displays current stored XP of a given player |
| /geokeeper store all [playerName] | GeoPlugin.commands.geokeeper.others | Stores all of your XP into given players XP |
| /geokeeper store [# of levels] [playerName] | GeoPlugin.commands.geokeeper.others | Stores a given amount of your levels into given players XP |
| /geokeeper retrieve all [playerName] | GeoPlugin.commands.geokeeper.others | Retrieve all stored XP of a given player to you |
| /geokeeper retrieve [# of levels] [playerName] | GeoPlugin.commands.geokeeper.others | Retrieves a given number of levels of a given player to you |

## Magnet
Holding a valid magnet item in your offhand will pull items and xp orbs to you when you move. Sneaking will disable the magnet effect (this is configurable).

### Magnet Strengths:
Weak: Default pickup range of 2 blocks
Strong: Default pickup range of 4 blocks

### Valid Magnet Item Types:
Weak: Rabbit Foot, Heart of the Sea, Ghast Tear, Nautilus Shell
Strong: Enchanted Golden Apple (Notch Apple), Nether Star, Netherite Ingot

### Commands
| Command | Permission Node | Description |
|----|----|----|
| /geoplugin magnet | GeoPlugin.commands.geoplugin.magnet | Add magnet lore to main hand item |
| /geoplugin magnet [weak/strong] | GeoPlugin.commands.geoplugin.magnet | Spawns a magnet of given strength |
|  | GeoPlugin.mechanics.magnet.weak | Gives access to use the weak magnets |
|  | GeoPlugin.mechanics.magnet.strong | Gives access to use the strong magnets |

## Right Click Harvest
Allows right click harvesting and auto-replanting of the crops listed below.

### Supported Crops:
- Wheat
- Potatoes
- Carrots
- Beetroot
- Nether Wart

## Chat Commands
Adds a couple silly chat commands.

### Commands
| Command | Permission Node | Description | Alias(es) |
|----|----|----|----|
| /heart (name) | GeoPlugin.commands.heart | Shows your love for everyone or the target name | /love |
| /rng (max) (count) | GeoPlugin.commands.rng | Generate a 1-20 with no arguments, or a given number of numbers with given maximum | /dice |
| /blamegeo | GeoPlugin.commands.blamegeo | Assigns blame | /blame, /geo |
| /poggers | GeoPlugin.commands.blamegeo | Assigns blame | /pog |
| /promotion [username] [rank] | GeoPlugin.commands.promotion | Annouces promotion of given player to the given rank in chat, and launches corresponding fireworks on the given player, and sends them a title message | /congrats, /promo |

# All Permission Nodes as a Tree

- GeoPlugin.*
    - GeoPlugin.notes.*
        - GeoPlugin.notes.moderator
            - GeoPlugin.commands.gnote -- Allows use of /gnote
            - GeoPlugin.notes.loginNote -- Allows view of play login note
        - GeoPlugin.commands.gnote.remove -- Allows removal of notes with /gnote
    - GeoPlugin.geokeeper.*
        - GeoPlugin.commands.geokeeper -- Allows use of /geokeeper
        - GeoPlugin.commands.geokeeper.other -- Allows use of /geokeeper on other players
        - GeoPlugin.geokeeper.death.high -- Stored high amount of XP on death
        - GeoPlugin.geokeeper.death.medium -- Stored medium amount of XP on death
        - GeoPlugin.geokeeper.death.low -- Stored low amount of XP on death
    - GeoPlugin.mechanics.*
        - GeoPlugin.mechanics.magnet.weak -- Allows usage of weak magnets
        - GeoPlugin.mechanics.magnet.strong -- Allows usage of strong magnets
        - GeoPlugin.mechanics.harvest -- Allows usage of the right click harvest
        - GeoPlugin.mechanics.grow -- Allows usage of the extra bonemeal growth
    - GeoPlugin.chat.*
        - GeoPlugin.commands.heart -- Allows usage of /heart
        - GeoPlugin.commands.rng -- Allows usage of /rng
        - GeoPlugin.commands.blamegeo -- Allows usage of /blamegeo
        - GeoPlugin.commands.promotion -- Allows usage of /promotion
        - GeoPlugin.commands.poggers -- Allows usage of /poggers
        - GeoPlugin.commands.poggers.others -- Allows usage of /poggers on others
    - GeoPlugin.jank.*
        - GeoPlugin.jank.mountTeleport -- Allows mounts to teleport with the player
        - GeoPlugin.commands.explode -- Allows usage of /explode
    - GeoPlugin.geoplugin.*
        - GeoPlugin.commands.geoplugin - Allows use of /geoplugin
        - GeoPlugin.commands.geoplugin.reload - Allows reload of config file
        - GeoPlugin.commands.geoplugin.magnet - Allows usage of the magnet subcommand
