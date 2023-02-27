package net.scriptshatter.fberb.blocks.client;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.blocks.Machine;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.networking.packets.Get_my_brain_C2S;
import software.bernie.example.client.renderer.armor.WolfArmorRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Machine_render extends GeoBlockRenderer<Machine> {
    public Machine_render(BlockEntityRendererFactory.Context context) {
        super(new Machine_model());
        addRenderLayer(new Machine_item_grid(this));
    }


    @Override
    public void actuallyRender(MatrixStack poseStack, Machine animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Get_my_brain_C2S.turn(animatable.getPos());
        float speed = Bird_parts.INV.get(this.animatable).get_turn();
        this.model.getBone("bone4").ifPresent(turnwheel -> turnwheel.setRotX(-speed));
        this.model.getBone("bone2").ifPresent(turnwheel -> turnwheel.setRotZ(speed));
        this.model.getBone("bone3").ifPresent(turnwheel -> turnwheel.setRotZ(-speed));

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
