package net.scriptshatter.fberb.networking.packets;

import com.ibm.icu.text.BidiRun;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.blocks.Machine;
import net.scriptshatter.fberb.blocks.Phoenix_block_entities;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.components.Machine_parts;
import net.scriptshatter.fberb.networking.Youve_got_mail;

import java.util.HashMap;

public class Get_machine_inv_S2C_packet {
    // Yea, this is actually a C2S packet, was originally gonna be a S2C packet but turns out I didn't need it, so I repurposed it.
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {

    }

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

        ClientPlayNetworking.send(Youve_got_mail.MACHINE_INV_ID, buf);
    }


}
