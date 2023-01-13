package net.scriptshatter.fberb;

import io.github.apace100.apoli.screen.GameHudRender;
import net.fabricmc.api.ClientModInitializer;
import net.scriptshatter.fberb.client.Temp_hud;

public class Phoenix_client implements ClientModInitializer {
    //Registers the temperature bar.
    @Override
    public void onInitializeClient() {
        GameHudRender.HUD_RENDERS.add(new Temp_hud());
    }
}
