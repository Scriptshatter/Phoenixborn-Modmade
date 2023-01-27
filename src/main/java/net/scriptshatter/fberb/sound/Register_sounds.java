package net.scriptshatter.fberb.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;

public class Register_sounds {
    public static SoundEvent DEEZ_NUTS = register_sound_event("deez_nuts", 30f);

    private static SoundEvent register_sound_event(String name, Float dist){
        Identifier id = new Identifier(Phoenix.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id, dist));
    }

    public static void help() {
        Phoenix.LOGGER.info("Help");
    }

}
