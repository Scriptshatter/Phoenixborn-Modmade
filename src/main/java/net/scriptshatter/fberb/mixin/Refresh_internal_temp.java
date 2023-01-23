package net.scriptshatter.fberb.mixin;


import io.github.apace100.origins.Origins;
import io.github.apace100.origins.component.PlayerOriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.util.Ect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;

@Mixin(PlayerOriginComponent.class)
public abstract class Refresh_internal_temp {
    @Shadow(remap = false)
    private PlayerEntity player;


    //Resets everything, though only if it's the Origin origin layer that's being reset.
    @Inject(method = "setOrigin", at = @At("HEAD"), remap = false)
    public void setorigin(OriginLayer layer, Origin origin, CallbackInfo ci) {
        if(player.isPlayer() && layer.equals(OriginLayers.getLayer(new Identifier(Origins.MODID, "origin")))){
            if(origin.equals(Ect.FIRE_BIRD)){
                Bird_parts.TEMP.get(player).set_internal_temp(0.7);
                Bird_parts.TEMP.get(player).set_temp(500);
                Bird_parts.TEMP.get(player).add_rebirths(-Bird_parts.TEMP.get(player).get_rebirths());
            }
            else if(origin.equals(Ect.FROST_BIRD)){
                Bird_parts.TEMP.get(player).set_internal_temp(0.4);
                Bird_parts.TEMP.get(player).set_temp(500);
            }
            else {
                ScaleType scaleType = ScaleRegistries.SCALE_TYPES.get(new Identifier(Phoenix.MOD_ID, "change_size"));
                ScaleData data = scaleType.getScaleData(player);
                data.setScale(1f);
            }
        }
    }
}
