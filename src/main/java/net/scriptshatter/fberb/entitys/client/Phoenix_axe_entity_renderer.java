package net.scriptshatter.fberb.entitys.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.entitys.Phoenix_axe_entity;
import net.scriptshatter.fberb.networking.packets.Axe_turn_C2S;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import virtuoel.pehkui.api.PehkuiConfig;

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
            axe.setRotX(Bird_parts.PHOENIX_AXE_NBT.get(animatable).get_turn());
        });
        poseStack.scale(1.1f, 1.1f, 1.1f);
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(partialTick, animatable.prevYaw, animatable.getYaw())));
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
