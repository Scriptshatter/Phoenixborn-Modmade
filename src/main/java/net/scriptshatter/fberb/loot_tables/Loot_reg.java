package net.scriptshatter.fberb.loot_tables;

import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonSerializer;
import net.scriptshatter.fberb.Phoenix;

public class Loot_reg {

    public static final LootConditionType TOOL_TEMP_CONDITION = registerLootCondition("tool_temp_condition", new Phoenix_shovel_condition.Serializer());

    private static LootConditionType registerLootCondition(String path, JsonSerializer<? extends LootCondition> serializer) {
        return Registry.register(Registries.LOOT_CONDITION_TYPE, new Identifier(Phoenix.MOD_ID, path), new LootConditionType(serializer));
    }

    public static void register_loot(){
        Phoenix.LOGGER.info("This manic flee's too much for me; a chef with a funny accent.");
    }
}
