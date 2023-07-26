package com.geoderp.geoplugin.Enchants;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class EnchantWrapper extends Enchantment {
    private final String enchantName;
    private final int maxLevel;
    
    public EnchantWrapper(String namespace, String enchantName, int maxLevel) {
        super(NamespacedKey.minecraft(namespace));
        this.enchantName = enchantName;
        this.maxLevel = maxLevel;
    }
    
    @Override
    public boolean canEnchantItem(ItemStack item) {
        return false;
    }

    @Override
    public boolean conflictsWith(Enchantment enchant) {
        return false;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public String getName() {
        return enchantName;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public boolean isCursed() {
        return false;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }
}
