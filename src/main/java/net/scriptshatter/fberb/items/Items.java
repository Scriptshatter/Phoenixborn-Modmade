package net.scriptshatter.fberb.items;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.scriptshatter.fberb.Phoenix;

public class Items {
    //Making the icon for the phoenix
    public static final Item PHOENIX_BROOCH = register_items("phoenix_brooch", new Phoenix_brooch(new Item.Settings()));

    private static Item register_items(String name, Item item){
        return Registry.register(Registry.ITEM, new Identifier(Phoenix.MOD_ID, name), item);
    }

    public static void werk(){
        Phoenix.LOGGER.info("AHHHH");
    }
}
