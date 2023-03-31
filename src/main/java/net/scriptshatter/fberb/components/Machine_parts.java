package net.scriptshatter.fberb.components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.util.collection.DefaultedList;
import net.scriptshatter.fberb.blocks.Machine;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Machine_parts implements Machine_anim_int, AutoSyncedComponent {
    private final DefaultedList<ItemStack> machine_inventory;

    private String status = "idle";
    private final List<ItemStack> crafted_items = new ArrayList<>();

    private float turn_stuff;
    private float craft_timer = 0;
    private float turn_wheel_speed = 0;
    private int being_used = 0;
    private final Machine blockEntity;
    public Machine_parts(BlockEntity blockEntity) {
        this.blockEntity = (Machine)blockEntity;
        this.machine_inventory = DefaultedList.ofSize(10, ItemStack.EMPTY);
    }

    @Override
    public void turn(float turn_by){
        if(this.turn_stuff + turn_by > 360){
            turn_by = 0;
        }
        else{
            turn_by += this.turn_stuff;
        }
        this.turn_stuff = turn_by;
        Bird_parts.INV.sync(blockEntity);
    }


    @Override
    public void add_item(int slot, ItemStack item) {
        if (!this.machine_inventory.get(slot).equals(new ItemStack(Items.AIR, 1))){
            take_item(slot);
        }
        ItemStack itemBuffer = item.copy();
        itemBuffer.setCount(1);
        this.machine_inventory.set(slot, itemBuffer);
        blockEntity.markDerty();
        Bird_parts.INV.sync(blockEntity);
    }

    @Override
    public void take_item(int slot) {
        this.machine_inventory.set(slot, new ItemStack(Items.AIR, 1));
        blockEntity.markDerty();
        Bird_parts.INV.sync(blockEntity);
    }

    @Override
    public void set_status(String status) {
        this.status = status;
        Bird_parts.INV.sync(blockEntity);
    }

    @Override
    public void change_time(float time) {
        if(this.craft_timer + time <= 0) this.craft_timer = 0;
        else this.craft_timer += time;
        blockEntity.markDerty();
        Bird_parts.INV.sync(blockEntity);
    }

    @Override
    public void set_time(int time) {
        this.craft_timer = Math.max(time, 0);
        blockEntity.markDerty();
        Bird_parts.INV.sync(blockEntity);
    }

    @Override
    public void set_being_used(int used) {
        this.being_used = used;
        blockEntity.markDerty();
        Bird_parts.INV.sync(blockEntity);
    }

    @Override
    public void change_being_used(int used) {
        this.being_used = Math.max(used + this.being_used, 0);
        blockEntity.markDerty();
        Bird_parts.INV.sync(blockEntity);
    }

    @Override
    public void change_speed(float speed) {
        if(this.turn_wheel_speed + speed <= 0) this.turn_wheel_speed = 0;
        else if (this.turn_wheel_speed > 0.25f) this.turn_wheel_speed = 0.25f;
        else turn_wheel_speed += speed;
        Bird_parts.INV.sync(blockEntity);
    }

    @Override
    public void craft_item(ItemStack itemStack) {
        machine_inventory.set(9, itemStack);
        blockEntity.markDerty();
        Bird_parts.INV.sync(blockEntity);
    }


    @Override
    public ItemStack get_crafted_item() {
        if(!machine_inventory.get(9).equals(ItemStack.EMPTY)) {
            ItemStack item = machine_inventory.get(9).copy();
            item.setCount(1);
            machine_inventory.get(9).decrement(1);
            blockEntity.markDerty();
            Bird_parts.INV.sync(blockEntity);
            return item;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean has_items_crafted() {
        return !this.machine_inventory.get(9).isEmpty();
    }

    @Override
    public ItemStack get_item(int slot) {
        return this.machine_inventory.get(slot);
    }

    @Override
    public String get_status() {
        return this.status;
    }

    @Override
    public int get_time() {
        return (int) this.craft_timer;
    }

    @Override
    public float get_turn() {
        return this.turn_stuff;
    }

    @Override
    public float get_speed() {
        return this.turn_wheel_speed;
    }

    @Override
    public boolean being_used() {
        return !(this.being_used <= 0);
    }

    @Override
    public DefaultedList<ItemStack> get_inv() {
        return this.machine_inventory;
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        this.blockEntity.readNbt(tag);
        this.status = tag.getString("status");
        this.craft_timer = tag.getFloat("craft_timer");
        this.being_used = tag.getInt("flames");
        this.turn_wheel_speed = tag.getFloat("turn_speed");
        this.turn_stuff = tag.getFloat("turnwheel_rot");
        this.machine_inventory.clear();
        crafted_items.clear();
        if(tag.contains("crafted_items")){
            NbtList nbtList = (NbtList) tag.get("crafted_items");
            for (int i = 0; i < Objects.requireNonNull(nbtList).size(); i++) {
                crafted_items.add(ItemStack.fromNbt(nbtList.getCompound(i)));
            }
        }
        Inventories.readNbt(tag, this.machine_inventory);
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        this.blockEntity.write(tag);
        tag.putString("status", this.status);
        tag.putFloat("turn_speed", this.turn_wheel_speed);
        tag.putFloat("craft_timer", this.craft_timer);
        tag.putInt("flames", this.being_used);
        tag.putFloat("turnwheel_rot", this.turn_stuff);
        NbtList nbtList = new NbtList();
        this.crafted_items.forEach(itemStack -> {
            NbtCompound item = new NbtCompound();
            itemStack.writeNbt(item);
            nbtList.add(item);
        });
        tag.put("crafted_items", nbtList);
        Inventories.writeNbt(tag, this.machine_inventory);
    }

    @Override
    public int size() {
        return 10;
    }

    @Override
    public boolean isEmpty() {
        Iterator<ItemStack> var1 = this.machine_inventory.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = var1.next();
        } while(itemStack.isEmpty());

        return false;
    }

    @Override
    public ItemStack getStack(int slot) {
        return this.machine_inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = Inventories.splitStack(this.machine_inventory, slot, amount);
        if (!itemStack.isEmpty()) {
            this.markDirty();
        }

        return itemStack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack itemStack = this.machine_inventory.get(slot);
        if (itemStack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.machine_inventory.set(slot, ItemStack.EMPTY);
            return itemStack;
        }
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        this.machine_inventory.set(slot, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getMaxCountPerStack()) {
            stack.setCount(this.getMaxCountPerStack());
        }

        this.markDirty();
    }

    @Override
    public void markDirty() {
        blockEntity.markDerty();
        Bird_parts.INV.sync(blockEntity);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void clear() {
        this.machine_inventory.clear();
        this.markDirty();
    }


    @Override
    public void provideRecipeInputs(RecipeMatcher finder) {
        for (ItemStack itemStack : this.machine_inventory) {
            finder.addUnenchantedInput(itemStack);
        }
    }
}
