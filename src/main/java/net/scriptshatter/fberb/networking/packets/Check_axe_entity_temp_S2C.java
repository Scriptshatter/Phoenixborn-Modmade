package net.scriptshatter.fberb.networking.packets;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.scriptshatter.fberb.networking.Youve_got_mail;

public class Check_axe_entity_temp_S2C {
    public static void call(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        int id = buf.readInt();
        if(client.player != null){
            Entity entity = client.player.clientWorld.getEntityById(id);
            if(entity != null) entity.discard();
        }
    }

    public static void axe_temp(int id, ServerPlayerEntity player){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeInt(id);

        ServerPlayNetworking.send(player, Youve_got_mail.CHECK_AXE_TEMP, buf);
    }
}
