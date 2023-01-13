package net.scriptshatter.fberb.actions;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.components.Bird_parts;

public class Change_revives_action {
    //The thing that changes the temp
    public static void action(SerializableData.Instance data, Entity entity) {
        if(entity instanceof ServerPlayerEntity) {
            Bird_parts.TEMP.get(entity).add_rebirths(data.get("amount"));
        }
    }

    //Makes the action into an action
    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(new Identifier(Phoenix.MOD_ID, "change_rebirths"),
                new SerializableData()
                        .add("amount", SerializableDataTypes.INT),
                Change_revives_action::action
        );
    }
}
