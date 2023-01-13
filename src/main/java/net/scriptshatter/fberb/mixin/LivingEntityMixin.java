package net.scriptshatter.fberb.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.World;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.util.Ect;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow
    public abstract @Nullable StatusEffectInstance getStatusEffect(StatusEffect effect);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }


    //Checks if they're a phoenix and their cold, basically. Lowers the speed at which they swing their fists if so.
    @Inject(method = "getHandSwingDuration", at = @At("HEAD"), cancellable = true)
    void slowCold(CallbackInfoReturnable<Integer> cir){
        if(Ect.has_origin(this, Ect.FIRE_BIRD) && Bird_parts.TEMP.get(this).get_temp() <= 500){
            int duration = 6 + (Bird_parts.TEMP.get(this).get_temp()/100);
            if (StatusEffectUtil.hasHaste(LivingEntity.class.cast(this))) {
                duration = duration - (1 + StatusEffectUtil.getHasteAmplifier(LivingEntity.class.cast(this)));
            }
            else if (this.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
                duration = duration + (1 + Objects.requireNonNull(this.getStatusEffect(StatusEffects.MINING_FATIGUE)).getAmplifier()) * 2;
            }
            cir.setReturnValue(duration);
        }
    }

}
