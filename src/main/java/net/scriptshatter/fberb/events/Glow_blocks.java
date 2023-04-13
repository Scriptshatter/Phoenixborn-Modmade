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
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.util.GetFuel;
import net.scriptshatter.fberb.util.Get_use_case;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class Glow_blocks implements WorldRenderEvents.AfterEntities{
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
            if(!camera.getFocusedEntity().world.getBlockState(pos).isIn(PICK_DETECT)){
                continue;
            }
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

    List<BlockPos> blockList(BlockPos pos, int x, int y, int z){
        List<BlockPos> list = new ArrayList<>();
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                for (int k = 0; k < z; k++) {
                    list.add(pos.up(i).west(j).south(k));
                    list.add(pos.up(i).east(j).south(k));
                    list.add(pos.up(i).west(j).north(k));
                    list.add(pos.up(i).east(j).north(k));
                    list.add(pos.down(i).west(j).south(k));
                    list.add(pos.down(i).east(j).south(k));
                    list.add(pos.down(i).west(j).north(k));
                    list.add(pos.down(i).east(j).north(k));
                }
            }
        }
        return list;
    }

    @Override
    public void afterEntities(WorldRenderContext context) {
        renderHighlights(context.matrixStack(), context.worldRenderer(), context.camera(), blockList(context.camera().getBlockPos(), 25, 25, 25), context.tickDelta());
    }
}
