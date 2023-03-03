package net.scriptshatter.fberb.gui;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.scriptshatter.fberb.Phoenix;

public class Tutorial_screen extends CottonClientScreen {
    public Tutorial_screen(GuiDescription description) {
        super(description);
    }

    public static void create(){
        Phoenix.LOGGER.info("Setting up screen");
    }
}
