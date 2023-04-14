package net.scriptshatter.fberb.events;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.effects.EffectsRegistery;
import net.scriptshatter.fberb.util.GetFuel;
import net.scriptshatter.fberb.util.Get_use_case;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class Glow_blocks implements WorldRenderEvents.AfterEntities{

    private int max_amount = 20;
    public final TagKey<Block> PICK_DETECT = TagKey.of(RegistryKeys.BLOCK, new Identifier(Phoenix.MOD_ID, "pickaxe_selectable"));

    public void renderHighlights(MatrixStack matrices, WorldRenderer worldRenderer, Camera camera, List<BlockPos> positions, float tickDelta) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        OutlineVertexConsumerProvider outlineVertexConsumerProvider = MinecraftClient.getInstance().getBufferBuilders().getOutlineVertexConsumers();
        int i = 16777215;
        int y = i >> 16 & 255;
        int u = i >> 8 & 255;
        int p = i & 255;
        outlineVertexConsumerProvider.setColor(y, u, p, 255);

        BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
        BlockModelRenderer blockModelRenderer = blockRenderManager.getModelRenderer();

        for (BlockPos pos : positions) {
            BlockState blockState = camera.getFocusedEntity().getEntityWorld().getBlockState(pos);
            VertexConsumer vertexConsumer = outlineVertexConsumerProvider.getBuffer(RenderLayer.getOutline(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE));

            matrices.push();
            matrices.translate(pos.getX() - camera.getPos().getX(), pos.getY() - camera.getPos().getY(), pos.getZ() - camera.getPos().getZ());
            blockModelRenderer.render(matrices.peek(), vertexConsumer, blockState, blockRenderManager.getModel(blockState), 0.0F, 0.0F, 0.0F, 15728880, OverlayTexture.packUv(0, 3));
            matrices.pop();
        }

        outlineVertexConsumerProvider.draw();
        GetFuel.render_outline_processor((Get_use_case) worldRenderer, tickDelta);
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
    }

    List<BlockPos> blockList(BlockPos pos, int x, int y, int z, ClientWorld world){
        List<BlockPos> list = new ArrayList<>();
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                for (int k = 0; k < z; k++) {
                    add_block(list, pos.up(i).west(j).south(k), world);
                    add_block(list, pos.up(i).east(j).south(k), world);
                    add_block(list, pos.up(i).west(j).north(k), world);
                    add_block(list, pos.up(i).east(j).north(k), world);
                    add_block(list, pos.down(i).west(j).south(k), world);
                    add_block(list, pos.down(i).east(j).south(k), world);
                    add_block(list, pos.down(i).west(j).north(k), world);
                    add_block(list, pos.down(i).east(j).north(k), world);
                }
            }
        }
        return list;
    }

    void add_block(List<BlockPos> list, BlockPos pos, ClientWorld world){
        if(world.getBlockState(pos).isIn(PICK_DETECT)){
            list.add(pos);
        }
    }

    @Override
    public void afterEntities(WorldRenderContext context) {
        if(MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.hasStatusEffect(EffectsRegistery.ORE_SIGHT)){
            int amp = Objects.requireNonNull(MinecraftClient.getInstance().player.getStatusEffect(EffectsRegistery.ORE_SIGHT)).getAmplifier()+1;
            renderHighlights(context.matrixStack(), context.worldRenderer(), context.camera(), blockList(context.camera().getBlockPos(), Math.min(amp, max_amount)*5, Math.min(amp, max_amount)*5, Math.min(amp, max_amount)*5, context.world()), context.tickDelta());
            //Phoenix.LOGGER.info("FPS: " + MinecraftClient.getInstance().getCurrentFps() + "\nMax Amount: " + this.max_amount);
            //Phoenix.LOGGER.info(String.valueOf(context.tickDelta()));
            if(MinecraftClient.getInstance().getCurrentFps() <= 5){
                this.max_amount--;
            }
            if(MinecraftClient.getInstance().getCurrentFps() <= 30 && context.tickDelta() <= 0.05){
                this.max_amount--;
            } else if (MinecraftClient.getInstance().getCurrentFps() >= 60 && context.tickDelta() <= 0.1) {
                this.max_amount++;
            }
        }
    }
}
