package net.scriptshatter.fberb;

import io.github.apace100.apoli.screen.GameHudRender;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.scriptshatter.fberb.blocks.Phoenix_block_entities;
import net.scriptshatter.fberb.blocks.Phoenix_blocks;
import net.scriptshatter.fberb.blocks.client.Machine_render;
import net.scriptshatter.fberb.client.Temp_hud;
import net.scriptshatter.fberb.entitys.client.Entity_register_registry_phoenix;
import net.scriptshatter.fberb.events.Glow_blocks;
import net.scriptshatter.fberb.events.Tool_charge;
import net.scriptshatter.fberb.gui.Tutorial_screen;
import net.scriptshatter.fberb.networking.Youve_got_mail;
import org.lwjgl.glfw.GLFW;

public class Phoenix_client implements ClientModInitializer {
    //Registers the temperature bar.

    public static KeyBinding power_tool;
    @Override
    public void onInitializeClient() {
        Tutorial_screen.create();
        GameHudRender.HUD_RENDERS.add(new Temp_hud());
        WorldRenderEvents.AFTER_ENTITIES.register(new Glow_blocks());

        Youve_got_mail.registerS2CMail();
        BlockEntityRendererFactories.register(Phoenix_block_entities.MACHINE, Machine_render::new);
        Entity_register_registry_phoenix.register();
        ClientTickEvents.END_CLIENT_TICK.register(new Tool_charge());
        power_tool = new KeyBinding("key.fberb.power_tool", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_BACKSLASH, "category." + Phoenix.MOD_ID);
        KeyBindingHelper.registerKeyBinding(power_tool);
        BlockRenderLayerMapImpl.INSTANCE.putBlock(Phoenix_blocks.MACHINE, RenderLayer.getTranslucent());
    }
}
