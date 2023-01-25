package net.scriptshatter.fberb.items.client;

import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.blocks.Machine;
import net.scriptshatter.fberb.items.Machine_item;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class Machine_item_model extends AnimatedGeoModel<Machine_item> {

    @Override
    public Identifier getModelResource(Machine_item object) {
        return new Identifier(Phoenix.MOD_ID, "geo/machine.geo.json");
    }

    @Override
    public Identifier getTextureResource(Machine_item object) {
        return new Identifier(Phoenix.MOD_ID, "textures/do_the_mario/pain.png");
    }

    @Override
    public Identifier getAnimationResource(Machine_item animatable) {
        return new Identifier(Phoenix.MOD_ID, "animations/machine.animation.json");
    }
}
