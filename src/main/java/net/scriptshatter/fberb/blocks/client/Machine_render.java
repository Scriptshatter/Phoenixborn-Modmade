package net.scriptshatter.fberb.blocks.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.blocks.Machine;
import net.scriptshatter.fberb.blocks.Machine_anim;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.geckolib3.util.EModelRenderCycle;

import javax.annotation.Nullable;
import java.util.Optional;

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

    @Override
    public void render(BlockEntity tile, float partialTicks, MatrixStack poseStack, VertexConsumerProvider bufferSource,
                       int packedLight, int packedOverlay) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        ItemStack test = new ItemStack(Items.ENDER_EYE);
        //MatrixStack thing = (MatrixStack) doup_var(poseStack);
        //itemRenderer.renderItem(new ItemStack(Items.IRON_AXE), this.)
        GeoBone first_slot = null;
        if(this.modelProvider.getModel(new Identifier(Phoenix.MOD_ID, "geo/machine.geo.json")).getBone("backplate").isPresent() && tile.getWorld() != null){
            poseStack.push();
            first_slot = this.modelProvider.getModel(new Identifier(Phoenix.MOD_ID, "geo/machine.geo.json")).getBone("backplate").get().childBones.get(6);
            poseStack.translate(first_slot.getPivotX()/10+0.2,first_slot.getPivotY()/15.5 , first_slot.getPivotZ()/15.6+0.5);
            poseStack.scale(0.25f, 0.25f, 0.25f);
            poseStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90));
            itemRenderer.renderItem(test, ModelTransformation.Mode.GUI, get_light(tile.getWorld(), tile.getPos()), OverlayTexture.DEFAULT_UV, poseStack, bufferSource, 1);
            //Phoenix.LOGGER.info(first_slot.name);
            //Phoenix.LOGGER.info(first_slot.getPivotX() + "\n" + first_slot.getPivotY() + "\n" + first_slot.getPivotZ());
            poseStack.translate(-first_slot.getPivotX()/10, -first_slot.getPivotY()/10, -first_slot.getPivotZ()/10);
            poseStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-90));
            poseStack.pop();
        }
        render((Machine) tile, partialTicks, poseStack, bufferSource, packedLight);
    }

    private int get_light(World world, BlockPos pos){
        int bLight = world.getLightLevel(LightType.BLOCK, pos);
        int sLight = world.getLightLevel(LightType.SKY, pos);
        return LightmapTextureManager.pack(bLight, sLight);
    }

}
