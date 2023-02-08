package net.scriptshatter.fberb.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.scriptshatter.fberb.items.Birb_item;
import net.scriptshatter.fberb.items.TempAPI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.client.RenderProvider;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    private void renderGuiQuad(BufferBuilder buffer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex((x), (y), 0.0).color(red, green, blue, alpha).next();
        buffer.vertex((x), (y + height), 0.0).color(red, green, blue, alpha).next();
        buffer.vertex((x + width), (y + height), 0.0).color(red, green, blue, alpha).next();
        buffer.vertex((x + width), (y), 0.0).color(red, green, blue, alpha).next();
        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }

    @Inject(method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At("HEAD"))
    public void itemModelHook(TextRenderer renderer, ItemStack stack, int x, int y, String countLabel, CallbackInfo ci){
        if(!stack.isEmpty()){
            Birb_item item = TempAPI.ITEM_WITH_TEMP.find(stack, null);
            if (item != null) {
                RenderSystem.disableDepthTest();
                RenderSystem.disableTexture();
                RenderSystem.disableBlend();
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferBuilder = tessellator.getBuffer();
                int i = item.getItemBarStepTemp(stack);
                int j = item.getItemBarColorTemp(stack);
                this.renderGuiQuad(bufferBuilder, x + 2, y + 11, 13, 2, 0, 0, 0, 255);
                this.renderGuiQuad(bufferBuilder, x + 2, y + 11, i, 1, j >> 16 & 255, (j >> 8 & 255)/5, j >> 8 & 255, 255);
                RenderSystem.enableBlend();
                RenderSystem.enableTexture();
                RenderSystem.enableDepthTest();
            }
        }
    }

}
