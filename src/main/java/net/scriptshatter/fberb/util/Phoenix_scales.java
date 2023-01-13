package net.scriptshatter.fberb.util;

import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import virtuoel.pehkui.api.*;

import java.util.Map;

public class Phoenix_scales {
    //What the scale changes
    private static final ScaleType[] CHANGE_SIZE_TYPES = {
            ScaleTypes.WIDTH,
            ScaleTypes.HEIGHT,
            ScaleTypes.DROPS,
            ScaleTypes.VISIBILITY
    };

    //Registers the scale modifier
    public static final ScaleModifier CHANGE_SIZE_MODIFIER = register(ScaleRegistries.SCALE_MODIFIERS,
            new TypedScaleModifier(() ->
                    Phoenix_scales.CHANGE_SIZE_TYPE));

    //Registers the scale type using the modifier
    public static final ScaleType CHANGE_SIZE_TYPE = register(ScaleRegistries.SCALE_TYPES,
            ScaleType.Builder.
                    create().
                    addDependentModifier(CHANGE_SIZE_MODIFIER).
                    affectsDimensions().
                    build());

    //Registers the ID in the namespace
    private static <T> T register(Map<Identifier, T> registry, T entry) {
        return ScaleRegistries.register(registry, new Identifier(Phoenix.MOD_ID, "change_size"), entry);
    }

    //Brings it all together.
    public static void init() {
        for (ScaleType type : CHANGE_SIZE_TYPES) {
            type.getDefaultBaseValueModifiers().add(CHANGE_SIZE_MODIFIER);
        }
    }
}
