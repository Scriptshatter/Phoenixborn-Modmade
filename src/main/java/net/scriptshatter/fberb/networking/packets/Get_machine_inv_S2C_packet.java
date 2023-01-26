package net.scriptshatter.fberb.networking.packets;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.scriptshatter.fberb.blocks.Machine;
import net.scriptshatter.fberb.components.Machine_parts;

import java.util.HashMap;

public class Get_machine_inv_S2C_packet {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        int size = buf.readInt();
        HashMap<Integer, ItemStack> list = Machine_parts.machine_template();
        for (int i = 0; i < size; i++) {
            list.put(i, buf.readItemStack());
        }
        BlockPos position = buf.readBlockPos();

        assert client.world != null;
        if(client.world.getBlockEntity(position) instanceof Machine machine){
            machine.setInventory(list);
        }
    }
}
