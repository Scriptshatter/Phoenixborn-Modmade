package net.scriptshatter.fberb.blocks.client;

import net.minecraft.client.render.*;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import net.scriptshatter.fberb.blocks.Machine;
import net.scriptshatter.fberb.components.Bird_parts;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

import javax.annotation.Nullable;

public class Machine_item_grid extends BlockAndItemGeoLayer<Machine> {
    public Machine_item_grid(GeoRenderer<Machine> renderer) {
        super(renderer);
    }

    @Nullable
    @Override
    protected ItemStack getStackForBone(GeoBone bone, Machine animatable) {
        // Tells the game what item belongs on what bone
        for (int i = 0; i < Bird_parts.INV.get(animatable).get_inv().size(); i++) {
            if(bone.getName().matches("slot"+i)){
                ItemStack item = Bird_parts.INV.get(animatable).get_inv().get(i);
                if(item != ItemStack.EMPTY){
                    return item;
                }
            }
        }
        return null;
    }


    @Override
    protected void renderStackForBone(MatrixStack poseStack, GeoBone bone, ItemStack stack, Machine animatable,
                                      VertexConsumerProvider bufferSource, float partialTick, int packedLight, int packedOverlay) {
        //Shrinks n' moves it.
        //Also turns it so that it looks normal.
        poseStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-90f));
        poseStack.scale(0.25f, 0.25f, 0.25f);
        poseStack.translate(0, 0 , 0.1);

        super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
    }

    @Override
    protected ModelTransformation.Mode getTransformTypeForStack(GeoBone bone, ItemStack stack, Machine animatable) {
        // Makes it look as if it were in an item frame
        if(bone.getName().startsWith("slot")){
            return ModelTransformation.Mode.FIXED;
        }
        return null;
    }

}
