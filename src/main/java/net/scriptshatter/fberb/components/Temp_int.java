package net.scriptshatter.fberb.components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.item.ItemStack;
import net.scriptshatter.fberb.entitys.Phoenix_shovel_entity;

import java.util.List;
import java.util.UUID;

public interface Temp_int extends AutoSyncedComponent {
    void change_temp(double amount);
    void add_rebirths(int amount);

    void set_internal_temp(double amount);
    void set_temp(double amount);

    void set_rage(int amount);
    void change_rage(int amount);
    void add_shovel(UUID shovel);
    void clear_shovels();

    int get_temp();
    int get_rebirths();
    List<UUID> get_shovels();
    boolean is_mad();

    double get_internal_temp();


}
