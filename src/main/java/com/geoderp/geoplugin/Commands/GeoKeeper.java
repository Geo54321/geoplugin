package com.geoderp.geoplugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.geoderp.geoplugin.Utility.Database;

public class GeoKeeper implements CommandExecutor {
    public Database dbObj;
    
    public GeoKeeper(Database dbObj) {
        this.dbObj = dbObj;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("GeoPlugin.commands.geokeeper")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length > 0) {
                    if (args.length == 1) {
                        // view other
                        player = Bukkit.getPlayer(args[0]);
                        if (player != null) {
                            if (validPlayer(player)) {
                                sender.sendMessage("§2" + player.getName() + " currently has " + getStoredXP(player) + " XP points stored.");
                            } 
                            else {
                                addUser(player, 0);
                                sender.sendMessage("§2" + player.getName() + " currently has " + getStoredXP(player) + " XP points stored.");
                            }
                        }
                        else {
                            sender.sendMessage("§cInvalid target player. Please try again.");
                        }
                    }
                    else if (args.length == 2) {
                        // store or retrive self
                        if (args[0].equals("retrieve")) {
                            if (validPlayer(player)) {
                                retrieveXP(player, args);
                                sender.sendMessage("§aYou currently have " + getStoredXP(player) + " XP points stored.");
                            } 
                            else {
                                addUser(player, 0);
                                retrieveXP(player, args);
                                sender.sendMessage("§aYou currently have " + getStoredXP(player) + " XP points stored.");
                            }
                        }
                        else if (args[0].equals("store")) {
                            if (validPlayer(player)) {
                                storeXP(player, args);
                                sender.sendMessage("§aYou currently have " + getStoredXP(player) + " XP points stored.");
                            } 
                            else {
                                storeXP(player, args);
                                sender.sendMessage("§aYou currently have " + getStoredXP(player) + " XP points stored.");
                            }
                        }
                    }
                    else if (args.length == 3) {
                        // store or retrieve other
                        player = Bukkit.getPlayer(args[0]);
                        if (player != null) {

                        }
                        else {
                            sender.sendMessage("§cInvalid target player. Please try again.");
                        }
                    }
                    else {
                        sender.sendMessage("§cInvalid number of arguments. Please try again.");
                    }
                }
                else {
                    if (validPlayer(player)) {
                        sender.sendMessage("§aYou currently have " + getStoredXP(player) + " XP points stored.");
                    } 
                    else {
                        addUser(player, 0);
                        sender.sendMessage("§aYou currently have " + getStoredXP(player) + " XP points stored.");
                    }
                }
            }
            else {
                // Console version here
            }
            return true;
        }
        else {
            sender.sendMessage("§cSorry but you do not have permission to use /geokeeper.");
        }
        return false;
    }

    public boolean validPlayer(Player player) {
        String uuid = player.getUniqueId().toString();
        if (dbObj.selectID("xp","player",uuid) == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public void addUser(Player player, int amount) {
        String uuid = player.getUniqueId().toString();
        dbObj.addUser(uuid, amount);
    }

    public int getStoredXP(Player player) {
        String uuid = player.getUniqueId().toString();
        return dbObj.getXP(uuid);
    }

    public void storeXP(Player player, String[] args) {
        if (args[1].equals("all")) {
            storeAllLevel(player);
        }
        else {
            storeSomeLevels(player, args[1]);
        }
    }

    public void storeAllLevel(Player player) {
        player.sendMessage("curr level: " + player.getLevel());
        int xpToStore = getXPOfLevel(player.getLevel());
        player.sendMessage("levels:" +xpToStore);
        xpToStore += player.getExp() * (getXPOfLevel(player.getLevel()+1) - getXPOfLevel(player.getLevel()));
        player.sendMessage("curr exp:" +player.getExp());
        player.sendMessage("levels:" +xpToStore);
        player.sendMessage("total exp: " + player.getTotalExperience());
        int stored = getStoredXP(player);
        
        dbObj.updateXP(stored+xpToStore, player.getUniqueId().toString());
        player.setLevel(0);
        player.setExp(0);
    }

    public void storeSomeLevels(Player player, String levelArg) {
        try {
            int levelToStore = Integer.parseInt(levelArg);
            int xpToStore = getXPOfLevel(player.getLevel()) - getXPOfLevel(player.getLevel()-levelToStore);
            int stored = getStoredXP(player);

            if (levelToStore > player.getLevel()) {
                player.sendMessage("§cYou do not have " + levelToStore + " levels to store. Storing all XP.");
                int newTotal = getXPOfLevel(player.getLevel()) + stored;
                newTotal += player.getExp() * (getXPOfLevel(player.getLevel()+1) - getXPOfLevel(player.getLevel()));
                dbObj.updateXP(newTotal, player.getUniqueId().toString());
                player.setLevel(0);
                player.setExp(0);
            }
            else {
                // Fix to actual partial calcs
                dbObj.updateXP(stored+xpToStore, player.getUniqueId().toString());
                player.setLevel(player.getLevel()-levelToStore);
            }

            
        }
        catch (Exception e) {
            player.sendMessage("§cIncorrect parameter, either use 'all' or an integer number of levels.");
        }
    }

    public void retrieveXP(Player player, String[] args) {
        if (args[1].equals("all")) {
            int stored = getStoredXP(player);
            int current = getXPOfLevel(player.getLevel());
            current += player.getExp() * (getXPOfLevel(player.getLevel()+1) - getXPOfLevel(player.getLevel()));
            int[] newVals = getLevelOfXP(current+stored);

            player.setLevel(newVals[0]);
            float whyTheActualFuckIsThisLevelPercentNotNumOfXPPoints = (float) newVals[1] / (float) (getXPOfLevel(newVals[0]+1) - getXPOfLevel(newVals[0]));
            player.setExp(whyTheActualFuckIsThisLevelPercentNotNumOfXPPoints);

            player.sendMessage("stored: " + stored);
            player.sendMessage("newvals: " + newVals[0]);
            player.sendMessage("newvals 2: " + newVals[1]);
            player.sendMessage("why tho: " + whyTheActualFuckIsThisLevelPercentNotNumOfXPPoints);
            
            dbObj.updateXP(0, player.getUniqueId().toString());
        }
        else {
            try {
                // Add a check for if you have that many levels
                int levelToPull = Integer.parseInt(args[1]);
                int xpToPull = getXPOfLevel(player.getLevel()+levelToPull) - getXPOfLevel(player.getLevel());
                int stored = getStoredXP(player);
                
                if (xpToPull > stored) {
                    player.sendMessage("§6You did not have enough XP stored to retrieve "+ levelToPull +" levels. Retrieved all stored XP.");
                    int[] newVals = getLevelOfXP(stored);

                    player.setLevel(newVals[0]);
                    float whyTheActualFuckIsThisLevelPercentNotNumOfXPPoints = (float) newVals[1] / (float) (getXPOfLevel(newVals[0]+1) - getXPOfLevel(newVals[0]));
                    player.setExp(whyTheActualFuckIsThisLevelPercentNotNumOfXPPoints);
                    dbObj.updateXP(0, player.getUniqueId().toString());
                }
                else {
                    // Fix to actual partial calcs
                    player.setLevel(player.getLevel()+levelToPull);
                    dbObj.updateXP(stored-xpToPull, player.getUniqueId().toString());
                }
            }
            catch (Exception e) {
                player.sendMessage("§cIncorrect parameter, either use 'all' or an integer number of levels.");
            }
        }
    }

    public int getXPOfLevel(int level) {
        double xp = 0;
        if (level <= 16) {
            xp = level * level + 6 * level;
        }
        else if (level <= 31) {
            xp = 2.5 * level * level - 40.5 * level + 360;
        }
        else {
            xp = 4.5 * level * level - 162.5 * level + 2220;
        }

        return (int) xp;
    }

    public int[] getLevelOfXP(int xp) {
        boolean goAgain = true;
        int level = 0;
        int counterXP = 0;
        while (goAgain) {
            int stepXP = 0;
            if (level <= 15) {
                stepXP += (2 * level) + 7;
            }
            else if (15 < level && level <= 30) {
                stepXP += (5 * level) - 38;
            }
            else {
                stepXP += (9 * level) - 158;
            }
            

            if (counterXP+stepXP > xp) {
                goAgain = false;
            }
            else if (counterXP+stepXP == xp) {
                goAgain = false;
                level++;
                counterXP += stepXP;
            }
            else {
                level++;
                counterXP += stepXP;
            }
        }
        return new int[]{level,xp-counterXP};
    }
}
