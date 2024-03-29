package net.scriptshatter.fberb.entitys.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.entitys.Phoenix_axe_entity;
import net.scriptshatter.fberb.networking.packets.Axe_turn_C2S;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class Phoenix_axe_entity_renderer extends GeoEntityRenderer<Phoenix_axe_entity> {
    protected Phoenix_axe_entity_renderer(EntityRendererFactory.Context ctx) {
        super(ctx, new Phoenix_axe_entity_model());
    }

    @Override
    public void actuallyRender(MatrixStack poseStack, Phoenix_axe_entity animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if(animatable.should_spin()){
            Axe_turn_C2S.turn(0.5f, animatable.getUuid());
        }
        this.model.getBone("bone").ifPresent(axe -> {
            if(!MinecraftClient.getInstance().isPaused()){
                axe.setRotX(Bird_parts.PHOENIX_AXE_NBT.get(animatable).get_turn());
            }
        });
        VertexConsumer buffer1 = animatable.isEnchanted() ? VertexConsumers.union(bufferSource.getBuffer(RenderLayer.getDirectGlint()), bufferSource.getBuffer(renderType)) : buffer;
        poseStack.scale(1.1f, 1.1f, 1.1f);
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(partialTick, animatable.prevYaw, animatable.getYaw())));
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer1, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
