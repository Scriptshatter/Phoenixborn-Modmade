package net.scriptshatter.fberb;

import io.github.apace100.apoli.screen.GameHudRender;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.scriptshatter.fberb.blocks.Phoenix_block_entities;
import net.scriptshatter.fberb.blocks.client.Machine_render;
import net.scriptshatter.fberb.client.Temp_hud;
import net.scriptshatter.fberb.items.Items;
import net.scriptshatter.fberb.items.client.Machine_item_render;
import net.scriptshatter.fberb.networking.Youve_got_mail;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class Phoenix_client implements ClientModInitializer {
    //Registers the temperature bar.
    @Override
    public void onInitializeClient() {
        GameHudRender.HUD_RENDERS.add(new Temp_hud());
        BlockEntityRendererRegistry.register(Phoenix_block_entities.MACHINE, Machine_render::new);
        GeoItemRenderer.registerItemRenderer(Items.MACHINE_ITEM, new Machine_item_render());
        Youve_got_mail.registerS2CMail();
    }
}
