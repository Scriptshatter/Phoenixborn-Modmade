package net.scriptshatter.fberb.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.scriptshatter.fberb.util.Phoenix_use_actions;

public interface Birb_item {

    String TEMP_KEY = "temp";
    Phoenix_use_actions get_use_case(PlayerEntity user);
    int max_temp();
    double temp(ItemStack stack);
    int getItemBarColorTemp(ItemStack stack);
    int getItemBarStepTemp(ItemStack stack);
    void change_temp(double amount, ItemStack itemStack);
}
