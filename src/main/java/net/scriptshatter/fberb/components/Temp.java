package net.scriptshatter.fberb.components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Temp implements Temp_int, AutoSyncedComponent {
    private double temp = 0;
    private final PlayerEntity player;
    private int rebirths;
    private float sight_time;

    private double internal_temp = 0.7;

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
    public void set_sight_time(float amount) {
        this.sight_time = amount;
        Bird_parts.TEMP.sync(this.player);
    }

    @Override
    public void change_sight_time(float amount) {
        this.sight_time = Math.max(amount + sight_time, 0);
        if(this.sight_time <= 5){
            Bird_parts.TEMP.sync(this.player);
        }
    }

    @Override
    public float get_sight_time() {
        return this.sight_time;
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
    public double get_internal_temp() {
        return this.internal_temp;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.temp = tag.getDouble("temp");
        this.internal_temp = tag.getDouble("internal_temp");
        this.rebirths = tag.getInt("rebirths");
        this.sight_time = tag.getFloat("ore_see_time");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putDouble("temp", this.temp);
        tag.putDouble("internal_temp", this.internal_temp);
        tag.putInt("rebirths", this.rebirths);
        tag.putFloat("ore_see_time", this.sight_time);
    }

    //Needs to update fast.
    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity player) {
        buf.writeDouble(this.temp);
        buf.writeInt(this.rebirths);
        buf.writeFloat(this.sight_time);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.temp = buf.readDouble();
        this.rebirths = buf.readInt();
        this.sight_time = buf.readFloat();
    }
}
