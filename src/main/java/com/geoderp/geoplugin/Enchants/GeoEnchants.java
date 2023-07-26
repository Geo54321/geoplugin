package com.geoderp.geoplugin.Enchants;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;

public class GeoEnchants {
    public static final Enchantment HEWING = new EnchantWrapper("hewing", "hewing", 2);
    public static final Enchantment FORGE = new EnchantWrapper("forge", "forge", 2);
    public static final Enchantment DRAIN = new EnchantWrapper("drain", "drain", 2);
    public static final Enchantment DEATHWOVEN = new EnchantWrapper("death_woven", "death_woven", 2);

    public static void registerAll() {
        boolean hewed = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(HEWING);
        boolean forged = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(FORGE);
        boolean drained = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(DRAIN);
        boolean weaved = Arrays.stream(Enchantment.values()).collect(Collectors.toList()).contains(DEATHWOVEN);
    
        if (!hewed)
            registerEnchant(HEWING);
        if (!forged)
            registerEnchant(FORGE);
        if (!drained)
            registerEnchant(DRAIN);
        if (!weaved)
            registerEnchant(DEATHWOVEN);
    }

    public static void registerEnchant(Enchantment enchant) {
        boolean registered = true;
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(enchant);
        }
        catch (Exception e) {
            registered = false;
            Bukkit.getLogger().log(Level.INFO, "Error Loading enchantment: " + enchant);
        }
        if (registered) {
            Bukkit.getLogger().info("[BlameGeo!] GeoEnchant " + enchant.getName() + " successfully registered.");
        }
    }
}

