package net.scriptshatter.fberb.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.util.Ect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity {



    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    //Checks if they're a phoenix and if they're cold. Lowers they're mining speed if so.
    @Inject(method = "getBlockBreakingSpeed", at = @At("RETURN"), cancellable = true)
    void slowCold(BlockState block, CallbackInfoReturnable<Float> cir){
        final float temp = (Bird_parts.TEMP.get(this).get_temp());

        if(temp <= 500 && Ect.has_origin((Entity) (Object) this, Ect.FIRE_BIRD))
        {
            cir.setReturnValue(cir.getReturnValueF() * ((temp*2)/1000));
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    void rage_break(CallbackInfo ci){

    }

}
