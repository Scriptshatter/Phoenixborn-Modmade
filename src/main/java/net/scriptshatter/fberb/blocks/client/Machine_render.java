package net.scriptshatter.fberb.blocks.client;

import net.scriptshatter.fberb.blocks.Machine;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class Machine_render extends GeoBlockRenderer<Machine> {
    public Machine_render() {
        super(new Machine_model());
    }

}
