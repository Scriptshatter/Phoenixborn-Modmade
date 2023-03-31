package net.scriptshatter.fberb.components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public interface Shovel_parts_int extends AutoSyncedComponent {
    void set_itemstack(ItemStack itemStack);
    void set_owner(UUID uuid);
    void decrease_temp(double amount);
    ItemStack get_itemstack();
    UUID get_owner();
    double get_temp();
}
