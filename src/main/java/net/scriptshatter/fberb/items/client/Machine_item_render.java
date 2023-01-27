package net.scriptshatter.fberb.items.client;

import net.scriptshatter.fberb.items.Machine_item;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class Machine_item_render extends GeoItemRenderer<Machine_item> {
    public Machine_item_render() {
        super(new Machine_item_model());
    }
}
