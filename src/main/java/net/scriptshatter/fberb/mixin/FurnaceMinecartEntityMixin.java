package net.scriptshatter.fberb.mixin;


import net.minecraft.entity.vehicle.FurnaceMinecartEntity;
import net.scriptshatter.fberb.util.Getdata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FurnaceMinecartEntity.class)
public abstract class FurnaceMinecartEntityMixin implements Getdata {
    //This makes the boolean
    @Shadow private int fuel;

    @Override
    public boolean has_fuel(){
        return this.fuel > 0;
    }
}
