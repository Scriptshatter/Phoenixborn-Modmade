package net.scriptshatter.fberb.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.networking.packets.*;

public class Youve_got_mail {
    public static final Identifier ITEM_SPIT = new Identifier(Phoenix.MOD_ID, "item_spit");
    public static final Identifier TURN = new Identifier(Phoenix.MOD_ID, "turn");
    public static final Identifier STATUS = new Identifier(Phoenix.MOD_ID, "status");
    public static final Identifier TIMER = new Identifier(Phoenix.MOD_ID, "timer");
    public static final Identifier AXE_TURN = new Identifier(Phoenix.MOD_ID, "axe_turn");
    public static final Identifier CHECK_AXE_TEMP = new Identifier(Phoenix.MOD_ID, "check_axe_temp");
    public static final Identifier CHANGE_TOOL_TEMP = new Identifier(Phoenix.MOD_ID, "change_tool_temp");
    public static final Identifier RENDER_LINE = new Identifier(Phoenix.MOD_ID, "render_line");
    public static final Identifier GATHER_BLOCKS = new Identifier(Phoenix.MOD_ID, "gather_blocks");

    public static void registerS2CMail(){
        ClientPlayNetworking.registerGlobalReceiver(CHECK_AXE_TEMP, Check_axe_entity_temp_S2C::call);
        ClientPlayNetworking.registerGlobalReceiver(RENDER_LINE, Render_line_S2C::call);
    }

    public static void registerC2SMail(){
        ServerPlayNetworking.registerGlobalReceiver(ITEM_SPIT, Item_spit_C2S_packet::call);
        ServerPlayNetworking.registerGlobalReceiver(TURN, Get_my_brain_C2S::call);
        ServerPlayNetworking.registerGlobalReceiver(STATUS, Set_status_C2S::call);
        ServerPlayNetworking.registerGlobalReceiver(TIMER, Set_craft_timer_C2S::call);
        ServerPlayNetworking.registerGlobalReceiver(AXE_TURN, Axe_turn_C2S::call);
        ServerPlayNetworking.registerGlobalReceiver(CHANGE_TOOL_TEMP, Change_tool_temp_C2S::call);
        ServerPlayNetworking.registerGlobalReceiver(GATHER_BLOCKS, Gather_blocks_C2S::call);
    }
}
