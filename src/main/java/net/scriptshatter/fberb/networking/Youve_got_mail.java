package net.scriptshatter.fberb.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.networking.packets.Get_machine_inv_S2C_packet;

public class Youve_got_mail {
    public static final Identifier MACHINE_INV_ID = new Identifier(Phoenix.MOD_ID, "machine_inv");

    public static void registerS2CMail(){
        ClientPlayNetworking.registerGlobalReceiver(MACHINE_INV_ID, Get_machine_inv_S2C_packet::receive);
    }

    public static void registerC2SMail(){
    }
}
