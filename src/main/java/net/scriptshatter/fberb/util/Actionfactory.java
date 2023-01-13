package net.scriptshatter.fberb.util;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.util.registry.Registry;
import net.scriptshatter.fberb.actions.Change_revives_action;
import net.scriptshatter.fberb.actions.Change_temp_action;

public class Actionfactory {
    //Makes the register function for actions.
    public static void register() {
        register(Change_temp_action.getFactory());
        register(Change_revives_action.getFactory());
    }

    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
