package net.scriptshatter.fberb.networking.packets;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.scriptshatter.fberb.blocks.Machine;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.networking.Youve_got_mail;

public class Item_spit_C2S_packet {
    public static void call(MinecraftServer minecraftServer, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        BlockPos pos = packetByteBuf.readBlockPos();
        World world = minecraftServer.getWorld(player.getWorld().getRegistryKey());
        assert world != null;
        Machine block = (Machine) world.getWorldChunk(pos).getBlockEntity(pos);
        if(block != null){
            Block.dropStack(world, pos.add(0, 1.55, 0), Bird_parts.INV.get(block).get_crafted_item());
        }
    }

    public static void item_spit(BlockPos pos){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeBlockPos(pos);

        ClientPlayNetworking.send(Youve_got_mail.ITEM_SPIT, buf);
    }


}
