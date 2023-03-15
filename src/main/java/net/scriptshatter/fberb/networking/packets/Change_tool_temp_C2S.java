package net.scriptshatter.fberb.networking.packets;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.items.Birb_item;
import net.scriptshatter.fberb.networking.Youve_got_mail;
import net.scriptshatter.fberb.util.Ect;

public class Change_tool_temp_C2S {
    public static void call(MinecraftServer minecraftServer, ServerPlayerEntity player, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender sender) {
        if(player.getHandItems() != null && Ect.has_origin(player, Ect.FIRE_BIRD)){
            player.getHandItems().forEach(itemStack -> {
                if(Bird_parts.TEMP.get(player).get_temp() > 50 && itemStack.getItem() instanceof Birb_item birb_item && birb_item.temp(itemStack) < birb_item.max_temp()){
                    birb_item.change_temp(1, itemStack);
                    Bird_parts.TEMP.get(player).change_temp(-1);
                }
            });
        }
    }

    public static void charge(){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());



        ClientPlayNetworking.send(Youve_got_mail.CHANGE_TOOL_TEMP, buf);
    }
}
