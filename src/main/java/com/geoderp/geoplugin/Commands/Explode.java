package com.geoderp.geoplugin.Commands;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

public class Explode implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("GeoPlugin.commands.explode")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Location splashZone = player.getLocation();
                splashZone.setY(splashZone.getY()+1);
    
                Firework main = (Firework) splashZone.getWorld().spawnEntity(splashZone, EntityType.FIREWORK);
                FireworkMeta meta = main.getFireworkMeta();
                meta.clearEffects();
                meta.setPower(1);
                meta.addEffect(FireworkEffect.builder().withColor(Color.RED).flicker(true).trail(true).build());
                main.setFireworkMeta(meta);
    
                player.setHealth(2);
    
                for (int g = 0; g < 100; g++) {
                    Firework death = (Firework) splashZone.getWorld().spawnEntity(splashZone, EntityType.FIREWORK);
                    death.setFireworkMeta(meta);
                    death.detonate();
                }

                splashZone.getWorld().playSound(splashZone, Sound.ENTITY_GENERIC_EXPLODE, (float) 8.0, (float) 0.56);
            }
            else {
                sender.sendMessage("§cSorry but you must be a player to explode :)");
            }
        }
        else {
            sender.sendMessage("§cSorry but you do not have permission to use the /heart command.");
        }
        return true;
    }
}
