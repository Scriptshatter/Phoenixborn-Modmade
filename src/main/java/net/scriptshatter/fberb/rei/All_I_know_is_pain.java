package net.scriptshatter.fberb.rei;

import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;

public class All_I_know_is_pain implements BuiltinPlugin, REIServerPlugin {
    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(Category_turnblast.TURNBLAST_DISPLAY, Display_turnblast.cerializer());
    }
}
