package net.scriptshatter.fberb.entitys.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumers;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.entitys.Phoenix_axe_entity;
import net.scriptshatter.fberb.entitys.Phoenix_shovel_entity;
import net.scriptshatter.fberb.networking.packets.Axe_turn_C2S;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class Phoenix_shovel_entity_renderer extends GeoEntityRenderer<Phoenix_shovel_entity> {
    public Phoenix_shovel_entity_renderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new DefaultedEntityGeoModel<>(new Identifier(Phoenix.MOD_ID, "phoenix_shovel"), false));
    }


    @Override
    public void actuallyRender(MatrixStack poseStack, Phoenix_shovel_entity animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        VertexConsumer buffer1 = animatable.isEnchanted() ? VertexConsumers.union(bufferSource.getBuffer(RenderLayer.getDirectGlint()), bufferSource.getBuffer(renderType)) : buffer;
        poseStack.scale(1.1f, 1.1f, 1.1f);
        poseStack.translate(0, -0.1, 0);
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(partialTick, animatable.prevYaw, animatable.getYaw())));
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer1, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
