package net.scriptshatter.fberb.util;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import net.minecraft.entity.Entity;
import net.minecraft.util.registry.Registry;
import net.scriptshatter.fberb.condition.Check_revives;
import net.scriptshatter.fberb.condition.Temp_condition;

public class Entity_conditions {
    //Makes the functions to register the conditions.
    public static void register() {
        register(Temp_condition.getFactory());
        register(Check_revives.getFactory());
    }

    private static void register(ConditionFactory<Entity> conditionFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
