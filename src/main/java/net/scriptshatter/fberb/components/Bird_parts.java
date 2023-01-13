package net.scriptshatter.fberb.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;

public final class Bird_parts implements EntityComponentInitializer {
    //Registers the components
    public static final ComponentKey<Temp_int> TEMP =
        ComponentRegistry.getOrCreate(new Identifier(Phoenix.MOD_ID, "temp"), Temp_int.class);
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, TEMP).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(Temp::new);
    }
}
