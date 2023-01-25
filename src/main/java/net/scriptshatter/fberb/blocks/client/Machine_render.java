package net.scriptshatter.fberb.blocks.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.blocks.Machine;
import net.scriptshatter.fberb.blocks.Machine_anim;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import javax.annotation.Nullable;

public class Machine_render extends GeoBlockRenderer<Machine> {
    public Machine_render(BlockEntityRendererFactory.Context context) {
        super(new Machine_model());
    }

    @Override
    public RenderLayer getRenderType(Machine animatable, float partialTick, MatrixStack poseStack,
                                     @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight,
                                     Identifier texture){
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }
}
