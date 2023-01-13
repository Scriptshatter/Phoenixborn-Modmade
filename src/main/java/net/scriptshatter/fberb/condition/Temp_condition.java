package net.scriptshatter.fberb.condition;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.components.Bird_parts;
import net.scriptshatter.fberb.util.Ect;

public class Temp_condition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        //Takes the temp and compares it if they're a bird.
        if (entity.isPlayer() && (Ect.has_origin(entity, Ect.FIRE_BIRD) || Ect.has_origin(entity, Ect.FROST_BIRD))) {
            int temp = Bird_parts.TEMP.get(entity).get_temp();
            return ((Comparison) data.get("comparison")).compare(temp, data.getInt("compare_to"));
        }

        return false;
    }

    //Make thing happen.
    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(new Identifier(Phoenix.MOD_ID, "check_temp"),
                new SerializableData()
                        .add("comparison", ApoliDataTypes.COMPARISON)
                        .add("compare_to", SerializableDataTypes.INT),
                Temp_condition::condition
        );
    }
}
