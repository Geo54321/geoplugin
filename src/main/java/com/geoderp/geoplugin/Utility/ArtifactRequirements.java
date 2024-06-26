package com.geoderp.geoplugin.Utility;

import java.util.ArrayList;

import org.bukkit.Material;

public class ArtifactRequirements {
    // Magnet Stuff
    public static Material[] validStrongMagnetMaterials = {Material.ENCHANTED_GOLDEN_APPLE, Material.NETHER_STAR, Material.NETHERITE_INGOT};
    public static Material[] validWeakMagnetMaterials = {Material.RABBIT_FOOT, Material.HEART_OF_THE_SEA, Material.GHAST_TEAR, Material.NAUTILUS_SHELL};
    public static String magnetName = "§dGeoMagnet";
    public static String magnetLoreString = "§5GeoMagnet";
    public static ArrayList<String> magnetLore = createMagnetLoreList();

    public static ArrayList<String> createMagnetLoreList() {
        ArrayList<String> loreArrayList = new ArrayList<String>();
        loreArrayList.add(magnetLoreString);

        return loreArrayList;
    }

    // Zoomies 
    // Other potential items - sugar, rabbit foot, 
    public static Material[] validZoomiesMaterials = {Material.FEATHER, Material.BLAZE_POWDER, Material.POWERED_RAIL};
    public static String zoomiesName = "§dGeoZoomies";
    public static String zoomiesLoreString = "§5GeoZoomies";
    public static ArrayList<String> zoomiesLore = createZoomiesLoreList();

    public static ArrayList<String> createZoomiesLoreList() {
        ArrayList<String> loreArrayList = new ArrayList<String>();
        loreArrayList.add(zoomiesLoreString);

        return loreArrayList;
    }
}
