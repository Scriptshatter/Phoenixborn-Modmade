package net.scriptshatter.fberb.networking.packets;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.scriptshatter.fberb.networking.Youve_got_mail;
import org.joml.Vector3f;

public class Render_line_S2C {
    public static void call(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        
    }

    public static void render_line(Vec3d start, Vec3d end, float thickness, ServerPlayerEntity player){
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());


        buf.writeDouble(start.x);
        buf.writeDouble(start.y);
        buf.writeDouble(start.z);

        buf.writeDouble(end.x);
        buf.writeDouble(end.y);
        buf.writeDouble(end.z);

        buf.writeFloat(thickness);

        ServerPlayNetworking.send(player, Youve_got_mail.RENDER_LINE, buf);
    }
}
