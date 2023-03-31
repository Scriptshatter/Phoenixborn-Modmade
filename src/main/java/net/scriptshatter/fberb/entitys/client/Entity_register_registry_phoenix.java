package net.scriptshatter.fberb.entitys.client;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.scriptshatter.fberb.entitys.Entity_registry;

public class Entity_register_registry_phoenix {

    public static void register(){
        EntityRendererRegistry.register(Entity_registry.PHOENIX_AXE_ENTITY, Phoenix_axe_entity_renderer::new);
    }

}
