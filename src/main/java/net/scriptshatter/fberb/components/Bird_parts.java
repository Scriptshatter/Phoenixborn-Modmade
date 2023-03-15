package net.scriptshatter.fberb.components;

import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.blocks.Machine;
import net.scriptshatter.fberb.entitys.Phoenix_axe_entity;

public final class Bird_parts implements EntityComponentInitializer, BlockComponentInitializer {
    //Registers the components
    public static final ComponentKey<Temp_int> TEMP =
        ComponentRegistry.getOrCreate(new Identifier(Phoenix.MOD_ID, "temp"), Temp_int.class);
    public static final ComponentKey<Phoenix_axe_interface> PHOENIX_AXE_NBT =
            ComponentRegistry.getOrCreate(new Identifier(Phoenix.MOD_ID, "phoenix_axe_turn"), Phoenix_axe_interface.class);
    public static final ComponentKey<Machine_anim_int> INV =
            ComponentRegistry.getOrCreate(new Identifier(Phoenix.MOD_ID, "machine"), Machine_anim_int.class);
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, TEMP).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(Temp::new);
        registry.beginRegistration(Phoenix_axe_entity.class, PHOENIX_AXE_NBT).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(Phoenix_axe_component::new);
    }

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.beginRegistration(Machine.class, INV).end(Machine_parts::new);
    }
}
