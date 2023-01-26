package net.scriptshatter.fberb.networking.packets;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.scriptshatter.fberb.blocks.Machine;
import net.scriptshatter.fberb.blocks.Phoenix_block_entities;
import net.scriptshatter.fberb.components.Bird_parts;
import software.bernie.geckolib3.network.ISyncable;

import static net.scriptshatter.fberb.networking.Youve_got_mail.SET_STATUS_ID;

public class Set_status_C2S_packet {
    public static void receive(MinecraftServer minecraftServer, ServerPlayerEntity player, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender sender) {
        final Machine block = Phoenix_block_entities.MACHINE.get(player.world, buf.readBlockPos());
        final String status = buf.readString();

        if(block != null){
            Bird_parts.INV.get(block).set_status(status);
        }
    }

    public static void sync_status(String status, BlockPos pos) {

        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(pos);
        buf.writeString(status);

        ClientPlayNetworking.send(SET_STATUS_ID, buf);
    }
}
