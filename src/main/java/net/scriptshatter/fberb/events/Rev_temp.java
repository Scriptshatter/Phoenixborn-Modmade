package net.scriptshatter.fberb.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.scriptshatter.fberb.components.Bird_parts;

public class Rev_temp implements ServerPlayerEvents.AfterRespawn{
    @Override
    public void afterRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
        if(Bird_parts.TEMP.get(newPlayer).get_temp() < 500){
            Bird_parts.TEMP.get(newPlayer).set_temp(500);
            Bird_parts.TEMP.get(oldPlayer).set_temp(500);
        }
        if(Bird_parts.TEMP.get(oldPlayer).get_temp() < 500){
            Bird_parts.TEMP.get(oldPlayer).set_temp(500);
            Bird_parts.TEMP.get(newPlayer).set_temp(500);
        }
    }
}
