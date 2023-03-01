package net.scriptshatter.fberb;

import io.github.apace100.apoli.screen.GameHudRender;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.fabricmc.fabric.impl.client.rendering.BuiltinItemRendererRegistryImpl;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.resource.ResourceReloader;
import net.scriptshatter.fberb.blocks.Phoenix_block_entities;
import net.scriptshatter.fberb.blocks.Phoenix_blocks;
import net.scriptshatter.fberb.blocks.client.Machine_render;
import net.scriptshatter.fberb.client.Temp_hud;
import net.scriptshatter.fberb.entitys.client.Entity_register_registry_phoenix;
import net.scriptshatter.fberb.items.Items;
import net.scriptshatter.fberb.networking.Youve_got_mail;
import software.bernie.example.GeckoLibMod;
import software.bernie.example.client.renderer.block.FertilizerBlockRenderer;
import software.bernie.example.registry.BlockEntityRegistry;
import software.bernie.example.registry.BlockRegistry;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class Phoenix_client implements ClientModInitializer {
    //Registers the temperature bar.
    @Override
    public void onInitializeClient() {
        GameHudRender.HUD_RENDERS.add(new Temp_hud());
        //BlockEntityRendererRegistry.register(Phoenix_block_entities.MACHINE, Machine_render::new);
        //GeoItemRenderer.registerItemRenderer(Items.MACHINE_ITEM, new Machine_item_render());

        Youve_got_mail.registerS2CMail();
        BlockEntityRendererFactories.register(Phoenix_block_entities.MACHINE, Machine_render::new);
        Entity_register_registry_phoenix.register();
        BlockRenderLayerMapImpl.INSTANCE.putBlock(Phoenix_blocks.MACHINE, RenderLayer.getTranslucent());
    }
}
