package com.geoderp.geoplugin.Utility;

import org.bukkit.entity.Player;

public class XP {
    public static int getXPOfLevel(int level) {
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

    public static int[] getLevelOfXP(int xp) {
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

    public static void setPlayerXP(Player player, int xp) {
        int[] xpOut = getLevelOfXP(xp);
        player.setLevel(xpOut[0]);
        float nextLevelPerc = (float) xpOut[1] / (getXPOfLevel(xpOut[0]+1) - getXPOfLevel(xpOut[0]));
        player.setExp(nextLevelPerc);
    }
}
