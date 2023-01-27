package net.scriptshatter.fberb.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.scriptshatter.fberb.Phoenix;
import net.scriptshatter.fberb.blocks.Phoenix_blocks;

public class Items {
    //Making the icon for the phoenix
    public static final Phoenix_brooch PHOENIX_BROOCH = register_items("phoenix_brooch", new Phoenix_brooch(new FabricItemSettings()), Phoenix.MOD_ID);
    public static final Charged_amethyst CHARGED_AMETHYST = register_items("charged_amethyst", new Charged_amethyst(new FabricItemSettings()), Phoenix.MOD_ID);
    public static final Machine_item MACHINE_ITEM = register_items("machine", new Machine_item(), Phoenix.MOD_ID);

    public static <I extends Item> I register_items(String name, I item, String modid){
        return Registry.register(Registries.ITEM, new Identifier(modid, name), item);
    }

    public static void werk(){
        Phoenix.LOGGER.info("AHHHH");
    }
}
