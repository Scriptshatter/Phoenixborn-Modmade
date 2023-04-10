package net.scriptshatter.fberb.components;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.scriptshatter.fberb.blocks.Phoenix_shovel_block_entity;
import net.scriptshatter.fberb.items.Birb_item;
import net.scriptshatter.fberb.items.Items;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class Shovel_parts implements Shovel_parts_int{
    ItemStack itemStack;
    UUID owner_uuid = null;
    Phoenix_shovel_block_entity block_entity;

    public Shovel_parts(Phoenix_shovel_block_entity phoenix_shovel_block_entity) {
        block_entity = phoenix_shovel_block_entity;
    }


    @Override
    public void readFromNbt(NbtCompound tag) {
        Bird_parts.SHOVEL.sync(block_entity);
        if(tag.contains("shovel") && ItemStack.fromNbt(tag.getCompound("shovel")).getItem() instanceof Birb_item){
            this.itemStack = ItemStack.fromNbt(tag.getCompound("shovel"));
        }
        if(tag.contains("owner_uuid")){
            this.owner_uuid = tag.getUuid("owner_uuid");
        }
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        if(this.itemStack != null){
            tag.put("shovel", this.itemStack.writeNbt(new NbtCompound()));
        }
        if(this.owner_uuid != null){
            tag.putUuid("owner_uuid", this.owner_uuid);
        }
        block_entity.markDirty();
        Bird_parts.SHOVEL.sync(block_entity);
    }

    @Override
    public void set_itemstack(ItemStack itemStack) {
        this.itemStack = new ItemStack(Items.PHOENIX_SHOVEL);
        this.itemStack.setNbt(itemStack.getNbt());
    }

    @Override
    public void set_owner(UUID uuid) {
        this.owner_uuid = uuid;
    }

    @Override
    public void decrease_temp(double amount) {
        if(this.itemStack.getItem() instanceof Birb_item){
            Items.PHOENIX_SHOVEL.change_temp(amount, this.itemStack);
        }
    }

    @Override
    public ItemStack get_itemstack() {
        return this.itemStack;
    }

    @Override
    public UUID get_owner() {
        return this.owner_uuid;
    }

    @Override
    public double get_temp() {
        if(this.itemStack != null && this.itemStack.getItem() instanceof Birb_item){
            return Items.PHOENIX_SHOVEL.temp(this.itemStack);
        }
        return 0;
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        if(this.itemStack != null){
            buf.writeItemStack(this.itemStack);
        }
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        if(buf.readItemStack() != null && buf.readItemStack().getItem() instanceof Birb_item){
            this.itemStack = buf.readItemStack();
        }
    }
}
