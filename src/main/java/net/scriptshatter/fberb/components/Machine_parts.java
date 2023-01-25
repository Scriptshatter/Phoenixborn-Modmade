package net.scriptshatter.fberb.components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.blocks.Machine;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Machine_parts implements Machine_anim_int, AutoSyncedComponent {
    private HashMap<Integer, ItemStack> machine_template(){
        HashMap<Integer, ItemStack> temp_potions = new HashMap<>();
        temp_potions.put(1, ItemStack.EMPTY);
        temp_potions.put(2, ItemStack.EMPTY);
        temp_potions.put(3, ItemStack.EMPTY);
        temp_potions.put(4, ItemStack.EMPTY);
        temp_potions.put(5, ItemStack.EMPTY);
        temp_potions.put(6, ItemStack.EMPTY);
        temp_potions.put(7, ItemStack.EMPTY);
        temp_potions.put(8, ItemStack.EMPTY);
        temp_potions.put(9, ItemStack.EMPTY);
        return temp_potions;
    }
    private final HashMap<Integer, ItemStack> machine_inventory = machine_template();
    private final Machine blockEntity;
    public Machine_parts(BlockEntity blockEntity) {
        this.blockEntity = (Machine)blockEntity;
    }

    @Override
    public void add_item(int slot, ItemStack item) {
        if (!this.machine_inventory.get(slot).equals(new ItemStack(Items.AIR, 1))){
            take_item(slot);
        }
        ItemStack itemBuffer = item.copy();
        itemBuffer.setCount(1);
        this.machine_inventory.put(slot, itemBuffer);
        Bird_parts.INV.sync(blockEntity);
    }

    @Override
    public void take_item(int slot) {
        this.machine_inventory.put(slot, new ItemStack(Items.AIR, 1));
        Bird_parts.INV.sync(blockEntity);
    }

    @Override
    public ItemStack get_item(int slot) {
        return this.machine_inventory.get(slot);
    }

    @Override
    public HashMap<Integer, ItemStack> get_inv() {
        return this.machine_inventory;
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        this.blockEntity.readNbt(tag);
        NbtList itemList = (NbtList) tag.get("Inventory");
        if(itemList != null){
            for (int i = 0; i < itemList.size(); i++) {
                NbtCompound itemTag = itemList.getCompound(i);
                ItemStack item = ItemStack.fromNbt(itemTag);
                int slot = itemTag.getInt("Slot");
                Phoenix.LOGGER.info(slot + item.getName().getString());
                this.machine_inventory.put(slot, item);
            }
        }
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        this.blockEntity.write(tag);
        NbtList itemList = new NbtList();
        this.machine_inventory.forEach((slot, item) -> {
            NbtCompound itemTag = new NbtCompound();
            itemTag.putInt("Slot", slot);
            item.writeNbt(itemTag);
            itemList.add(itemTag);
        });
        tag.put("Inventory", itemList);
    }
}
