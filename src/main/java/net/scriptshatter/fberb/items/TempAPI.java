package net.scriptshatter.fberb.items;

import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;

public final class TempAPI {
    public static final ItemApiLookup<Birb_item, Void> ITEM_WITH_TEMP =
            ItemApiLookup.get(new Identifier(Phoenix.MOD_ID, "item_temp"), Birb_item.class, Void.class);
}
