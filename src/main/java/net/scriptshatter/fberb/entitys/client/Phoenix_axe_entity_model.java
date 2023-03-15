package net.scriptshatter.fberb.entitys.client;

import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.entitys.Phoenix_axe_entity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class Phoenix_axe_entity_model extends DefaultedEntityGeoModel<Phoenix_axe_entity> {
    public Phoenix_axe_entity_model() {
        super(new Identifier(Phoenix.MOD_ID, "phoenix_axe"), false);
    }
}
