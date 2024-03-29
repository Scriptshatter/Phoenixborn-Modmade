package net.scriptshatter.fberb.effects;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;

import java.util.UUID;


public class EffectsRegistery {
    public static StatusEffect REBIRTH;
    public static StatusEffect ORE_SIGHT;
    private static final UUID rebirth = new UUID((long) 122245355563.234222243, (long) 277778472888.24234244);
    private static final UUID rebirth2 = new UUID((long) 1345345345345.3453453, (long) 345345335434534.3534534);
    private static StatusEffect register(String id, StatusEffect entry) {
        return Registry.register(Registries.STATUS_EFFECT, new Identifier(Phoenix.MOD_ID, id), entry);
    }

    public static void registerEffects() {
        REBIRTH = register("rebirth", new Rebirth(StatusEffectCategory.BENEFICIAL, 16742144).
                addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, rebirth.toString(), 3.0f, EntityAttributeModifier.Operation.ADDITION).
                addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, rebirth2.toString(), 0.5, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
        ORE_SIGHT = register("ore_sight", new EffectBasic(StatusEffectCategory.BENEFICIAL, 6579300));
    }
}
