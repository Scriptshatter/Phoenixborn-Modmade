package net.scriptshatter.fberb.components;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashMap;

public interface Machine_anim_int extends Component, Inventory, RecipeInputProvider {
    void turn(float turn_by);

    void add_item(int slot, ItemStack item);
    void take_item(int slot);
    void set_status(String status);
    void change_time(float time);
    void set_time(int time);
    void set_being_used(int used);
    void change_being_used(int used);
    void change_speed(float speed);
    void craft_item(ItemStack itemStack);

    ItemStack get_crafted_item();
    boolean has_items_crafted();
    ItemStack get_item(int slot);
    String get_status();
    int get_time();
    float get_turn();
    float get_speed();
    boolean being_used();
    DefaultedList<ItemStack> get_inv();
}
