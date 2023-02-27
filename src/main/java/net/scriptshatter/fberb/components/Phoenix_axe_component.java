package net.scriptshatter.fberb.components;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.scriptshatter.fberb.entitys.Phoenix_axe_entity;
import org.jetbrains.annotations.NotNull;

public class Phoenix_axe_component implements Phoenix_axe_interface {
    private float cur_turn;
    private final Phoenix_axe_entity entity;

    public Phoenix_axe_component(Phoenix_axe_entity phoenix_axe_entity) {
        this.entity = phoenix_axe_entity;
    }

    @Override
    public void change_turn(float value) {
        if(this.cur_turn + value > 360){
            value = 0;
        }
        else{
            value += this.cur_turn;
        }
        this.cur_turn = value;
        Bird_parts.PHOENIX_AXE_NBT.sync(this.entity);
    }

    @Override
    public float get_turn() {
        return this.cur_turn;
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        this.cur_turn = tag.getFloat("turn");
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        tag.putFloat("turn", this.cur_turn);
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity player) {
        buf.writeFloat(this.cur_turn);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.cur_turn = buf.readFloat();
    }

}

