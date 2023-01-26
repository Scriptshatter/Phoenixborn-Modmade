package net.scriptshatter.fberb.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.networking.packets.Get_machine_inv_S2C_packet;
import net.scriptshatter.fberb.networking.packets.Set_status_C2S_packet;

public class Youve_got_mail {
    public static final Identifier MACHINE_INV_ID = new Identifier(Phoenix.MOD_ID, "machine_inv");
    public static final Identifier SET_STATUS_ID = new Identifier(Phoenix.MOD_ID, "machine_stat");

    public static void registerS2CMail(){
        ClientPlayNetworking.registerGlobalReceiver(MACHINE_INV_ID, Get_machine_inv_S2C_packet::receive);
    }

    public static void registerC2SMail(){
        ServerPlayNetworking.registerGlobalReceiver(SET_STATUS_ID, Set_status_C2S_packet::receive);
    }
}
