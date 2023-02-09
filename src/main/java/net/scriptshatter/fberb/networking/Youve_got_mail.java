package net.scriptshatter.fberb.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.networking.packets.*;

public class Youve_got_mail {
    public static final Identifier MACHINE_INV_ID = new Identifier(Phoenix.MOD_ID, "machine_inv");
    public static final Identifier TURN = new Identifier(Phoenix.MOD_ID, "turn");
    public static final Identifier STATUS = new Identifier(Phoenix.MOD_ID, "status");
    public static final Identifier TIMER = new Identifier(Phoenix.MOD_ID, "timer");
    public static final Identifier CRAFT = new Identifier(Phoenix.MOD_ID, "craft");

    public static void registerS2CMail(){
        //ServerPlayNetworking.registerGlobalReceiver(MACHINE_INV_ID, Get_machine_inv_S2C_packet::call);
    }

    public static void registerC2SMail(){
        ServerPlayNetworking.registerGlobalReceiver(MACHINE_INV_ID, Get_machine_inv_S2C_packet::call);
        ServerPlayNetworking.registerGlobalReceiver(TURN, Get_my_brain_C2S::call);
        ServerPlayNetworking.registerGlobalReceiver(STATUS, Set_status_C2S::call);
        ServerPlayNetworking.registerGlobalReceiver(TIMER, Set_craft_timer_C2S::call);
        ServerPlayNetworking.registerGlobalReceiver(CRAFT, Finish_craft_C2S::call);
    }
}
