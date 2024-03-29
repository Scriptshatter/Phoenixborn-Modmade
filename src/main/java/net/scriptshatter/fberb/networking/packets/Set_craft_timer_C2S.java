package net.scriptshatter.fberb.networking.packets;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.scriptshatter.fberb.blocks.Machine;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.networking.Youve_got_mail;
import net.scriptshatter.fberb.recipe.Turn_blast_recipe;

import java.util.Optional;

public class Set_craft_timer_C2S {
    public static void call(MinecraftServer minecraftServer, ServerPlayerEntity player, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender sender) {
        BlockPos pos = buf.readBlockPos();
        World world = minecraftServer.getWorld(player.getWorld().getRegistryKey());
        assert world != null;
        Machine block = (Machine) world.getWorldChunk(pos).getBlockEntity(pos);
        if(block != null){
            Optional<Turn_blast_recipe> match = world.getRecipeManager().getFirstMatch(Turn_blast_recipe.Type.INSTANCE, Bird_parts.INV.get(block), world);
            match.ifPresent(turn_blast_recipe -> Bird_parts.INV.get(block).set_time(turn_blast_recipe.get_blast_time()));
        }
    }

    public static void timer(BlockPos pos){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeBlockPos(pos);

        ClientPlayNetworking.send(Youve_got_mail.TIMER, buf);
    }
}
