package net.scriptshatter.fberb.networking.packets;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.scriptshatter.fberb.items.Phoenix_pickaxe;
import net.scriptshatter.fberb.networking.Youve_got_mail;

import java.util.ArrayList;
import java.util.List;

public class Gather_blocks_C2S {
    public static void call(MinecraftServer minecraftServer, ServerPlayerEntity player, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf buf, PacketSender sender) {
        BlockPos pos = buf.readBlockPos();
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        List<BlockPos> list = new ArrayList<>();
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                for (int k = 0; k < z; k++) {
                    list.add(pos.up(i).west(j).south(k));
                    list.add(pos.up(i).east(j).south(k));
                    list.add(pos.up(i).west(j).north(k));
                    list.add(pos.up(i).east(j).north(k));
                    list.add(pos.down(i).west(j).south(k));
                    list.add(pos.down(i).east(j).south(k));
                    list.add(pos.down(i).west(j).north(k));
                    list.add(pos.down(i).east(j).north(k));
                }
            }
        }
        buf.writeCollection(list, PacketByteBuf::writeBlockPos);
    }

    public static List<BlockPos> turn(BlockPos pos, int x, int y, int z){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());

        buf.writeBlockPos(pos);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);

        ClientPlayNetworking.send(Youve_got_mail.TURN, buf);
        return buf.readCollection(DefaultedList::ofSize, PacketByteBuf::readBlockPos);
    }
}
