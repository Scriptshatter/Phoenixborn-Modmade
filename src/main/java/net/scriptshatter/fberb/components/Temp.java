package net.scriptshatter.fberb.components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.scriptshatter.fberb.entitys.Phoenix_shovel_entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Temp implements Temp_int, AutoSyncedComponent {
    private double temp = 0;
    private final PlayerEntity player;
    private int rebirths;
    private int rage;

    private double internal_temp = 0.7;
    private List<UUID> phoenix_shovels = new ArrayList<>();

    public Temp(PlayerEntity player) {
        this.player = player;
    }

    //Fun fact: I made a mistake when coding this for the first time that made it add the amount 3 times
    @Override
    public void change_temp(double amount) {
        if ((this.temp + amount) >= 1000){
            this.temp = 1000;
        } else if ((this.temp + amount) <= 0) {
            this.temp = 0;
        }
        else{
            this.temp += amount;
        }
        Bird_parts.TEMP.sync(this.player);
    }

    @Override
    public void add_rebirths(int amount) {
        this.rebirths += amount;
        Bird_parts.TEMP.sync(this.player);
    }

    @Override
    public void set_internal_temp(double amount) {
        this.internal_temp = amount;
    }


    @Override
    public void set_temp(double amount) {
        if (amount >= 1000){
            this.temp = 1000;
        } else if (amount <= 0) {
            this.temp = 0;
        }
        else{
            this.temp = amount;
        }
        Bird_parts.TEMP.sync(this.player);
    }

    @Override
    public void set_rage(int amount) {
        this.rage = Math.max(amount, 0);
        Bird_parts.TEMP.sync(this.player);
    }

    @Override
    public void change_rage(int amount) {
        if (this.rage + amount <= 0) {
            this.rage = 0;
        }
        else{
            this.rage += amount;
        }
        Bird_parts.TEMP.sync(this.player);
    }

    @Override
    public void add_shovel(UUID shovel) {
        this.phoenix_shovels.add(shovel);
    }

    @Override
    public void clear_shovels() {
        this.phoenix_shovels.clear();
    }

    @Override
    public int get_temp() {
        return (int)this.temp;
    }

    @Override
    public int get_rebirths() {
        return this.rebirths;
    }

    @Override
    public List<UUID> get_shovels() {
        return this.phoenix_shovels;
    }

    @Override
    public boolean is_mad() {
        return this.rage > 0;
    }

    @Override
    public double get_internal_temp() {
        return this.internal_temp;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.temp = tag.getDouble("temp");
        this.internal_temp = tag.getDouble("internal_temp");
        this.rebirths = tag.getInt("rebirths");
        this.rage = tag.getInt("rage");
        tag.getList("phoenix_shovels", 10).forEach(nbtElement -> this.phoenix_shovels.add(NbtHelper.toUuid(nbtElement)));
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putDouble("temp", this.temp);
        tag.putDouble("internal_temp", this.internal_temp);
        tag.putInt("rebirths", this.rebirths);
        tag.putInt("rage", this.rage);
        NbtList list = new NbtList();
        phoenix_shovels.forEach(uuid -> list.add(NbtHelper.fromUuid(uuid)));
        tag.put("phoenix_shovels", list);
    }

    //Needs to update fast.
    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity player) {
        buf.writeDouble(this.temp);
        buf.writeInt(this.rebirths);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.temp = buf.readDouble();
        this.rebirths = buf.readInt();
    }
}
