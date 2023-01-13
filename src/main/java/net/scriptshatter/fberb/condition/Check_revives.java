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

public class Check_revives {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        //Takes the temp and compares it if they're a bird.
        if ((Ect.has_origin(entity, Ect.FIRE_BIRD))) {
            int rebirths = Bird_parts.TEMP.get(entity).get_rebirths();
            return ((Comparison) data.get("comparison")).compare(rebirths, data.getInt("compare_to"));
        }

        return false;
    }

    //Make thing happen.
    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(new Identifier(Phoenix.MOD_ID, "check_rebirths"),
                new SerializableData()
                        .add("comparison", ApoliDataTypes.COMPARISON)
                        .add("compare_to", SerializableDataTypes.INT),
                Check_revives::condition
        );
    }
}
