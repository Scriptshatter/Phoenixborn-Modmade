package net.scriptshatter.fberb.util;

import io.github.apace100.origins.Origins;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.origin.OriginRegistry;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;

public class Ect {
    //Gets the origins ready and makes a condition that checks if you have a particular origin.
    public static final Identifier fire_id = new Identifier(Phoenix.MOD_ID, "phoenix");
    public static final Identifier frost_id = new Identifier(Phoenix.MOD_ID, "frost");
    public static final Origin FIRE_BIRD = OriginRegistry.get(fire_id);
    public static final Origin FROST_BIRD = OriginRegistry.get(frost_id);

    public static boolean has_origin(Entity entity, Origin origin){
        if(!entity.isPlayer() || ModComponents.ORIGIN.get(entity).getOrigin(OriginLayers.getLayer(new Identifier(Origins.MODID, "origin"))) == null){
            return false;
        }
        return entity.isPlayer() && ModComponents.ORIGIN.get(entity).getOrigin(OriginLayers.getLayer(new Identifier(Origins.MODID, "origin"))).equals(origin);
    }
}
