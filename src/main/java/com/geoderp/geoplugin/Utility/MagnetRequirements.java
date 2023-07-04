package com.geoderp.geoplugin.Utility;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

public class MagnetRequirements {
    public static Material[] validStrongMaterials = {Material.ENCHANTED_GOLDEN_APPLE, Material.NETHER_STAR, Material.NETHERITE_INGOT};
    public static Material[] validWeakMaterials = {Material.RABBIT_FOOT, Material.HEART_OF_THE_SEA, Material.GHAST_TEAR, Material.NAUTILUS_SHELL};
    public static boolean requireUnbreakable = true;
    public static String name = "GeoMagnet";
    public static String lore = "GeoMagnet";
    public static List<String> loreList = Arrays.asList(lore);
}
