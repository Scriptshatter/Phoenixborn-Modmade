package net.scriptshatter.fberb.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.scriptshatter.fberb.util.Dmg_sources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    public void not_even_creative_can_save_you_from_stupidity(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir){
        if(damageSource == Dmg_sources.STUPID){
            cir.setReturnValue(false);
        }
    }
}
