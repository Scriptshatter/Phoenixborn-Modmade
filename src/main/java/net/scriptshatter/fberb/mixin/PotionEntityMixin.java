package net.scriptshatter.fberb.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.util.Ect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin extends Entity {

    public PotionEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    //Basically, this only happens if your splashed with water. I was to lazy to inject a method that would do every potion.
    @Inject(method = "applyWater", at = @At("TAIL"))
    void CoolDown(CallbackInfo ci){
        Box box2 = this.getBoundingBox().expand(4.0, 2.0, 4.0);
        if(!this.world.isClient()){
            List<ServerPlayerEntity> list3 = this.world.getNonSpectatingEntities(ServerPlayerEntity.class, box2);
            for (ServerPlayerEntity player : list3) {
                if(Ect.has_origin(player, Ect.FIRE_BIRD) || Ect.has_origin(player, Ect.FROST_BIRD)){
                    Bird_parts.TEMP.get(player).change_temp(-15);
                }
            }
        }
    }

}
