package net.scriptshatter.fberb.components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;

public interface Temp_int extends AutoSyncedComponent {
    void change_temp(double amount);
    void add_rebirths(int amount);

    void set_internal_temp(double amount);
    void set_temp(double amount);

    void set_rage(int amount);
    void change_rage(int amount);

    int get_temp();
    int get_rebirths();
    boolean is_mad();

    double get_internal_temp();


}
