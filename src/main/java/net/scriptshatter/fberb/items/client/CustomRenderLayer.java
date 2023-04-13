package net.scriptshatter.fberb.items.client;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;

public class CustomRenderLayer {

    public static final RenderLayer GLOWING_BLOCK = RenderLayer.of("glowing_block", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT, VertexFormat.DrawMode.LINES, 256, RenderLayer.MultiPhaseParameters.builder().transparency(RenderPhase.TRANSLUCENT_TRANSPARENCY).target(RenderPhase.ITEM_TARGET).texture(RenderPhase.BLOCK_ATLAS_TEXTURE).cull(RenderPhase.DISABLE_CULLING).lightmap(RenderPhase.ENABLE_LIGHTMAP).overlay(RenderPhase.ENABLE_OVERLAY_COLOR).build(false));
}
