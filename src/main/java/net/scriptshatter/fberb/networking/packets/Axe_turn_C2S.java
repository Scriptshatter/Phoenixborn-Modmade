package net.scriptshatter.fberb.networking.packets;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.entitys.Phoenix_axe_entity;
import net.scriptshatter.fberb.networking.Youve_got_mail;

import java.util.UUID;

public class Axe_turn_C2S {
    public static void call(MinecraftServer minecraftServer, ServerPlayerEntity player, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender sender) {
        float turn_amount = buf.readFloat();
        UUID target_uuid = buf.readUuid();
        if(player.getWorld().getEntity(target_uuid) instanceof Phoenix_axe_entity target){
            Bird_parts.PHOENIX_AXE_NBT.get(target).change_turn(turn_amount);
        }
    }

    public static void turn(float turn_amount, UUID uuid){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeFloat(turn_amount);
        buf.writeUuid(uuid);

        ClientPlayNetworking.send(Youve_got_mail.AXE_TURN, buf);
    }
}
