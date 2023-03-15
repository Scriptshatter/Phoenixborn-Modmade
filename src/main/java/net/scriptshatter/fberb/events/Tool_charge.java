package net.scriptshatter.fberb.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.scriptshatter.fberb.networking.packets.Change_tool_temp_C2S;

import static net.scriptshatter.fberb.Phoenix_client.power_tool;

public class Tool_charge implements ClientTickEvents.EndTick {
    @Override
    public void onEndTick(MinecraftClient client) {
        if (client.player != null && power_tool.isPressed()){
            Change_tool_temp_C2S.charge();
        }
    }
}
