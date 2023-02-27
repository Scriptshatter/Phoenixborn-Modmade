package net.scriptshatter.fberb.items.client;

import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.items.Phoenix_axe;
import software.bernie.geckolib.model.GeoModel;

public class Phoenix_axe_model extends GeoModel<Phoenix_axe> {
    @Override
    public Identifier getModelResource(Phoenix_axe animatable) {
        return new Identifier(Phoenix.MOD_ID, "geo/item/phoenix_axe.geo.json");
    }

    @Override
    public Identifier getTextureResource(Phoenix_axe animatable) {
        return new Identifier(Phoenix.MOD_ID, "textures/item/phoenix_axe.png");
    }

    @Override
    public Identifier getAnimationResource(Phoenix_axe animatable) {
        return new Identifier(Phoenix.MOD_ID, "animations/block/machine.animation.json");
    }
}
