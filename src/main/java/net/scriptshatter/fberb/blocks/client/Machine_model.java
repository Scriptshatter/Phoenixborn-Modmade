package net.scriptshatter.fberb.blocks.client;

import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.blocks.Machine;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class Machine_model extends AnimatedGeoModel<Machine> {
    @Override
    public Identifier getModelResource(Machine object) {
        return new Identifier(Phoenix.MOD_ID, "geo/machine.geo.json");
    }

    @Override
    public Identifier getTextureResource(Machine object) {
        return new Identifier(Phoenix.MOD_ID, "textures/do_the_mario/pain.png");
    }

    @Override
    public Identifier getAnimationResource(Machine animatable) {
        return new Identifier(Phoenix.MOD_ID, "animations/machine.animation.json");
    }
}
