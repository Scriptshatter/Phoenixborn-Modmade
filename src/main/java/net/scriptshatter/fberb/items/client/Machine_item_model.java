package net.scriptshatter.fberb.items.client;

import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.items.Machine_item;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class Machine_item_model extends GeoModel<Machine_item> {

    @Override
    public Identifier getModelResource(Machine_item animatable) {
        return new Identifier(Phoenix.MOD_ID, "geo/item/machine.geo.json");
    }

    @Override
    public Identifier getTextureResource(Machine_item animatable) {
        return new Identifier(Phoenix.MOD_ID, "textures/block/machine.png");
    }

    @Override
    public Identifier getAnimationResource(Machine_item animatable) {
        return new Identifier(Phoenix.MOD_ID, "animations/block/machine.animation.json");
    }
}
