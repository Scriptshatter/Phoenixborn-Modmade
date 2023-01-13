package net.scriptshatter.fberb.effects;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.scriptshatter.fberb.Phoenix;

import java.util.UUID;


public class EffectsRegistery {
    public static StatusEffect REBIRTH;
    private static final UUID rebirth = new UUID((long) 122245355563.234222243, (long) 277778472888.24234244);
    private static final UUID rebirth2 = new UUID((long) 1345345345345.3453453, (long) 345345335434534.3534534);

    public static StatusEffect registerStatusEffect(String name){
        return Registry.register(Registry.STATUS_EFFECT, new Identifier(Phoenix.MOD_ID, name),
                new Rebirth(StatusEffectCategory.BENEFICIAL, 16742144).
                        addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, rebirth.toString(), 3.0f, EntityAttributeModifier.Operation.ADDITION).
                        addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, rebirth2.toString(), 0.5, EntityAttributeModifier.Operation.MULTIPLY_TOTAL));
    }

    public static void registerEffects() {
        REBIRTH = registerStatusEffect("rebirth");
    }
}
