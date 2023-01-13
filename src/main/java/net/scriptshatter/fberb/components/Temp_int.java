package net.scriptshatter.fberb.components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;

public interface Temp_int extends AutoSyncedComponent {
    void change_temp(double amount);
    void add_rebirths(int amount);

    void set_internal_temp(double amount);
    void set_temp(double amount);

    int get_temp();
    int get_rebirths();

    double get_internal_temp();


}
