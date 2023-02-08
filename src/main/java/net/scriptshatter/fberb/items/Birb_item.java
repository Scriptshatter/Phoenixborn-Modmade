package net.scriptshatter.fberb.items;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public interface Birb_item {

    int max_temp();
    double temp(ItemStack stack);
    int getItemBarColorTemp(ItemStack stack);
    int getItemBarStepTemp(ItemStack stack);
}
