package net.scriptshatter.fberb.events;

import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.util.GetFuel;
import net.scriptshatter.fberb.util.Get_use_case;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Environment(EnvType.CLIENT)
public class Glow_blocks implements WorldRenderEvents.AfterEntities{

    private static final Identifier GLOW_TEXTURE = new Identifier("textures/block/stone.png");
    private static final float GLOW_ALPHA = 0.25f;
    private static final float GLOW_SCALE = 1.1f;
    public final TagKey<Block> PICK_DETECT = TagKey.of(RegistryKeys.BLOCK, new Identifier(Phoenix.MOD_ID, "pickaxe_selectable"));

    Model gamer = new Model(RenderLayer::getOutline) {
        @Override
        public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
            matrices.scale(GLOW_SCALE, GLOW_SCALE, GLOW_SCALE);
            //VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getOutline(GLOW_TEXTURE));
            Matrix4f matrix = matrices.peek().getPositionMatrix();
            float time = (System.currentTimeMillis() % 1000) / 1000f;

            for (int i = 0; i < 6; i++) {
                vertices.vertex(matrix, -0.5f, -0.5f, -0.5f).texture(time + i * 0.16f, 0).color(1.0f, 1.0f, 1.0f, GLOW_ALPHA).light(240, 240).normal(0.0f, 0.0f, 0.0f).overlay(0, 1).next();
                vertices.vertex(matrix, -0.5f, -0.5f, 0.5f).texture(time + i * 0.16f, 1).color(1.0f, 1.0f, 1.0f, GLOW_ALPHA).light(240, 240).normal(0.0f, 0.0f, 0.0f).overlay(0, 1).next();
                vertices.vertex(matrix, 0.5f, -0.5f, 0.5f).texture(time + i * 0.16f + 0.16f, 1).color(1.0f, 1.0f, 1.0f, GLOW_ALPHA).light(240, 240).normal(0.0f, 0.0f, 0.0f).overlay(0, 1).next();
                vertices.vertex(matrix, 0.5f, -0.5f, -0.5f).texture(time + i * 0.16f + 0.16f, 0).color(1.0f, 1.0f, 1.0f, GLOW_ALPHA).light(240, 240).normal(0.0f, 0.0f, 0.0f).overlay(0, 1).next();
            }

            matrices.pop();
        }
    };

    public void renderHighlights(MatrixStack matrices, WorldRenderer worldRenderer, Camera camera, List<BlockPos> positions, float tickDelta, ClientWorld world) {

        RenderSystem.setShaderTexture(0, GLOW_TEXTURE);
        AtomicBoolean shouldRenderShader = new AtomicBoolean();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        shouldRenderShader.lazySet(true);
        world.getEntities().forEach(entity -> {
            if(MinecraftClient.getInstance().hasOutline(entity)){
                shouldRenderShader.lazySet(false);
            }
        });
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
            //gamer.render(matrices, outlineVertexConsumerProvider.getBuffer(RenderLayer.getOutline(GLOW_TEXTURE)), 15728880, OverlayTexture.DEFAULT_UV, 1f, 1f, 1f, 1f);
            matrices.pop();
        }

        if(shouldRenderShader.get()){
            outlineVertexConsumerProvider.draw();
            GetFuel.render_outline_processor((Get_use_case) worldRenderer, tickDelta);
        }

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
        renderHighlights(context.matrixStack(), context.worldRenderer(), context.camera(), blockList(context.camera().getBlockPos(), 25, 25, 25), context.tickDelta(), context.world());
    }
}
