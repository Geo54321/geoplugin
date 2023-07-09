package com.geoderp.geoplugin.Utility;

import java.util.ArrayList;

import org.bukkit.Material;

public class MagnetRequirements {
    public static Material[] validStrongMaterials = {Material.ENCHANTED_GOLDEN_APPLE, Material.NETHER_STAR, Material.NETHERITE_INGOT};
    public static Material[] validWeakMaterials = {Material.RABBIT_FOOT, Material.HEART_OF_THE_SEA, Material.GHAST_TEAR, Material.NAUTILUS_SHELL};
    public static String name = "§dGeoMagnet";
    public static String loreString = "§5GeoMagnet";
    public static ArrayList<String> lore = createLoreList();

    public static ArrayList<String> createLoreList() {
        ArrayList<String> loreArrayList = new ArrayList<String>();
        loreArrayList.add(loreString);

        return loreArrayList;
    }
}
