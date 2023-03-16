package net.scriptshatter.fberb.loot_tables;

import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;

public class Phoenix_shovel_condition implements LootCondition {
    @Override
    public LootConditionType getType() {
        return null;
    }

    @Override
    public boolean test(LootContext lootContext) {
        return false;
    }
}
