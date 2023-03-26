package net.scriptshatter.fberb.loot_tables;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;
import net.scriptshatter.fberb.items.Birb_item;

public class Phoenix_shovel_condition implements LootCondition {

    private final int temp;

    public Phoenix_shovel_condition(int temp) {
        this.temp = temp;
    }

    @Override
    public LootConditionType getType() {
        return Loot_reg.TOOL_TEMP_CONDITION;
    }

    @Override
    public boolean test(LootContext lootContext) {
        ItemStack stack = lootContext.get(LootContextParameters.TOOL);
        if(stack != null && stack.getItem() instanceof Birb_item birb_item){
            return birb_item.temp(stack) >= this.temp;
        }
        return false;
    }

    public static LootCondition.Builder builder(int temp) {
        return () -> new Phoenix_shovel_condition(temp);
    }


    public static class Serializer implements JsonSerializer<Phoenix_shovel_condition> {
        public void toJson(JsonObject jsonObject, Phoenix_shovel_condition condition, JsonSerializationContext jsonSerializationContext) {
            jsonObject.addProperty("temp", condition.temp);
        }

        public Phoenix_shovel_condition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            int temp = JsonHelper.getInt(jsonObject, "temp");
            return new Phoenix_shovel_condition(temp);
        }
    }
}
