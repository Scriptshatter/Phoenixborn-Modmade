package net.scriptshatter.fberb.items.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.blocks.Machine;
import net.scriptshatter.fberb.blocks.client.Machine_model;
import net.scriptshatter.fberb.items.Machine_item;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import javax.annotation.Nullable;

public class Machine_item_render extends GeoItemRenderer<Machine_item> {
    public Machine_item_render() {
        super(new Machine_item_model());
    }

    @Override
    public RenderLayer getRenderType(Machine_item animatable, float partialTick, MatrixStack poseStack,
                                     @Nullable VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, int packedLight,
                                     Identifier texture){
        return RenderLayer.getEntityTranslucent(getTextureLocation(animatable));
    }
}
