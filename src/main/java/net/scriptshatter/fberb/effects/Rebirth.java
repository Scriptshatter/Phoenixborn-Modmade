package net.scriptshatter.fberb.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.util.Ect;

public class Rebirth extends StatusEffect {

    protected Rebirth(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(Ect.has_origin(entity, Ect.FIRE_BIRD)){
            Bird_parts.TEMP.get(entity).change_temp((amplifier+1)*5);
        }
        if (entity.getHealth() < entity.getMaxHealth()) {
            entity.heal(0.1f);
        }
        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
