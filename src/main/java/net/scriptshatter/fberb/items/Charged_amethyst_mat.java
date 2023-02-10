package net.scriptshatter.fberb.items;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

public class Charged_amethyst_mat implements ToolMaterial {
    @Override
    public int getDurability() {
        return 1360;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return 7;
    }

    @Override
    public float getAttackDamage() {
        return 3;
    }

    @Override
    public int getMiningLevel() {
        return 5;
    }

    @Override
    public int getEnchantability() {
        return 11;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Items.PHOENIX_BROOCH);
    }
}
