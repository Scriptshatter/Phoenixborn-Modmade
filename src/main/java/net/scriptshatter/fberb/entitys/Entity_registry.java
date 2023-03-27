package net.scriptshatter.fberb.entitys;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;

public class Entity_registry {
    public static EntityType<Phoenix_axe_entity> PHOENIX_AXE_ENTITY;
    public static EntityType<Phoenix_shovel_entity> PHOENIX_SHOVEL_ENTITY;

    public Entity_registry(){
        PHOENIX_AXE_ENTITY = Registry.register(Registries.ENTITY_TYPE, new Identifier(Phoenix.MOD_ID, "phoenix_axe"), FabricEntityTypeBuilder.<Phoenix_axe_entity>create(SpawnGroup.MISC, Phoenix_axe_entity::new).dimensions(EntityDimensions.fixed(0.25f, 0.25f)).trackRangeBlocks(4).trackedUpdateRate(10).build());
        PHOENIX_SHOVEL_ENTITY = Registry.register(Registries.ENTITY_TYPE, new Identifier(Phoenix.MOD_ID, "phoenix_shovel"), FabricEntityTypeBuilder.<Phoenix_shovel_entity>create(SpawnGroup.MISC, Phoenix_shovel_entity::new).dimensions(EntityDimensions.fixed(0.25f, 0.25f)).trackRangeBlocks(4).trackedUpdateRate(10).build());
    }
}
