package net.scriptshatter.fberb.components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.UUID;

public interface Temp_int extends AutoSyncedComponent {
    void change_temp(double amount);
    void add_rebirths(int amount);

    void set_internal_temp(double amount);
    void set_temp(double amount);

    void set_sight_time(float amount);
    void change_sight_time(float amount);
    float get_sight_time();

    int get_temp();
    int get_rebirths();

    double get_internal_temp();


}
