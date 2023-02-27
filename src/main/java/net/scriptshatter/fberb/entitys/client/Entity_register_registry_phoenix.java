package net.scriptshatter.fberb.entitys.client;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.entitys.Entity_registry;

public class Entity_register_registry_phoenix {

    public static final EntityModelLayer MODEL_CUBE_LAYER = new EntityModelLayer(new Identifier("entitytesting", "cube"), "main");

    public static void register(){
        // In 1.17, use EntityRendererRegistry.register (seen below) instead of EntityRendererRegistry.INSTANCE.register (seen above)
        EntityRendererRegistry.register(Entity_registry.PHOENIX_AXE, Phoenix_axe_entity_renderer::new);

    }

}
