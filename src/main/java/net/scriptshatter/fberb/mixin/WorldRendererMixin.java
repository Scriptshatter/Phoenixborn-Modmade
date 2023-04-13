package net.scriptshatter.fberb.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.WorldRenderer;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.util.Get_use_case;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements Get_use_case {

    @Override
    public void render_processor(float tickDelta) {
        if(entityOutlinePostProcessor != null){
            this.entityOutlinePostProcessor.render(tickDelta);
            this.client.getFramebuffer().beginWrite(false);
        }
    }

    @Shadow
    private PostEffectProcessor entityOutlinePostProcessor;

    @Final
    @Shadow
    private MinecraftClient client;
}

