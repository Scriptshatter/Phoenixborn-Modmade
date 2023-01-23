package net.scriptshatter.fberb.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.scriptshatter.fberb.Phoenix;

public class Items {
    //Making the icon for the phoenix
    public static final Item PHOENIX_BROOCH = register_items("phoenix_brooch", new Phoenix_brooch(new Item.Settings().group(ItemGroup.MISC)), Phoenix.MOD_ID);
    public static final Item CHARGED_AMETHYST = register_items("charged_amethyst", new Charged_amethyst(new Item.Settings().group(ItemGroup.MATERIALS)), Phoenix.MOD_ID);

    public static Item register_items(String name, Item item, String modid){
        return Registry.register(Registry.ITEM, new Identifier(modid, name), item);
    }

    public static void werk(){
        Phoenix.LOGGER.info("AHHHH");
    }
}
