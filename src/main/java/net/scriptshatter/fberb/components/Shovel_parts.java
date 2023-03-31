package net.scriptshatter.fberb.components;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.scriptshatter.fberb.blocks.Phoenix_shovel_block_entity;
import net.scriptshatter.fberb.items.Birb_item;
import net.scriptshatter.fberb.items.Items;

import java.util.UUID;

public class Shovel_parts implements Shovel_parts_int{
    ItemStack itemStack = ItemStack.EMPTY;
    UUID owner_uuid = null;
    Phoenix_shovel_block_entity block_entity;

    public Shovel_parts(Phoenix_shovel_block_entity phoenix_shovel_block_entity) {
        block_entity = phoenix_shovel_block_entity;
    }


    @Override
    public void readFromNbt(NbtCompound tag) {
        Bird_parts.SHOVEL.sync(block_entity);
        this.itemStack = ItemStack.fromNbt(tag.getCompound("shovel"));
        if(tag.contains("owner_uuid")){
            this.owner_uuid = tag.getUuid("owner_uuid");
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.put("shovel", this.itemStack.writeNbt(new NbtCompound()));
        if(this.owner_uuid != null){
            tag.putUuid("owner_uuid", this.owner_uuid);
        }
        block_entity.markDirty();
        Bird_parts.SHOVEL.sync(block_entity);
    }

    @Override
    public void set_itemstack(ItemStack itemStack) {
        this.itemStack = itemStack;
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
        if(this.itemStack.getItem() instanceof Birb_item){
            return Items.PHOENIX_SHOVEL.temp(this.itemStack);
        }
        return 0;
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity recipient) {
        buf.writeItemStack(this.itemStack);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.itemStack = buf.readItemStack();
    }
}
