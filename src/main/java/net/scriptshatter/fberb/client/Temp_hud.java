package net.scriptshatter.fberb.client;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.apace100.apoli.screen.GameHudRender;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.util.Ect;

public class Temp_hud extends DrawableHelper implements GameHudRender {
    //SWING YOUR ARMS FROM SIDE TO SIDE
    private static final Identifier MARIO = new Identifier(Phoenix.MOD_ID, "textures/do_the_mario/the_mario.png");
    private static final Identifier ICON = new Identifier(Phoenix.MOD_ID, "textures/do_the_mario/phoenix.png");

    @Override
    @Environment(EnvType.CLIENT)
    public void render(MatrixStack matrixStack, float tickDelta) {
        //Make the temperature meter
        MinecraftClient client = MinecraftClient.getInstance();
        assert client.player != null;
        if(Ect.has_origin(client.player, Ect.FIRE_BIRD) || Ect.has_origin(client.player, Ect.FROST_BIRD)) {
            //Get the data needed
            int temp = 0;
            int width = 0;
            int height = 0;
            int temp_pos;
            int rebirths = 0;
            if (client.player != null) {
                temp = Bird_parts.TEMP.get(client.player).get_temp();
                width = client.getWindow().getScaledWidth() / 2;
                height = client.getWindow().getScaledHeight();
                rebirths = Bird_parts.TEMP.get(client.player).get_rebirths();
            }
            float color = (float) temp / 1000;
            //If you have your attack indicator set to hotbar.
            if(client.options.getAttackIndicator().getValue() != AttackIndicator.HOTBAR){
                temp_pos = width + 95;
            }
            else{
                temp_pos = width - 310;
            }

            //Make the empty temperature bar appear
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, MARIO);
            drawTexture(matrixStack, temp_pos, height - 27, 0, 0, 32, 32, 32, 64);



            //Fills the bar up depending on how much temp you have.
            RenderSystem.setShaderColor(color, (1 - color) / 5, 1 - color, 1);
            drawTexture(matrixStack, temp_pos, height - 27, 0, 32, (int) (32 * color), 32, 32, 64);


            //Display rebirths
            if(Ect.has_origin(client.player, Ect.FIRE_BIRD) && rebirths != 0){
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, ICON);
                drawTexture(matrixStack, 5, 2,0,0,16,16,16,16);
                drawCenteredText(matrixStack, client.textRenderer, String.valueOf(rebirths), 30, 7, 0xFFFFFF);
            }
        }
    }
}
