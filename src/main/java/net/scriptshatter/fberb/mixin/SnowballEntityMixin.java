package net.scriptshatter.fberb.mixin;

import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.util.Ect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowballEntity.class)
public abstract class SnowballEntityMixin {

    //Fun fact: Snowballs are cold!
    @Inject(method = "onEntityHit", at = @At("TAIL"))
    protected void makeCold(EntityHitResult entityHitResult, CallbackInfo ci){
        if(Ect.has_origin(entityHitResult.getEntity(), Ect.FIRE_BIRD) || Ect.has_origin(entityHitResult.getEntity(), Ect.FROST_BIRD)){
            Bird_parts.TEMP.get(entityHitResult.getEntity()).change_temp(-10);
        }
    }

}
