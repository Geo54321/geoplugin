package com.geoderp.geoplugin.Commands;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class RNG implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("GeoPlugin.commands.rng")) {
            Random rand = new Random();
            int max = 20;
            if (args.length == 0) {
                int rng = rand.nextInt(max) + 1;
                sender.sendMessage("§bYou rolled a " + rng + " out of " + max + ".");
            }
            else if (args.length == 1) {
                try {
                    max = Integer.parseInt(args[0]);
                    int rng = rand.nextInt(max) + 1;
                    sender.sendMessage("§bYou rolled a " + rng + " out of " + max + ".");
                }
                catch (Exception e) {
                    sender.sendMessage("§cRange max must be an integer number.");
                }
            }
            else if (args.length == 2) {
                try {
                    max = Integer.parseInt(args[0]);
                    int count = Integer.parseInt(args[1]);
                    if (count < 101) {
                        int[] results = new int[count];

                        for (int n = 0; n < count; n++) {
                            results[n] = rand.nextInt(max) + 1;
                        }

                        sender.sendMessage("§bYou rolled " + Arrays.toString(results) + " out of " + max + ".");
                    }
                    else {
                        sender.sendMessage("§cThe maximum number of rolls is 100.");
                    }
                }
                catch (Exception e) {
                    sender.sendMessage("§cRange max and number of results must be an integer numbers.");
                }
            }
        }
        else {
            sender.sendMessage("§cSorry but you do not have permission to use the /rng command.");
        }
        return true;
    }
}
